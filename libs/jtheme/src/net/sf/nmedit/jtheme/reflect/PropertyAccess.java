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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PropertyAccess
{
    
    private static final Object[] NO_ARGS = new Object[0];
    private String property;
    private Method getter;
    private Method setter;

    public PropertyAccess(String property, Method getter, Method setter)
    {
        this.property = property;
        this.getter = getter;
        this.setter = setter;
    }
    
    public String getPropertyName()
    {
        return property;
    }
    
    public Class<?> getPropertyClass()
    {
        return getter.getReturnType();
    }
    
    public Object get(Object target)
    {
        try
        {
            return getter.invoke(target, NO_ARGS);
        }
        catch (IllegalArgumentException e)
        {
            assert false : "get failed: "+e;
        }
        catch (IllegalAccessException e)
        {
            assert false : "get failed: "+e;
        }
        catch (InvocationTargetException e)
        {
            assert false : "get failed: "+e;
        }
        return null;
    }
    
    public void set(Object target, Object arg)
    {
        try
        {
            setter.invoke(target, new Object[] {arg});
        }
        catch (IllegalArgumentException e)
        {
            assert false : "set failed: "+e;
        }
        catch (IllegalAccessException e)
        {
            assert false : "set failed: "+e;
        }
        catch (InvocationTargetException e)
        {
            assert false : "set failed: "+e;
        }
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(getClass().getName());
        sb.append("[");
        sb.append("property=");
        sb.append(property);
        sb.append(",getter=");
        sb.append(getter);
        sb.append(",setter=");
        sb.append(setter);
        sb.append("]");
        
        return sb.toString();
    }
    
}

