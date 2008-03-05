/* Copyright (C) 2007 Christian Schneider
 * 
 * This file is part of JTheme.
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
 * Created on Jan 20, 2007
 */
package net.sf.nmedit.jtheme.component;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.plaf.JTComponentUI;

/**
 * A specialized version of JTBaseComponent. This class is
 * intended for the module component and it's child components.
 *  
 * This class uses several optimizations for better performance:
 * <ul>
 *  <li>PropertyChangeListeners are not supported. Related
 *    methods are not implemented.</li>
 *  <li>Layout manager support is removed. Related methods
 *    are not implemented: ({@link #doLayout()}, 
 *    {@link #invalidate()}, {@link #revalidate()},
 *    {@link #validate()}, {@link #validateTree()})</li>
 *  <li>Uses optionally a non volatile double buffer
 *    (enabled by default). </li>
 *  <li>{@link #paintComponent(Graphics)} paints the
 *    two layers of this component. The static layer contains
 *    the rendered parts that do not change at runtime
 *    and the dynamic layer containing the rendered parts
 *    which can change at runtime. <br/>
 *    This makes it possible to replace the rendering
 *    method of the static layer with a image. This
 *    can be done using {@link #setStaticLayerBackingStore(Image)}.
 *  </li>
 *  <li>If {@link #isReducible()} is true and the static layer
 *  backing store is available the component does not have to 
 *  be created since it already appears in the image. 
 *  </li>
 * </ul>
 * 
 * @see net.sf.nmedit.jtheme.component.plaf.JTComponentUI
 * @author Christian Schneider
 */
public class JTComponent extends JTBaseComponent
{

    private static final long serialVersionUID = 3945939382230355343L;
    // the static layer backing store image
    //private Image staticLayerBackingStore;
    // the non-volatile double buffer 
    //private transient DoubleBuffer doubleBuffer;

    /**
     * Creates a new component.
     */
    public JTComponent(JTContext context)
    {
        super(context);
        setOpaque(true);
    }
    
    /**
     * Returns the component's ui delegate.
     */
    public JTComponentUI getUI() 
    {
        return (JTComponentUI)ui;
    }
    
    /**
     * Sets the look and feel delegate for this component.
     * 
     * @throws ClassCastException if the specified argument is not
     *   instanceof {@link JTComponentUI}
     * @see JComponent#setUI(javax.swing.plaf.ComponentUI)
     */
    public void setUI(ComponentUI ui)
    {
        setUI((JTComponentUI) ui);
    }
    
    protected void setUI(JTComponentUI ui)
    {
        super.setUI(ui);
    }
    
    /**
     * Sets the static layer backing store image.
     * If the image is not null, then it is used instead of the
     * custom {@link #paintStaticLayer(Graphics2D)} implementation
     * to paint the static layer.
     * 
     * If the specified argument is not null, the opaque value
     * of this component is set to true.
     * 
     * @param staticLayerBackingStore the static layer replacement
     */
    /*public void setStaticLayerBackingStore(Image staticLayerBackingStore)
    {
        this.staticLayerBackingStore = staticLayerBackingStore;
        setOpaque(isOpaque());
    }*/
    
    /**
     * Returns the static layer backing store image.
     * @return the static layer backing store image
     */
    /*public final Image getStaticLayerBackingStore()
    {
        return staticLayerBackingStore;
    }*/

    /**
     * Returns true if the static layer backing store image is set.
     * @return true if the static layer backing store image is set
     */
    /*public final boolean hasStaticLayerBackingStore()
    {
        return staticLayerBackingStore != null; 
    }*/
    
    /**
     * Returns the x-offset of this component
     * relative to the origin of the backing store image.
     */
    protected int getStaticLayerBackingStoreOffsetX()
    {
        return getX();
    }
    
    /**
     * Returns the y-offset of this component
     * relative to the origin of the backing store image.
     */
    protected int getStaticLayerBackingStoreOffsetY()
    {
        return getY();
    }

    /**
     * Sets the opacity of this component. If the static layer backing
     * store image is set, then this will set the opaque value to true.
     */
    public void setOpaque(boolean isOpaque)
    {
        super.setOpaque(opacityOverwrite(isOpaque));
    }
    
