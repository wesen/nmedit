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
package net.sf.nmedit.jpatch.clavia.nordmodular;

import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PAssignmentEvent;

public class MidiController
{
    public static final int MODULATION_WHEEL = 1;
    public static final int FootPedal = 4;
    public static final int VOLUME = 7;
    
    private final int ID;
    private PParameter parameter;
    private MidiControllerSet set;

    MidiController(MidiControllerSet set, int ID)
    {
        this.set = set;
        this.ID = ID;
        this.parameter = null;
    }
    
    public MidiControllerSet getMidiControllerSet()
    {
        return set;
    }
    
    public int getControlId()
    {
        return ID;
    }

    public void setParameter(PParameter parameter)
    {
        if (this.parameter != parameter)
        {
            if (this.parameter != null)
                fireDeassigned(this.parameter);
            this.parameter = null;
            
            if (parameter != null)
            {
                MidiController[] all = set.midiControllerList;
                for (int i=all.length-1;i>=0;i--)
                {
                    MidiController mc = all[i];
                    if (mc != this && mc.getParameter() == parameter)
                    {
                        mc.setParameter(null);
                        break;
                    }
                }
            }
            
            this.parameter = parameter;
            if (parameter != null)
                fireAssigned(this.parameter);
        }
    }
    
    private void fireDeassigned(PParameter parameter)
    {
        PAssignmentEvent e = new PAssignmentEvent();
        e.midiDeAssigned(this, parameter);
        set.getPatch().fireAssignmentEvent(e);
    }
    
    private void fireAssigned(PParameter parameter)
    {
        PAssignmentEvent e = new PAssignmentEvent();
        e.midiAssigned(this, parameter);
        set.getPatch().fireAssignmentEvent(e);
    }

    public PParameter getParameter()
    {
        return parameter;
    }
    
    public final static boolean isValidCC(int cc) {
        return (0<=cc && cc<120) && (cc!=32);
    }
    
    public String getName()
    {
        return MidiController.getDefaultName(ID);
    }

    public static String getDefaultName(int midiControllerID)
    {
        switch (midiControllerID)
        {
            case 1: return "Modulation Wheel (CC 1)";
            case 2: return "Breath Controller (CC 2)";
            case 4: return "Foot Pedal (CC 4)";
            case 5: return "Portamento Time (CC 5)";
            case 6: return "Data Entry Slider (CC 6)";
            case 7: return "Volume (CC 7)";
            case 8: return "Balance (CC 8)";
            case 10: return "Pan (CC 10)";
            case 11: return "Expression (CC 11)";
            case 65: return "Portamento (CC 65)";
            case 66: return "Sostenuto (CC 66)";
            case 67: return "Soft Pedal (CC 67)";
            case 69: return "Hold 2 Pedal (CC 69)";
            case 91: return "Effects Depth (CC 91)";
            case 92: return "Tremolo Depth (CC 92)";
            case 93: return "Chorus Depth (CC 93)";
            case 94: return "Celeste Depth (CC 94)";
            case 95: return "Phaser Depth (CC 95)";
            case 96: return "Data Increment (CC 96)";
            case 97: return "Data Decrement (CC 97)";
            default: return "Controller " + midiControllerID;
        }
    }

    public String toString()
    {
        return getClass().getName()+"[id="+getControlId()+",name='"+getName()+"']";
    }
    
}
