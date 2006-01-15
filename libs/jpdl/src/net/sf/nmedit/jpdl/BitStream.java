/*
    Protocol Definition Language
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package net.sf.nmedit.jpdl;

import java.util.*;

public class BitStream
{
    public BitStream()
    {
	position = 0;
	bits = new LinkedList();
    }

    public void append(int data, int size)
    {
	for (int i = size-1; i >= 0; i--) {
	    bits.add(new Integer((data >> i) & 1));
	}
    }

    public int getPosition()
    {
	return position;
    }

    public int getSize()
    {
	return bits.size();
    }

    public void setPosition(int position)
    {
	this.position = position;
    }

    public void setSize(int size)
    {
	bits = bits.subList(0, size);
	if (position > size) {
	    position = size;
	}	
    }
    
    public boolean isAvailable(int amount)
    {
	return position+amount <= bits.size();
    }

    public int getInt(int size)
    {
	int result = 0;
	for (int i = 0; i < size; i++) {
	    result = (result << 1) +
		((Integer)bits.get(position++)).intValue();
	}
	return result;
    }
    
    private List bits;
    private int position;
}