    protected boolean opacityOverwrite(boolean isOpaque)
    {
        return isOpaque /*|| staticLayerBackingStore!=null*/;
    }
    
    /**
     * Returns true if the component does not paint the dynamic layer
     * and if it can be removed at runtime.
     * 
     * @return returns false.
     */
    public boolean isReducible()
    {
        return false;
    }

    /**
     * Returns true if this component uses a non-volatile double buffer
     * to store it's appearance. 
     * 
     * If enabled repainting with a valid double buffer is much faster
     * but it also needs more memory.
     * 
     * Only subclasses that have complex painting code should return
     * true, others should overwrite this method and return false.
     * 
     * Components that are {@link #isReducible() reducible} should
     * return false.
     * 
     * In an environment where a parent container paints
     * components above JTComponent components should return
     * true, otherwise false.
     */
    protected boolean isNonVolatileDoubleBufferEnabled()
    {
        return (!isReducible()) && getContext().hasModuleContainerOverlay();
    }
    
    /**
     * If the double buffer is used it sets the flag
     * indicating that the double buffer has to be updated.
     */
    /*private final void setDoubleBufferNeedsUpdateFlag()
    {
        if (doubleBuffer != null)
            doubleBuffer.needsUpdate = true;
    }*/

    /**
     * Sets the double buffer needs update flag.
     * @see Component#repaint()
     */
    /*public void repaint() 
    {
        // double buffer must be updated
        // we have to set the flag here because we can't
        // be sure that repaint(long,int,int,int,int)
        // is called by super.repaint()
        setDoubleBufferNeedsUpdateFlag();
        super.repaint(0,0,0,getWidth(), getHeight());
    }*/

    /**
     * Sets the double buffer needs update flag.
     * @see Component#repaint(long)
     */
    /*public void repaint(long tm) 
    {
        // double buffer must be updated
        // we have to set the flag here because we can't
        // be sure that repaint(long,int,int,int,int)
        // is called by super.repaint(long)
        setDoubleBufferNeedsUpdateFlag();
        super.repaint(tm,0,0,getWidth(),getHeight());
    }*/

    /**
     * Sets the double buffer needs update flag.
     * @see Component#repaint(int, int, int, int)
     */
    /*public void repaint(int x, int y, int width, int height) 
    {
        // double buffer must be updated
        // we have to set the flag here because we can't
        // be sure that repaint(long,int,int,int,int)
        // is called by super.repaint(int,int,int,int)
        setDoubleBufferNeedsUpdateFlag();
        super.repaint(0, x, y, width, height);
    }*/

    /**
     * Sets the double buffer needs update flag.
     * @see JComponent#repaint(java.awt.Rectangle)
     */
    /*public void repaint(Rectangle r) 
    {
        // double buffer must be updated
        // we have to set the flag here because we can't
        // be sure that repaint(long,int,int,int,int)
        // is called by super.repaint(Rectangle)
        setDoubleBufferNeedsUpdateFlag();
        super.repaint(0,r.x,r.y,r.width,r.height);
    }*/
    
    /**
     * Sets the double buffer needs update flag.
     * @see Component#repaint(long, int, int, int, int)
     */
    /*public void repaint(long tm, int x, int y, int width, int height) 
    {
        // double buffer must be updated
        setDoubleBufferNeedsUpdateFlag();
        super.repaint(tm, x, y, width, height);    
    }*/

    /**
     * Calls paint.
     * @see JComponent#update(java.awt.Graphics)
     */
    public void update(Graphics g)
    {
        // call paint directly
        paint(g);
    }

    /**
     * Paints the component.
     * @see JComponent#paint(java.awt.Graphics)
     */
    public void paint(Graphics g)
    {
        // we do not change anything here
        super.paint(g);
    }

    /**
     * Paints the component's children.
     * @see JComponent#paintChildren(java.awt.Graphics)
     */
    protected void paintChildren(Graphics g)
    {
        // no change
        super.paintChildren(g);
    }

