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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleMetrics;
import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PConnectionListener;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.plaf.JTModuleContainerUI;
import net.sf.nmedit.jtheme.store2.ModuleElement;

public class JTModuleContainer extends JTBaseComponent 
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 4287282305262880549L;

    public static final String uiClassId = "ModuleContainerUI";
    
    private boolean optimizedDrawing;
    private JTCableManager cableManager;
    private JTPatch patchContainer;
    private PModuleContainer moduleContainer;
    private ContentSynchronisation cs;
    //private CableOverlay overlay;

    public JTModuleContainer(JTContext context, JTCableManager cableManager)
    {
        super(context);
        setOpaque(true);
        setFocusable(true);
        optimizedDrawing = true;// 'true' does not work: !context.hasModuleContainerOverlay();
                                // we have to overwrite boolean isPaintingOrigin() which is package private
        /*overlay = new CableOverlay();
        super.add(overlay, 0);
        overlay.setEnabled(false);*/

        setCableManager(cableManager);
        cs = createContentSynchronisation();
        if (cs != null)
            cs.install();
    }

    protected boolean isRepaintOrigin()
    {
        return true;
    }
    
    public void updateModuleContainerDimensions()
    {
        SwingUtilities.invokeLater( new Runnable() {

            long time = System.currentTimeMillis();
            
            public void run()
            {
                if (time>lastModuleContainerDimensionUpdate)
                    updateModuleContainerDimensionsNow();
                
            }} );
    }
    
    private long lastModuleContainerDimensionUpdate = 0;

    private void updateModuleContainerDimensionsNow()
    {
        lastModuleContainerDimensionUpdate = System.currentTimeMillis();
        
        Dimension d = computePreferredSize(null);
        setPreferredSize(d);
        setSize(d);   
        repaint();
    }
    
