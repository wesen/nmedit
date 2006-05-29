/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on May 3, 2006
 */
package net.sf.nmedit.nomad.util.graphics.shape;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import net.sf.nmedit.nomad.main.background.Background;
import net.sf.nmedit.nomad.util.cache.MultiCache;
import net.sf.nmedit.nomad.util.cache.op.TileFactoryOp;
import net.sf.nmedit.nomad.util.collection.Array2D;
import net.sf.nmedit.nomad.util.collection.List;
import net.sf.nmedit.nomad.util.collection.ListEntry;
import net.sf.nmedit.nomad.util.computing.Worker;
import net.sf.nmedit.nomad.util.computing.WorkerJob;
import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;

public class ShapeDisplay<T extends Shape> implements Background, Iterable<T>
{   
    
    public static boolean DEBUG_SD = false;
    
    private final static int RENDER_COUNT = 20;
    public final static int TILE_SIZE = 128; // power of 2
    
    public final static MultiCache<BufferedImage> tileFactory
        = new MultiCache<BufferedImage>(new TileFactoryOp(TILE_SIZE,
                TILE_SIZE, TRANSLUCENT));

    private int updateCount = 0;
    
    private Map<T, Approximation> map = new Hashtable<T, Approximation>();
    private Tile[] tiles = new Tile[0];
    private int tw = 0;
    private int th = 0;
    private double dcontour = 3;
    private Rectangle modifiedArea = new Rectangle(0,0,0,0);
    private int modifycount = 0;

    private JComponent display;

    private RenderOp renderOp;
    
    private Worker worker = new Worker();
    
    private QualityRenderJob renderJob = new QualityRenderJob();

    protected final static int FLAG_ADDED   = 1<<0;
    protected final static int FLAG_REMOVED = 1<<1;
    protected final static int FLAG_UPDATED = 1<<2;
    
    private int flags = 0;
    
    public ShapeDisplay(JComponent display, RenderOp renderOp)
    {
        this.display = display;
        this.renderOp = renderOp;
    }
    
    public RenderOp getRenderOp()
    {
        return renderOp;
    }
    
    public Set<T> getShapes()
    {
        return map.keySet();
    }
    
    private void assureSize(int w, int h)
    {
        if (tw<w||th<h)
        {
            w = Math.max(tw, w);
            h = Math.max(th, h);
            int size = w*h;
            Tile[] newTiles = new Tile[size];
            Array2D.<Tile>transfer(newTiles, w, h, tiles, tw, th);
            
            // horizontal: fill
            for (int i=Array2D.getIndex(0, th, w, h);i<size;i++)
            {
                newTiles[i] = new Tile();
            }
        
            // vertical fill
            for (int y=0;y<th;y++)
                for (int x=tw;x<w;x++)
                    Array2D.<Tile>set(newTiles, x, y, w, h, new Tile());

            tw = w;
            th = h;
            tiles = newTiles;
        }
    }
    
    private void shrinkSize()
    {
        int r = tw-1;
        boolean hasTile = false;
        while (r>=0)
        {
            for (int y=0;y<th;y++)
            {
                if (!Array2D.<Tile>get(tiles, r, y, tw, th).isEmpty())
                {
                    hasTile = true;
                    break;
                }
            }
            if (hasTile)
                break;
            else
                r --;
        }
        int nw = r+1;

        if (nw==0)
        {
            flush();
            tw = 0;
            th = 0;
            tiles = new Tile[0];
            // clear
            return;
        }

        int b = th-1;
        hasTile = false;
        while (b>=0)
        {
            for (int x=0;x<nw;x++)
            {
                if (!Array2D.<Tile>get(tiles, x, b, tw, th).isEmpty())
                {
                    hasTile = true;
                    break;
                }
            }
            if (hasTile)
                break;
            else
                b --;
        }
        
        int nh = b+1;
        
        if (nh==0)
        {
            flush();
            tw = 0;
            th = 0;
            tiles = new Tile[0];
            // clear
            return;
        }
        
        if (nw<tw||nh<th)
        {
            int min, max;
            
            min = Math.min(nw, tw);
            max = Math.max(nw, tw);
            for (int i=min;i<max;i++)
                for (int j=0;j<th;j++)
                    Array2D.<Tile>get(tiles, i, j, tw, th).flush();
            
            min = Math.min(nh, th);
            max = Math.max(nh, th);
            for (int j=min;j<max;j++)
                for (int i=0;i<tw;i++)
                    Array2D.<Tile>get(tiles, i, j, tw, th).flush();
            
            Tile[] newTiles = new Tile[nw*nh];
            Arrays.fill(newTiles, null);
            Array2D.<Tile>transfer(newTiles, nw, nh, tiles, tw, th);
            tiles = newTiles;
            tw = nw;
            th = nh;
        }
    }
    
