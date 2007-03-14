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

import java.awt.Font;
import java.text.ParseException;
import java.util.regex.Pattern;

public class FontProperty implements PropertyHandler
{
    
    private static Pattern splitPattern = Pattern.compile("\\s*,\\s*");

    public Font parseString(String value) throws ParseException
    {
        if (value == null) return null;
        value = value.trim();
        if (value.length() == 0) return null;
        
        String[] pieces = splitPattern.split(value);
        if (pieces.length<2 || pieces.length>3)
            throw new ParseException(value, 0);

        String fontName = unquote(pieces[0]);
        
        if (fontName == null)
            throw new ParseException(value, 0);
        
        
        int size;
        try
        {
            size = Integer.parseInt(pieces[1]);
        }
        catch (NumberFormatException e)
        {
            throw new ParseException(value, 0);
        }
        
        int flags = Font.PLAIN;
        
        if (pieces.length>2)
        {
            String sf = pieces[2];
            for (int i=0;i<sf.length();i++)
            {
                char c = sf.charAt(i);
                if (c=='i')
                {
                    if ((flags&Font.ITALIC)>0)
                        throw new ParseException(value,0);
                    
                    flags|=Font.ITALIC;
                }
                else if (c=='b')
                {
                    if ((flags&Font.BOLD)>0)
                        throw new ParseException(value,0);
                    flags|=Font.BOLD;
                }
                else
                    throw new ParseException(value,0);
            }
        }
        
        return new Font(fontName, flags, size);
    }

    private String unquote(String s)
    {
        boolean c1 = s.startsWith("'");
        boolean c2 = s.endsWith("'");

        if (c1 || c2)
        {
            if (!(c1 && c2))
                return null;
            return s.substring(1, s.length()-1);
        }
        return s;
    }

    public String toString(Object value)
    {
        if (value == null) return "";

        Font font = (Font) value;
        
        StringBuilder sb = new StringBuilder();
        sb.append(formatName(font.getName()));
        sb.append(",");
        sb.append(Integer.toString(font.getSize()));
        if (!font.isPlain())
            sb.append(",");
        if (font.isBold())
        {
            sb.append("b");
        }
        if (font.isItalic())
        {
            sb.append("i");
        }
        return sb.toString();
    }

    private CharSequence formatName(String name)
    {
        for (int i=0;i<name.length();i++)
            if (Character.isWhitespace(name.charAt(i)))
                return "'"+name+"'";
        return name;
    }

}

