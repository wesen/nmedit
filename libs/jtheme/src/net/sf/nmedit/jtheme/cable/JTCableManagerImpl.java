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
package net.sf.nmedit.jtheme.cable;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.component.JTConnector;

public class JTCableManagerImpl implements JTCableManager
{
    
    
    private int size = 0;
    private ManagedCable[] cables = new ManagedCable[10];

    private CableRenderer cableRenderer;
    private Rectangle visibleRegion = null;
    private transient float visibleRegionOuterSq;

    private transient int structureModCount;
    private transient int modCount;
    private transient int unchangedMod;
    private transient Point center = new Point(0, 0);
    private transient Rectangle sharedRect;
    private transient Rectangle dirtyRect;
    /*
    private transient Rectangle singleDirtyRegion;
    private transient Cable singleUpdateCable;
    private transient int singleUpdateCount = 0;*/
    private JComponent view;
    private JComponent owner;
    
    public JTCableManagerImpl(CableRenderer cableRenderer)
    {
        this.cableRenderer = cableRenderer;
    }
    
    public void setOwner(JComponent owner)
    {
        this.owner = owner;
    }

    public JTCableManagerImpl()
    {
        super();
    }
    
    public JComponent getOwner()
    {
        return owner;
    }

    protected int checkComputation()
    {
        if (unchangedMod == modCount)
            return 0;
        
        int updateResult = 0;
        
        // compute center
        int cx = 0;
        int cy = 0;

        if (size > 0)
        {
            if (sharedRect == null)
                sharedRect = new Rectangle();
            
            if (dirtyRect == null)
            {
                dirtyRect = new Rectangle(0,0,0,0);
            }
            else
            {
                // we do not reset the dirtyRect region because
                // we can not be sure that notifyRepaintManager()
                // was called
            }

            int updateCount = 0;

            for (int i=0;i<size;i++)
            {
                ManagedCable mc = cables[i];
                
                if (mc.update)
                {
                    sharedRect = mc.getOldBounds(sharedRect);
                    if (updateCount == 0)
                        dirtyRect.setBounds(sharedRect);
                    else
                        SwingUtilities.computeUnion(sharedRect.x, sharedRect.y, sharedRect.width, sharedRect.height, dirtyRect);
                    mc.checkUpdate();
                    sharedRect = mc.getBounds(sharedRect);
                    SwingUtilities.computeUnion(sharedRect.x, sharedRect.y, sharedRect.width, sharedRect.height, dirtyRect);
                    mc.dirty = true;
                    updateCount ++;
                }
            }

            updateResult = updateCount;
            
            final int oldcx = center.x;
            final int oldcy = center.y;
            
            if (visibleRegion != null)
            {
                cx = visibleRegion.x+(visibleRegion.width/2);
                cy = visibleRegion.y+(visibleRegion.height/2);
            }
    
            center.x = cx<0?0:cx;
            center.y = cy<0?0:cy;

            boolean allDirty = false;
            if (oldcx!=cx||oldcy!=cy)
            {
                allDirty = true;
                updateResult++;
            }
            if (allDirty||updateCount>0)
            {
                // update distances 
                for (int i=0;i<size;i++)
                {
                    ManagedCable c = cables[i];
                    if (c.dirty||allDirty)
                    {
                        computeDistanceSq(c, c.getBounds(sharedRect));
                        if (c.dirty && (!allDirty))
                        {
                            updateCount--;
                            if (updateCount == 0)
                                break;
                        }
                        c.dirty = false;
                    }
                }
            }
            
            // sort array by distance
            if (updateResult>0)
                Arrays.sort(cables, 0, size);
        }

        // visible region
        if (visibleRegion != null)
        {
            visibleRegionOuterSq = computeOuterDistanceSq(visibleRegion);
        }
        else
        {
            visibleRegionOuterSq = Float.MAX_VALUE;
        }
        
        //dirtyRegionSq = Math.min(dirtyRegionSq, visibleRegionOuterSq);
        // mark as updated
        unchangedMod = modCount;
        
        return updateResult;
    }

    public void markCompletelyDirty()
    {
        modCount ++;
    }
    
