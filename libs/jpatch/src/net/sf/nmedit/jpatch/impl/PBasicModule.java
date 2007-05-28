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
package net.sf.nmedit.jpatch.impl;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.PComponent;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PModuleMetrics;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.event.ModuleEvent;
import net.sf.nmedit.jpatch.event.ModuleListener;
import net.sf.nmedit.jpatch.event.PParameterListener;

public class PBasicModule extends PBasicComponent<PModuleDescriptor> implements PModule
{

    private String title;
    private PParameter[] parameters;
    private PConnector[] connectors;
    private PModuleContainer parent;
    private EventListenerList listenerList = new EventListenerList();
    protected int sx = 0;
    protected int sy = 0;

    public PBasicModule(PModuleDescriptor descriptor)
    {
        super(descriptor, -1);
        this.title = descriptor.getName();
        createComponents();
    }
    
    void setComponentIndex(int index)
    {
        this.componentIndex = index;
    }
    
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        String oldTitle = this.title;
        if (oldTitle != title || (title!=null && (!title.equals(oldTitle))))
        {
            this.title = title;
            fireModuleRenamed(oldTitle, title);
        }
    }

    public void addModuleListener(ModuleListener l)
    {
        listenerList.add(ModuleListener.class, l);
    }

    public void removeModuleListener(ModuleListener l)
    {
        listenerList.remove(ModuleListener.class, l);
    }

    protected void fireModuleRenamed(String oldTitle, String newTitle)
    {
        ModuleEvent moduleEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PParameterListener.class) 
            {
                // Lazily create the event:
                if (moduleEvent == null)
                {
                    moduleEvent = new ModuleEvent(this);
                    moduleEvent.moduleRenamed(oldTitle);
                }
                ((ModuleListener)listeners[i+1]).moduleRenamed(moduleEvent);
            }
        }
    }

    protected void fireModuleMoved(int oldScreenX, int oldScreenY)
    {
        ModuleEvent moduleEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PParameterListener.class) 
            {
                // Lazily create the event:
                if (moduleEvent == null)
                {
                    moduleEvent = new ModuleEvent(this);
                    moduleEvent.moduleMoved(oldScreenX, oldScreenY);
                }
                ((ModuleListener)listeners[i+1]).moduleMoved(moduleEvent);
            }
        }
    }

    /**
     * Creates a connector component for this module using the specified descriptor.
     * Overwrite this to use a different connector implementation.
     * 
     * @param descriptor describes the connector that will be created 
     * @return connector
     */
    protected PConnector createConnector(PConnectorDescriptor descriptor, int componentIndex)
    {
        return new PBasicConnector(this, descriptor, componentIndex);
    }

    /**
     * Creates a parameter component for this module using the specified descriptor.
     * Overwrite this to use a different parameter implementation.
     * 
     * @param descriptor describes the parameter that will be created 
     * @return parameter
     */
    protected PParameter createParameter(PParameterDescriptor descriptor, int componentIndex)
    {
        return new PBasicParameter(descriptor, this, componentIndex);
    }
    
    /**
     * Creates the components of this module: connectors and parameters.
     * 
     * @see #createConnector(PConnectorDescriptor)
     * @see #createParameter(PParameterDescriptor)
     */
    protected void createComponents()
    {
        PModuleDescriptor d = getDescriptor();
        connectors = new PConnector[d.getConnectorDescriptorCount()];
        for (int i = connectors.length-1; i>=0; i--)
            connectors[i] = createConnector(d.getConnectorDescriptor(i), i);
        parameters = new PParameter[d.getParameterDescriptorCount()];
        for (int i = parameters.length-1; i>=0; i--)
            parameters[i] = createParameter(d.getParameterDescriptor(i), connectors.length+i);
    }

    public PComponent getComponent(int index)
    {
        if (index<connectors.length)
        {
            return connectors[index];
        }
        else
        {
            index -= connectors.length;
            return parameters[index];
        }
    }

    public int getComponentCount()
    {
        return connectors.length+parameters.length;
    }

    public PConnector getConnector(PConnectorDescriptor descriptor)
    {
        if (getDescriptor() != descriptor.getParentDescriptor())
            throw new InvalidDescriptorException(descriptor, "parents are not equal");
        int index = descriptor.getDescriptorIndex();
        if (index<0 || index>=getConnectorCount())
            throw new InvalidDescriptorException(descriptor, "invalid connector index: "+index);
        return connectors[index];
    }

    public PConnector getConnector(int index)
    {
        return connectors[index];
    }

    public int getConnectorCount()
    {
        return connectors.length;
    }

    public PParameter getParameter(PParameterDescriptor descriptor)
    {
        if (getDescriptor() != descriptor.getParentDescriptor())
            throw new InvalidDescriptorException(descriptor, "parents are not equal");
        int index = descriptor.getDescriptorIndex();
        if (index<0 || index>=getParameterCount())
            throw new InvalidDescriptorException(descriptor, "invalid parameter index: "+index);
        return parameters[index];
    }

    public PParameter getParameter(int index)
    {
        return parameters[index];
    }

    public int getParameterCount()
    {
        return parameters.length;
    }

    public PModuleContainer getParentComponent()
    {
        return parent;
    }
    
    public void setParent(PModuleContainer parent)
    {
        PModuleContainer oldParent = this.parent;
        if (oldParent != parent)
        {
            this.parent = parent;
        }
    }

    public int getComponentIndex()
    {
        return componentIndex;
    }

    public void removeAllConnections()
    {
        PModuleContainer c = getParentComponent();
        if (c == null) return;
        PConnectionManager m = c.getConnectionManager();
        if (m != null) m.removeAllConnections(m.connections(this));
    }

    public PComponent getComponentByComponentId(Object id)
    {
        PDescriptor d = getDescriptor().getComponentByComponentId(id);
        if (d instanceof PConnectorDescriptor)
            return getConnector((PConnectorDescriptor)d);
        else if (d instanceof PParameterDescriptor)
            return getParameter((PParameterDescriptor)d);
        else 
            return null;
    }

    public PConnector getConnectorByComponentId(Object id)
    {
        PConnectorDescriptor d = getDescriptor().getConnectorByComponentId(id);
        return d!=null ? getConnector(d) : null;
    }

    public PParameter getParameterByComponentId(Object id)
    {
        PParameterDescriptor d = getDescriptor().getParameterByComponentId(id);
        return d!=null ? getParameter(d) : null;
    }

    public Point getScreenLocation()
    {
        return new Point(getScreenX(), getScreenY());
    }

    public int getScreenX()
    {
        return sx;
    }

    public int getScreenY()
    {
        return sy;
    }

    public void setScreenLocation(int x, int y)
    {
        PModuleMetrics m = getModuleMetrics();
        if (m != null)
        {
            x = Math.max(0, Math.min(x, m.getMaxScreenX()));
            y = Math.max(0, Math.min(y, m.getMaxScreenY()));
        }
        int oldx = this.sx;
        int oldy = this.sy;
        
        if (oldx!=x||oldy!=y)
        {
            this.sx = x;
            this.sy = y;
            fireModuleMoved(oldx, oldy);
        }
    }

    public void setScreenLocation(Point location)
    {
        setScreenLocation(location.x, location.y);
    }

    public PPatch getPatch()
    {
        PModuleContainer mc = getParentComponent();
        return mc == null ? null : mc.getPatch();
    }

    public PModuleMetrics getModuleMetrics()
    {
        return getDescriptor().getModules().getMetrics();
    }

    public int getInternalX()
    {
        PModuleMetrics m = getModuleMetrics();
        return m == null ? sx : m.screenToInternalX(sx);
    }

    public int getInternalY()
    {
        PModuleMetrics m = getModuleMetrics();
        return m == null ? sy : m.screenToInternalY(sy);
    }

    public Point getInternalLocation()
    {
        return new Point(getInternalX(), getInternalY());
    }
    
    public void setInternalLocation(int x, int y)
    {
        PModuleMetrics m = getModuleMetrics();
        if (m == null) setScreenLocation(x, y);
        else setScreenLocation(m.internalToScreenX(x), m.internalToScreenY(y));
    }

    public void setInternalLocation(Point location)
    {
        setInternalLocation(location.x, location.y);
    }

    public int getInternalHeight()
    {
        PModuleMetrics m = getModuleMetrics();
        return m == null ? getIntAttribute("height", 1) : m.getInternalHeight(this);
    }

    public int getInternalWidth()
    {
        PModuleMetrics m = getModuleMetrics();
        return m == null ? getIntAttribute("width", 1) : m.getInternalWidth(this);
    }

    public int getScreenWidth()
    {
        PModuleMetrics m = getModuleMetrics();
        return m == null ? getIntAttribute("width", 1) : m.getScreenWidth(this);
    }

    public int getScreenHeight()
    {
        PModuleMetrics m = getModuleMetrics();
        return m == null ? getIntAttribute("height", 1) : m.getScreenHeight(this);
    }

    public void setInternalSize(int width, int height)
    {
        // ignore
    }

    public void setInternalSize(Dimension size)
    {
        setInternalSize(size.width, size.height);
    }

    public void setScreenSize(int width, int height)
    {
        PModuleMetrics m = getModuleMetrics();
        if (m != null)
            setInternalSize(m.screenToInternalX(width), m.screenToInternalY(height));
    }

    public void setScreenSize(Dimension size)
    {
        setScreenSize(size.width, size.height);
    }
    
    public String toString()
    {
        return getClass().getName()+"[name="+getName()+",component-id="+getComponentId()+"]";
    }
    
}
