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
import java.text.ParseException;

public class ColorProperty implements PropertyHandler
{

    public Color parseString(String value) throws ParseException
    {
        if ((!value.startsWith("#"))||(value.length()!=1+2+2+2))
            throw new ParseException(value, 0);

        String rs = value.substring(1, 1+2);
        String rg = value.substring(1+2, 1+2+2);
        String rb = value.substring(1+2+2, 1+2+2+2); 
        
        int r = str2(rs);
        int g = str2(rg);
        int b = str2(rb);
        
        if (r<0 || g<0 || b<0)
            throw new ParseException(value, 0);
        
        return new Color(r, g, b);
        
    }

    public String toString(Object value)
    {
        return toString((Color) value);
    }

    public String toString(Color color)
    {
        return "#"+int2(color.getRed())+int2(color.getGreen())+int2(color.getBlue());
    }

    private String int2(int c)
    {
        String s = Integer.toHexString(c&0xFF).toUpperCase();
        if (s.length() == 1)
            s = "0"+s;
        return s;
    }

    private int str2(String s)
    {
        int x1 = str2(s.charAt(0))*16;
        int x0 = str2(s.charAt(1));
        
        if (x1<0 || x0<0)
            return -1;
        return x1+x0;
    }

    private int str2(char c)
    {
        if ('0'<=c && c<='9')
            return c-'0';
        if ('a'<=c && c<='z')
            return (c-'a')+10;
        if ('A'<=c && c<='Z')
            return (c-'A')+10;
        return -1;
    }
}

