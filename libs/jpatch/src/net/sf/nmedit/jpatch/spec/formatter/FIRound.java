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

public class FIRound extends FormattingInstruction
{

    private int base;
    private double basem;
    private double basep;

    public FIRound( FormattingInstructionClass fiClass, int base )
    {
        super( fiClass );
        this.base = base;
        basem = Math.pow(10, -base);
        basep = Math.pow(10, base);
    }
    
    public String toString()
    {
        return getFormattingInstructionClass().getFunctionName()+"("+base+")";
    }
    
    public Object getValue(Parameter parameter, Object value)
    {
        if (value instanceof Number)
        {
            double v = ((Number)value).doubleValue();
            return firound(v);
        }
        return "error";
    }

    private Object firound( double v )
    {
        if (base<0)
        {
            return Math.round(v * basem) * basep;
        } 
        else if (base>0)
        {
            return (int) (Math.round(v * basem) * basep);
        } 
        else
        {
            return Math.round(v);
        }
    }

    public boolean isUnionPossible(FormattingInstruction fi)
    {
        return fi instanceof FIRound;
    }
    
    public FormattingInstruction computeUnion(FormattingInstruction fi)
    {
        if (isUnionPossible(fi))
        {
            FIRound fir = (FIRound) fi;
            return new FIRound(getFormattingInstructionClass(), Math.max(base, fir.base));
        }
        // else error
        return super.computeUnion(fi);
    }

}
