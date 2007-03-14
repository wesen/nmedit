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
package net.sf.nmedit.jtheme.reflect;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class PropertyAccessGroup implements Iterable<PropertyAccess>
{

    private Class<?> clazz;
    private Map<String, PropertyAccess> group;

    public PropertyAccessGroup(Class<?> clazz, Map<String, PropertyAccess> group)
    {
        this.clazz = clazz;
        this.group = group;
    }
    
    public Class<?> getTarget()
    {
        return clazz;
    }

    public String[] getPropertyNames()
    {
        Collection<String> c = group.keySet();
        return c.toArray(new String[c.size()]);
    }
    
    public Iterator<String> getPropertyNameIterator()
    {
        return group.keySet().iterator();
    }
    
    public int getParameterCount()
    {
        return group.size();
    }
    
    public PropertyAccess getPropertyAccess(String propertyName)
    {
        return group.get(propertyName);
    }
    
    public PropertyAccess[] getProperties()
    {
        Collection<PropertyAccess> c = group.values();
        return c.toArray(new PropertyAccess[c.size()]);
    }
    
    public Iterator<PropertyAccess> getPropertyIterator()
    {
        return group.values().iterator();
    }
    
    public Iterator<PropertyAccess> iterator()
    {
        return getPropertyIterator();
    }
    
}

