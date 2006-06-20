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
 * Created on Apr 17, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03;

public enum KeyboardAssignment
{
       
    NONE (0) ,
    VELOCITY(1),
    NOTE(2);
    
    private final int ID;

    private KeyboardAssignment(int ID)
    {
        this.ID = ID;
    }
    
    public final int getID()
    {
        return ID;
    }
    
    public String toString()
    {
        switch (ID)
        {
            case 0: return "None";
            case 1: return "Velocity";
            case 2: return "Note";
            default: throw new IndexOutOfBoundsException();
        }
    }
    
    public static KeyboardAssignment byID(int ID)
    {
        switch (ID)
        {
            case 0: return NONE;
            case 1: return VELOCITY;
            case 2: return NOTE;
            default: throw new IndexOutOfBoundsException();
        }
    }
    
}
