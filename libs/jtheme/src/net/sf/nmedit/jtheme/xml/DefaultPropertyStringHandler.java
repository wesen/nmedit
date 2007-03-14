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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public class DefaultPropertyStringHandler 
{
    
    private transient Map<Class<?>, PropertyHandler> map;

    protected Map<Class<?>, PropertyHandler> getMap()
    {
        if (map == null)
        {
            map = new HashMap<Class<?>, PropertyHandler>();
            createMap(map);
        }
        return map;
    }
    
    protected void createMap(Map<Class<?>, PropertyHandler> map)
    {
        ColorProperty pcolor = new ColorProperty();
        map.put(Color.class, pcolor);
        map.put(ColorUIResource.class, pcolor);
       // map.put(PrintColorUIResource.class, pcolor);
        
        map.put(String.class, new StringPropertyHandler());
        map.put(Point.class, new PointProperty());
        map.put(Dimension.class, new DimensionProperty());
        
        FontProperty pfont = new FontProperty();
        map.put(Font.class, pfont);
        map.put(FontUIResource.class, pfont);


        map.put(Integer.class, new IntegerProperty());
        map.put(Integer.TYPE, new IntegerTypeProperty());

        map.put(Boolean.class, new BooleanProperty());
        map.put(Boolean.TYPE, new BooleanTypeProperty());
    }

    public Object parseString(Class<?> clazz, String value) throws ParseException
    {
        PropertyHandler psh = getMap().get(clazz);
        
        if (psh == null)
            throw new IllegalArgumentException("type not supported: "+clazz);
        
        return psh.parseString(value);
    }

    public String toString(Class<?> clazz, Object value)
    {
        if (!clazz.isInstance(value))
        {
            // This is just wrong but, there is a problem with primitive types like Integer.TYPE
            // how was it solved before ????
            if (!(clazz.isPrimitive()))
            
                throw new IllegalArgumentException("invalid class "+clazz+" for "+(value!= null ? value.getClass() : null));
        }
        
        PropertyHandler psh = getMap().get(clazz);
        
        if (psh == null)
            throw new IllegalArgumentException("type not supported: "+clazz);
        
        return psh.toString(value);
    }

    
    
    
}

