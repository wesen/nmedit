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

import java.util.Iterator;

import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.PComponent;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PLight;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PRoles;
import net.sf.nmedit.jpatch.PUndoableEditFactory;
import net.sf.nmedit.jpatch.history.PUndoableEditSupport;

/**
 * The reference implementation of interface {@link PComponent}.
 * @author Christian Schneider
 */
public abstract class PBasicComponent<P extends PDescriptor> implements PComponent
{
    
    /**
     * The descriptor of this component
     */
    private P descriptor;
    protected int componentIndex = -1;
    private transient PUndoableEditFactory editFactory;

    public PBasicComponent(P descriptor, int componentIndex)
    {
        assert descriptor != null 
            : "the specified descriptor is null";
        this.descriptor = descriptor;
        this.componentIndex = componentIndex;
    }
    
    public PUndoableEditSupport getEditSupport()
    {
        PComponent parent = getParentComponent();
        return (parent != null) ? parent.getEditSupport() : null;
    }
    
    /**
     * Posts edits to the parent component.
     * Overwrite this to implement different bahaviour,
     * for example like notifying listeners.
     * 
     * @param edit
     */
    public void postEdit(UndoableEdit edit)
    {
        PComponent parent = getParentComponent();
        if (parent != null) parent.postEdit(edit);
    }
    
    public boolean isUndoableEditSupportEnabled()
    {
        PComponent parent = getParentComponent();
        return parent != null && parent.isUndoableEditSupportEnabled();
    }

    public PUndoableEditFactory getUndoableEditFactory()
    {
        if (this.editFactory == null)
        {
            PComponent parent = getParentComponent();
            if (parent != null)
                this.editFactory = parent.getUndoableEditFactory();
        }
        return this.editFactory;
    }
    
    public P getDescriptor()
    {
        return descriptor;
    }
    
    public PComponent getComponent(int index)
    {
        throw new IndexOutOfBoundsException("index out of bounds:"+index);
    }

    public int getComponentCount()
    {
        return 0;
    }

    public PComponent getParentComponent()
    {
        return null;
    }

    public String getName()
    {
        return getDescriptor().getName();
    }
    
    public int getComponentIndex()
    {
        return componentIndex;
    }
    
    public Object getComponentId()
    {
        return getDescriptor().getComponentId();
    }
    
    public PComponent getComponentByComponentId(Object id)
    {
        return null;
    }

    public PConnector getConnectorByComponentId(Object id)
    {
        return null;
    }

    public PParameter getParameterByComponentId(Object id)
    {
        return null;
    }

    public PLight getLightByComponentId(Object id)
    {
        return null;
    }
    
    public Object getAttribute(String name)
    {
        return getDescriptor().getAttribute(name);
    }
    
    public void setAttribute(String name, Object value)
    {
        getDescriptor().setAttribute(name, value);
    }
    
    public int getIntAttribute(String name, int defaultValue)
    {
        return getDescriptor().getIntAttribute(name, defaultValue);
    }

    public float getFloatAttribute(String name, float defaultValue)
    {
        return getDescriptor().getFloatAttribute(name, defaultValue);
    }

    public double getDoubleAttribute(String name, double defaultValue)
    {
        return getDescriptor().getDoubleAttribute(name, defaultValue);
    }
    
    public String getStringAttribute(String name)
    {
        return getDescriptor().getStringAttribute(name);
    }
    
    public boolean getBooleanAttribute(String name, boolean defaultValue)
    {
        return getDescriptor().getBooleanAttribute(name, defaultValue);
    }
    
    public int getAttributeCount()
    {
        return getDescriptor().getAttributeCount();
    }
    
    public Iterator<String> attributeKeys()
    {
        return getDescriptor().attributeKeys();
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("[name=");
        sb.append(getName());
        sb.append(",component-id=");
        sb.append(getComponentId());
        sb.append(",attributes={");
        
        Iterator<String> keys = attributeKeys();
        while (keys.hasNext())
        {
            String k = keys.next();
            sb.append(k);
            sb.append("=");
            sb.append(getAttribute(k));
            sb.append(";");
        }
            
        sb.append("}");
        
        sb.append(",parent=");
        sb.append(getParentComponent());
        sb.append("]");
        return sb.toString();
    }
    
    public PRoles getRoles()
    {
        return descriptor.getRoles();
    }
    
}
