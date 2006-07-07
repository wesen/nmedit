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
 * Created on Apr 21, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Header;

public class HeaderEvent extends PatchEvent
{

    private int valueIndex;
    private Header header;
    private int old;
    private int newValue;

    public HeaderEvent(Header header)
    {
        setID(HEADER_VALUE_CHANGED);
        this.header =  header;
    }
    
    public void valueChanged(int oldValue, int newValue, int valueIndex)
    {
        setOldValue(oldValue);
        setNewValue(newValue);
        setValueIndex(valueIndex);
    }
    
    private void setNewValue( int newValue )
    {
        this.newValue = newValue;
    }

    public int getNewValue()
    {
        return newValue;
    }
    
    public void setOldValue(int old)
    {
        this.old = old;
    }
    
    public int getOldValue()
    {
        return old;
    }

    public void setValueIndex( int index )
    {
        this.valueIndex = index;
    }

    public int getValueIndex()
    {
        return valueIndex;
    }
    
    public Header getHeader()
    {
        return header;
    }
    
}
