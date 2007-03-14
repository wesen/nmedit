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
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch.spec.formatter;

import java.util.ArrayList;
import java.util.List;


public class FormatterParser
{

    static String test = "str('** '),scale(34),offset(-68),offset(2.2222),round(-2),scale(100),round(0),str(' **')";

    public static void main(String[] args)
    {
        FormatterRegistry fr = new FormatterRegistry();
        Formatter fmt = parseFormatter(fr, test);
        System.out.println(fmt.getString(null,2));
        // should print '** 222 **'
        System.out.println(fmt);
    }
    
    private static void errorAt(String msg, String str, int pos)
    {
        throw new IllegalArgumentException(msg+" @"+pos+" :"+str.substring(0, Math.min(str.length(),pos+1)));
    }
    
    public static Formatter parseFormatter(FormatterRegistry registry, String str)
    {
        SequentialFormatter sf = new SequentialFormatter();
        
        int pos = 0;
        
        StringBuilder sb = new StringBuilder();
        
        while (pos<str.length())
        {
            int end = str.indexOf('(', pos);
            
            if (pos == end)
                errorAt("function name missing",str,pos);
            
            String functionName = str.substring(pos, end).trim();
            pos = end+1;
            
            // signature
            if (pos>=str.length())
                errorAt("signature missing at",str,pos);
            
            List<String> args = new ArrayList<String>();
            boolean signatureComplete = false;
           
            while (!signatureComplete)
            {
                if (str.charAt(pos)=='\'')
                {
                    pos ++;
                    boolean terminated = false;
                    
                    while (pos<str.length())
                    {
                        char c = str.charAt(pos); 
                        if (c == '\'')
                        {
                            if (pos+1<str.length() && str.charAt(pos+1)=='\'')
                            {
                                sb.append(c);
                                pos +=2;
                            }
                            else
                            {
                                terminated = true;
                                pos++;
                                break;
                            }
                        }
                        else
                        {
                            sb.append(c);
                            pos ++;
                        }
                    }
                    
                    if (!terminated)
                        errorAt("unterminated string",str,pos);

                    args.add(sb.toString());
                    sb.setLength(0);
                }
                else
                {
                    int end1 = str.indexOf(',', pos);
                    int end2 = str.indexOf(')', pos);
                    end = end1 < 0 ? end2 : ( end2 <0 ? end1 : (Math.min(end1, end2)));

                    if (end<0)
                        errorAt("incomplete signature",str,pos);
                    
                    args.add(str.substring(pos, end).trim());
                    pos = end;
                }
                
                pos = skipws(str, pos);
                
                if (pos>=str.length())
                    errorAt("incomplete signature",str,pos);

                switch (str.charAt(pos))
                {
                    case ',':
                        pos ++;
                        // more args
                        break ;
                    case ')':
                        pos ++;
                        pos = skipws(str, pos);
                        if (pos<str.length() && str.charAt(pos) == ',')
                           pos ++;
                        
                        signatureComplete = true;
                        break;
                    default :
                        errorAt("illegal character, expected ',' or ')'",str,pos);
                }
            }
            
            String[] argss = args.toArray(new String[args.size()]);
            
            FormattingInstruction fi = registry.getFormattingInstruction(functionName, argss);
            
            sf.append(fi);
            
        }
        
        return sf;
    }

    private static int skipws(String str, int pos)
    {
        while (pos<str.length() && Character.isWhitespace(str.charAt(pos)))
        {
            pos++;
        }
        return pos;
    }
    
}
