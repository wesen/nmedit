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
package net.sf.nmedit.jpatch.spec;

import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.ParameterDescriptor;
import net.sf.nmedit.jpatch.spec.formatter.DefaultFormatter;
import net.sf.nmedit.jpatch.spec.formatter.Formatter;

public class DefaultParameterDescriptor extends BasedDescriptor implements ParameterDescriptor
{
    
    public static final Formatter DEFAULT_FORMATTER = new DefaultFormatter();
    
    private int minValue = 0;
    private int maxValue = 127;
    private int defaultValue = minValue;
    private ModuleDescriptor module;
    private String name;
    private Formatter formatter = DEFAULT_FORMATTER;

    private String className = null;
    private int index;

    public DefaultParameterDescriptor(ModuleDescriptor module, String name, int index)
    {
        this.module = module;
        this.name = name;
        this.index = index;
    }
    
    public String getFormattedValue(Parameter parameter, int value)
    {
        if (formatter == null)
            return Integer.toString(value);
        else
            return formatter.getString(parameter, value);
    }
    
    public Formatter getFormatter()
    {
        return formatter; 
    }
    
    public void setFormatter(Formatter formatter)
    {
        this.formatter = formatter;
    }
    
    public void setDefaultValue(int defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
    public void setMinValue(int minValue)
    {
        this.minValue = minValue;
    }
    
    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public int getDefaultValue()
    {
        return defaultValue;
    }

    public int getRange()
    {
        return getMaxValue()-getMinValue()+1;
    }

    public int getMinValue()
    {
        return minValue;
    }

    public int getMaxValue()
    {
        return maxValue;
    }

    public ParameterDescriptor getSourceDescriptor()
    {
        return null;
    }

    public ModuleDescriptor getModuleDescriptor()
    {
        return module;
    }

    public String getComponentName()
    {
        return name;
    }

    public String getName()
    {
        return getClass().getName();
    }

    public String toString()
    {
        return getName()+"[name='"+name+"',min="+minValue+",max="+maxValue+",default="+defaultValue+"]";
    }

    public void setParameterClass( String className )
    {
        this.className = className;
    }
    
    public String getParameterClass()
    {
        return className;
    }
    
    public int getIndex()
    {
        return index;
    }
    
}
