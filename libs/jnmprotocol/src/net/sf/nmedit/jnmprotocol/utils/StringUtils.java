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
 * Created on Jan 6, 2007
 */
package net.sf.nmedit.jnmprotocol.utils;

public class StringUtils
{

    public static String asHex( byte[] b )
    {
        if (b.length == 0)
            return "[]";
        
        StringBuilder sb = new StringBuilder(b.length*3+1);
        sb.append('[');
        sb.append(asHex(b[0]));
        for (int i=1;i<b.length;i++)
        {
            sb.append(' ');
            sb.append(asHex(b[i]));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String asHex(byte b)
    {
        int unsignedbyte = (int)(b&0xFF);
        
        return (unsignedbyte<=0xF ? "0" : "") + Integer.toHexString(unsignedbyte).toUpperCase();
    }

}
