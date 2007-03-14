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
 * Created on Dec 11, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jpatch.AbstractParameter;
import net.sf.nmedit.jpatch.Module;

public class Morph extends AbstractParameter
{

    private MorphSection section;
    private MorphDescriptor descriptor;
    private int value;
    private List<NMParameter> assignments = new ArrayList<NMParameter>(2);

    public Morph(MorphSection section, MorphDescriptor descriptor)
    {
        this.section = section;
        this.descriptor = descriptor;
        this.value = descriptor.getDefaultValue();
    }
    
    public int getAssignmentsCount()
    {
        return assignments.size();
    }
    
    public void add(NMParameter param)
    {
        // max capacity/assignments = 25
        if (assignments.size()<25)
            assignments.add(param);
    }
    
    public void remove(NMParameter param)
    {
        assignments.remove(param);
    }

    @Override
    public int getValue()
    {
        return value;
    }

    @Override
    public void setValue( int value )
    {
        value = Math.max(getMinValue(), Math.min(getMaxValue(), value));
        if (this.value != value)
        {
            this.value = value;
            fireParameterValueChanged();
        }
    }

    public MorphDescriptor getDescriptor()
    {
        return descriptor;
    }

    public Module getOwner()
    {
        return section;
    }

    public NMParameter[] getAssignments()
    {
        return assignments.toArray(new NMParameter[assignments.size()]);
    }
    
    public String toString()
    {
        return "Morph[id="+getDescriptor().getMorphId()+",value="+getValue()
        +",assignments="+assignments.size()+"]";
    }

}
