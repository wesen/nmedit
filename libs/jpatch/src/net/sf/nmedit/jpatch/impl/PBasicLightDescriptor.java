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

import net.sf.nmedit.jpatch.PLightDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;

public class PBasicLightDescriptor extends PBasicDescriptor implements
        PLightDescriptor//, Serializable
{

    private static final long serialVersionUID = -6555243015172013690L;
    private int defaultValue = 0;
    private int minValue = 0;
    private int maxValue = 1;
    private int type = TYPE_UNKNOWN;
    private transient PModuleDescriptor parent;
    
    public PBasicLightDescriptor(PModuleDescriptor parent, String name, Object componentId)
    {
        super(name, componentId);
        this.parent = parent;
    }

    void setParent(PModuleDescriptor parent)
    {
        this.parent = parent;
    }

    public void setType(int type)
    {
        this.type = type;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setDefaultValue(int value)
    {
        this.defaultValue = value;
    }
    
    public void setMaxValue(int value)
    {
        this.maxValue = value;
    }
    
    public void setMinValue(int value)
    {
        this.minValue = value;
    }

    public int getDefaultValue()
    {
        return defaultValue;
    }

    public int getMaxValue()
    {
        return maxValue;
    }

    public int getMinValue()
    {
        return minValue;
    }
    
    public PModuleDescriptor getParentDescriptor()
    {
        return parent;
    }
/*
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
    */
}
