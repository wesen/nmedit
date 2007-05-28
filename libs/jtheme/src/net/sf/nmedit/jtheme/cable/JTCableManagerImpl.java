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
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.nmutils.iterator.ArrayIterator;

/**
 * TODO bug: sometimes at least one element in cables[0..cableCount-1] is null  
 * 
 * @author christian
 */
public class JTCableManagerImpl implements JTCableManager
{

    private static final boolean DEBUG = false;
    
    private Cable[] cables = new Cable[0];
    private int visibleCount = 0;
    private int cableCount = 0;
    private transient Rectangle cachedRectangle;
    private Rectangle visibleRegion = new Rectangle(0,0,0,0);
    private Rectangle prevVisibleRegion = null;
    private CableRenderer cableRenderer;
    private transient Rectangle dirtyRegion;
    private boolean dirty = false;
    private boolean completelyDirty = false;
    private JComponent view ;
    private transient int prevVisibleCount = 0;

    public JTCableManagerImpl(CableRenderer cableRenderer)
    {
        this.cableRenderer = cableRenderer;
    }

    public JTCableManagerImpl()
    {
        this(null);
    }

    private void storeDirtyRegion(Rectangle r)
    {
        storeDirtyRegion(r.x, r.y, r.width, r.height);
    }
    
    private void storeDirtyRegion(int x, int y, int w, int h)
    {
        if (!completelyDirty)
        {
            if (dirtyRegion == null)
                dirtyRegion = new Rectangle();
            
            if (x<0) {w+=x; x = 0;}
            if (y<0) {h+=y; y = 0;}
            if (w>0 && h>0)
                SwingUtilities.computeUnion(x, y, w, h, dirtyRegion);   
        }        
        dirty = true;
    }
    
    private Rectangle getCachedRectangle()
    {
        if (cachedRectangle == null)
            cachedRectangle = new Rectangle();
        return cachedRectangle;
    }
    
    private Rectangle bounds(Cable c)
    {
        return c.getBounds(getCachedRectangle());
    }
    
    private boolean checkIsVisible(Cable c)
    {
        return bounds(c).intersects(visibleRegion);
    }
    
    private void swap(int a, int b)
    {
        Cable tmp = cables[a];
        cables[a] = cables[b];
        cables[b] = tmp;
    }
    
    private void updateOrder()
    {
        if (prevVisibleRegion == null)
            return;
        
        Rectangle oldr = prevVisibleRegion;
        Rectangle newr = visibleRegion;
        prevVisibleRegion = null;

        if (DEBUG)
        {
            String r;
            if (oldr.contains(newr))
                r = "smaller";
            else if (newr.contains(oldr))
                r = "larger";
            else
                r = "?";
            System.out.println("updateOrder: region="+r);
        }
        
        if (oldr.contains(newr))
        {
            // smaller
            for (int i=0;i<visibleCount;i++)
            {
                if (!checkIsVisible(cables[i]))
                {
                    swap(i, --visibleCount);
                }
            }
        }
        else if (newr.contains(oldr))
        {
            // larger
            for (int i=visibleCount;i<cableCount;i++)
            {
                if (checkIsVisible(cables[i]))
                {
                    if (i!=visibleCount)
                        swap(i, visibleCount);
                    visibleCount++;
                }
            }
        }
        else 
        {
            visibleCount = 0;
            for (int i=0;i<cableCount;i++)
            {
                if (checkIsVisible(cables[i]))
                {
                    // check if we have to swap cables
                    if (i+1>visibleCount)
                    {
                        swap(visibleCount, i);
                    }
                    visibleCount++;
                }
            }
        }
    }
    
    private int indexOf(Cable cable)
    {
        for (int i=cableCount-1;i>=0;i--)
            if (cable == cables[i])
                return i;
        return -1;
    }
    
    public boolean contains(Cable cable)
    {
        return indexOf(cable)>=0;
    }
    
    public void add(Cable cable)
    {
        if (contains(cable))
            return;

        if (cableCount>=cables.length)
        {
            Cable[] a = new Cable[(cableCount+1)*3/2];
            System.arraycopy(cables, 0, a, 0, cableCount);
            cables = a;
        }

        // append
        cables[cableCount++] = cable;
        
        if (applyOrder(cable, cableCount-1))
        {
            markDirty(cable);
        }
    }
    
    private boolean applyOrder(Cable cable, int index)
    {
        // returns true if cable becomes visible/invisible or is/remains visible

        if (checkIsVisible(cable))
        {
            if (index>visibleCount)
            {
                // cable becomes visible
                swap(visibleCount++, index);
            }
            else if (index == visibleCount)
            {
                visibleCount++;
            }
            return true;
        }
        else
        {
            if (index<visibleCount)
            {
                // cable becomes invisible
                if (index==visibleCount-1)
                    visibleCount--;
                else
                    swap(--visibleCount, index);
                return true;
            }
        }
        
        return false;
    }

    public void update(Cable cable)
    {
        if (completelyDirty)
            return;
        
        int index = indexOf(cable);
        if (index<0)
            return;
        
        markDirty(cable);
        cable.updateEndPoints();
        
        if (applyOrder(cable, index))
            markDirty(cable);
    }
    
    private boolean isCableAtIndexVisible(int index)
    {
        return index<visibleCount;
    }

