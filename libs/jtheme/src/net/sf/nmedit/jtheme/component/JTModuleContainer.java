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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.border.Border;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleMetrics;
import net.sf.nmedit.jpatch.event.ModuleContainerEvent;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.plaf.JTModuleContainerUI;
import net.sf.nmedit.jtheme.store.ModuleStore;

public class JTModuleContainer extends JTBaseComponent 
{
    
    public static final String uiClassId = "ModuleContainerUI";
    
    private boolean optimizedDrawing;
    private JTCableManager cableManager;
    private JTPatch patchContainer;
    private PModuleContainer moduleContainer;
    private ContentSynchronisation cs;

    public JTModuleContainer(JTContext context, JTCableManager cableManager)
    {
        super(context);
        setOpaque(true);
        optimizedDrawing = true;// does not work: !context.hasModuleContainerOverlay();
                                // we have to overwrite boolean isPaintingOrigin() which is package private 
        setCableManager(cableManager);
        
        cs = createContentSynchronisation();
        if (cs != null)
            cs.install();
    }
    
    protected ContentSynchronisation createContentSynchronisation()
    {
        return new ContentSynchronisation();
    }
    
    protected class ContentSynchronisation implements ModuleContainerListener
    {
        private PModuleContainer mc;
        private boolean oneUpdate = false;
        
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
        }

        protected void uninstall(PModuleContainer mc)
        {
            mc.removeModuleContainerListener(this);
        }

        public void moduleAdded(ModuleContainerEvent e)
        {
            createUIFor(e.getModule());
        }

        public void moduleRemoved(ModuleContainerEvent e)
        {
            removeUI(e.getModule());
        }

        private void createUIFor(PModule module)
        {
            ModuleStore ms = getContext().getStorageContext().getModuleStoreById(module.getComponentId());
            
            try
            {
                JTModule mui = ms.createModule(getContext(), module);
                mui.setLocation(module.getScreenLocation());
                add(mui);
                
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

    }
    
    public void setModuleContainer(PModuleContainer mc)
    {
        this.moduleContainer = mc;
        
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
                oldManager.setView(null);
            this.cableManager = cableManager;
            if (cableManager != null)
                cableManager.setView(this);
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
        
        JTCableManager cableManager = getCableManager();
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
