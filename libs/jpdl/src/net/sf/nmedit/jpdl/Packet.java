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

public class Packet
{
    public Packet()
    {
    }

    public void clear()
    {
	allVariables.clear();
	packets.clear();
	variables.clear();
	packetLists.clear();
	intLists.clear();
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public String getName()
    {
	return name;
    }

    public boolean contains(String packetName)
    {
	if (getName().equals(packetName)) {
	    return true;
	}
	else {
	    for (Iterator i = packets.values().iterator(); i.hasNext(); ) {
		if (((Packet)i.next()).contains(packetName)) {
		    return true;
		}
	    }
	    for (Iterator m = packetLists.values().iterator(); m.hasNext(); ) {
		for (Iterator n = ((List)m.next()).iterator(); n.hasNext(); ) {
		    if (((Packet)n.next()).contains(packetName)) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public void bindPacket(Packet packet, String name)
    {
	packets.put(name, packet);
    }

    public void bindVariable(int number, String name)
    {
	allVariables.add(name);
	variables.put(name, new Integer(number));
    }

    public void bindPacketList(List list, String name)
    {
	packetLists.put(name, list);
    }

    public void bindVariableList(List list, String name)
    {
	intLists.put(name, list);
    }

    public Packet getPacket(String name)
    {
	int pos = name.indexOf(":");
	if (pos >= 0) {
	    return ((Packet)packets.get(name.substring(0, pos)))
		.getPacket(name.substring(pos+1));
	}
	return (Packet)packets.get(name);
    }

    public int getVariable(String name)
    {
	int pos = name.indexOf(":");
	if (pos >= 0) {
	    return ((Packet)packets.get(name.substring(0, pos)))
		.getVariable(name.substring(pos+1));
	}
	return variables.containsKey(name) ?
	    ((Integer)variables.get(name)).intValue() : -1;
    }

    public List getAllVariables()
    {
	return allVariables;
    }
    
    public List getPacketList(String name)
    {
	int pos = name.indexOf(":");
	if (pos >= 0) {
	    return ((Packet)packets.get(name.substring(0, pos)))
		.getPacketList(name.substring(pos+1));
	}
	return (List)packetLists.get(name);	
    }

    public List getVariableList(String name)
    {
	int pos = name.indexOf(":");
	if (pos >= 0) {
	    return ((Packet)packets.get(name.substring(0, pos)))
		.getVariableList(name.substring(pos+1));
	}
	return (List)intLists.get(name);
    }

    private HashMap variables = new HashMap();
    private LinkedList allVariables = new LinkedList();
    private HashMap packets = new HashMap();
    private HashMap packetLists = new HashMap();
    private HashMap intLists = new HashMap();
    private String name;
}
