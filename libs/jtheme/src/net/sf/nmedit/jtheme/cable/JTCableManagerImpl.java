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

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.component.JTConnector;

public class JTCableManagerImpl implements JTCableManager, Runnable
{
    
    private CableRenderer cableRenderer;
    private JComponent view;
    private JComponent owner;
    private int autorepaintDisabledCounter = 0;
    
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

    public boolean contains(Cable cable)
    {
        return cables.contains(cable);
    }
    
    private boolean isConnected(Cable c, PModule m)
    {
        return c.getSourceModule() == m || c.getDestinationModule() == m;
    }
    
    public void getCables(Collection<Cable> c, PModule module)
    {
        for (Cable cable: cables)
        {
            if (isConnected(cable, module)) 
                c.add(cable);
        }
    }
    
    public void getCables(Collection<Cable> c, Collection<? extends PModule> modules)
    {
        for (Cable cable: cables)
        {
            if (modules.contains(cable.getSourceModule()) || modules.contains(cable.getDestinationModule()))
                c.add(cable);
        }
    }

    public Cable createCable(JTConnector source, JTConnector destination)
    {
        return new SimpleCable(source, destination, new SimpleCableGeometrie());
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
    
    private void markCompletelyDirty()
    {
        dirty.setBounds(owner.getBounds());
        repaintIfDirty();
    }

    public JComponent getView()
    {
        return view;
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

    public int size()
    {
        return cables.size();
    }

    private transient Rectangle cachedRect;
    private Rectangle clipRect = new Rectangle(); // must not be null
    
    public void paintCables(Graphics2D g, CableRenderer cableRenderer)
    {
        Rectangle clip = g.getClipBounds(clipRect);
        boolean hasClip = clip.x>0 || clip.y>0 || view == null
            || (clip.x+clip.width<view.getWidth())
            || (clip.y+clip.height<view.getHeight());
        
        if ((!hasClip) || ((!dirty.isEmpty()) && clip.contains(dirty)))
        {
            dirty.setBounds(0, 0, 0, 0); // ensure bounds are cleared
        }
        
        cableRenderer.initRenderer(g);
        
        if (!hasClip)
        {
            for (Cable c: cables)
                cableRenderer.render(g, c);
        }
        else
        {
            enlarge(clip, cableRenderer.getCableDiameter()); // enlarge so that we don't miss a cable
            for (Cable c: cables)
            {
                Rectangle b = (cachedRect=c.getBounds(cachedRect));
                if (clip.intersects(b))
                    cableRenderer.render(g, c);
            }
        }
        
    }

    private Rectangle dirty = new Rectangle(0,0,0,0);
    
    private void markDirty(Cable c, boolean automaticRepaint)
    {
        if (view != null && view.isShowing())
        {
            Rectangle bounds = c.getBounds();
            if (dirty.isEmpty())
                dirty.setBounds(bounds);
            else
                SwingUtilities.computeUnion(bounds.x, bounds.y, bounds.width, bounds.height, dirty);
            
            if (automaticRepaint) 
                repaintIfDirty();
        }
        else
        {
            //System.out.println("not showing");
        }
    }
    
    private List<Cable> cables = new ArrayList<Cable>();

    public void add(Cable c)
    {
        if (!cables.contains(c))
        {
            if (cables.add(c))
                markDirty(c, true);
        }
    }
    
    public void remove(Cable c)
    {
        if (cables.remove(c))
            cableRemoved(c);
    }


    private void cableRemoved(Cable c)
    {
        markDirty(c, true);
    }
    
    public void clear()
    {
        if (!cables.isEmpty())
        {
            for (Cable c: cables)
                markDirty(c, false);
            cables.clear();
            repaintIfDirty();
        }
    }
    
    public void remove(Cable[] cables)
    {
        for (int i=0;i<cables.length;i++)
        {
            Cable c = cables[i];
            if (this.cables.remove(c))
            {
                markDirty(c, false);
            }
            repaintIfDirty();
        }
    }

    public void remove(Collection<Cable> cables)
    {
        for (Cable c: cables)
        {
            if (this.cables.remove(c))
            {
                markDirty(c, false);
            }
            repaintIfDirty();
        }
    }

    public void update(Cable c)
    {
        markDirty(c, true);
        repaintIfDirty();
    }
    
    public void update(Cable[] cables)
    {
        for (int i=0;i<cables.length;i++)
            markDirty(cables[i], false);
        repaintIfDirty();
    }
    
    public void update(Collection<Cable> cables)
    {
        for (Cable c: cables)
            markDirty(c, false);
        repaintIfDirty();
    }

    private void repaintIfDirty()
    {
        if (!isAutoRepaintEnabled()) return;
        if (!dirty.isEmpty())
        {
            if (EventQueue.isDispatchThread())
            {
                run();
            }
            else
            {
                EventQueue.invokeLater(this); // invokes this.run()
            }
        }
    }

    public void run()
    {
        letRepaintManagerRepaintIfDirty();
    }
    
    private void letRepaintManagerRepaintIfDirty()
    {
        if (dirty.isEmpty())
            return;
        
        enlarge(dirty, 10);
        
        RepaintManager.currentManager(view).addDirtyRegion(view, dirty.x, dirty.y, dirty.width, dirty.height);
        dirty.setBounds(0, 0, 0, 0);   
    }

    private void enlarge(Rectangle r, int size)
    {
        r.x = Math.max(0, r.x-size);
        r.y = Math.max(0, r.y-size);
        r.width +=size+size;
        r.height+=size+size;
    }

    public Iterator<Cable> iterator()
    {
        return new Iterator<Cable>()
        {
            
            Cable removable;
            Iterator<Cable> iter = cables.iterator();

            public boolean hasNext()
            {
                return iter.hasNext();
            }

            public Cable next()
            {
                removable = iter.next();
                return removable;
            }

            public void remove()
            {
                iter.remove();
                // reaches here if no exception occured
                // removable is then != null
                if (removable == null) return; // this should never be true
                
                Cable c = removable;
                removable = null;
                cableRemoved(c);
            }
            
        };
    }

    public void clearAutoRepaintDisabled()
    {
        if (autorepaintDisabledCounter>0)
        {
            autorepaintDisabledCounter--;
            if (isAutoRepaintEnabled())
                repaintIfDirty();
        }
    }

    public boolean isAutoRepaintEnabled()
    {
        return autorepaintDisabledCounter<=0;
    }

    public void setAutoRepaintDisabled()
    {
        autorepaintDisabledCounter++;
    }

    public void shake()
    {
        for (Cable cable: cables)
        {
            markDirty(cable, false);
            cable.shake();
            markDirty(cable, false);
        }
        repaintIfDirty();
    }
    
}
