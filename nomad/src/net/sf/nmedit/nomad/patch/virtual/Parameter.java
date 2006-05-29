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
 * Created on Apr 10, 2006
 */
package net.sf.nmedit.nomad.patch.virtual;

import net.sf.nmedit.nomad.patch.virtual.event.ListenableAdapter;
import net.sf.nmedit.nomad.patch.virtual.event.ParameterEvent;
import net.sf.nmedit.nomad.patch.virtual.misc.Assignment;
import net.sf.nmedit.nomad.xml.dom.module.DParameter;


public class Parameter extends ListenableAdapter<ParameterEvent> implements Assignment
{

    private final DParameter definition;
    private int value;
    
    /**
     * 
     * morph = -127..127bbm
     * 
     * WRONG:
     * 
     * The real morph value has the same range as 'value'.
     * The internal morph value is relative to 'value'
     * and is in the range -maxValue<=internalMorph<=maxValue.
     * A negative value means it is smaller than value.
     * A positive value means it is bigger than value.
     * A value of null (0) means it is equal than value.
     */
    private int morphRange;
    private final Module module;
    private Knob assignedKnob;
    private Morph assignedMorph;
    //private MidiController assignedMidiController;
    // private MidiController midiController;

    private static ParameterEvent eventMessage = new ParameterEvent();

    /*
    private Morph morph;*/

    public Parameter( DParameter definition, Module module )
    {
        this.module = module;
        this.definition = definition;
        this.value = definition.getDefaultValue();
        this.morphRange = 0;
    }
  /*  
    void setAssignedMidiController(MidiController mc)
    {
        if (assignedMidiController!=mc)
        {
            assignedMidiController = mc;
        }
    }
*/
    void setAssignedMorph(Morph m)
    {
        if (assignedMorph!=m)
        {
            assignedMorph = m;
            eventMessage.morphAssignment(this);
            fireEvent(eventMessage);
        }
    }
    
    public Morph getAssignedMorph()
    {
        return assignedMorph;
    }
    
    void setAssignedKnob(Knob k)
    {
        if (assignedKnob!=k)
        {
            assignedKnob = k;
            eventMessage.knobAssignment(this);
            fireEvent(eventMessage);
        }
    }
    
    public Knob getAssignedKnob()
    {
        return assignedKnob;
    }

    public Module getModule()
    {
        return module;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int v)
    {
        if (this.value != v && isValueInRange(v))
        {
            this.value = v;
            eventMessage.valueChanged(this);
            fireEvent(eventMessage);
        }
    }
    
    public int getMorphRange()
    {
        // we assure that the morph value is in the range minValue <= morph <= maxValue.
        //return Math.max(getMinValue(), Math.min(value+internalMorph, getMaxValue()));
        return morphRange;
    }
    
    public void setMorphRange(int morph)
    {
        if (morphRange!=morph)
        {
            morphRange = morph;
            eventMessage.morphRangeChanged(this);
            fireEvent(eventMessage);
        }
    }

    public final DParameter getDefinition()
    {
        return definition;
    }

    public String getName()
    {
        return definition.getName();
    }

    public String getFormattedValue( int value )
    {
        return definition.getFormattedValue( value );
    }

    public String getFormattedValue( int value, int maxdigits )
    {
        return definition.getFormattedValue( value, maxdigits );
    }

    public int getNumStates()
    {
        return definition.getNumStates();
    }

    public int getMinValue()
    {
        return definition.getMinValue();
    }

    public int getMaxValue()
    {
        return definition.getMaxValue();
    }

    public int getDefaultValue()
    {
        return definition.getDefaultValue();
    }

    public int getID()
    {
        return definition.getId();
    }

    public boolean isValueInRange(int v)
    {
        return getMinValue()<=v && v<=getMaxValue();
    }
    
    public void zeroMorph()
    {
        setMorphRange(0);
    }
    
}
