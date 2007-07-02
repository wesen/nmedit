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
 * Created on Sep 4, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.io.Serializable;

import net.sf.nmedit.nmutils.collections.IndexedElement;

/**
 * A note configuration containing the note number, the notes attack
 * and relese velocity.
 * 
 * @author Christian Schneider
 */
public class Note implements Comparable<Note>, IndexedElement<Note>, Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -7223760755067137921L;

    /**
     * bits 21..15: release velocity
     * bits 14.. 8: attack velocity
     * bits  7.. 0: note number
     */
    private final int value; 

    private final static String NAME_NOTENUMBER = "note number";
    private final static String NAME_ATTACKVELOCITY = "attack velocity";
    private final static String NAME_RELEASEVELOCITY = "release velocity";
    
    /**
     * Creates a new note configuration.
     * 
     * @param noteNumber the notenumber in the range [0..127]
     * @param attackVelocity the attack velocity in the range [0..127]
     * @param releaseVelocity the release velocity in the range [0..127]
     * @throws IllegalArgumentException when one argument is out of range
     */
    public Note(int noteNumber, int attackVelocity, int releaseVelocity)
    {
        // check arguments
        checkRange(NAME_NOTENUMBER, noteNumber);
        checkRange(NAME_ATTACKVELOCITY, attackVelocity);
        checkRange(NAME_RELEASEVELOCITY, releaseVelocity);
        
        this.value = noteNumber | (attackVelocity<<8) | (releaseVelocity<<15);
    }

    /**
     * Validates the range of the specified value.
     *  
     * @param name name of the argument
     * @param value the checked value
     * @throws IllegalArgumentException if the specified
     * value is not in the range [0..127] 
     */
    private final static void checkRange(String name, int value)
    {
        if ((value<0) || (127<value))
            throw new IllegalArgumentException(name+" out of range:"+value);
    }

    /**
     * Comparison by note number.
     * @see Comparable#compareTo(T)
     */
    public int compareTo( Note o )
    {
        return getNoteNumber() - o.getNoteNumber();
    }

    /**
     * The specified object is equal to this object 
     * when it is a Note with the same note number.
     */
    public boolean equals(Object o)
    {
        return (o==this) || ((o != null) && (o instanceof Note) && (compareTo((Note)o)==0));
    }

    /**
     * Returns the note number
     * @return the note number
     */
    public int hashCode()
    {
        return getNoteNumber();
    }

    /**
     * Returns the note number
     * @return the note number
     */
    public int getIndex()
    {
        return getNoteNumber();
    }

    /**
     * Returns the note number
     * @return the note number
     */
    public int getNoteNumber()
    {
        return value & 0x7F;
    }

    /**
     * Returns the attack velocity
     * @return the attack velocity
     */
    public int getAttackVelocity()
    {
        return (value>>8) & 0x7F;
    }

    /**
     * Returns the release velocity
     * @return the release velocity
     */
    public int getReleaseVelocity()
    {
        return (value>>15) & 0x7F;        
    }
    
    public String toString()
    {
        return "["+NAME_NOTENUMBER+"="+getNoteNumber()+","
        +NAME_ATTACKVELOCITY+"="+getAttackVelocity()+","
        +NAME_RELEASEVELOCITY+"="+getReleaseVelocity()+"]";
    }

    public Note getElement()
    {
        return this;
    }
    
}
