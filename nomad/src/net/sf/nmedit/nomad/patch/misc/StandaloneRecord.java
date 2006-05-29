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
package net.sf.nmedit.nomad.patch.misc;

/**
 * Implementation of the class {@link net.sf.nmedit.nomad.patch.misc.Record}.
 * 
 * @author Christian Schneider
 */
public class StandaloneRecord extends Record
{
    
    /**
     * string value.
     */
    private String stringValue;
    
    /**
     * list of integers
     */
    private int[] values;
    
    /**
     * the limit is the number of valid values in the list {@link #values} 
     * 
     * @see #setSize(int)
     */
    private int limit;
    
    /**
     * ID of the current section
     */
    private int sectionID;
    
    public StandaloneRecord()
    {
        values = new int[100];
        sectionID = -1;
        reset();
    }
    
    /**
     * Sets the size of the integer list. 
     * Current values of the list might be list after
     * calling this method.
     * 
     * @param size
     */
    public void setSize(int size)
    {
        // set number of valid values
        limit = size;
        
        // check if the array can not hold
        // all values
        if (limit>values.length)
        {
            // enlarge the array
            values = new int[limit];
        }
    }

    @Override
    public int getValueCount()
    {
        return limit;
    }

    @Override
    public int getValue(int index)
    {
        if (index>=limit)
        {
            throw new IndexOutOfBoundsException();
        }
        return values[index];
    }

    @Override
    public String getString()
    {
        return stringValue;
    }

    @Override
    public int getSectionID()
    {
        return sectionID;
    }
    
    /**
     * Sets the string value property.
     * @param s the new string value
     */
    public void setString(String s)
    {
        stringValue = s == null ? "" : s;
    }

    /**
     * Sets the section ID property
     * @param sectionID the new section ID
     */
    public void setSectionID(int sectionID)
    {
        this.sectionID = sectionID;
    }

    /**
     * Sets a value in the integer list at the given index
     *  
     * @param index index of the value
     * @param value the value
     * @throws IndexOutOfBoundsException 
     */
    public void setValue( int index, int value )
    {
        if (index>=limit)
            throw new IndexOutOfBoundsException();
        
        values[index] = value;
    }

    /**
     * Sets the string value to an empty string and
     * sets the size of the integer list to zero.
     */
    public void reset()
    {
        stringValue = "";
        limit = 0;
    }
    
    /**
     * Sets the integer list to the size of
     * the given integer list and copies it's values.
     * 
     * @param values the new integer list
     */
    public void setValues(int[] list)
    {
        setSize(list.length);
        for (int i=0;i<list.length;i++)
            this.values[i] = list[i];
    }
    
}
