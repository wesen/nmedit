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

/*
 * Created on Jan 13, 2007
 */
package net.sf.nmedit.jnmprotocol2.utils;

import java.util.Arrays;

import net.sf.nmedit.jpdl2.stream.IntStream;
import net.sf.nmedit.jpdl2.PDLPacket;

public class NmCharacter
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

    /**
     * Extracts a string from the specified packet. The packet
     * must contain the variable list 'chars'
     * @param name the source packet 
     * @return the extracted string
     */
    public static String extractName(PDLPacket name)
    {
        return extractName(name, "chars");
    }

    /**
     * Extracts a string from the specified packet.
     * @param name the source packet 
     * @param variableListName name of the variable list containing the string
     * @return the extracted string
     */
    public static String extractName(PDLPacket name, String variableListName)
    {
        // module name limit:16<20 / notes can be longer
        StringBuilder builder = new StringBuilder(16); 
        int[] list = name.getVariableList(variableListName);
        for (int i=0;i<list.length;i++)
        {
            int value = list[i];
            if (value==0) break;
            builder.append((char) value);
        }
        return builder.toString();
    }

    /**
     * Appends the first 16 characters of the specified string to the intstream.
     * @param intStream destination
     * @param nmString the string
     * @throws IllegalArgumentException if the string contains illegal characters
     */
    public static void appendString(IntStream intStream, String nmString)
    {
        appendString(intStream, nmString, 16);
    }

    /**
     * Appends the first maxLen characters of the specified string to the intstream.
     * @param intStream destination
     * @param nmString the string
     * @param maxLen maximum number of characters
     * @throws IllegalArgumentException if the string contains illegal characters
     */
    public static void appendString(IntStream intStream, String nmString, int maxLen)
    { 
        // TODO 
        // byte[] chars = nmString.getBytes("ISO-8859-1");
        
        int len = Math.min(nmString.length(), maxLen);
        for (int i=0;i<len;i++)
        {
            char c = nmString.charAt(i);
            if (!isCharacter(c))
                throw new IllegalArgumentException("invalid character at position: "+i+", string='"+nmString+"'");
            intStream.append(c);
        }
        if (len<maxLen)
        {
            intStream.append(0); // string termination
        }
    }
    
    /**
     * Returns true if the specified character is supported by the nord modular synthesizer.
     */
    public static boolean isCharacter(char c)
    {
        return c<charmap.length && charmap[c];
    }
    
    /**
     * Returns true if each of the characters is supported by the nord modular synthesizer.
     */
    public static boolean isValid(CharSequence cs)
    {
        for (int i=0;i<cs.length();i++)
            if (!isCharacter(cs.charAt(i)))
                return false;
        return true;
    }
    
    private static boolean[] charmap = new boolean[256];

    static
    {
        // set all to false
        Arrays.fill(charmap, false);
        // small letters
        Arrays.fill(charmap, 'a', 'z'+1, true);
        // big letters
        Arrays.fill(charmap, 'A', 'Z'+1, true);
        // digits
        Arrays.fill(charmap, '0', '9'+1, true);
        // other characters
        charmap[' ']=true;
        charmap['!']=true;
        charmap['"']=true;
        charmap['#']=true;
        charmap['$']=true;
        charmap['%']=true;
        charmap['&']=true;
        charmap['\'']=true;
        charmap['(']=true;
        charmap[')']=true;
        charmap['*']=true;
        charmap['+']=true;
        charmap[',']=true;
        charmap['-']=true;
        charmap['.']=true;
        charmap['/']=true;
        charmap[':']=true;
        charmap[';']=true;
        charmap['<']=true;
        charmap['=']=true;
        charmap['>']=true;
        charmap['?']=true;
        charmap[0x40]=true; // strange symbol 
        charmap['[']=true;
        charmap[0x5C]=true;// looks like (extended) ascii character 157
        charmap[']']=true;
        charmap['^']=true;
        charmap['_']=true;
        charmap[0x60]=true;// ? accon - not the a ???
        charmap['{']=true;
        charmap['|']=true;
        charmap['}']=true;
    }

}
