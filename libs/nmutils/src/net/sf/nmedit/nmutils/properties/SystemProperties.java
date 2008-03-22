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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.nmedit.nmutils.properties.type.Property;

public class SystemProperties
{

    private SystemProperties parent;
    private String subsetPrefix;
    
    public SystemProperties(SystemProperties parent, String subsetPrefix)
    {
        this.parent = parent;
        this.subsetPrefix = subsetPrefix;
    }

    public Set<String> keySet()
    {
        if (parent == null)
            return Collections.emptySet();
        Set<String> set = new HashSet<String>();
        for(String key: parent.keySet())
        {
            if (subsetPrefix == null)
                set.add(key);
            else if (key.startsWith(subsetPrefix))
                set.add(key.substring(subsetPrefix.length()));
        }
        return set;
    }

    public void defineDimensionProperty(String name, Dimension defaultValue)
    {
        setProperty(name, new Property.Dimension(defaultValue));
    }

    public void defineRectangleProperty(String name, Rectangle defaultValue)
    {
        setProperty(name, new Property.Rectangle(defaultValue));
    }

    public void definePointProperty(String name, Point defaultValue)
    {
        setProperty(name, new Property.Point(defaultValue));
    }

    public void defineBooleanProperty(String name, Boolean defaultValue)
    {
        setProperty(name, new Property.Boolean(defaultValue));
    }

    public void defineStringProperty(String name, String defaultValue)
    {
        setProperty(name, new Property.String(defaultValue));
    }
    
    public <T> Property<T> getProperty(String name, Class<T> type)
    {
        if (parent == null)
            return null;
        return parent.getProperty(resolveKey(name), type);
    }
    
    public Property<?> getProperty(String name)
    {
        if (parent == null)
            return null;
        return parent.getProperty(resolveKey(name));
    }

    protected <T> void setProperty(String key, Property<T> property)
    {
        if (parent != null)
            parent.setProperty(resolveKey(key), property);
    }
    
    public <T> T getValue(Class<T> type, String key)
    {
        Property<T> property = getProperty(key, type);
        if (property == null) return null;
        return property.getValue();
    }
    
    public <T> boolean setValue(String name, Class<T> type, T value)
    {
        Property<T> p = getProperty(name, type);
        if (p != null)
        {
            p.setValue(value);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setBooleanValue(String name, Boolean value)
    {
        setValue(name, Boolean.class, value);
    }

    public void setRectangleValue(String name, Rectangle value)
    {
        setValue(name, Rectangle.class, value);
    }

    public void setPointValue(String name, Point value)
    {
        setValue(name, Point.class, value);
    }

    public void setDimensionValue(String name, Dimension value)
    {
        setValue(name, Dimension.class, value);
    }
    
    public Boolean booleanValue(String name)
    {
        return getValue(Boolean.class, name);
    }

    public Rectangle rectangleValue(String name)
    {
        return getValue(Rectangle.class, name);
    }

    public Point pointValue(String name)
    {
        return getValue(Point.class, name);
    }

    public Dimension dimensionValue(String name)
    {
        return getValue(Dimension.class, name);
    }
    
    protected String resolveKey(String key)
    {
        return subsetPrefix != null && key != null ? subsetPrefix+key : key;
    }

    public String stringValue(String name)
    {
        return getValue(String.class, name);
    }
    
}
