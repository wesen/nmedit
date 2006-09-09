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
 * Created on Sep 9, 2006
 */
package net.sf.nmedit.jtheme;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.dom.ComponentNode;
import net.sf.nmedit.jtheme.dom.ContainerNode;
import net.sf.nmedit.jtheme.dom.PropertyNode;
import net.sf.nmedit.jtheme.types.ValueType;

public class ComponentBuilder
{
    
    private Theme theme;

    public ComponentBuilder(Theme theme)
    {
        this.theme = theme;
    }

    public ThemeConfiguration getThemeConfiguration()
    {
        return theme.getThemeConfiguration();
    }
    
    protected void configure(JComponent component, ComponentNode cn) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        for (PropertyNode propertyNode : cn.values())
        {
            Object value = propertyNode.getUserObject();

            Property p = getThemeConfiguration().getProperty(
                    component.getClass(), propertyNode.getPropertyName() );

            if (p == null)
                throw new RuntimeException(
                 "property <"+propertyNode.getPropertyName()+"> not found in class "+
                 component.getClass()
                );
            
            if (value==null)
            {   
                ValueType<?> type = getThemeConfiguration()
                    .getValueType( p.getPropertyClass() );
                
                if (type == null)
                    throw new RuntimeException(
                      "type found for "+p.getPropertyClass()      
                    );

                try
                {
                    value = type.fromString(propertyNode.getPropertyValue());
                }
                catch (RuntimeException e)
                {
                    throw new RuntimeException(
                    "could not convert property "+
                    propertyNode.getPropertyName()+"=<"+
                    propertyNode.getPropertyValue()+"> in class "+
                    component.getClass() , e);
                }
                propertyNode.setUserObject(value);
            }

            p.setValue(component, value);
        }
    }
    
    protected JComponent buildComponent(ComponentNode cn) throws
        IllegalArgumentException, InvocationTargetException,
        InstantiationException, IllegalAccessException
    {
        JComponent component = getTheme().createComponent(cn.getComponentName());
        
        configure(component, cn);
        
        return component;
    }
    
    private Theme getTheme()
    {
        return theme;
    }

    public JComponent build(ContainerNode plan)
    {
        try
        {
            return tryBuild(plan);
        }
        catch  (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public JComponent tryBuild(ContainerNode plan) 
        throws InstantiationException, IllegalAccessException, 
        IllegalArgumentException, InvocationTargetException
    {
        JComponent container = getTheme().createComponent(Theme.CONTAINER_ALIAS);
        
        for (ComponentNode cn : plan)
        {
            JComponent c = buildComponent(cn);
            container.add(c);
        }
        
        return container;
    }
    
}
