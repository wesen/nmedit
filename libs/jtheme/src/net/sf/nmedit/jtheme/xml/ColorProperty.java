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

import net.sf.nmedit.nmutils.Hex;

public class ColorProperty implements PropertyHandler
{
    
    public Color parseString(String value) throws ParseException
    {
        return parseColor(value);
    }

    public static Color parseColor(String value) throws ParseException
    {
        Color color = Hex.htmlHexColor(value);
        if (color == null) throw new ParseException(value, 0);
        return color;
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

}

