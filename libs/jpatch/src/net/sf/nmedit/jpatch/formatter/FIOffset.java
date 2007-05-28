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
package net.sf.nmedit.jpatch.formatter;

import net.sf.nmedit.jpatch.PParameter;

public class FIOffset extends FormattingInstruction
{

    private double offset;
    private boolean integer;

    public FIOffset( FormattingInstructionClass fiClass, double offset )
    {
        super( fiClass );
        this.offset = offset;
        integer = Math.round(offset) == offset;
    }
    
    public String toString()
    {
        return getFormattingInstructionClass().getFunctionName()+"("+offset+")";
    }
    
    public Object getValue(PParameter parameter, Object value)
    {
        if (integer &&  value instanceof Integer)
        {
            return (((Integer) value).intValue() + (int) offset);
        }
        else
        if (value instanceof Number)
        {
            return ((Number)value).doubleValue() + offset;
        }
        return "error";
    }

    public boolean isUnionPossible(FormattingInstruction fi)
    {
        return fi instanceof FIOffset;
    }
    
    public FormattingInstruction computeUnion(FormattingInstruction fi)
    {
        if (isUnionPossible(fi))
        {
            FIOffset fio = (FIOffset) fi;
            
            return new FIOffset(getFormattingInstructionClass(), offset + fio.offset);
        }
        // else error
        return super.computeUnion(fi);
    }
    
}