    /**
     * Paints the component's border. If 
     * {@link #hasStaticLayerBackingStore()} is true
     * the border is not painted because the component
     * assumes, that the static layer backing store image
     * already contains the painted border.
     * 
     * Overriding this method is not possible, instead
     * overwrite {@link #renderBorder(Graphics)}.
     * 
     * @see #renderBorder(Graphics)
     * @see JComponent#paintBorder(java.awt.Graphics)
     */
    protected void paintBorder(Graphics g)
    {
        // paint(java.awt.Graphics) calls
        // paintComponent(java.awt.Graphics)
        // then paintBorder(java.awt.Graphics)
        // If the static layer image is available it
        // contains the border and we do not have to
        // paint it again.
        /*
        if (hasStaticLayerBackingStore())
            return;
*/
        renderBorder(g);
    }
    
    /**
     * Paints the component's border.
     * @see #paintBorder(Graphics)
     */
    protected void renderBorder(Graphics g)
    {
        super.paintBorder(g);
    }

    /**
     * Paints the static and dynamic layers of this component.
     * @see JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g)
    {
        paintComponentWithoutDoubleBuffer((Graphics2D)g);
        /*
        
        if (doubleBuffer == null && isNonVolatileDoubleBufferEnabled() && isDisplayable())
            doubleBuffer = new DoubleBuffer();
        else
        {
            paintComponentWithoutDoubleBuffer((Graphics2D)g);
            return;
        }
        
        doubleBuffer.prepareNonVolatile(this);
        if (doubleBuffer.needsUpdate)
        {
            Graphics2D dbGraphics = doubleBuffer.createOffscreenGraphics();
            try
            {
                dbGraphics.setFont(g.getFont());
                dbGraphics.setColor(g.getColor());
                doubleBuffer.needsUpdate = false;
                paintComponentWithoutDoubleBuffer(dbGraphics);
            }
            finally
            {
                dbGraphics.dispose();
            }
        }
        doubleBuffer.flip(g);
        */
    }

    // double buffer 
    private transient DoubleBuffer db;
    
    protected void setDoubleBufferNeedsUpdate()
    {
        if (db != null) db.needsUpdate = true;
    }

    protected void paintComponentWithDoubleBuffer(Graphics g)
    {
        if (db == null) db = new DoubleBuffer(false);
        db.prepareNonVolatile(this);
        if (db.needsUpdate)
        {
            Graphics2D bufferG2 = db.createOffscreenGraphics();
            try
            {
                bufferG2.setFont(getFont());
                bufferG2.setColor(getBackground());
                paintComponentWithoutDoubleBuffer(bufferG2);
            }
            finally
            {
                bufferG2.dispose();
            }
            db.needsUpdate = false;
        }
        db.flip(g);
    }
    
    /**
     * Paints the static and dynamic layers of this component without
     * double buffering.
     */
    protected final void paintComponentWithoutDoubleBuffer(Graphics2D g2)
    {
        // paintStaticLayer
        paintStaticLayerOrBackingStore(g2);
        // paintDynamicLayer
        paintDynamicLayer(g2);
    }
    
    /**
     * Paints the static layer of this component. Uses
     * the static layer backing store image instead if available.
     */
    protected void paintStaticLayerOrBackingStore(Graphics2D g2)
    {/*
        if (hasStaticLayerBackingStore())
        {
            int r = getWidth();
            int b = getHeight();
            int ox = getStaticLayerBackingStoreOffsetX();
            int oy = getStaticLayerBackingStoreOffsetY();
           
            g2.drawImage(
                    // image
                    staticLayerBackingStore, 
                    // destination
                    0, 0, r, b,
                    // source
                    ox, oy, ox+r, oy+b,
                    // ImageObserver
                    null);
        }
        else*/
        {
            paintStaticLayer(g2);
        }
    }

    /**
     * Calls the UI delegate's
     * {@link JTComponentUI#paintStaticLayer(Graphics2D, JComponent)} method. 
     */
    protected void paintStaticLayer(Graphics2D g2)
    {
        if (ui != null)
        {
            Graphics2D scratchGraphics = g2 == null ? null : (Graphics2D) g2.create();
            try
            {
                ((JTComponentUI)ui).paintStaticLayer(scratchGraphics, this);
            }
            finally
            {
                if (scratchGraphics != null)
                    scratchGraphics.dispose();
            }
        }
    }

    /**
     * Calls the UI delegate's
     * {@link JTComponentUI#paintDynamicLayer(Graphics2D, JComponent)} method. 
     */
    protected void paintDynamicLayer(Graphics2D g2)
    {
        if (ui != null)
        {
            Graphics2D scratchGraphics = g2 == null ? null : (Graphics2D) g2.create();
            try
            {
                ((JTComponentUI)ui).paintDynamicLayer(scratchGraphics, this);
            }
            finally
            {
                if (scratchGraphics != null)
                    scratchGraphics.dispose();
            }
        }
    }
    
    /**
     * Renderes the static layer of this component and of each 
     *   child that is subclass of JTComponent using the specified graphics.
     */
    protected void renderStaticLayerImage(Graphics2D g)
    {
        g.setFont(getFont());
        g.setColor(getForeground());
        paintStaticLayer(g);
        renderBorder(g);
        for (int i=getComponentCount()-1;i>=0;i--)
        {
            Component c = getComponent(i);
            if (c instanceof JTComponent)
            {
                Graphics2D gchild = (Graphics2D)
                    g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
                try
                {
                    gchild.setFont(c.getFont());
                    gchild.setColor(c.getForeground());
                    ((JTComponent) c).paintStaticLayer(gchild);
                    ((JTComponent) c).renderBorder(gchild);
                }
                finally
                {
                    gchild.dispose();
                }
            }
        }
    }

    /**
     * The non volatile double buffer.
     */
    private static class DoubleBuffer
    {

        private GraphicsConfiguration gc;
        private BufferedImage image;
        boolean needsUpdate;
        int w;
        int h;
        private boolean containsBorder;
        private Insets insets = new Insets(0,0,0,0);
        
        public DoubleBuffer(boolean containsBorder)
        {
            this.containsBorder = containsBorder;
        }

        public void prepareNonVolatile(JTComponent c)
        {
            int oldw = w;
            int oldh = h;
            w = c.getWidth();
            h = c.getHeight();

            if (w<1) w=1;
            if (h<1) h=1;
            
            if (image == null || oldw!=w || oldh!=h)
            {
                flush();
                image = createNonVolatileImage(c, w, h);
            }
            else if (needsUpdate && (containsBorder && !c.isOpaque()))
            {
                erase(image, w, h);
            }
        }

        public Graphics2D createOffscreenGraphics()
        { 
            return image.createGraphics();
        }

        private void erase(Image img, int w, int h)
        {
             Graphics2D g2 = (Graphics2D) img.getGraphics();
             try
             {
                 g2.setComposite(AlphaComposite.Clear);
                 g2.fillRect(0, 0, w, h);
             }
             finally
             {
                 g2.dispose();
             }
        }

        private BufferedImage createNonVolatileImage( JTComponent c, int w, int h )
        {
            if (gc == null)
            { 
                gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
            }
            insets = c.getInsets(insets);
            boolean opaque = c.isOpaque();
            if (containsBorder)
            {
                Border b = c.getBorder();
                if (b != null) opaque &= b.isBorderOpaque();
            }
            
            // Create a non-volatile image that can be optimally blitted.
            // The image is translucent depending on the components opacity setting.
            return gc.createCompatibleImage(w, h, opaque ?  
                    Transparency.OPAQUE : Transparency.TRANSLUCENT);
        }

        public void flush()
        {
            needsUpdate = true;
            if (image != null)
            {
                image.flush();
                image = null;
            }
        }

        public void flip( Graphics g )
        {
            int t = insets.top;
            int b = insets.bottom;
            int l = insets.left;
            int r = insets.right;
            
            int dx2 = Math.max(0, w-r-l);
            int dy2 = Math.max(0, h-b-t);
            
            g.drawImage(image, l, t, l+dx2, t+dy2, 
                    l, t, l+dx2, t+dy2, null);
        }
        
    }

}
