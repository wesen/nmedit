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
package net.sf.nmedit.nmutils;

import java.awt.Color;

public class Hex
{

    
    public static Color htmlHexColor(CharSequence s)
    {
        if (s.length()==1+3)
        {
            if (s.charAt(0)!='#') return null;
            float r = hexCharacterToInt(s.charAt(1))/16f;
            float g = hexCharacterToInt(s.charAt(2))/16f;
            float b = hexCharacterToInt(s.charAt(3))/16f;
            return (r <0 || g<0 || b<0) ? null : new Color(r, g, b);
        }
        else if (s.length()==1+6)
        {
            if (s.charAt(0)!='#') return null;
            int value = hexStringToInt(s, 1, s.length());
            return value<0 ? null : new Color(value);
        }
        return null;
    }

    // endIndex exclusive
    public static int hexStringToInt(CharSequence s, int beginIndex, int endIndex)
    {   
        if (beginIndex<0 || beginIndex>=endIndex || endIndex>s.length()) return -1;
        
        int value = 0;
        for (int i=beginIndex;i<=endIndex-1;i++)
        {
            int digit = hexCharacterToInt(s.charAt(i));
            if (digit < 0) return -1;
            value = (value * 16) + digit;
        }
        return value;
    }
    
    public static int hexCharacterToInt(char c)
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
