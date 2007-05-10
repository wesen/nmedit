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

import net.sf.nmedit.jprotocol.PRParameterDescriptor;

public class PRParameterDescriptorImpl implements PRParameterDescriptor
{

    private String name;
    private String binding;
    private int defaultValue;
    private boolean hasDefaultValue;
    
    protected PRParameterDescriptorImpl(String name, String binding, int defaultValue,
            boolean hasDefaultValue)
    {
        this.name = name;
        this.binding = binding;
        this.defaultValue = defaultValue;
        this.hasDefaultValue = hasDefaultValue;
    }
    
    public PRParameterDescriptorImpl(String name, String binding, int defaultValue)
    {
        this(name, binding, defaultValue, true);
    }
    
    public PRParameterDescriptorImpl(String name, String binding)
    {
        this(name, binding, -1, false);
    }
    
    public String getBinding()
    {
        return binding;
    }

    public int getDefaultValue()
    {
        return hasDefaultValue() ? defaultValue : -1;
    }

    public String getName()
    {
        return name;
    }

    public boolean hasDefaultValue()
    {
        return hasDefaultValue;
    }

    public String toString()
    {
        return getClass().getName()+"[name="+name+",binding="+binding+",default="
        +(hasDefaultValue ? "null": defaultValue) +"]";
    }
    
}
