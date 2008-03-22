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
package net.sf.nmedit.jpatch.impl;

import net.sf.nmedit.jpatch.Formatter;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;

/**
 * The reference implementation of interface {@link PParameterDescriptor}.
 * @author Christian Schneider
 */
public class PBasicParameterDescriptor extends PBasicDescriptor
    implements PParameterDescriptor//, Serializable
{
    private static final long serialVersionUID = -4704592363083615614L;

    private int defaultValue = 0;
    private int minValue = 0;
    private int maxValue = 127;

   // private transient String formatterId;
    private transient Formatter formatter = null;
    private transient PParameterDescriptor extensionDescriptor;
    private transient PModuleDescriptor parent;
    
    public PBasicParameterDescriptor(PModuleDescriptor parent, String name, Object componentId)
    {
        super(name, componentId);
        this.parent = parent;
    }
    
    void setParent(PModuleDescriptor parent)
    {
        this.parent = parent;
    }
    
    public PModuleDescriptor getParentDescriptor()
    {
        return parent;
    }

    public Formatter getFormatter()
    {
        /*
        if (formatter == null)
        {
            if (formatterId != null)
            {
                formatter = getParentDescriptor()
                .getModules()
                .getFormatterRegistry()
                .getFormatterById(formatterId);
            }
        }
        */
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

    public String getDisplayValue(int value)
    {
        Formatter f = getFormatter();
        if (f == null)
            return Integer.toString(value);
        else
            return f.getString(this, value);
    }
    
    public String getDisplayValue(PParameter parameter, int value)
    {
        Formatter f = getFormatter();
        if (f == null)
            return Integer.toString(value);
        else
            return f.getString(parameter, value);
    }

    public int getMaxValue()
    {
        return maxValue;
    }

    public int getMinValue()
    {
        return minValue;
    }

    public int getType()
    {
        return PT_VALUE;
    }

    protected void toStringProperties(StringBuilder sb)
    {
        super.toStringProperties(sb);
        sb.append(",min="+minValue+",max="+maxValue+",default="+defaultValue);
    }

    public PParameterDescriptor getExtensionDescriptor()
    {
        return extensionDescriptor;
    }
    
    public void setExtensionDescriptor(PParameterDescriptor d)
    {
        this.extensionDescriptor = d;
    }

    /*
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }*/
    
}
