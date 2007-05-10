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
package net.sf.nmedit.jprotocol.impl;

import java.util.Arrays;

import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jprotocol.PRMessage;
import net.sf.nmedit.jprotocol.PRMessageDescriptor;
import net.sf.nmedit.jprotocol.PRParameterDescriptor;

public class PRMessageImpl implements PRMessage
{
    
    private PRMessageDescriptor descriptor;
    private int[] values;

    protected PRMessageImpl(PRMessageDescriptor descriptor)
    {
        this.descriptor = descriptor;
        values = new int[descriptor.getParameterCount()];
        Arrays.fill(values, -1);
    }

    public static PRMessage createMessage(PRMessageDescriptor descriptor)
    {
        return new PRMessageImpl(descriptor);
    }

    public int getId()
    {
        return descriptor.getId();
    }
    
    public String getName()
    {
        return descriptor.getName(); 
    }
    
    public int getValueAt(int index)
    {
        if (index<0 || index>=values.length)
            throw new IllegalArgumentException("invalid parameter index: "+index);
        
        int value = values[index];
        if (value<0) // use default value
        {
            PRParameterDescriptor p = descriptor.getParameterAt(index);
            if (p.hasDefaultValue())
                value = p.getDefaultValue();
            // TODO else error ???
        }
        return value;
    }
    
    public void setValueAt(int index, int value)
    {
        if (index<0 || index>=values.length)
            throw new IllegalArgumentException("invalid parameter index: "+index);
        
        values[index] = value;
    }
    
    public void setValues(Packet packet)
    {
        for (PRParameterDescriptor param: descriptor.getParameters())
        {
            int value = packet.getVariable(param.getBinding());
            setValue(param, value);
        }
    }

    public PRMessageDescriptor getDescriptor()
    {
        return descriptor;
    }
    
    private void checkParameterIndex(int index, String paramName)
    {
        if (index<0)
            throw new IllegalArgumentException("parameter not found: "+paramName);        
    }

    public int getValue(String paramName)
    {
        PRParameterDescriptor parameter = descriptor.getParameterByName(paramName);
        int index = descriptor.getParameterIndex(parameter);
        checkParameterIndex(index, paramName);
        int value = values[index];
        if (value<0)
        {
            if (parameter.hasDefaultValue())
                value = parameter.getDefaultValue();
            // TODO else error ???
        }
        return value;
    }

    public void setValue(String paramName, int value)
    {
        int index = descriptor.getParameterIndex(paramName);
        checkParameterIndex(index, paramName);
        values[index] = value;
    }

    public int getValue(PRParameterDescriptor parameter)
    {
        return getValue(parameter.getName());
    }

    public void setValue(PRParameterDescriptor parameter, int value)
    {
        setValue(parameter.getName(), value);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append('[');
        sb.append("name="+getName());
        sb.append(",id="+getId());
        
        for (PRParameterDescriptor p: descriptor.getParameters())
        {
            sb.append(',');
            sb.append(p.getName());
            sb.append('=');
            sb.append(getValue(p));
        }        
        sb.append(']');
        return sb.toString();
    }
    
}