    protected float computeDistanceSq(Point p)
    {
        return (float) Point.distanceSq(center.x, center.y, p.x, p.y);
    }
    
    protected float computeDistanceSq(float x, float y)
    {
        return (float) Point.distanceSq(center.x, center.y, x, y);
    }

    protected float computeOuterDistanceSq(Rectangle bounds)
    {
        float cx = ((bounds.x*2f)+bounds.width)*2f/4f;
        float cy = ((bounds.y*2f)+bounds.height)*2f/4f;
        
        float dx, dy;

        dx = bounds.x-cx;
        dy = bounds.y-cy;   
        float brad = (float)Math.sqrt((dx*dx)+(dy*dy));

        dx = center.x-cx;
        dy = center.y-cy;
        float crad = (float)Math.sqrt((dx*dx)+(dy*dy));
        
        float outerRadSq = crad+brad;
        outerRadSq = outerRadSq*outerRadSq;
        return outerRadSq;
    }

    protected float computeInnerDistanceSq(Rectangle bounds)
    {
        float cx = ((bounds.x*2f)+bounds.width)*2f/4f;
        float cy = ((bounds.y*2f)+bounds.height)*2f/4f;
        
        float dx, dy;

        dx = bounds.x-cx;
        dy = bounds.y-cy;   
        float brad = (float)Math.sqrt((dx*dx)+(dy*dy));

        dx = center.x-cx;
        dy = center.y-cy;
        float crad = (float)Math.sqrt((dx*dx)+(dy*dy));
        
        float innerRadSq = Math.max(0, crad-brad);
        innerRadSq = innerRadSq*innerRadSq;
        return innerRadSq;
    }

    protected void computeDistanceSq(ManagedCable c, Rectangle bounds)
    {
        float cx = ((bounds.x*2f)+bounds.width)*2f/4f;
        float cy = ((bounds.y*2f)+bounds.height)*2f/4f;
        
        float dx, dy;

        dx = bounds.x-cx;
        dy = bounds.y-cy;   
        float brad = (float)Math.sqrt((dx*dx)+(dy*dy));

        dx = center.x-cx;
        dy = center.y-cy;
        float crad = (float)Math.sqrt((dx*dx)+(dy*dy));
        
        float innerRadSq = Math.max(0, crad-brad);
        float outerRadSq = crad+brad;
        outerRadSq = outerRadSq*outerRadSq;
        innerRadSq = innerRadSq*innerRadSq;

        c.outerDistanceSq = outerRadSq;
        c.innerDistanceSq = innerRadSq;
    }

    private void ensureCapacity(int capacity)
    {
        modCount ++;
        structureModCount++;
        if (cables.length<=capacity)
        {
            ManagedCable[] a = new ManagedCable[(capacity*2)+1];
            System.arraycopy(cables, 0, a, 0, size);
            cables = a;
        }
    }

    protected ManagedCable managedCable(Cable cable)
    {
        int index = indexOf(cable);
        return (index>=0) ? cables[index] : null;
    }
    
    public void add(Cable cable)
    {
        ensureCapacity(size+1);
        cables[size++] = new ManagedCable(cable);
    }
    
    public int indexOf(Cable cable)
    {
        for (int i=0;i<size;i++)
            if (cable==cables[i].cable)
                return i;
        return -1;
    }
    
    public boolean contains(Cable cable)
    {
        return indexOf(cable)>=0;
    }
    
    private void fastRemoveAt(int index)
    {
        System.arraycopy(cables, index+1, cables, index, size-index-1);
        
        ManagedCable mc = cables[size-1];

        sharedRect = mc.getOldBounds(sharedRect);
        if (dirtyRect == null)
            dirtyRect = new Rectangle(sharedRect);
        else
        {
            SwingUtilities.computeUnion(sharedRect.x, sharedRect.y, sharedRect.width, sharedRect.height, dirtyRect);
        }
        sharedRect = mc.getBounds(sharedRect);
        SwingUtilities.computeUnion(sharedRect.x, sharedRect.y, sharedRect.width, sharedRect.height, dirtyRect);

        cables[size-1] = null;
        size --;
        structureModCount++;
        modCount++;        
    }
    
