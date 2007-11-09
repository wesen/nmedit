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

public class ConstantMatcher extends Matcher
{
    public ConstantMatcher(int value, int size,
			   Condition condition, boolean optional)
    {
	super(condition, optional);
	this.value = value;
	this.size = size;
    }

    public boolean match(Protocol protocol, BitStream data,
			 Packet result, int reserved)
    {
	trace(protocol);
	return data.isAvailable(size + reserved) && data.getInt(size) == value;
    }

    public boolean apply(Protocol protocol, Packet packet,
			 IntStream data, BitStream result)
    {
	trace(protocol);
	result.append(value, size);
	return true;
    }

    public int minimumSize()
    {
	if (isOptional()) {
	    return 0;
	}
	else {
	    return size;
	}
    }

    private int value;
    private int size;
    
    public String getSource()
    {
        return (isOptional() ? "?":"") + value+":"+size;
    }

}
