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

import java.text.ParseException;

public abstract class XYPropertyHandler implements PropertyHandler
{

    public Object parseString(String value) throws ParseException
    {
        String[] strings = value.split(",");
        if (strings.length!=2)
            throw new ParseException(value, 0);

        String xs = strings[0].trim();
        String ys = strings[1].trim();
        
        try
        {
            return createValue(Integer.parseInt(xs), Integer.parseInt(ys));
        }
        catch (NumberFormatException e)
        {
            throw new ParseException(value, 0);
        }
    }
    
    protected abstract Object createValue(int x, int y);

    public abstract String toString(Object value);
    
    public String toString(int x, int y)
    {
        return x+","+y;
    }

}

