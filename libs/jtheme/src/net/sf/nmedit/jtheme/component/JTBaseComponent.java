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
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.help.HelpHandler;
import net.sf.nmedit.jtheme.help.HelpSupport;

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
public class JTBaseComponent extends JComponent implements HelpSupport
{

    protected static final int FLAG_INVALIDATE = 1;
    protected static final int FLAG_VALIDATE = 2;
    protected static final int FLAG_REVALIDATE = 3;
    protected static final int FLAG_VALIDATE_TREE = 4;
    protected static final int FLAG_PROPERTY_SUPPORT = 5;
    protected static final int DEFAULT_FLAGS = 
        FLAG_INVALIDATE;
    
    private int jtflags = DEFAULT_FLAGS;

    protected void setJTFlag(int aFlag, boolean aValue) {
        if(aValue) {
            jtflags |= (1 << aFlag);
        } else {
            jtflags &= ~(1 << aFlag);
        }
    }
    
    protected boolean getJTFlag(int aFlag) {
        int mask = (1 << aFlag);
        return ((jtflags & mask) == mask);
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 4189716019536899996L;

    // the context
    private JTContext context;

    public JTBaseComponent(JTContext context)
    {
        this.context = context;
    }

    public HelpHandler getHelpHandler()
    {
        Container c = getParent();
        if (c != null && c instanceof HelpSupport)
            return ((HelpSupport) c).getHelpHandler();
        return null;
    }
    
    public void showHelpFor(Object o)
    {
        HelpHandler h = getHelpHandler();
        if (h != null) h.showHelpFor(o);
    }
    
    public boolean hasHelpFor(Object o)
    {
        HelpHandler h = getHelpHandler();
        return h != null && h.hasHelpFor(o);
    }
    
    public JTContext getContext()
    {
        return context;
    }
    
    public void enableJTFlags()
    {
        setJTFlag(FLAG_INVALIDATE, true);
        setJTFlag(FLAG_VALIDATE, true);
        setJTFlag(FLAG_REVALIDATE, true);
        setJTFlag(FLAG_VALIDATE_TREE, true);
        setJTFlag(FLAG_PROPERTY_SUPPORT, true);
    }

    
    /**
     * Returns a mutable list of all components, which are instance
     * of the specified base class, in this container.
     * @return components in this container
     */
    public <C extends Component> List<C> getComponents(Class<C> base)
    {
        int count = getComponentCount(); // >= result.size();
        List<C> components = new ArrayList<C>(count);
        if (count>0) // grab lock only when necessary
        {
            synchronized (getTreeLock())
            {
                for (int i=0;i<count;i++)
                {
                    Component c = getComponent(i);
                    if (base.isInstance(c))
                        components.add(base.cast(c));
                }
            }
        }
        return components;
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void invalidate() 
    {
        if (getJTFlag(FLAG_INVALIDATE))
            super.invalidate();
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void validate() 
    {
        if (getJTFlag(FLAG_VALIDATE))
            super.validate();
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void revalidate() 
    {
        if (getJTFlag(FLAG_REVALIDATE))
            super.revalidate();
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    protected void validateTree() 
    {
        if (getJTFlag(FLAG_VALIDATE_TREE))
            super.validateTree();
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.addPropertyChangeListener(listener); 
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.addPropertyChangeListener(propertyName, listener); 
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
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, short oldValue, short newValue) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, long oldValue, long newValue) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, float oldValue, float newValue) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, double oldValue, double newValue) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
            super.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Optimization: calling this method does nothing.
     */
    public void firePropertyChange(String propertyName, char oldValue, char newValue) 
    {
        if (getJTFlag(FLAG_PROPERTY_SUPPORT))
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
   
}
