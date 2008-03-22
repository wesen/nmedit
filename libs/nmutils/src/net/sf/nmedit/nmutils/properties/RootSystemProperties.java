/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.nmutils.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.nmedit.nmutils.properties.type.Property;

public class RootSystemProperties extends SystemProperties
{

    private Properties properties;
    private Map<String, Property<?>> propertyMap = new HashMap<String, Property<?>>(); 

    public RootSystemProperties(Properties properties)
    {
        super(null, null);
        this.properties = properties;
        if (this.properties == null)
            this.properties = new Properties();
    }
    
    public Set<String> keySet()
    {
        return propertyMap.keySet();
    }

    public Properties getProperties()
    {
        Properties set = new Properties();
        
        for (Entry<String, Property<?>> entry: propertyMap.entrySet())
        {
            String key = entry.getKey();
            Property<?> p = entry.getValue();
            if (key == null || p == null || p.getDefaultValue() == p.getValue() ||
                    (p.getDefaultValue()!=null && p.getDefaultValue().equals(p.getValue())))
                continue;
            
            String stringValue = p.getValueString();
            if (stringValue != null)
                set.setProperty(key, stringValue);
        }
        return set;
    }
    
    public <T> Property<T> getProperty(String name, Class<T> type)
    {
        Property<?> p = propertyMap.get(name);
        if (p != null && p.getType().equals(type))
        {
            return (Property<T>)p;
        }
        return null;
    }
    
    public Property<?> getProperty(String name)
    {
        return propertyMap.get(name);
    }

    protected <T> void setProperty(String key, Property<T> property)
    {
        if (property != null && (property.getDefaultValue() == property.getValue()
                || (property.getDefaultValue() != null && property.getDefaultValue().equals(property.getValue()))))
        {
            // set value from map
            String s = properties.getProperty(key);
            try
            {
                property.setValue(property.parseString(s));
            }
            catch (IllegalArgumentException e)
            {
                // ignore
                e.printStackTrace();
            }
        }
        propertyMap.put(key, property);
    }

    public <T> boolean setValue(String name, Class<T> type, T value)
    {
        Property<T> p = getProperty(name, type);
        if (p != null) 
        {
            p.setValue(value);
            return true;
        }
        return false;
    }
}
