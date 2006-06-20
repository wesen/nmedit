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
 * Created on Apr 8, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc;

/**
 * Contains a list of integer values and a single string. 
 * 
 * The record class is close to the patch file format which contains
 * either a list of integers, a list of integers followed by a single string,
 * or only a single string. 
 * 
 * @author Christian Schneider
 */
public abstract class Record
{   
    /**
     * Returns the number of available integers in the current record. 
     * @return number of available integers in the current record
     */
    public abstract int getValueCount();

    /**
     * Returns a value of the current record. The value
     * is selected by it's index. Possible indices depend
     * on the current section ID and are declared
     * in {@link net.sf.nmedit.nomad.patch.Format}..
     * 
     * @param index index of the requested value
     * @return choosen value of the current record
     */
    public abstract int getValue(int index);

    /**
     * Returns the string value of a record if available or otherwise an empty string.
     * @return the string value of a record if available or otherwise an empty string
     */
    public abstract String getString();

    /**
     * Returns the ID of the current section. If no section has
     * been parsed a negative value will be returned.
     * 
     * @return the ID of the current section
     */
    public abstract int getSectionID();
    
}
