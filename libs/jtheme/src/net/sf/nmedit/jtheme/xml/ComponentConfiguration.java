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
package net.sf.nmedit.jtheme.xml;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.reflect.PropertyAccess;
import net.sf.nmedit.jtheme.reflect.PropertyAccessGroup;

public class ComponentConfiguration
{

    private Class<?> target;
    private Map<String, Object> defaults = new HashMap<String, Object>();
    private Map<String, Object> values = new HashMap<String, Object>();
    private transient PropertyAccessGroup accessGroup;
    private transient Map<String, Object> variants;

    public ComponentConfiguration(Class<?> target)
    {
        this.target = target;
    }
    
    public Class<?> getTarget()
    {
        return target;
    }
    
    public boolean isTarget(Object o)
    {
        return target == o;
    }
    
    private PropertyAccessGroup getAccessGroup(JTComponent c)
    {
        if (accessGroup == null)
        {
            accessGroup = c.getContext().getBuilder().getPropertyAccessGroup(c.getClass());
        }
        return accessGroup;
    }

    private void putValues(Map<String, Object> values, JTComponent source)
    {
        variants = null;
        
        PropertyAccessGroup ag = getAccessGroup(source);
        values.clear();
        
        for (PropertyAccess pa : ag)
        {
            values.put(pa.getPropertyName(), pa.get(source));
        }
    }
    
    private void checkTarget(JTComponent c)
    {
        if (!isTarget(c.getClass()))
            throw new IllegalArgumentException("invalid target: "+c.getClass());
    }

    public void updateDefaultValues(JTComponent defaultState)
    {
        checkTarget(defaultState);
        putValues(defaults, defaultState);
    }

    public void updateValues(JTComponent currentState)
    {
        checkTarget(currentState);
        putValues(values, currentState);
    }
    
    public Map<String, Object> getDefaultValues()
    {
        return defaults;
    }
    
    public Map<String, Object> getValues()
    {
        return values;
    }
    
    public void updateDefaultValue(String propertyName, JTComponent source)
    {
        updateValue(defaults, propertyName, source);
    }

    public void updateValue(String propertyName, JTComponent source)
    {
        updateValue(values, propertyName, source);
    }
    
    private void updateValue(Map<String, Object> map, String propertyName, JTComponent source)
    {
        checkTarget(source);
        PropertyAccess pa = getAccessGroup(source).getPropertyAccess(propertyName);
        map.put(propertyName, pa.get(target));
    }
    
    public Map<String, Object> getVariants()
    {
        if (variants != null)
            return variants;
        
        variants = new HashMap<String, Object>(values);
        for (String key : defaults.keySet())
        {
            Object defaultValue = defaults.get(key);
            Object currentValue = variants.get(key);
            
            if (defaultValue == currentValue || (defaultValue != null && defaultValue.equals(currentValue)))
            {
                variants.remove(key);
            }
        }

        return variants;
    }
    
    public void print(PrintStream out)
    {
        DefaultPropertyStringHandler dpsh = new DefaultPropertyStringHandler();
        
        for (String key : values.keySet())
        {
            
            System.out.println(key+":"+values.get(key)+";");
        }
    }
    
}

