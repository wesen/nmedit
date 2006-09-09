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
 * Created on Sep 8, 2006
 */
package net.sf.nmedit.jtheme;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.types.ConnectorSpecType;
import net.sf.nmedit.jtheme.types.ParameterSpecType;
import net.sf.nmedit.jtheme.types.ValueType;

public class ThemeConfiguration
{

    private Set<Class<? extends JComponent>> components
    = new HashSet<Class<? extends JComponent>>();
    
    private Map<Class<? extends JComponent>,String> aliasMap
    = new HashMap<Class<? extends JComponent>,String>();
    
    private Map<Class,ValueType<?>> valueTypes = new HashMap<Class,ValueType<?>>();

    public ThemeConfiguration()
    {
        // install types
        putValueType(new ValueType.DimensionType());
        putValueType(new ValueType.FontType());
        putValueType(new ValueType.PointType());
        putValueType(new ValueType.StringType());
        putValueType(new ValueType.IntegerType());
        putValueType(Integer.TYPE, new ValueType.IntegerTypeType());
        putValueType(new ValueType.DoubleType());
        putValueType(Double.TYPE, new ValueType.DoubleType());
        putValueType(new ValueType.BooleanType());
        putValueType(Boolean.TYPE, new ValueType.BooleanType());
        putValueType(new ValueType.LongType());
        putValueType(Long.TYPE, new ValueType.LongType());
        
        // TODO remove other types
        // other types
        putValueType(new ConnectorSpecType());
        putValueType(new ParameterSpecType());
    }
    
    public void installComponent(Class<? extends JComponent> componentClass)
    {
        components.add(componentClass);
    }

    public String getAlias(Class<? extends JComponent> componentClazz)
    {
        return aliasMap.get(componentClazz);
    }
    
    public void uninstallComponent(Class<? extends JComponent> componentClazz)
    {
        aliasMap.remove(componentClazz);
        components.remove(componentClazz);
    }
    
    public Property getProperty(Class<? extends JComponent> c, String propertyName)
    {
        return Utils.getProperties(c).get(propertyName);
    }
    
    public <T> void putValueType(Class<?super T> c, ValueType<T> vt)
    {
        valueTypes.put(c, vt);
    }
    
    public <T> void putValueType(ValueType<T> vt)
    {
        putValueType(vt.getClassType(), vt);
    }
    
    public <T> ValueType<T> getValueType(Class<T> t)
    {
        return (ValueType<T>) valueTypes.get(t);
    }

    public <T> T getProperty(JComponent c, String propertyName)
    {
        Property p = getProperty(c.getClass(), propertyName);
        if (p==null)
            throw new RuntimeException("class "+c.getClass()+" has no such property '"+propertyName+"'");
        return p.<T>getValue(c);
    }

    public void setProperty( JComponent c, String propertyName, Object value ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Property p = getProperty(c.getClass(), propertyName);
        if (p==null)
            throw new RuntimeException("class "+c.getClass()+" has no such property '"+propertyName+"'");
        p.setValue(c, value);
    }

    public String getPropertyString(JComponent c, String propertyName)
    {
        Property p = getProperty(c.getClass(), propertyName);
        if (p==null)
            throw new RuntimeException("class "+c.getClass()+" has no such property '"+propertyName+"'");
        
        ValueType<?> t = getValueType(p.getGetter().getReturnType());
        if (t==null)
            throw new RuntimeException("unknown value type "+p.getGetter().getReturnType());
        
        return p.getStringValue(c, t);
    }

    public void setPropertyString( JComponent c, String propertyName, String representation)
    {
        Property p = getProperty(c.getClass(), propertyName);
        if (p==null)
            throw new RuntimeException("class "+c.getClass()+" has no such property '"+propertyName+"'");
        
        ValueType<?> t = getValueType(p.getGetter().getReturnType());
        if (t==null)
            throw new RuntimeException("unknown value type "+p.getGetter().getReturnType());
        
        try
        {
            p.setStringValue(c, t, representation);
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException("setting <"+propertyName+"='"+representation+"'> in <"+c.getClass()+"> failed.", e);
        }
    }

    public Map<String, Property> getProperties( JComponent component )
    {
        return Utils.getProperties(component.getClass());
    }
    
}
