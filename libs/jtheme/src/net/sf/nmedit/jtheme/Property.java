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
import java.lang.reflect.Method;

import net.sf.nmedit.jtheme.types.ValueType;

public class Property
{

    private Method get;
    private Method set;
    private String propertyName;

    public Property(String propertyName, Method get, Method set)
    {
        this.propertyName = propertyName;
        this.get = get;
        this.set = set;
    }
    
    public Class<?> getPropertyClass()
    {
        return get.getReturnType();
    }
    
    public String getPropertyName()
    {
        return propertyName;
    }
    
    public Method getGetter()
    {
        return get;
    }
    
    public Method getSetter()
    {
        return set;
    }

    private static Object[] NO_ARGS = new Object[0];
    
    public <T> T getValue(Object o)
    {
        try
        {
            return (T) get.invoke(o, NO_ARGS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public void setValue(Object o, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
            set.invoke(o, new Object[]{value});
    }
    
    public <T> String getStringValue(Object o, ValueType<T> t)
    {
        return t.toString((T) getValue(o));
    }
    
    public <T> void setStringValue(Object o, ValueType<T> t, String representation)
    {
        try
        {
            setValue(o, t.fromString(representation));
        }
        catch (Exception e)
        {
            throw new RuntimeException("in object "+o+" setting failed: "+t.getClass()+":<"+representation+">", e);
        }
    }
    
}
