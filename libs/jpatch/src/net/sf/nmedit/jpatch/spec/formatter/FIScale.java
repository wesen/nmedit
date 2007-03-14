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

public class FIScale extends FormattingInstruction
{

    private double factor;
    private boolean integer;

    public FIScale( FormattingInstructionClass fiClass, double factor )
    {
        super( fiClass );
        this.factor = factor;
        integer = Math.round(factor) == factor;
    }
    
    public String toString()
    {
        return getFormattingInstructionClass().getFunctionName()+"("+factor+")";
    }
    
    public Object getValue(Parameter parameter, Object value)
    {
        if (integer &&  value instanceof Integer)
        {
            return ((Integer) value)*((int)factor);
        }
        else
        if (value instanceof Number)
        {
            return ((Number)value).doubleValue() * factor;
        }
        return "error";
    }

    public boolean isUnionPossible(FormattingInstruction fi)
    {
        return fi instanceof FIScale;
    }
    
    public FormattingInstruction computeUnion(FormattingInstruction fi)
    {
        if (isUnionPossible(fi))
        {
            FIScale fis = (FIScale) fi;
            
            return new FIScale(getFormattingInstructionClass(), factor * fis.factor);
        }
        // else error
        return super.computeUnion(fi);
    }
    
}
