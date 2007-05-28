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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import net.sf.nmedit.jpatch.ImageSource;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;

public class PBasicDescriptor implements PDescriptor, Serializable
{
    private static final long serialVersionUID = -7021888357211502978L;
    private transient Map<String, Object> attributeMap = null;
    private int descriptorIndex = -1;
    private String name;
    private Object componentId;

    public PBasicDescriptor(String name, Object componentId)
    {
        this.name = name;
        this.componentId = componentId;
    }
    
    protected Map<String, Object> attributeMap()
    {
        if (attributeMap == null)
            attributeMap = createAttributeMap();
        return attributeMap;
    }
    
    protected Map<String, Object> createAttributeMap()
    {
        return new HashMap<String, Object>();
    }

    public Object getAttribute(String name)
    {
        return attributeMap == null
            ? null : attributeMap.get(name);
    }

    public int getAttributeCount()
    {
        return attributeMap == null ? 0 : attributeMap.size();
    }

    public int getIntAttribute(String name, int defaultValue)
    {
        Object att = getAttribute(name);
        return (att == null || (!(att instanceof Integer)))
        ? defaultValue : ((Integer)att).intValue();
    }

    public boolean getBooleanAttribute(String name, boolean defaultValue)
    {
        Object att = getAttribute(name);
        return (att == null || (!(att instanceof Boolean)))
        ? defaultValue : ((Boolean)att).booleanValue();
    }

    public String getName()
    {
        return name;
    }

    public PDescriptor getParentDescriptor()
    {
        return null;
    }

    public void setAttribute(String name, Object value)
    {
        attributeMap().put(name, value);
    }

    public Iterator<String> attributeKeys()
    {
        return attributeMap().keySet().iterator();
    }

    public String getStringAttribute(String name)
    {
        Object att = getAttribute(name);
        return (att == null || (!(att instanceof String)))
            ? null : (String) att;
    }

    public int getDescriptorIndex()
    {
        return descriptorIndex;
    }

    public void setDescriptorIndex(int index)
    {
        this.descriptorIndex = index;
    }

    public ImageSource get16x16Icon()
    {
        return getImage("icon16x16");
    }
    
    public ImageSource get32x32Icon()
    {
        return getImage("icon32x32");
    }

    public ImageSource getImage(String key)
    {
        Object att = getAttribute(key);
        return (att == null || (!(att instanceof ImageSource)))
        ? null : (ImageSource)att;
    }

    public <T> Iterator<T> iterator(final Class<T> clazz)
    {
        return new Iterator<T>()
        {
            Iterator<Object> iter = attributeMap().values().iterator();
            T next;
            void align()
            {
                if (next != null)
                {
                    while (iter.hasNext())
                    {
                        Object o = iter.next();
                        if (clazz.isInstance(o))
                        {
                            next = clazz.cast(o);
                            break;
                        }
                    }
                }
            }

            public boolean hasNext()
            {
                align();
                return next!=null;
            }

            public T next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                T e = next;
                next = null;
                return e;
            }

            public void remove()
            {
               throw new UnsupportedOperationException();
            }
        };
    }
    
    public Iterator<ImageSource> getImages()
    {
        return iterator(ImageSource.class);
    }

    public Object getComponentId()
    {
        return componentId;
    }

    public PDescriptor getComponentByComponentId(Object id)
    {
        return null;
    }

    public PConnectorDescriptor getConnectorByComponentId(Object id)
    {
        PDescriptor d = getComponentByComponentId(id);
        return d != null && d instanceof PConnectorDescriptor ?
                (PConnectorDescriptor) d : null;
    }

    public PParameterDescriptor getParameterByComponentId(Object id)
    {
        PDescriptor d = getComponentByComponentId(id);
        return d != null && d instanceof PParameterDescriptor ?
                (PParameterDescriptor) d : null;
    }

    private void disposeCache()
    {
        if (attributeMap != null)
        {
            Iterator<String> iter = attributeMap.keySet().iterator();
            while (iter.hasNext())
            {
                if (iter.next().startsWith(CACHE_KEY_PREFIX))
                    iter.remove();
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        disposeCache(); // remove all cache entries
        out.defaultWriteObject();
        out.writeObject(attributeMap());
    }
    
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        attributeMap = (Map<String,Object>) in.readObject();
    }
}
