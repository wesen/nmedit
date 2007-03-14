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

public class FIStr extends FormattingInstruction
{

    private String string;

    public FIStr( FormattingInstructionClass fiClass, String string)
    {
        super( fiClass );
        this.string = string;
    }
    
    public String toString()
    {
        return getFormattingInstructionClass().getFunctionName()+"('"+escape(string)+"')";
    }
    
    private static String escape( String s )
    {
        return s.replaceAll("'", "''");
    }

    public boolean isString()
    {
        return true;
    }
    
    public Object getValue(Parameter parameter, Object value)
    {
        return string;
    }

    public boolean isUnionPossible(FormattingInstruction fi)
    {
        return fi instanceof FIStr;
    }
    
    public FormattingInstruction computeUnion(FormattingInstruction fi)
    {
        if (isUnionPossible(fi))
        {
            FIStr fis = (FIStr) fi;
            
            return new FIStr(getFormattingInstructionClass(), string + fis.string);
        }
        // else error
        return super.computeUnion(fi);
    }
    
}
