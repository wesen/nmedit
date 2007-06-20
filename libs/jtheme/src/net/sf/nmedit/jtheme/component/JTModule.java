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
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PModuleListener;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.plaf.JTModuleUI;

public class JTModule extends JTComponent
    implements PModuleListener
{

    public static final String uiClassID = "JTModuleUI";
    private PModule module;
    private boolean selected = false;
    
    public JTModule(JTContext context)
    {
        super(context);
        enableEvents(MouseEvent.COMPONENT_EVENT_MASK);
        setFocusable(true);
        setOpaque(true);
    }
    
    protected void processEvent(ComponentEvent e)
    {
        if (e.getID() == ComponentEvent.COMPONENT_MOVED
                && module != null)
        {
            module.setScreenLocation(getX(), getY());
        }
        super.processEvent(e);
    }
    
    public void setSelected(boolean selected)
    {
        if (this.selected != selected)
        {
            this.selected = selected;
            repaint();
            // repaint parent 
            Container parent = getParent();
            if (parent != null && parent instanceof JComponent)
            {
                JComponent jparent = (JComponent) parent;
                RepaintManager
                .currentManager(jparent)
                .addDirtyRegion(jparent, getX(), getY(), getWidth()+1, getHeight()+1);
            }
        }
    }
    
    public boolean isSelected()
    {
        return selected;
    }
    
    /**
     * Sets the component's ui delegate.
     */
    public void setUI(JTModuleUI ui)
    {
        super.setUI(ui);
    }

    /**
     * Returns the component's ui delegate.
     */
    public JTModuleUI getUI()
    {
        return (JTModuleUI) ui;
    }

    public String getUIClassID()
    {
        return uiClassID;
    }

    public final void setStaticLayerBackingStore(Image staticLayerBackingStore)
    {
        super.setStaticLayerBackingStore(staticLayerBackingStore);
        setStaticLayerBackingStoreInChildren();
    }
    
    public final void setStaticLayerBackingStoreInChildren()
    {
        Image img = getStaticLayerBackingStore();
        for (int i=getComponentCount()-1;i>=0;i--)
        {
            Component c = getComponent(i);
            if (c instanceof JTComponent)
            {
                ((JTComponent) c).setStaticLayerBackingStore(img);
            }
        }
    }

    /**
     * @return returns 0 (zero)
     */
    protected final int getStaticLayerBackingStoreOffsetX()
    {
        return 0;
    }

    /**
     * @return returns 0 (zero)
     */
    protected final int getStaticLayerBackingStoreOffsetY()
    {
        return 0;
    }

    /**
     * @return returns false
     */
    public final boolean isReducible()
    {
        return false;
    }

    /**
     * @return returns false
     */
    protected final boolean isNonVolatileDoubleBufferEnabled()
    {
        return false;
    }

    /**
     * Paints the component.
     */
    protected void paintComponent(Graphics g)
    {
        paintStaticLayerOrBackingStore((Graphics2D) g);
    }

    /**
     * @see JTComponent#renderStaticLayerImage(Graphics2D)
     */
    public final void renderStaticLayerImage(Graphics2D g)
    {
        super.renderStaticLayerImage(g);
    }

    protected void paintBorder(Graphics g)
    {
        super.paintBorder(g);
        
        if (ui != null && isSelected())
            getUI().paintSelection(g, this);
    }
    
    /**
     * Renderes the static layer of this component and
     * of each child that is subclass of {@link JTComponent}
     * in an image and returns the image.
     * 
     * @see #renderStaticLayerImage(Graphics2D)
     */
    public BufferedImage renderStaticLayerImage()
    {
        GraphicsConfiguration gc = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice()
        .getDefaultConfiguration();
        
        int w = getWidth();
        int h = getHeight();
        
        if (w<1) w = 1;
        if (h<1) h = 1;
        
        BufferedImage img = gc.createCompatibleImage(w, h, Transparency.OPAQUE);
        
        Graphics2D g2 = img.createGraphics();
        try
        {
            renderStaticLayerImage(g2);
        }
        finally
        {
            g2.dispose();
        }
        return img; 
    }

    public void setModule(PModule module)
    {
        PModule oldModule = this.module;
        
        if (oldModule != module)
        {
            if (this.module != null)
                this.module.removeModuleListener(this);
            this.module = module;
            if (module != null)
            {
                module.addModuleListener(this);
                
                setBounds(module.getScreenX(),
                        module.getScreenY(),
                        module.getScreenWidth(),
                        module.getScreenHeight());
            }
            if (ui != null)
                getUI().moduleChanged(this, oldModule, module);
        }
    }
    
    public PModule getModule()
    {
        return module;
    }

    public void moduleMoved(PModuleEvent e)
    {
        setLocation(
        e.getModule().getScreenX(),
        e.getModule().getScreenY());
    }

    public void moduleRenamed(PModuleEvent e)
    {
        // TODO Auto-generated method stub
        
    }

}