    public void flush()
    {
        for (int i=tiles.length-1;i>=0;i--)
        {
            Tile t = tiles[i];
            if (t!=null) t.flush();
        }
    }
    
    public void beginUpdate()
    {
        updateCount ++;
    }
    
    public void endUpdate()
    {
        if (updateCount<=0)
            throw new IllegalStateException();
        
        updateCount--;
        
        if (updateCount == 0)
        {
            if (flags!=0)
            {
                if ((flags&(FLAG_ADDED|FLAG_REMOVED))!=0)
                    shrinkSize();   
                flags = 0;   
            }
            if (modifycount>0)
            {
                modifycount = 0;
                update(modifiedArea.x, modifiedArea.y, modifiedArea.width, modifiedArea.height);        
                modifiedArea.setSize(0,0);
            }
        }
    }
    
    private void update(int x, int y, int w, int h)
    {
        RepaintManager.currentManager(display)
            .addDirtyRegion(display, x*TILE_SIZE, y*TILE_SIZE, w*TILE_SIZE, h*TILE_SIZE);
    }
    
    private void update(int x, int y)
    {
        RepaintManager.currentManager(display)
            .addDirtyRegion(display, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public MultiCache<BufferedImage> getTileFactory()
    {
        return tileFactory;
    }
    
    public void repaint(T shape)
    {
        Approximation ap = map.get(shape);
        if (ap!=null)
            repaintTiles(ap);
    }
    
    public void update(T shape)
    {
        Approximation ap = map.get(shape);
        if (ap!=null)
        {
            flags|=FLAG_UPDATED;
            
            beginUpdate();
            updateTiles(ap);
            endUpdate();
        }
    }

    public void add(T shape)
    {
        if (!map.containsKey(shape))
        {
            //Cable c = (Cable) shape;
            //System.out.println(c.getP1()+", "+c.getP2());

            flags|=FLAG_ADDED;
            
            Approximation ap = new Approximation(shape, dcontour, TILE_SIZE);
            map.put(shape, ap);
            beginUpdate();
            addToTiles(ap);
            endUpdate();
        }
    }
    
    public void remove(T shape)
    {
        Approximation ap = map.remove(shape);
        if (ap!=null)
        {
            flags|=FLAG_REMOVED;
            
            beginUpdate();
            removeFromTiles(ap);
            endUpdate();
        }
    }
    
    public boolean contains(T shape)
    {
        return map.containsKey(shape);
    }
    
    public void clear()
    {
        flush();
        tw = 0;
        th = 0;
        tiles = new Tile[0];
        map.clear();
    }
    
    public Iterator<T> iterator()
    {
        return map.keySet().iterator();
    }

    private Rectangle clipBounds = new Rectangle();
    
    public void paintBackground( Component c, Graphics g, int x, int y, int width, int height )
    {
        clipBounds.setBounds(0, 0, tw*TILE_SIZE, th*TILE_SIZE);
        g.getClipBounds(clipBounds);

        int cx = clipBounds.x;
        int cy = clipBounds.y;
        int cr = clipBounds.x+clipBounds.width;
        //cr+= (TILE_SIZE-(cr%TILE_SIZE));
        int cb = clipBounds.y+clipBounds.height;
        //cb+= (TILE_SIZE-(cb%TILE_SIZE));

        cr+=TILE_SIZE;
        cb+=TILE_SIZE;
        
        cx/=TILE_SIZE;
        cy/=TILE_SIZE;
        cr/=TILE_SIZE;
        cb/=TILE_SIZE;

        cx = Math.max(0,cx);
        cy = Math.max(0,cy);
        cr = Math.min(cr, tw-1);
        cb = Math.min(cb, th-1);
        
        boolean enqueued = !renderJob.isComplete();
        
        int tx = cx*TILE_SIZE;
        int ty_top = cy*TILE_SIZE;
        int ty;
        for (int i=cx;i<=cr;i++)
        {
            ty = ty_top;
            for (int j=cy;j<=cb;j++)
            {
                Tile tile = Array2D.<Tile>get(tiles, i, j, tw, th);
                
                if (!tile.isEmpty())
                {
                    if (tile.isFlushed())
                    {
                        tile.restore();
                        render(tile, tx, ty, RenderOp.OPTIMIZE_SPEED);
                        renderJob.add(i,j, tile);
                    }
                    
                    g.drawImage(tile.img, tx,ty, null);
                    
                    if (DEBUG_SD)
                    {
                        Color prevColor = g.getColor();
                        g.setColor(Color.BLUE);                        
                        g.drawRect(tx+4, ty+4, TILE_SIZE-9, TILE_SIZE-9);
                        g.drawRect(tx, ty, TILE_SIZE, TILE_SIZE);
                        g.setColor(prevColor);
                        
                        int cnt = 0;
                        ListEntry list = tile.getInternalList();
                        while (list!=null)
                        {
                            cnt ++;
                            list = list.remaining;
                        }
                        g.setColor(Color.RED);
                        g.drawString(""+cnt, tx+TILE_SIZE/2, ty+20);
                    }
                    
                }
                ty+=TILE_SIZE;
            }
            tx+=TILE_SIZE;
        }
        
        if (DEBUG_SD)
        {
            Graphics2D g2 = (Graphics2D) g;
            Color prevColor = g.getColor();
            g2.setColor(Color.GREEN);
            for (Approximation ap : map.values())
            {
                paintApproximation(g2, ap);
            }
            
            // draw bounds
            g2.drawRect(0, 0, tw*TILE_SIZE-1, th*TILE_SIZE-1);
            
            g.setColor(prevColor);
        }
        
        if ((!enqueued)&&(!renderJob.isComplete()))
        {
            worker.enqueue(renderJob);
        }
    }
    
    private void render(Tile tile, int gx, int gy, int optimization)
    {
        ListEntry<Shape> shapes = tile.getInternalList();
        if (shapes!=null)
        {
            Graphics2D g2 = tile.img.createGraphics();
            //g2.setClip(0,0,TILE_SIZE,TILE_SIZE);
            g2.translate(-gx, -gy);

            renderOp.configure(g2, optimization);
            do
            {
                renderOp.render(g2, shapes.item);
                shapes = shapes.remaining;
            }
            while (shapes!=null);

            g2.dispose();
        }
    }

    private void paintApproximation( Graphics2D g2, Approximation ap )
    {
        PathIterator pi = ap.getShape().getPathIterator(null, ap.getFlatness());
        double[] segment = new double[6];
        
        int bx = 0;
        int by = 0;
        while (!pi.isDone())
        {
            int type = pi.currentSegment(segment);
            int x = (int) segment[0];
            int y = (int) segment[1];
            g2.drawRect(x-2, y-2, 5, 5);
            switch (type)
            {
                case PathIterator.SEG_MOVETO:
                    break ;
                case PathIterator.SEG_LINETO:
                case PathIterator.SEG_CLOSE:
                    g2.drawLine(x, y, bx, by);
                    break ;
            }
            bx = x; by = y;
            pi.next();
        }
    }

    public int getTransparency()
    {
        return TRANSLUCENT;
    }
    
    private void updateTiles(Approximation ap)
    {
        removeFromTiles(ap);
        ap.update();
        addToTiles(ap);
    }

    private void addToTiles(Approximation ap)
    {
        addModifiedArea(ap, true);

        int[] xpoints = ap.getX();
        int[] ypoints = ap.getY();
        Shape shape = ap.getShape();
        for (int i=ap.getNumPoints()-1;i>=0;i--)
        {
            Array2D.<Tile>get(tiles, xpoints[i], ypoints[i], tw, th).add(shape);
        }
    }

    private void repaintTiles(Approximation ap)
    {   
        addModifiedArea(ap, false);    

        int[] xpoints = ap.getX();
        int[] ypoints = ap.getY();
        for (int i=ap.getNumPoints()-1;i>=0;i--)
        {
            Array2D.<Tile>get(tiles, xpoints[i], ypoints[i], tw, th).flush();
        }
    }
    
    private void removeFromTiles(Approximation ap)
    {
        addModifiedArea(ap, false);    
        
        int[] xpoints = ap.getX();
        int[] ypoints = ap.getY();
        Shape shape = ap.getShape();
        for (int i=ap.getNumPoints()-1;i>=0;i--)
        {
            Array2D.<Tile>get(tiles, xpoints[i], ypoints[i], tw, th).remove(shape);
        }
    }

    private void addModifiedArea(Approximation ap, boolean setSize)
    {
        int l = Math.max(ap.getGridBoundsLeft(),0);
        int t = Math.max(ap.getGridBoundsTop() ,0);
        int r = ap.getGridBoundsRight();
        int b = ap.getGridBoundsBottom();

        if (r<l||b<t) return;
        
        int w = r-l+1;
        int h = b-t+1;
        
        modifycount ++;
        GraphicsToolkit.union(modifiedArea, l, t, w, h);
        
        if (setSize) assureSize(l+w,t+h);
    }
    
    private static class Tile extends List<Shape>
    {
        BufferedImage img = null;

        public boolean isFlushed()
        {
            return img==null;
        }
        
        public void add(Shape t)
        {
            super.add(t);
            flush();
        }
        
        public boolean remove(Shape t)
        {
            if (super.remove(t))
            {
                flush();
                return true;
            }
            return false;
        }
        
        public void restore()
        {
            if (img==null)
                img = tileFactory.get();
        }
        
        public void flush()
        {
            if (img!=null)
            {
                tileFactory.put(img);
                img = null;
            }
        }
    }
    
    private static class TileRenderJob
    {
        int x, y;
        Tile tile;
        public TileRenderJob(int x, int y, Tile tile)
        {
            this.x=x;
            this.y=y;
            this.tile=tile;
        }
    }
    
    private class QualityRenderJob extends List<TileRenderJob> implements WorkerJob
    {
        private Set<Tile> set = new HashSet<Tile>();

        public void add(int x, int y, Tile tile)
        {
            if (!set.contains(tile))
            {
                set.add(tile);
                add(new TileRenderJob(x, y, tile));
            }
        }
        
        public boolean isComplete()
        {
            return isEmpty();
        }

        public void step()
        {
            int atOnce = RENDER_COUNT;
            while ((!isComplete())&&(atOnce>0))
            {
                TileRenderJob job = removeTop();
                Tile t = job.tile;
                set.remove(t);
                
                if (!t.isFlushed())
                {
                    Graphics2D g2 = t.img.createGraphics();
                    GraphicsToolkit.clearRegion(g2, 0, 0, TILE_SIZE, TILE_SIZE);
                    g2.dispose();
                    render(t, job.x*TILE_SIZE, job.y*TILE_SIZE, RenderOp.OPTIMIZE_QUALITY);
                    update(job.x, job.y);
                    atOnce--;
                }
            }
        }
        
    }
    
}
