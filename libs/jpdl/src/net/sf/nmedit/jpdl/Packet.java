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
	    for (Packet packet: packets.values()) {
		if (packet.contains(packetName)) {
		    return true;
		}
	    }
	    for (List<Packet> packetList: packetLists.values()) {
		for (Packet packet: packetList) {
		    if (packet.contains(packetName)) {
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
	variables.put(name, number);
    }

    public void bindPacketList(List<Packet> list, String name)
    {
	packetLists.put(name, list);
    }

    public void bindVariableList(List<Integer> list, String name)
    {
	intLists.put(name, list);
    }

    public Packet getPacket(String name)
    {
	int pos = name.indexOf(":");
	if (pos >= 0) {
	    return packets.get(name.substring(0, pos))
		.getPacket(name.substring(pos+1));
	}
	return packets.get(name);
    }

    public int getVariable(String name)
    {
	int pos = name.indexOf(":");
	if (pos >= 0) {
	    return packets.get(name.substring(0, pos))
		    .getVariable(name.substring(pos+1));
	}
    
    Integer variable = variables.get(name);
    return variable == null ? -1 : variable;
    }

    public List<String> getAllVariables()
    {
	return allVariables;
    }
    
    public List<Packet> getPacketList(String name)
    {
	int pos = name.indexOf(":");
	if (pos >= 0) {
	    return packets.get(name.substring(0, pos))
		.getPacketList(name.substring(pos+1));
	}
	return packetLists.get(name);	
    }

    public List<Integer> getVariableList(String name)
    {
	int pos = name.indexOf(":");
	if (pos >= 0) {
	    return packets.get(name.substring(0, pos))
		.getVariableList(name.substring(pos+1));
	}
	return intLists.get(name);
    }
    
    public Iterator<Packet> packets()
    {
        return packets.values().iterator();
    }

    private Map<String, Integer> variables = new HashMap<String, Integer>();
    private List<String> allVariables = new LinkedList<String>();
    private Map<String, Packet> packets = new HashMap<String, Packet>();
    private Map<String, List<Packet>> packetLists = new HashMap<String, List<Packet>>();
    private Map<String, List<Integer>> intLists = new HashMap<String, List<Integer>>();
    private String name;
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("[");
        sb.append("name='");
        sb.append(name);
        sb.append("'");
        toStringParams(sb);
        sb.append("]");
        return sb.toString();
    }

    private void toStringParams(StringBuilder sb)
    {
        sb.append(",variables={");
        int count = allVariables.size()-1;
        for (String varName: allVariables)
        {
            sb.append(varName);
            sb.append("=");
            sb.append(variables.get(varName));
            if (count>0)
                sb.append(",");
            count--;
        }
        sb.append("}, packets={");
        count = packets.size()-1;
        for (String packet: packets.keySet())
        {
            sb.append(packet);
            if (count>0)
                sb.append(",");
            count--;
        }
        sb.append("}");
    }
    
}
