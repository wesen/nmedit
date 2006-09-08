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
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.Assignment;

public class MidiController
{

    private final int ID;
    private Assignment assignment;
    private MidiControllerSet set;

    MidiController(MidiControllerSet set, int ID)
    {
        this.set = set;
        this.ID = ID;
        this.assignment = null;
    }
    
    public int getID()
    {
        return ID;
    }

    public void deAssign()
    {
        assignTo(null);
    }
    
    public void assignTo(Assignment a)
    {
        if (this.assignment != a)
        {
            Assignment oldValue = this.assignment;
            this.assignment = a;
            set.fireAssignmentChanged(this, oldValue, a);
        }
    }

    public final static boolean isValidCC(int cc) {
        return (0<=cc && cc<=120) && (cc!=32);
    }
    
    public Assignment getAssignment()
    {
        return assignment;
    }
    
    public boolean isAssignedTo(Object object)
    {
        return assignment == object;
    }
    
    public String getName()
    {
        return MidiController.getDefaultName(ID);
    }
    
    public static String getDefaultName(int midiControllerID)
    {
        switch (midiControllerID)
        {
            case 1: return "Modulation Wheel";
            case 2: return "Breath Controller";
            case 4: return "Foot Pedal";
            case 5: return "Portamento Time";
            case 6: return "Data Entry Slider";
            case 7: return "Volume";
            case 8: return "Balance";
            case 10: return "Pan";
            case 11: return "Expression";
            case 65: return "Portamento";
            case 66: return "Sostenuto";
            case 67: return "Soft Pedal";
            case 69: return "Hold 2 Pedal";
            case 91: return "Effects Depth";
            case 92: return "Tremolo Depth";
            case 93: return "Chorus Depth";
            case 94: return "Celeste Depth";
            case 95: return "Phaser Depth";
            case 96: return "Data Increment";
            case 97: return "Data Decrement";
            default: return null;
        }
    }

}
