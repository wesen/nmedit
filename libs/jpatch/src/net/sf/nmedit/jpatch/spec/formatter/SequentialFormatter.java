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

import net.sf.nmedit.jpatch.Parameter;

public class SequentialFormatter implements Formatter
{

    private static final FormattingInstruction[] EMPTY = new FormattingInstruction[0];  
    private FormattingInstruction[] fiList = EMPTY;
    private int size = 0;

    private StringBuilder sb = new StringBuilder();
    
    public String getString( Parameter parameter, int value )
    {
        if (size == 0)
            return Integer.toString(value);

        sb.setLength(0);
        
        boolean appendLast = false;
        
        Object v = value;
        try
        {
            for (int i=0;i<size;i++)
            {
                FormattingInstruction fi = fiList[i];
                if (fi.isString())
                {
                    if (i>0)
                        sb.append(v);
                    sb.append(fi.getValue(parameter, value));
                    v = value;
                    appendLast = false;
                }
                else
                {
                    v = fi.getValue(parameter, v);
                    appendLast = true;
                }
            }
            
            if (appendLast)
                sb.append(v);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return "error";
        }
        
        return sb.toString();
    }

    public void append( FormattingInstruction fi )
    {
        if (size > 0)
        {
            FormattingInstruction fi1 = fiList[size-1];
            if (fi1.isUnionPossible(fi))
            {
                fiList[size-1] = fi1.computeUnion(fi);
                return ;
            }
        }
        
        if (size>=fiList.length)
        {
            FormattingInstruction[] finew = new FormattingInstruction[size+2];
            System.arraycopy(fiList, 0, finew, 0, size);
            fiList = finew;
        }
        fiList[size++] = fi;
    }

    public String toString()
    {
        return getFunction();
    }

    private String getFunction()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[");
        if (size>0)
        {
            sb.append(fiList[0]);
            for (int i=1;i<size;i++)
            {
                sb.append(',');
                sb.append(fiList[i]);
            }
        }
        sb.append("]");
        
        return sb.toString();
    }
    
}
