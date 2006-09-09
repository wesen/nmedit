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
import java.util.Map;

import javax.swing.JComponent;

public class ComponentConfiguration
{
    
    private Map<Property,Object> defaultValues = new HashMap<Property,Object>();
    
    private ThemeConfiguration config;
    private JComponent component;
    private Map<String,Property> properties;

    public ComponentConfiguration(ThemeConfiguration config, JComponent component)
    {
        this.config = config;
        this.component = component;
        properties = config.getProperties(component);
        setDefaults();
    }
    
    public ThemeConfiguration getThemeConfiguration()
    {
        return config;
    }
    
    public JComponent getComponent()
    {
        return component;
    }
    
    public Map<String,Property> getProperties()
    {
        return properties;
    }
    
    private void setDefaults()
    {
        for (Property p : properties.values())
            defaultValues.put(p, p.getValue(component));
    }

    public void restoreDefaults()
    {
        for (Property p : defaultValues.keySet())
            try
            {
                p.setValue(component, defaultValues.get(p));
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    public boolean isChanged()
    {
        for (Property p : defaultValues.keySet())
        {
            Object defaultValue = defaultValues.get(p);
            Object currentValue = p.getValue(component);
            if (!eq(defaultValue, currentValue))
                return true;
        }
        return false;
    }
    
    private static boolean eq(Object a, Object b)
    {
        return a==null ? b==null : a.equals(b);
    }

}
