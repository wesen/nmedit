/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/


package net.sf.nmedit.jnmprotocol.utils;

import net.sf.nmedit.jnmprotocol.utils.NmCharacter;

public class StringUtils
{

    public static String toText( byte[] b)
    {
        if (b.length == 0)
            return "";
        
        boolean invalid = false;
        
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<b.length;i++)
        {
            char c = (char)b[i];
            if (NmCharacter.isCharacter(c))
            {
                if (invalid)
                    sb.append("...");
                sb.append(c);
                invalid = false;
            }
            else
            {
                invalid = true;
            }
        }
        return sb.toString();
    }
    
    public static String toHexadecimal( byte[] b )
    {
        if (b.length == 0)
            return "[]";
        
        StringBuilder sb = new StringBuilder(b.length*3+1);
        sb.append('[');
        sb.append(toHexadecimal(b[0]));
        for (int i=1;i<b.length;i++)
        {
            sb.append(' ');
            sb.append(toHexadecimal(b[i]));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String toHexadecimal(byte b)
    {
        int unsignedbyte = (int)(b&0xFF);
        return (unsignedbyte<=0xF ? "0" : "") + Integer.toHexString(unsignedbyte).toUpperCase();
    }

}
