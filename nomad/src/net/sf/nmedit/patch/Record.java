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
package net.sf.nmedit.patch;


public class Record
{
    
    private String stringValue;
    private int[] values;
    private int limit;
    private int sectionID;
    
    public Record()
    {
        values = new int[100];
        reset();
    }
    
    public void setSize(int size)
    {
        limit = size;
        if (limit>values.length)
        {
            values = new int[limit];
        }
    }
    
    public int getValueCount()
    {
        return limit;
    }
    
    public void getValues(int[] dst)
    {
        if (dst.length<limit)
            throw new IllegalArgumentException("Target array too small.");
        
        for (int i=0;i<limit;i++)
            dst[i] = values[i];
    }

    public int getValue(int index)
    {
        if (index>=limit)
        {
            throw new IndexOutOfBoundsException();
        }
        return values[index];
    }
    
    public String getString()
    {
        return stringValue;
    }
    
    public void setString(String s)
    {
        stringValue = s == null ? "" : s;
    }

    public int getSectionID()
    {
        return sectionID;
    }

    public void setSectionID(int sectionID)
    {
        this.sectionID = sectionID;
    }

    public void setValue( int index, int value )
    {
        values[index] = value;
    }

    public void reset()
    {
        stringValue = "";
        limit = 0;
        sectionID = -1;
    }
    
    public void setValues(int[] values)
    {
        setSize(values.length);
        for (int i=0;i<values.length;i++)
            this.values[i] = values[i];
    }
    
}
