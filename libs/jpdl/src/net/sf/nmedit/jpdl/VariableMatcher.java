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

public class VariableMatcher extends Matcher
{
    public VariableMatcher(int count, String variable, int size, int terminal,
			   Condition condition, boolean optional)
    {
	super(condition, optional);
	this.count = count;
	this.variable = variable;
	this.size = size;
	this.terminal = terminal;
    }

    public boolean match(Protocol protocol, BitStream data, Packet result)
    {
	LinkedList variableList = new LinkedList();
	
	trace(protocol);
	
	for (int i = 0; i < count; i++) {
	    if (data.isAvailable(size)) {
		int value = data.getInt(size);
		variableList.add(new Integer(value));
		
		if (value == terminal) {
		    break;
		}
	    }
	    else {
		return false;
	    }
	}
	
	if (count == 1) {
	    result.bindVariable(((Integer)variableList.get(0)).intValue(),
				variable);
	}
	else {
	    result.bindVariableList(variableList, variable);
	}
	return true;
    }

    public boolean apply(Protocol protocol, Packet packet,
			 IntStream data, BitStream result)
    {
	LinkedList variableList = new LinkedList();
	
	trace(protocol);
	
	for (int i = 0; i < count; i++) {
	    if (data.isAvailable(1)) {
		int value = data.getInt();
		variableList.add(new Integer(value));
		result.append(value, size);
		
		if (value == terminal) {
		    break;
		}
	    }
	    else {
		return false;
	    }
	}
	
	if (count == 1) {
	    packet.bindVariable(((Integer)variableList.get(0)).intValue(),
				variable);
	}
	else {
	    packet.bindVariableList(variableList, variable);
	}
	return true;
    }
  
    private void trace(Protocol protocol)
    {
	if (count == 1) {
	    protocol.trace(variable + ":" + size);
	}
	else {
	    protocol.trace("" + count + "*" + variable + ":" + size);
	}
    }

    private int count;
    private String variable;
    private int size;
    private int terminal;
}
