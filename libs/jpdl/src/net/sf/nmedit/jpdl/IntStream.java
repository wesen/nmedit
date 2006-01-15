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

public class IntStream
{
    public IntStream()
    {
	position = 0;
	ints = new LinkedList();
    }

    public void append(int data)
    {
	ints.add(new Integer(data));
    }

    public int getPosition()
    {
	return position;
    }
    
    public int getSize()
    {
	return ints.size();
    }

    public void setPosition(int position)
    {
	this.position = position;
    }

    public void setSize(int size)
    {
	ints = ints.subList(0, size);
	if (position > size) {
	    position = size;
	}
    }

    public boolean isAvailable(int amount)
    {
	return position+amount <= ints.size();
    }

    public int getInt()
    {
	return ((Integer)ints.get(position++)).intValue();
    }

    private List ints;
    private int position;
}
