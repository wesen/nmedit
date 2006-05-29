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

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;

import net.sf.nmedit.nomad.patch.virtual.event.MorphEvent;
import net.sf.nmedit.nomad.patch.virtual.misc.Assignment;

public class Morph extends HashSet<Parameter> implements Assignment
{

    private final int ID;
    private int value;
    private KeyboardAssignment keyboardAssignment;
    private final MorphSet set;
    private final static int maxCapacity = 25;
    private static MorphEvent eventMessage = new MorphEvent();

    // TODO right colors
    private Color clMorph1 = Color.decode("#BD7B7B");
    private Color clMorph2 = Color.decode("#84B284");
    private Color clMorph3 = Color.decode("#8081B2");
    private Color clMorph4 = Color.decode("#B8AE7C");
    
    // assign to : keyboard: Note / Velocity
    
    public Morph(MorphSet set, int ID)
    {
        this.set = set;
        this.ID = ID;
        this.keyboardAssignment = KeyboardAssignment.NONE;
    }
    
    public Color getColor()
    {
        switch (ID)
        {
            case 0: return clMorph1;
            case 1: return clMorph2;
            case 2: return clMorph3;
            case 3: return clMorph4;
            default: return clMorph1;
        }
    }

    public boolean add( Parameter p )
    {
        if (size()<maxCapacity)
        {
            if (p.getAssignedMorph()!=null)
            {
                p.getAssignedMorph().remove(p);
            }
         
            if (super.add(p))
            {
                p.setAssignedMorph(this);
                
                eventMessage.assigned(this, null, p);
                set.fireEvent(eventMessage);
                
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean remove( Object obj )
    {
        if (super.remove(obj))
        {
            Parameter p = ((Parameter) obj);
            p.setAssignedMorph(null);
            
            eventMessage.assigned(this, p, null);
            set.fireEvent(eventMessage);

            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean addAll( Collection<? extends Parameter> c )
    {
        throw new UnsupportedOperationException();
    }
    
    public final int getID()
    {
        return ID;
    }
    
    public String getName()
    {
        return "Morph"+(ID+1);
    }

    public void setValue( int value )
    {
        if (this.value!=value)
        {
            this.value = value;
            eventMessage.valueChanged(this);
            set.fireEvent(eventMessage);
        }
    }
    
    public int getValue()
    {
        return value;
    }

    public void setKeyboardAssignment( KeyboardAssignment keyboardAssignment )
    {
        if (this.keyboardAssignment!=keyboardAssignment)
        {
            this.keyboardAssignment = keyboardAssignment;
            eventMessage.keyboardAssignmentChanged(this);
            set.fireEvent(eventMessage);
        }
    }
    
    public KeyboardAssignment getKeyboardAssignment()
    {
        return keyboardAssignment;
    }

}
