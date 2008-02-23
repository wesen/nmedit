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
package net.sf.nmedit.jpatch.clavia.nordmodular.misc;

public class Cycles
{
    
    private long fixpointValue;
    private double toFixedPointFactor;
    private int precision;
    
    public Cycles()
    {
        this(5);
    }
    
    public Cycles(int precision)
    {
        if (precision<0)
            throw new IllegalArgumentException("precision can not be negative: "+precision);

        this.fixpointValue = 0;
        this.precision = precision;
        this.toFixedPointFactor = Math.pow(10, precision);
    }
    
    public int getPrecision()
    {
        return precision;
    }
    
    public long toFixpoint(double value)
    {
        return (long) (value*toFixedPointFactor);
    }
    
    public double fromFixpoint(long value)
    {
        return value/toFixedPointFactor;
    }

    public void add(double value)
    {
        long fValue = toFixpoint(value);
        long newFixpointValue = fixpointValue+fValue;
        assert (newFixpointValue-fixpointValue) == fValue;
        fixpointValue = newFixpointValue; 
    }
    
    public void subtract(double value)
    {
        long fValue = toFixpoint(value);
        long newFixpointValue = Math.max(0, fixpointValue-fValue);
        assert (newFixpointValue+fValue)==fixpointValue;
        fixpointValue = newFixpointValue;
    }
    
    public double getCycles()
    {
        return fromFixpoint(fixpointValue);
    }
    
    public String toString()
    {
        return getClass().getName()+"[precision="+precision+",fp="+fixpointValue+",cycles="+getCycles()+"]";
    }
    
}
