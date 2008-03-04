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

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PModuleListener;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.plaf.JTModuleUI;

public class JTModule extends JTComponent
    implements PModuleListener
{

    public static final String PROPERTY_MODULE = "module";
    public static final String PROPERTY_TITLE = "module.title";
    public static final String PROPERTY_SELECTED = "module.selected";
    
    /**
     * 
     */
    private static final long serialVersionUID = -1588824728080062930L;
    public static final String uiClassID = "JTModuleUI";
    private PModule module;
    private boolean selected = false;
    private String title = null;
    
    public JTModule(JTContext context)
    {
        super(context);
        enableEvents(MouseEvent.COMPONENT_EVENT_MASK);
        setFocusable(true);
        setOpaque(true);
        setJTFlag(FLAG_INVALIDATE, true);
        setJTFlag(FLAG_VALIDATE, true);
        setJTFlag(FLAG_REVALIDATE, true);
        setJTFlag(FLAG_PROPERTY_SUPPORT, true);
    }
    
    public void setTitle(String title)
    {
        String oldValue = this.title;
        String newValue = title;
        if (oldValue != newValue || (oldValue != null && !oldValue.equals(newValue)))
        {
            this.title = newValue;
            if (module != null)
                module.setTitle(newValue);
            firePropertyChange(PROPERTY_TITLE, oldValue, newValue);
            repaint();
        }
    }
    
    public String getTitle()
    {
        return title;
    }
    
    protected void processEvent(AWTEvent e)
    {
        if (e.getID() == ComponentEvent.COMPONENT_MOVED && module != null)
        {
            module.setScreenLocation(getX(), getY());
            updateCablesForThisModule();
        } 
        super.processEvent(e);
    }
    
    private JTCableManager getCableManager() {
        JTModuleContainer mc;
        try
        {
            mc = (JTModuleContainer) getParent();
        }
        catch (ClassCastException e)
        {
            // parent not a module container
            mc = null;
        }
        return mc == null ? null : mc.getCableManager();
    }
    
    protected void updateCablesForThisModule() {
    	JTCableManager cman = getCableManager();
    	if (cman == null)
    		return;
    	if (module == null)
    		return;

        Collection<Cable> cables = new LinkedList<Cable>();
        cman.getCables(cables, module);

        if (!cables.isEmpty()) {
        	cman.update(cables);
        	for (Cable c: cables)
        		c.updateEndPoints();

        	cman.update(cables);
        }
	}
    
	public void setSelected(boolean selected)
    {
        boolean oldValue = this.selected;
        boolean newValue = selected;
        
        if (oldValue != newValue)
        {
            this.selected = newValue;
            firePropertyChange(PROPERTY_SELECTED, oldValue, newValue);
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

    /*public final void setStaticLayerBackingStore(Image staticLayerBackingStore)
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
    }*/

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
                // set bounds before adding listener
                setBounds(module.getScreenX(),
                        module.getScreenY(),
                        module.getScreenWidth(),
                        module.getScreenHeight());
                module.addModuleListener(this);
            }
            firePropertyChange(PROPERTY_MODULE, oldModule, module);
            if (module != null)
                setTitle(module.getTitle());
            else
                setTitle(null);
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
        PModule pmodule = e.getModule();
        setTitle(pmodule.getTitle());
    }
    
}