    public void remove(Cable cable)
    {
        int index = indexOf(cable);
        
        if (index<0)
            return;
        
        // pack
        for (int i=index+1;i<cableCount;i++)
            cables[i-1] = cables[i];
        cables[cableCount-1] = null;
        cableCount--;
        
        if (isCableAtIndexVisible(index))
        {
            visibleCount--;
            markDirty(cable);
        }
    }

    public Iterator<Cable> getCables()
    {
        return new ArrayIterator<Cable>(cables, 0, cableCount);
    }

    public Iterator<Cable> iterator()
    {
        return getCables();
    }

    public void getVisible(Collection<Cable> c)
    {
        for (int i=0;i<visibleCount;i++)
            c.add(cables[i]);
    }
    
    private boolean isConnected(Cable c, PModule m)
    {
        return c.getSourceModule() == m || c.getDestinationModule() == m;
    }
    
    public void getCables(Collection<Cable> c, PModule module)
    {
        for (int i=0;i<cableCount;i++)
        {
            if (isConnected(cables[i], module))
                c.add(cables[i]);
        }
    }
    
    public void getCables(Collection<Cable> c, Collection<? extends PModule> modules)
    {
        for (int i=0;i<cableCount;i++)
        {
            Cable cable = cables[i];
            if (modules.contains(cable.getSourceModule()) || modules.contains(cable.getDestinationModule()))
                c.add(cable);
        }
    }

    public int size()
    {
        return cableCount;
    }

    public void setVisibleRegion(int x, int y, int width, int height)
    {
        if (prevVisibleRegion == null)
            prevVisibleRegion = new Rectangle(visibleRegion);
        visibleRegion.setBounds(x, y, width, height);
    }

    public void setVisibleRegion(Rectangle r)
    {        
        setVisibleRegion(r.x, r.y, r.width, r.height);
    }

    public Rectangle getVisibleRegion()
    {
        return getVisibleRegion(null);
    }

    public Rectangle getVisibleRegion(Rectangle r)
    {
        if (r == null)
            r = new Rectangle();
        r.setBounds(visibleRegion);
        return r;
    }

    public Cable createCable(JTConnector source, JTConnector destination)
    {
        return new SimpleCable(source, destination, new Pseudo3DCableGeometrie());
    }

    public void clear()
    {
        for (int i=0;i<visibleCount;i++)
            markDirty(cables[i]);
        
        if (cableCount>0) Arrays.fill(cables, 0);
        cableCount = 0;
        visibleCount = 0;
        dirty = false;
        completelyDirty = false;
    }
    
    public CableRenderer getCableRenderer()
    {
        return cableRenderer;
    }

    public void setCableRenderer(CableRenderer cableRenderer)
    {
        if (this.cableRenderer != cableRenderer)
        {
            this.cableRenderer = cableRenderer;
            markCompletelyDirty();
        }
    }

    public void markCompletelyDirty()
    {
        dirty = completelyDirty = true;
    }

    public boolean hasDirtyRegion()
    {
        return dirty;
    }

    public void markDirty(Cable cable)
    {
        if (view != null)
            storeDirtyRegion(bounds(cable));
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

    public void paintCables(Graphics2D g2)
    {
        paintCables(g2, cableRenderer);
    }
    
    public void paintCables(Graphics2D g2, CableRenderer cableRenderer)
    {
        if (DEBUG)
        {
            if (prevVisibleCount!=visibleCount)
            {
                prevVisibleCount = visibleCount;
                System.out.println("paint:visible:"+visibleCount+"/"
                        +cableCount);
            }
        }
        
        updateOrder();
        markCompletelyClean();

        if (visibleCount <= 0)
            return ;
        
        cableRenderer.initRenderer(g2);
        for (int i=0;i<visibleCount;i++)
        {
            cableRenderer.render(g2, cables[i]);
        }
    }

    public Rectangle getCoveredArea()
    {
        return getCoveredArea(null);
    }

    public Rectangle getCoveredArea(Rectangle r)
    {
        if (r == null)
            r = new Rectangle();
        r.setBounds(0, 0, 0, 0);
        
        if (cableCount > 0)
        {
            Rectangle bounds = getCachedRectangle();
            
            for (int i=0;i<cableCount;i++)
            {
                cables[i].getBounds(bounds);
                SwingUtilities.computeUnion( bounds.x, bounds.y, 
                        bounds.width, bounds.height, r);
            }
        }
        
        return r;
    }

    public void notifyRepaintManager()
    {
        if (view == null)
            return ;

        RepaintManager rm = RepaintManager.currentManager(view);
        
        if (completelyDirty || (dirty && dirtyRegion == null))
        {
            rm.markCompletelyDirty(view);
            markCompletelyClean();
        }
        else if (dirty && dirtyRegion != null)
        {
            SwingUtilities.computeIntersection(visibleRegion.x, 
                    visibleRegion.y, visibleRegion.width,
                    visibleRegion.height, dirtyRegion);
            
            final int add = 20;

            dirtyRegion.x-=add;
            dirtyRegion.y-=add;
            dirtyRegion.width+=add*2;
            dirtyRegion.height+=add*2;
            
            rm.addDirtyRegion(view, dirtyRegion.x, dirtyRegion.y, dirtyRegion.width, dirtyRegion.height);
            markCompletelyClean();   
        }
    }
    
    private void markCompletelyClean()
    {
        dirty = completelyDirty = false;
        if (dirtyRegion != null)
            dirtyRegion.setBounds(0, 0, 0, 0);
    }

}
