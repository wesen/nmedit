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
package net.sf.nmedit.jpatch.clavia.nordmodular.event;

import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.event.PPatchEvent;

public class PAssignmentEvent extends PPatchEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = 4539602407743939224L;
    public static final int KNOB_ASSIGNED = CUSTOM_EVENT_START+1;
    public static final int KNOB_DEASSIGNED = CUSTOM_EVENT_START+2;
    public static final int MORPH_ASSIGNED = CUSTOM_EVENT_START+3;
    public static final int MORPH_DEASSIGNED = CUSTOM_EVENT_START+4;
    public static final int MIDICTRL_ASSIGNED = CUSTOM_EVENT_START+5;
    public static final int MIDICTRL_DEASSIGNED = CUSTOM_EVENT_START+6;

    public PAssignmentEvent()
    {
        super(null, -1, null);
    }
    
    private int targetId;
    
    private void set(int targetId, PParameter parameter, int id)
    {
        this.targetId = targetId;
        this.arg = parameter;
        this.id = id;
    }
    
    public void knobAssigned(Knob k, PParameter parameter)
    {
        set(k.getID(), parameter, KNOB_ASSIGNED);
    }
    
    public void knobDeAssigned(Knob k, PParameter parameter)
    {
        set(k.getID(), parameter, KNOB_DEASSIGNED);
    }

    public void morphAssigned(int morphGroup, PParameter parameter)
    {
        set(morphGroup, parameter, MORPH_ASSIGNED);
    }
    
    public void morphDeAssigned(int morphGroup, PParameter parameter)
    {
        set(morphGroup, parameter, MORPH_DEASSIGNED);
    }

    public void midiAssigned(MidiController mc, PParameter parameter)
    {
        set(mc.getControlId(), parameter, MIDICTRL_ASSIGNED);
    }
    
    public void midiDeAssigned(MidiController mc, PParameter parameter)
    {
        set(mc.getControlId(), parameter, MIDICTRL_DEASSIGNED);
    }

    public int getMorphGroup()
    {
        return (id == MORPH_ASSIGNED || id == MORPH_DEASSIGNED)
            ? targetId : -1;
    }

    public int getKnobId()
    {
        return (id == KNOB_ASSIGNED || id == KNOB_DEASSIGNED)
            ? targetId : -1;
    }

    public int getMidiControllerId()
    {
        return (id == MIDICTRL_ASSIGNED || id == MIDICTRL_DEASSIGNED)
            ? targetId : -1;
    }

    public boolean isAssignmentEvent()
    {
        return id == MORPH_ASSIGNED || id == KNOB_ASSIGNED || id == MIDICTRL_ASSIGNED;
    }
    
    public PParameter getParameter()
    {
        return (PParameter) arg;
    }
    
    public String toString()
    {
        boolean de = false;
        String what = null;
        switch (id)
        {
            case MORPH_DEASSIGNED:
                de = true;
            case MORPH_ASSIGNED:
                what = "morph";
                break;
            case MIDICTRL_DEASSIGNED:
                de = true;
            case MIDICTRL_ASSIGNED:
                what = "MIDI controller";
                break;
            case KNOB_DEASSIGNED:
                de = true;
            case KNOB_ASSIGNED:
                what = "Knob";
                break;
        }
        
        return getClass().getName()+"[action='"+(de?"de":"")+"assign "+what+"',id="+id+",parameter="+getParameter()+"]";
    }
    
}
