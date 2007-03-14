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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jtheme.component.JTConnector;

public class JTDefaultCableManager implements JTCableManager
{
    
    public static final boolean DEBUG = false;

    private CableRenderer cableRenderer;
    private Set<Cable> cables = new HashSet<Cable>();
    private Set<Cable> visible = new HashSet<Cable>();
    
    private Rectangle visibleRegion = new Rectangle();
    private Rectangle dirtyRegion = new Rectangle();
    private boolean completelyDirty = false;
    
    private transient Rectangle cachedRectangle;
    private boolean rebuildVisibleList = true;
    private JComponent view;

    public JTDefaultCableManager()
    {
        this(null);
    }

    public Iterator<Cable> getVisible()
    {
        return visible.iterator();
    }

    public Iterator<Cable> getCables()
    {
        return cables.iterator();
    }
    
    public void setView(JComponent view)
    {
        JComponent oldView = this.view;
        if (oldView != view)
        {
            this.view = view;
        }
    }
    
    public JComponent getView()
    {
        return view;
    }
    
    public JTDefaultCableManager(CableRenderer cr)
    {
        this.cableRenderer = cr;
    }
    
    private Rectangle getCachedRectangle()
    {
        if (cachedRectangle == null)
            cachedRectangle = new Rectangle();
        return cachedRectangle;
    }

    public void add(Cable cable)
    {
        if (cables.add(cable))
        {
            if (DEBUG)
            {
                System.out.println(toShortString()+" add "+cable);
            }
            
            cable.updateEndPoints();
            if (!rebuildVisibleList)
            {
                if (intersectsVisibleRegion(cable))
                {
                    visible.add(cable);
                }
            }
        }
    }

    public void clear()
    {
        if (!visible.isEmpty())
            addDirtyRegion(visibleRegion);
        
        cables.clear();
        visible.clear();
        rebuildVisibleList = false;
    }

    public Rectangle getCoveredArea()
    {
        return getCoveredArea(null);
    }

    public Rectangle getCoveredArea(Rectangle r)
    {
        if (r == null)
            r = new Rectangle();
        
        if (!cables.isEmpty())
        {
            Rectangle tmp = getCachedRectangle();
            
            for (Cable cable : this)
            {
                cable.getBounds(tmp);
                SwingUtilities.computeUnion( tmp.x, tmp.y, 
                        tmp.width, tmp.height, r);
            }
        }
        
        return r;
    }

    public void remove(Cable cable)
    {
        if (DEBUG)
        {
            if (cables.contains(cable))
                System.out.println(toShortString()+" remove "+cable);
        }
        if (cables.remove(cable) && visible.remove(cable))
        {
            markDirty(cable);
        }
    }

    public int size()
    {
        return cables.size();
    }

    public void markDirty(Cable cable)
    {
        if (!completelyDirty)
        {
            addDirtyRegion(cable.getBounds(getCachedRectangle()));
        }
    }
    
    public Iterator<Cable> iterator()
    {
        return cables.iterator();
    }

    public void update(Cable cable)
    {
        if (DEBUG)
        {
            System.out.println(toShortString()+" update "+cable);
        }
        markDirty(cable);
        cable.updateEndPoints();
        
        if (!rebuildVisibleList)
        {
            // we update the visible list only if
            // not the whole list will be updated
            
            if (visible.contains(cable))
            {
                if (!intersectsVisibleRegion(cable))
                {
                    visible.remove(cable);
                }
            }
            else
            {
                if (intersectsVisibleRegion(cable))
                {
                    visible.add(cable);
                }
            }
        }
    }

    private boolean intersectsVisibleRegion(Cable cable)
    {
        return visibleRegion.intersects(cable.getBounds(getCachedRectangle()));
    }

    private static final boolean isEmpty(int width, int height)
    {
        return width<=0 || height<= 0;
    }
    
    protected void markCompletelyClean()
    {
        dirtyRegion.setBounds(0, 0, 0, 0);
        completelyDirty = false;
    }
    
    public boolean hasDirtyRegion()
    {
        return !dirtyRegion.isEmpty();
    }

    public void markCompletelyDirty()
    {
        completelyDirty = true;
    }

