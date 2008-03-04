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
import java.awt.Rectangle;
import java.lang.ref.SoftReference;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.PComponent;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PLight;
import net.sf.nmedit.jpatch.PLightDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PModuleMetrics;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PUndoableEditFactory;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PModuleListener;
import net.sf.nmedit.jpatch.util.ObjectCache;
import net.sf.nmedit.jpatch.util.ObjectFilter;
import net.sf.nmedit.jpatch.util.ObjectFilterResult;

/**
 * The reference implementation of interface {@link PModule}.
 * @author Christian Schneider
 */
public class PBasicModule extends PBasicComponent<PModuleDescriptor> implements PModule
{

    private static final PBasicParameter[] EMPTYP = new PBasicParameter[0];
    private static final PConnector[] EMPTYC = new PConnector[0];
    private static final PLight[] EMPTYL = new PLight[0];
    
    
    private String title;
    private PBasicParameter[] parameters;
    private PConnector[] connectors;
    private PLight[] lights;
    private PModuleContainer parent;
    private EventListenerList listenerList = new EventListenerList();
    protected int sx = 0;
    protected int sy = 0;
    private SoftReference<ParameterCache> paramCacheRef = null;

    public PBasicModule(PModuleDescriptor descriptor)
    {
        super(descriptor, -1);
        this.title = descriptor.getName();
        createComponents();
    }
    
    public List<PParameter> getParameters(ObjectFilter<PParameter> filter)
    {
        ParameterCache cache;
        if (paramCacheRef == null || (cache = paramCacheRef.get()) == null)
            paramCacheRef = new SoftReference<ParameterCache>(cache = new ParameterCache());
        return cache.getItems(filter);
    }
    
    private class ParameterCache extends ObjectCache<PParameter>
    {
        @Override
        protected List<PParameter> applyFilter(ObjectFilter<PParameter> filter)
        {
            return ObjectFilterResult.filter(parameters, filter);
        }
    }
    
    void setComponentIndex(int index)
    {
        this.componentIndex = index;
    }
    
    public String getTitle()
    {
        return title;
    }

    protected UndoableEdit createRenameEdit(String oldtitle, String newtitle)
    {
        PUndoableEditFactory factory = getUndoableEditFactory();
        if (factory != null)
            return factory.createRenameEdit(this, oldtitle, newtitle);
        return null;
    }

    protected UndoableEdit createMoveEdit(int oldScreenX, int oldScreenY,
            int newScreenX, int newScreenY)
    {
        PUndoableEditFactory factory = getUndoableEditFactory();
        if (factory != null)
            return factory.createMoveEdit(this, oldScreenX, oldScreenY,
                    newScreenX, newScreenY);
        return null;
    }
    
    public void setTitle(String title)
    {        
        String oldtitle = this.title;
        String newtitle = title;
        if (!(oldtitle == newtitle || (oldtitle!=null && title.equals(oldtitle))))
        {
            this.title = newtitle;
            if (isUndoableEditSupportEnabled())
            {
                UndoableEdit edit = createRenameEdit(oldtitle, newtitle);
                if (edit != null) postEdit(edit);
            }
            fireModuleRenamed(oldtitle, newtitle);
        }
    }

    public void addModuleListener(PModuleListener l)
    {
        listenerList.add(PModuleListener.class, l);
    }

    public void removeModuleListener(PModuleListener l)
    {
        listenerList.remove(PModuleListener.class, l);
    }

