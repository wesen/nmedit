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

public class PacketMatcher extends Matcher
{
    public PacketMatcher(String count, String parserName, String binding,
			 Condition condition, boolean optional)
    {
	super(condition, optional);
	this.count = count;
	this.parserName = parserName;
	this.binding = binding;
    }

    public boolean match(Protocol protocol, BitStream data,
			 Packet result, int reserved)
    {
	int iterations;
	List<Packet> packetList = new LinkedList<Packet>();
	PacketParser packetParser = protocol.getPacketParser(parserName);
	
	trace(protocol);
	
	if (packetParser == null) {
	    return false;
	}
	
	if (count.length()==0) {
	    iterations = 1;
	}
	else {
	    iterations = result.getVariable(count);
	    if (iterations < 0) {
		return false;
	    }
	}
	
	for (int i = 0; i < iterations; i++) {
	    Packet packet = new Packet();
	    if (packetParser.parse(data, packet, reserved)) {
		packetList.add(packet);
	    }
	    else {
		return false;
	    }
	}
	
	if (count.length()==0) {
	    result.bindPacket(packetList.get(0), binding);
	}
	else {
	    result.bindPacketList(packetList, binding);
	}
	return true;
    }

    public boolean apply(Protocol protocol, Packet packet,
			 IntStream data, BitStream result)
    {
	int iterations;
	PacketParser packetParser = protocol.getPacketParser(parserName);
	
	trace(protocol);
	
	if (packetParser == null) {
	    return false;
	}
	
	if (count.length()==0) {
	    iterations = 1;
	}
	else {
	    iterations = packet.getVariable(count);
	    if (iterations < 0) {
		return false;
	    }
	}
	
	for (int i = 0; i < iterations; i++) {
	    if (!packetParser.generate(data, result)) {
		return false;
	    }
	}
	return true;
    }
    
    public int minimumSize()
    {
	return 0;
    }

    private String count;
    private String parserName;
    private String binding;

    public String getSource()
    {
        return (isOptional() ? "?":"") + (count.length()==0 ? "" : count + "*") + parserName + "$" + binding;
    }
    
}
