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

public class PacketParser
{
    public PacketParser(String name, int padding, Protocol protocol)
    {
	this.name = name;
	this.padding = padding;
	this.protocol = protocol;
    }

    public boolean parse(BitStream data, Packet result)
    {
	boolean conditional = false;
	boolean conditionalMatch = false;
	int dataPos = data.getPosition();
	
	protocol.trace("PARSE PACKET " + name);
	
	result.setName(name);
	
	for (Iterator i = matchers.iterator(); i.hasNext(); ) {
	    Matcher matcher = (Matcher)i.next();
	    if (matcher.isConditional()) {
		conditional = true;
		if (matcher.trueCondition(result)) {
		    boolean success = matcher.match(protocol, data, result);
		    if (success) {
			conditionalMatch = true;
		    }
		}
	    }
	    else {
		boolean success = matcher.match(protocol, data, result);
		if (!success && !matcher.isOptional()) {
		    protocol.trace("FAILED " + name);
		    data.setPosition(dataPos);
		    result.clear();
		    return false;
		}
	    }
	}
    
	if (!conditional || (conditional && conditionalMatch)) {
	    data.getInt(((data.getPosition()-dataPos) % padding) == 0 ?
			 0 :
			padding - ((data.getPosition()-dataPos) % padding));
	    protocol.trace("MATCHED " + name);
	    return true;
	}
	
	protocol.trace("FAILED " + name);
	data.setPosition(dataPos);
	result.clear();
	return false;
    }

    public boolean generate(IntStream data, BitStream result)
    {
	Packet packet = new Packet();
	boolean conditional = false;
	boolean conditionalMatch = false;
	int dataPos = data.getPosition();
	int resultSize = result.getSize();
	
	protocol.trace("GENERATE PACKET " + name);
	
	for (Iterator i = matchers.iterator(); i.hasNext(); ) {
	    Matcher matcher = (Matcher)i.next();
	    if (matcher.isConditional()) {
		conditional = true;
		if (matcher.trueCondition(packet)) {
		    boolean success =
			matcher.apply(protocol, packet, data, result);
		    if (success) {
			conditionalMatch = true;
		    }
		}
	    }
	    else {
		boolean success =
		    matcher.apply(protocol, packet, data, result);
		if (!success && !matcher.isOptional()) {
		    protocol.trace("FAILED " + name);
		    data.setPosition(dataPos);
		    result.setSize(resultSize);
		    return false;
		}
	    }
	}
	
	if (!conditional || (conditional && conditionalMatch)) {
	    result.append(0,
			  ((result.getSize()-resultSize) % padding) == 0 ?
			  0 :
			  padding - ((result.getSize()-resultSize) % padding));
	    protocol.trace("MATCHED " + name);
	    return true;
	}
	
	protocol.trace("FAILED " + name);
	data.setPosition(dataPos);
	result.setSize(resultSize);
	return false;
    }

    public void addPacketMatcher(String count, String packetName,
				 String binding, Condition condition,
				 boolean optional)
    {
	matchers.add(new PacketMatcher(count, packetName, binding,
				       condition, optional));
    }

    public void addVariableMatcher(int count, String variable, int size,
				   int terminal, Condition condition,
				   boolean optional)
    {
	matchers.add(new VariableMatcher(count, variable, size, terminal,
					 condition, optional));
    }

    public void addConstantMatcher(int constant, int size,
				   Condition condition, boolean optional)
    {
	matchers.add(new ConstantMatcher(constant, size, condition, optional));
    }
  
    private String name;
    private int padding;
    private Protocol protocol;
    private LinkedList matchers = new LinkedList();
}