    public void remove(Cable cable)
    {
        int index = indexOf(cable);
        if (index>=0)
            fastRemoveAt(index);
    }
    
    // managed cable
    private static class ManagedCable implements Comparable<ManagedCable>
    {
        
        public boolean dirty = true;
        public boolean update = true;
        Cable cable;
        float outerDistanceSq = 0;
        float innerDistanceSq = 0;
        Rectangle oldBounds;
        
        public ManagedCable(Cable cable)
        {
            this.cable = cable;
        }

        public void setOldBounds()
        {
            if (oldBounds == null)
                oldBounds = new Rectangle();
            oldBounds = getBounds(oldBounds);
        }
        
        public Rectangle getOldBounds(Rectangle r)
        {
            if (oldBounds == null)
            {
                oldBounds = new Rectangle();
                oldBounds = getBounds(oldBounds);
            }
            
            r.setBounds(oldBounds);
            return r;
        }
        
        public Rectangle getBounds(Rectangle r)
        {
            return cable.getBounds(r);
        }

        public boolean isManaged(Cable testCable)
        {
            return this.cable == testCable;
        }

        public int compareTo(ManagedCable o)
        {
            return (int) Math.signum(innerDistanceSq-o.innerDistanceSq);
        }
        
        public boolean checkUpdate()
        {
            final boolean updated = update;
            if (update)
            {
                cable.updateEndPoints();
                update = false;
            }
            return updated;
        }
        
    }

    public void clear()
    {
        Arrays.fill(cables, 0, size, null);
        size = 0;
        unchangedMod = ++modCount;
    }

    private boolean isConnected(Cable c, PModule m)
    {
        return c.getSourceModule() == m || c.getDestinationModule() == m;
    }
    
    public void getCables(Collection<Cable> c, PModule module)
    {
        for (int i=0;i<size;i++)
        {
            if (isConnected(cables[i].cable, module))
                c.add(cables[i].cable);
        }
    }
    
    public void getCables(Collection<Cable> c, Collection<? extends PModule> modules)
    {
        for (int i=0;i<size;i++)
        {
            Cable cable = cables[i].cable;
            if (modules.contains(cable.getSourceModule()) || modules.contains(cable.getDestinationModule()))
                c.add(cable);
        }
    }

    public Iterator<Cable> getCables()
    {
        return new Iterator<Cable>()
        {

            int remove = -1;
            int index = 0;
            int expectedModCount = structureModCount;
            
            public boolean hasNext()
            {
                checkMod();
                return index<size;
            }

            public Cable next()
            {
                checkMod();
                if (!hasNext())
                    throw new NoSuchElementException();
                return cables[remove = index++].cable;
            }

            public void remove()
            {
                checkMod();
                if (remove<0)
                    throw new IllegalStateException();
                fastRemoveAt(remove);
                remove = -1;
                expectedModCount = structureModCount;
            }

            private void checkMod()
            {
                if (expectedModCount != structureModCount)
                    throw new ConcurrentModificationException();
            }
            
        };
    }

    public Cable createCable(JTConnector source, JTConnector destination)
    {
        return new SimpleCable(source, destination, new Pseudo3DCableGeometrie());
    }

    public CableRenderer getCableRenderer()
    {
        return cableRenderer;
    }

    public void setView(JComponent view)
    {
        JComponent oldView = this.view;
        if (oldView != view)
        {
            this.view = view;
            markCompletelyDirty();
        }
    }
    
    public JComponent getView()
    {
        return view;
    }

    public Rectangle getVisibleRegion()
    {
        return getVisibleRegion(null);
    }

    public Rectangle getVisibleRegion(Rectangle r)
    {
        int vx, vy, vw, vh;
        
        if (visibleRegion == null)
        {
            vx = 0;
            vy = 0;
            vw = Integer.MAX_VALUE;
            vh = Integer.MAX_VALUE;
        }
        else
        {
            vx = visibleRegion.x;
            vy = visibleRegion.y;
            vw = visibleRegion.width;
            vh = visibleRegion.height;
        }
        
        if (r == null)
            r = new Rectangle(vx, vy, vw, vh);
        else
            r.setBounds(vx, vy, vw, vh);
        return r;
    }

