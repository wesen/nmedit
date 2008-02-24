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
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.JTContext;

/**
 * A specialized version of JComponent.
 * 
 * This class uses several optimizations for better performance:
 * <ul>
 *  <li>PropertyChangeListeners are not supported. Related
 *    methods are not implemented.</li>
 *  <li>Layout manager support is removed. Related methods
 *    are not implemented: ({@link #doLayout()}, 
 *    {@link #invalidate()}, {@link #revalidate()},
 *    {@link #validate()}, {@link #validateTree()})</li>
 * </ul>
 * 
 * @author Christian Schneider
 */
public class JTBaseComponent extends JComponent
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 4189716019536899996L;

//    private transient JTBaseComponent repaintOrigin;
    
    // the context
    private JTContext context;
    
    private transient int roDX;
    private transient int roDY;

    public JTBaseComponent(JTContext context)
    {
        this.context = context;
        enableEvents(HierarchyEvent.HIERARCHY_EVENT_MASK | ComponentEvent.COMPONENT_MOVED);
    }

    protected void processComponentEvent(ComponentEvent e)
    {
        if (e.getID() == ComponentEvent.COMPONENT_MOVED)
            clearRepaintOrigin();
        
        super.processComponentEvent(e);
    }
    
    private void clearRepaintOrigin()
    {/*
        repaintOrigin = null;
        for (int i=getComponentCount()-1;i>=0;i--)
        {
            Component c = getComponent(i);
            if (c instanceof JTBaseComponent)
            {
                ((JTBaseComponent)c).clearRepaintOrigin();
            }
        }*/
    }

    protected void processHierarchyEvent(HierarchyEvent e) 
    {
        if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED)>0)
            clearRepaintOrigin();
        super.processHierarchyEvent(e);
    }
    
    public JTContext getContext()
    {
        return context;
    }
    
    protected boolean isRepaintOrigin()
    {
        return false;
    }
    
    /**
     * Throws an {@link UnsupportedOperationException}
     * This component does not support layout managers.
     * @throws UnsupportedOperationException
     */
    public void setLayout(LayoutManager layout)
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Returns null.
     * This component does not support layout managers.
     * @return returns null.
     */
    public LayoutManager getLayout()
    {
        return null;
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void doLayout() 
    {
        // no op
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void invalidate() 
    {
        // no op
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void validate() 
    {
        // no op
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void revalidate() 
    {
        // no op
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    protected void validateTree() 
    {
        // no op
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) 
    {
        // no op
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) 
    {
        // no op
    }
    
    /**
     * Allows calling {@link Component#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)}.
     */
    public void addPropertyChangeListenerIndirection(String propertyName, PropertyChangeListener listener) 
    {
        super.addPropertyChangeListener(propertyName, listener);
    }
    
    /**
     * Allows calling {@link Component#addPropertyChangeListener(java.beans.PropertyChangeListener)}.
     */
    protected void addPropertyChangeListenerIndirection(PropertyChangeListener listener)
    {
        super.addPropertyChangeListener(listener);
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) 
    {
        // no op
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) 
    {
        // no op
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, short oldValue, short newValue) 
    {
        // no op
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, long oldValue, long newValue) 
    {
        // no op
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) 
    {
        // no op
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, float oldValue, float newValue) 
    {
        // no op
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, double oldValue, double newValue) 
    {
        // no op
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, char oldValue, char newValue) 
    {
        // no op
    }

    /**
     * Allows calling {@link Component#firePropertyChange(java.lang.String, boolean, boolean)}
     */
    protected void firePropertyChangeIndirection(String propertyName, boolean oldValue, boolean newValue) 
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Allows calling {@link Component#firePropertyChange(java.lang.String, byte, byte)}
     */
    protected void firePropertyChangeIndirection(String propertyName, byte oldValue, byte newValue) 
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Allows calling {@link Component#firePropertyChange(java.lang.String, short, short)}
     */
    protected void firePropertyChangeIndirection(String propertyName, short oldValue, short newValue) 
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Allows calling {@link Component#firePropertyChange(java.lang.String, long, long)}
     */
    protected void firePropertyChangeIndirection(String propertyName, long oldValue, long newValue) 
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Allows calling {@link Component#firePropertyChange(java.lang.String, int, int)}
     */
    protected void firePropertyChangeIndirection(String propertyName, int oldValue, int newValue) 
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Allows calling {@link Component#firePropertyChange(java.lang.String, float, float)}
     */
    public void firePropertyChangeIndirection(String propertyName, float oldValue, float newValue) 
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Allows calling {@link Component#firePropertyChange(java.lang.String, double, double)}
     */
    public void firePropertyChangeIndirection(String propertyName, double oldValue, double newValue) 
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Allows calling {@link Component#firePropertyChange(java.lang.String, char, char)}
     */
    protected void firePropertyChangeIndirection(String propertyName, char oldValue, char newValue) 
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Sets the double buffer needs update flag.
     * @see Component#repaint()
     */
    public void repaint() 
    {
        repaint(0,0,0,getWidth(), getHeight());
    }

    /**
     * Sets the double buffer needs update flag.
     * @see Component#repaint(long)
     */
    public void repaint(long tm) 
    {
        repaint(tm,0,0,getWidth(),getHeight());
    }

    /**
     * Sets the double buffer needs update flag.
     * @see Component#repaint(int, int, int, int)
     */
    public void repaint(int x, int y, int width, int height) 
    {
        repaint(0, x, y, width, height);
    }

    /**
     * Sets the double buffer needs update flag.
     * @see JComponent#repaint(java.awt.Rectangle)
     */
    public void repaint(Rectangle r) 
    {
        repaint(0,r.x,r.y,r.width,r.height);
    }
    /*
    private JTBaseComponent findRepaintOrigin()
    {
        if (repaintOrigin == null)
        {
            roDX = getX();
            roDY = getY();
            Container c = getParent();
            while (c != null && c instanceof JTBaseComponent)
            {
                JTBaseComponent b = (JTBaseComponent) c;
                if (b.isRepaintOrigin())
                {
                    repaintOrigin = b;
                    return b;
                }
                roDX += c.getX();
                roDY += c.getY();
                c = c.getParent();
            }
            roDX = roDY = 0;
            repaintOrigin = this;
        }
        return repaintOrigin;
    }
    */
    /**
     * Sets the double buffer needs update flag.
     * @see Component#repaint(long, int, int, int, int)
     */
    /*
    public void repaint(long tm, int x, int y, int width, int height) 
    {   
        JTBaseComponent origin = findRepaintOrigin();
        if (origin != this)
        {/*
            Container c = this;
            while (c != null && c != origin)
            {
                x+=c.getX();
                y+=c.getY();
                c = c.getParent();
            }*//*
            origin.repaint(tm, roDX, roDY, width, height);
        }
        else
            super.repaint(tm, x, y, width, height);
    }*/

}
