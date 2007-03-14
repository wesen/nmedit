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
 * Created on Nov 21, 2006
 */
package net.sf.nmedit.nomad.core.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMUtilities
{
    
    public static void centerRectangle(Rectangle rect, Dimension d)
    {
        centerRectangle(rect, d.width, d.height);
    }
    
    public static void centerRectangle(Rectangle rect, int w, int h)
    {
        rect.x = (w-rect.width)/2;
        rect.y = (h-rect.height)/2;
    }
    
    public static void fitRectangle(Rectangle rect, Dimension d)
    {
        fitRectangle(rect, d.width, d.height);
    }
    
    public static void fitRectangle(Rectangle rect, int w, int h)
    {
        // check top left point
        if (rect.x<0)
        {
            rect.width-=(-rect.x);
            rect.x = 0;
        }
        if (rect.y<0)
        {
            rect.height-=(-rect.y);
            rect.y = 0;
        }
        // check bottom right point
        int r = rect.x+rect.width;
        int b = rect.y+rect.height;
        
        if (r>w)
            rect.width-=(r-w);
        if (b>h)
            rect.height-=(b-h);
        
        // note: rect.width,rect.height can be <= 0
    }
    
    public static Color getColorProperty (Map p, Object key, Color defaultValue)
    {
        Object value = p.get(key);
        
        if (value instanceof Color)
            return (Color) value;
        else if (value instanceof String)
        {
            try
            {
                return Color.decode((String) value);
            }
            catch (NumberFormatException e)
            { }
        }
        return defaultValue;
    }
    
    private static Pattern rectanglePattern = null;

    /**
     * calling this is not thread safe
     */
    public static Rectangle getRectangleProperty (Map p, Object key, Rectangle defaultValue)
    {
        Object value = p.get(key);
        
        if (value instanceof Rectangle)
            return (Rectangle) value;
        else if (value instanceof String)
        {
            Rectangle r = parseRectangle((String)value);
            if (r!=null)
                return r;
        }
        return defaultValue;
    }
    
    public static String toString(Rectangle r)
    {
        return r.x+" "+r.y+" "+r.width+" "+r.height;
    }
    
    public static Rectangle parseRectangle(String s)
    {
        if (rectanglePattern == null)
            rectanglePattern = Pattern.compile("\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s*");
     
        Matcher m = rectanglePattern.matcher(s);
        
        if (m.matches())
        {
            return new Rectangle( Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)));
        }
        return null;
    }
    
    /**
     * calling this is not thread safe
     */
    public static boolean getBooleanProperty (Map p, Object key, boolean defaultValue)
    {
        Object value = p.get(key);
        
        if (value instanceof Boolean)
            return (Boolean) value;
        else if (value instanceof String)
        {
            try
            {
                return Boolean.parseBoolean((String) value);
            }
            catch (NumberFormatException e)
            {
                // ignore
            }
        }
            
        return defaultValue;
    }

}