    public boolean hasDirtyRegion()
    {
        return modCount!=unchangedMod;
    }

    public void paintCables(Graphics2D g2)
    {
        paintCables(g2, cableRenderer);
    }
    
    public void setCableRenderer(CableRenderer cableRenderer)
    {
        if (this.cableRenderer != cableRenderer)
        {
            this.cableRenderer = cableRenderer;
            markCompletelyDirty();
        }
    }

    public void setVisibleRegion(int x, int y, int width, int height)
    {
        if (visibleRegion == null)
            visibleRegion = new Rectangle(x, y, width, height);
        else
            visibleRegion.setBounds(x, y, width, height);
        markCompletelyDirty();
    }

    public void setVisibleRegion(Rectangle r)
    {
        this.visibleRegion = r;
        markCompletelyDirty();
    }

    public int size()
    {
        return size;
    }

    public void getVisible(Collection<Cable> c)
    {
        checkComputation();
        for (int i=0;i<size;i++)
        {
            ManagedCable mc = cables[i];
            if (mc.innerDistanceSq<=visibleRegionOuterSq)
                c.add(mc.cable);
        }
    }

    public void update(Cable cable)
    {
        markDirty(cable);
        notifyRepaintManager();
    }

    public Iterator<Cable> iterator()
    {
        return getCables();
    }

    public void markDirty(Cable cable)
    {
        int index = indexOf(cable);
        if (index>=0)
        {
            ManagedCable mc = cables[index];
            mc.update = true;
            modCount ++;
        }
    }
    
    public void notifyRepaintManager()
    {
        if (view != null && checkComputation()>0 && dirtyRect != null)
        {
            enlarge(dirtyRect);
            RepaintManager
                .currentManager(view)
                .addDirtyRegion(view,
                        dirtyRect.x, dirtyRect.y, 
                        dirtyRect.width, dirtyRect.height);
            dirtyRect.setBounds(0,0,0,0);
        }
    }
    
    public void paintCables(Graphics2D g, CableRenderer cableRenderer)
    {
        checkComputation();
        modCount = unchangedMod;

        Rectangle clip = g.getClipBounds();

        float innerClipSq = 0;
        float outerClipSq = Float.MAX_VALUE;


        if (sharedRect == null)
            sharedRect = new Rectangle();
        
        cableRenderer.initRenderer(g);
        if (clip != null)
        {
            enlarge(clip);
            innerClipSq = Math.max(0, computeInnerDistanceSq(clip));
            outerClipSq = computeOuterDistanceSq(clip);
            
            for (int i=0;i<size;i++)
            {
                ManagedCable mc = cables[i];
               
                if (mc.innerDistanceSq<=outerClipSq)
                {   
                    if (mc.outerDistanceSq>=innerClipSq)
                       if (mc.getBounds(sharedRect).intersects(clip))
                       {
                           mc.setOldBounds();
                           cableRenderer.render(g, mc.cable);
                       }
                }
                else
                {
                    break;
                }
            }
        }
        else
        {
        
            for (int i=0;i<size;i++)
            {
                ManagedCable mc = cables[i];
               
                if (mc.innerDistanceSq<=outerClipSq)
                {   
                    if (mc.outerDistanceSq>=innerClipSq)
                    {
                       mc.setOldBounds();
                       cableRenderer.render(g, mc.cable);
                    }
                }
                else
                {
                    break;
                }
            }
        }
    }

    private void enlarge(Rectangle r)
    {
        enlarge(r, 10);
    }
    
    private void enlarge(Rectangle r, int enlargement)
    {
        final int enlargement2 =enlargement*2;
        r.x-=enlargement;
        r.y-=enlargement;
        r.width+=enlargement2;
        r.height+=enlargement2;

        if (r.x<0)
        {
            r.width+=r.x;
            r.x = 0;
        }
        if (r.y<0)
        {
            r.height+=r.y;
            r.y = 0;
        }
    }

}
