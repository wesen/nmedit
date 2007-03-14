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

import net.sf.nmedit.jpatch.Parameter;

public class Knob
{

    private final int ID;
    private KnobSet set;
    private Parameter parameter = null;
    
    Knob(KnobSet set, int ID)
    {
        this.set = set;
        this.ID = ID;
    }
    
    public KnobSet getKnobSet()
    {
        return set;
    }
    
    public final int getID()
    {
        return ID;
    }
    
    public void setParameter(Parameter parameter)
    {
        this.parameter = parameter;
    }
    
    public Parameter getParameter()
    {
        return parameter;
    }

    public String getName()
    {
        return Knob.getDefaultName(ID);
    }

    public static String getDefaultName(int knobID)
    {
        if (0<=knobID && knobID<=17)
        {
            return "Knob"+(knobID+1);
        }
        else
        {
            switch(knobID)
            {
                case 19: return "Pedal";
                case 20: return "After touch";
                case 22: return "On/Off switch";
                default: return null;
            }
        }        
    }
    
}
