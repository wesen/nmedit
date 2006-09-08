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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.sf.nmedit.nomad.main.background.Background;

public class ShapeDisplay2<T extends Shape> implements Background, Iterable<T>
{   
    
    public final static int TILE_SIZE = 128; // power of 2

    private int updateCount = 0;
    
    private Rectangle modifiedArea = new Rectangle(0,0,0,0);

    private JComponent display;

    private RenderOp renderOp;
    
    private HashSet<T> items = new HashSet<T>();
    private Hashtable<T,Rectangle> track = new Hashtable<T,Rectangle>();
    
    public ShapeDisplay2(JComponent display, RenderOp renderOp)
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
        return items;
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
            //if (modifycount>0)
            {
             //   modifycount = 0;
                update(modifiedArea.x, modifiedArea.y, modifiedArea.width, modifiedArea.height);        
                modifiedArea.setSize(0,0);
            }
        }
    }
    
    private void update(int x, int y, int w, int h)
    {
        display.repaint(x,y,w,h);
    }
    
    public void repaint(T shape)
    {
        addModifiedArea(shape);
    }
    
    public void add(T shape)
    {
        if (!items.contains(shape))
        {
            //Cable c = (Cable) shape;
            //System.out.println(c.getP1()+", "+c.getP2());

            items.add(shape);
            addModifiedArea(shape);
        }
    }
    
    private void addModifiedArea(T shape)
    {
        beginUpdate();
        Rectangle r = shape.getBounds();
        if (modifiedArea.isEmpty())
            modifiedArea.setBounds(r);
        else
            modifiedArea = SwingUtilities.computeUnion(r.x, r.y, r.width,r.height, modifiedArea);
        
        r = track.put(shape, r);
        // r now is old rect
        if (r!=null)
        {
            modifiedArea = SwingUtilities.computeUnion(r.x, r.y, r.width,r.height, modifiedArea);
        }
        
        endUpdate();
    }
    
    public void remove(T shape)
    {
        if (items.remove(shape))
        {
            addModifiedArea(shape);
            track.remove(shape);
        }
    }
    
    public boolean contains(T shape)
    {
        return items.contains(shape);
    }
    
    public void clear()
    {
        track.clear();
        items.clear();
        modifiedArea.setBounds(0,0,Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    public Iterator<T> iterator()
    {
      return items.iterator();
    }

    private Rectangle clipBounds = new Rectangle();
    
    public void paintBackground( Component c, Graphics g, int x, int y, int width, int height )
    {
        clipBounds.setBounds(0, 0, 0, 0);
        g.getClipBounds(clipBounds);

        Graphics2D g2 = (Graphics2D) g;
        renderOp.configure(g2, RenderOp.OPTIMIZE_QUALITY);
        
        if (items.size()>0)
        {
            Iterator<T> iter = items.iterator();
            for (int i=items.size()-1;i>=0;i--)
            {
                T t = iter.next();
                //Rectangle r = track.get(t);
                //if (r!=null && clipBounds.intersects(r/*t.getBounds()*/))
                {
                    renderOp.render(g2, t);
                }
            }
        }
    }
    
    public int getTransparency()
    {
        return TRANSLUCENT;
    }
   
}