/*
    public Component findComponentAt(int x, int y) { 
        if (!(contains(x, y) && isVisible() && isEnabled())) {
            return null;
        }
        synchronized (getTreeLock())
        {
            for (int i=getComponentCount()-1;i>=0;i--)
            {
                Component comp = getComponent(i);
                if (comp != overlay)
                {
                    if (comp instanceof Container)
                    {
                        comp = ((Container)comp).findComponentAt(x-comp.getX(), y-comp.getY());
                    }
                    else
                    {
                        comp = comp.getComponentAt(x, y);
                    }
                    if (comp != null && comp.isVisible() && comp.isEnabled())
                    {
                        return comp;
                    }
                }
            }
        }
        return this;
    }*/
    public void repaintOverlay(Rectangle r)
    {
        repaintOverlay(r.x, r.y, r.width, r.height);
    }
    
    public void repaintOverlay(int x, int y, int width, int height)
    {
        /*overlay.*/repaint(x, y, width, height);
    }
    
    protected ContentSynchronisation createContentSynchronisation()
    {
        return new ContentSynchronisation();
    }
    
    protected class ContentSynchronisation implements PModuleContainerListener, PConnectionListener
    {
        private PModuleContainer mc;
        private PConnectionManager pc;
        //private boolean oneUpdate = false;
        
        public void install()
        {
           // no op
        }

        public void update()
        {
            setInstalledContainer(moduleContainer);
        }

        private void setInstalledContainer(PModuleContainer moduleContainer)
        {
            if (this.mc != null)
                uninstall(this.mc);
            this.mc = moduleContainer;
            if (this.mc != null)
                install(this.mc);
        }

        protected void install(PModuleContainer mc)
        {
            mc.addModuleContainerListener(this);
            pc = mc.getConnectionManager();
            if (pc != null)
                pc.addConnectionListener(this);
        }

        protected void uninstall(PModuleContainer mc)
        {
            mc.removeModuleContainerListener(this);
            if (pc != null)
                pc.removeConnectionListener(this);
            pc = null;
        }

        public void moduleAdded(PModuleContainerEvent e)
        {
            createUIFor(e.getModule());
        }

        public void moduleRemoved(PModuleContainerEvent e)
        {
            removeUI(e.getModule());
        }

        private void createUIFor(PModule module)
        {
            ModuleElement ms = getContext().getStorageContext().getModuleStoreById(module.getComponentId());

            try
            {
                JTModule mui = ms.createModule(getContext(), module);
                mui.setLocation(module.getScreenLocation());    
                add(mui);
                /*
                if (mui.getStaticLayerBackingStore() == null)
                {
                    ms.setStaticLayer(mui.renderStaticLayerImage());
                    mui.setStaticLayerBackingStore(mui.getStaticLayerBackingStore());
                }
                */
                // TODO revalidate/repaint container
                mui.repaint();
            }
            catch (JTException e)
            {
                e.printStackTrace();
            }
        }

        public void removeUI(PModule module)
        {
            Component[] components = JTModuleContainer.this.getComponents();
            for (int i=components.length-1;i>=0;i--)
            {
                Component c = components[i];
                if (c instanceof JTModule)
                {
                    JTModule mui = (JTModule) c;
                    if (mui.getModule() == module)
                    {
                        Rectangle bounds = mui.getBounds();
                        remove(mui);
                        // TODO revalidate/repaint
                        repaint(bounds);
                        
                        break;
                    }
                }
            }
        }

        public void connectionAdded(PConnectionEvent e)
        {
            colorCables(e, true);
        }

        public void connectionRemoved(PConnectionEvent e)
        {
            colorCables(e, false);
        }

        public void colorCables(PConnectionEvent e, boolean joined)
        {
            if (joined)
            {
                Color color = null;
                PConnector graph = null;
                if (e.getDestination().getOutputConnector() != null)
                {
                    graph = e.getSource();
                    color = e.getDestination().getOutputConnector().getSignalType().getColor();
                    
                }
                else if (e.getSource().getOutputConnector() != null)
                {
                    graph = e.getDestination();
                    color = e.getSource().getOutputConnector().getSignalType().getColor();
                }
                
                if (graph != null) 
                {
                    
                    Collection<Cable> cables = getCables(graph); 
                    for (Cable cable: cables)
                    {
                        cable.setColor(color);
                    }
                    getCableManager().update(cables);
                }
            }
            else
            {
                PConnector graph = null;
                if (e.getDestination().getOutputConnector() != null)
                {
                    graph = e.getSource();
                }
                else if (e.getSource().getOutputConnector() != null)
                {
                    graph = e.getDestination();
                }
                
                // make grey
                Collection<Cable> cables = getCables(graph); 
                for (Cable cable: cables)
                {
                    // TODO lookup correct color in patch 
                    cable.setColor(Color.WHITE);
                }
                getCableManager().update(cables);
            }
        }
        
        private Collection<Cable> getCables(PConnector c)
        {
            Collection<Cable> cables = new ArrayList<Cable>(getCableManager().size());
            Collection<PConnection> connections = c.getGraphConnections();
            
            for (Cable cable: getCableManager())
            {
                for (Iterator<PConnection> iter=connections.iterator();iter.hasNext();)
                {
                    if (isSameConnection(iter.next(), cable))
                        cables.add(cable);
                }
            }
            return cables;
        }

        private boolean isSameConnection(PConnection c, Cable cable)
        {
            return isSameConnection(c.getA(), c.getB(), cable.getSource(), cable.getDestination())
            || isSameConnection(c.getA(), c.getB(), cable.getDestination(), cable.getSource());
        }

        private boolean isSameConnection(PConnector a1, PConnector b1,
                PConnector a2, PConnector b2)
        {
            // TODO Auto-generated method stub
            return a2==a2 && b1==b2;
        }
        
    }
    
    public void setModuleContainer(PModuleContainer mc)
    {
        this.moduleContainer = mc;
        setName(moduleContainer == null ? null : moduleContainer.getName());
        PModuleMetrics metrics = null;
        if (mc != null) metrics = mc.getModuleMetrics();
        if (metrics == null) setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        else setMaximumSize(new Dimension(metrics.getMaxScreenX(), metrics.getMaxScreenY()));
        
        if (cs != null)
            cs.update();
    }

    public PModuleContainer getModuleContainer()
    {
        return moduleContainer;
    }
    
    public JTPatch getPatchContainer()
    {
        return patchContainer;
    }
    
    public void setPatchContainer(JTPatch patchContainer)
    {
        this.patchContainer = patchContainer;
    }

    public JTModuleContainerUI getUI()
    {
        return (JTModuleContainerUI) ui;
    }
    
    public void setUI(JTModuleContainerUI ui)
    {
        super.setUI(ui);
    }
    
    public String getUIClassID()
    {
        return uiClassId;
    }

    protected void setCableManager(JTCableManager cableManager)
    {
        JTCableManager oldManager = this.cableManager;
        if (oldManager != cableManager)
        {
            if (oldManager != null)
            {
                oldManager.setOwner(null);
                oldManager.setView(null);
            }
            this.cableManager = cableManager;
            if (cableManager != null)
            {
                cableManager.setOwner(this);
                cableManager.setView(this);
            }
        }
    }
    
    public JTCableManager getCableManager()
    {
        return cableManager;
    }
    
    public boolean isOptimizedDrawingEnabled()
    {
        return optimizedDrawing;
    }

    /**
     * Calls {@link #paint(Graphics)}.
     */
    public void update(Graphics g)
    {
        paint(g);
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
    }
    
    public void setBorder(Border border)
    {
        if (border != null)
            throw new UnsupportedOperationException("border not supported");
    }
    
    /*public void setLayout(LayoutManager layout)
    {
        if (layout != null)
            throw new UnsupportedOperationException("layout not supported");
    }*/
    
    protected final void paintBorder(Graphics g)
    {
        // no op
    }
    
    protected void paintChildren(Graphics g)
    {
        super.paintChildren(g);
        if (cableManager != null)
        {
            Graphics gs = g.create();
            try
            {
                cableManager.paintCables((Graphics2D) gs);
            }
            finally
            {
                gs.dispose();
            }
        }
        if (ui != null)
        {
            getUI().paintChildrenHack(g);
        }
    }
    
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }

    public Dimension computePreferredSize(Dimension dim)
    {
        if (dim == null) 
            dim = new Dimension(0,0);
        else
            dim.setSize(0,0);
        
        for (int i=getComponentCount()-1;i>=0;i--)
        {
            Component c = getComponent(i);

            dim.width = Math.max(dim.width, c.getX()+c.getWidth());
            dim.height = Math.max(dim.height, c.getY()+c.getHeight());
            
        }
        
        return dim;
    }

    public boolean isDnDAllowed()
    {
        return getContext().isDnDAllowed();
    }

}