    protected void fireModuleRenamed(String oldTitle, String newTitle)
    {
        PModuleEvent moduleEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PModuleListener.class) 
            {
                // Lazily create the event:
                if (moduleEvent == null)
                {
                    moduleEvent = new PModuleEvent(this);
                    moduleEvent.moduleRenamed(oldTitle);
                }
                ((PModuleListener)listeners[i+1]).moduleRenamed(moduleEvent);
            }
        }
    }

    protected void fireModuleMoved(int oldScreenX, int oldScreenY)
    {
        PModuleEvent moduleEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PModuleListener.class) 
            {
                // Lazily create the event:
                if (moduleEvent == null)
                {
                    moduleEvent = new PModuleEvent(this);
                    moduleEvent.moduleMoved(oldScreenX, oldScreenY);
                }
                ((PModuleListener)listeners[i+1]).moduleMoved(moduleEvent);
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
    protected PBasicParameter createParameter(PParameterDescriptor descriptor, int componentIndex)
    {
        return new PBasicParameter(descriptor, this, componentIndex);
    }
    
    protected PLight createLight(PLightDescriptor descriptor, int componentIndex)
    {
        return new PBasicLight(descriptor, this, componentIndex);
    }
    
    /**
     * Creates the components of this module: connectors and parameters.
     * 
     * @see #createConnector(PConnectorDescriptor,int)
     * @see #createParameter(PParameterDescriptor,int)
     */
    protected void createComponents()
    {
        PModuleDescriptor d = getDescriptor();
        
        if (d.getConnectorDescriptorCount()>0)
        {
            connectors = new PConnector[d.getConnectorDescriptorCount()];
            for (int i = connectors.length-1; i>=0; i--)
                connectors[i] = createConnector(d.getConnectorDescriptor(i), i);
        }
        else
        {
            connectors = EMPTYC;
        }
        int offset = connectors.length;
        if (d.getParameterDescriptorCount()>0)
        {
            boolean extensions = false;
            parameters = new PBasicParameter[d.getParameterDescriptorCount()];
            for (int i = parameters.length-1; i>=0; i--)
            {
                PParameterDescriptor p = d.getParameterDescriptor(i);
                parameters[i] = createParameter(p, offset+i);
                extensions |= p.getExtensionDescriptor()!=null;
            }

            if (extensions)
            {
                // link extensions
                
                for (int i=parameters.length-1;i>=0;i--)
                {
                    PBasicParameter p = parameters[i];
                    PParameterDescriptor ext = p.getDescriptor().getExtensionDescriptor();
                    if (ext!=null) p.setExtensionParameter(parameters[ext.getDescriptorIndex()]);
                }
            }
        }
        else
        {
            parameters = EMPTYP;
        }
        offset+=parameters.length;
        if (d.getLightDescriptorCount()>0)
        {
            lights = new PLight[d.getLightDescriptorCount()];
            for (int i = lights.length-1; i>=0; i--)
                lights[i] = createLight(d.getLightDescriptor(i), offset+i);
        }
        else
        {
            lights = EMPTYL;
        }
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
            if (index<parameters.length)
            {
                return parameters[index];
            }
            else
            {
                index -= parameters.length;
                return lights[index];
            }
        }
    }

    public int getComponentCount()
    {
        return connectors.length+parameters.length+lights.length;
    }

    public PConnector getConnector(PConnectorDescriptor descriptor)
    {
        PDescriptor parent = descriptor.getParentDescriptor();
        PDescriptor thisDescriptor = getDescriptor();
        if (!(thisDescriptor == parent || thisDescriptor.equals(parent)))
            throw new InvalidDescriptorException(descriptor, "parents are not equal "+descriptor+" in "+this);
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
        PDescriptor parent = descriptor.getParentDescriptor();
        if (!(getDescriptor() == parent || getDescriptor().equals(parent)))
            throw new InvalidDescriptorException(descriptor, "parents are not equal "+descriptor+" in "+this);
        int index = descriptor.getDescriptorIndex();
        if (index<0 || index>=getParameterCount())
            throw new InvalidDescriptorException(descriptor, "invalid parameter index: "+index);
        return parameters[index];
    }

    public PLight getLight(PLightDescriptor descriptor)
    {
        PDescriptor parent = descriptor.getParentDescriptor();
        if (!(getDescriptor() == parent || getDescriptor().equals(parent)))
            throw new InvalidDescriptorException(descriptor, "parents are not equal "+descriptor+" in "+this);
        int index = descriptor.getDescriptorIndex();
        if (index<0 || index>=getLightCount())
            throw new InvalidDescriptorException(descriptor, "invalid light index: "+index);
        return lights[index];
    }

    public PParameter getParameter(int index)
    {
        return parameters[index];
    }

    public PLight getLight(int index)
    {
        return lights[index];
    }

    public int getLightCount()
    {
        return lights.length;
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
        if (d != null) return getConnector(d); 
        else return null;
    }

    public PParameter getParameterByComponentId(Object id)
    {
        PParameterDescriptor d = getDescriptor().getParameterByComponentId(id);
        if (d != null) return getParameter(d);
        else return null;
    }

    public PLight getLightByComponentId(Object id)
    {
        PLightDescriptor d = getDescriptor().getLightDescriptorByComponentId(id);
        if (d != null) return getLight(d);
        else return null;
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
            
            x = m.alignScreenX(x);
            y = m.alignScreenY(y);
            
            x = Math.max(0, Math.min(x, m.getMaxScreenX()));
            y = Math.max(0, Math.min(y, m.getMaxScreenY()));
        }
        int oldx = this.sx;
        int oldy = this.sy;
        
        if (oldx!=x||oldy!=y)
        {
            this.sx = x;
            this.sy = y;

            if (isUndoableEditSupportEnabled())
            {
                UndoableEdit edit = createMoveEdit(oldx, oldy, x, y);
                if (edit != null) postEdit(edit);
            }
            
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
    
    public Rectangle getInternalBounds(Rectangle rv) {
        if (rv == null) {
            return new Rectangle(getInternalX(), getInternalY(), getInternalWidth(), getInternalHeight());
        }
        else {
            rv.setBounds(getInternalX(), getInternalY(), getInternalWidth(), getInternalHeight());
            return rv;
        }
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
    
    public Rectangle getScreenBounds(Rectangle rv) {
        if (rv == null) {
            return new Rectangle(getScreenX(), getScreenY(), getScreenWidth(), getScreenHeight());
        }
        else {
            rv.setBounds(getScreenX(), getScreenY(), getScreenWidth(), getScreenHeight());
            return rv;
        }
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
        return getClass().getName()+"[name="+getName()+",component-id="+getComponentId()+",component-index="+getComponentIndex()+",title="+title+"]";
    }

	public PModule cloneModule() {
		PBasicModule newM = new PBasicModule(getDescriptor());
		newM.cloneFromModule(this);
		return newM;
	}
	
	public void cloneFromModule(PModule m) {
		for (int i = 0; i < m.getParameterCount(); i++) {
			PParameter src = m.getParameter(i);
			PParameter dst = getParameter(i);
			dst.setDoubleValue(src.getDoubleValue());
			dst.setMorphGroup(src.getMorphGroup());
		}
		title = m.getTitle();
		sx = m.getScreenX();
		sy = m.getScreenY();
	}
}
