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
 * Created on Apr 11, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03;

public class Note
{

    private int noteNumber;
    private int attackVelocity;
    private int releaseVelocity;

    public Note(int noteNumber, int attackVelocity, int releaseVelocity)
    {
        this.noteNumber = noteNumber;
        this.attackVelocity = attackVelocity;
        this.releaseVelocity = releaseVelocity;
    }

    public int getAttackVelocity()
    {
        return attackVelocity;
    }

    public int getNoteNumber()
    {
        return noteNumber;
    }

    public int getReleaseVelocity()
    {
        return releaseVelocity;
    }
    
    public int hashCode()
    {
        return noteNumber;
    }

    public boolean equals(Object o)
    {
        return o==this || (o instanceof Note && ((Note)o).noteNumber == this.noteNumber);
    }
    
}