    protected void addDirtyRegion(int x, int y, int width, int height)
    {
        if (completelyDirty)
            return ;
        
        if (x<0) 
        {
            width = width+x;
            x = 0;
        }
        if (y<0) 
        {
            height = height+y;
            y = 0;
        }
        
        if (isEmpty(width, height))
            return;
        
        if (dirtyRegion.isEmpty())
            dirtyRegion.setBounds(x, y, width, height);
        else
            SwingUtilities.computeUnion(x, y, width, height, dirtyRegion);
    }
    
    protected boolean finalizeDirtyRegion()
    {
        if (completelyDirty)
        {
            dirtyRegion.setBounds(visibleRegion);
            return true;
        }
        
        if (dirtyRegion.isEmpty())
            return false;
        
        final int d = cableRenderer.getCableDiameter();

        dirtyRegion.x -= d;
        dirtyRegion.y -= d;
        dirtyRegion.width += d*2;
        dirtyRegion.height += d*2;
        
        if (!visibleRegion.isEmpty())
        {
            SwingUtilities.computeIntersection(visibleRegion.x, visibleRegion.y, 
                    visibleRegion.width, visibleRegion.height, dirtyRegion);
        }
        
        return !dirtyRegion.isEmpty();
    }
    
    protected void addDirtyRegion(Rectangle r)
    {
        addDirtyRegion(r.x, r.y, r.width, r.height);
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
    
    public void setVisibleRegion(int x, int y, int width, int height)
    {
        if (DEBUG)
        {
            System.out.println(toShortString()+" set visible region:"
                    +x+" "+y+" "+width+" "+height
            );
        }

        if (visibleRegion.x == x 
                && visibleRegion.y == y
                && visibleRegion.width == width
                && visibleRegion.height == height)
        {
            return;
        }
        
        markCompletelyClean();
        addDirtyRegion(x, y, width, height);
        visibleRegion.setBounds(x, y, width, height);
        rebuildVisibleList = true;
    }

    public void setVisibleRegion(Rectangle r)
    {
        setVisibleRegion(r.x, r.y, r.width, r.height);
    }
    
    public CableRenderer getCableRenderer()
    {
        return cableRenderer;
    }

    public void setCableRenderer(CableRenderer cr)
    {
        if (this.cableRenderer != cr)
        {
            this.cableRenderer = cr;
            markCompletelyDirty();
        }
    }

    public void paintCables(Graphics2D g2)
    {
        paintCables(g2, cableRenderer);
    }

    public void paintCables(Graphics2D g2, CableRenderer cableRenderer)
    {
        checkCables();
        
        if (visible.isEmpty())
            return ;
        
        Rectangle tmpRect = getCachedRectangle();
        cableRenderer.initRenderer(g2);
        for (Cable cable : visible)
        {
            //cable.getBounds(tmpRect);
            //if (g2.hitClip(tmpRect.x, tmpRect.y, tmpRect.width, tmpRect.height))
            {
                cableRenderer.render(g2, cable);
            }
        }
        
        markCompletelyClean();
    }

    private final void checkCables()
    {
        if (!rebuildVisibleList)
            return;

        int before = visible.size();
        
        rebuildVisibleList = false;
        visible.clear();
        for (Cable cable : cables)
        {
            if (intersectsVisibleRegion(cable))
                visible.add(cable);
        }

        if (DEBUG)
        {
            System.out.println(toShortString()+", rebuild visible list, before:"+before+" after:"+visible.size());
        }
        
    }

    public void notifyRepaintManager()
    {
        if ((!finalizeDirtyRegion()) || view == null)
            return;
 
        RepaintManager repaintManager = 
            RepaintManager.currentManager(view);
        
        repaintManager.addDirtyRegion(view, dirtyRegion.x, dirtyRegion.y, 
                dirtyRegion.width, dirtyRegion.height);
        
        markCompletelyClean();
    }
    
    private String toShortString()
    {
        return getClass().getName();
    }

    public Cable createCable(JTConnector source, JTConnector destination)
    {
        return new SimpleCable(source, destination, new Pseudo3DCableGeometrie());
    }
}

