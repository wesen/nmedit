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
	this.totalMinimumSize = 0;
    }

    public boolean parse(BitStream data, Packet result)
    {
	return parse(data, result, 0);
    }

    public boolean parse(BitStream data, Packet result, int reserved)
    {
	boolean conditional = false;
	boolean conditionalMatch = false;
	int dataPos = data.getPosition();
	
	if (protocol.isTraceEnabled()) {
	    protocol.trace("PARSE PACKET " + name);
	}
	
	result.setName(name);
	
	int minimumSize = totalMinimumSize + reserved;
	for (Matcher matcher: matchers) {
	    minimumSize -= matcher.minimumSize();
	    if (matcher.isConditional()) {
		conditional = true;
		if (matcher.trueCondition(result)) {
		    boolean success = matcher.match(protocol, data,
						    result, minimumSize);
		    if (success) {
			conditionalMatch = true;
		    }
		}
	    }
	    else {
		boolean success = matcher.match(protocol, data,
						result, minimumSize);
		if (!success && !matcher.isOptional()) {
            if (protocol.isTraceEnabled())
            {
                protocol.trace("FAILED " + name+"; "+matcher);
            }
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
	    if (protocol.isTraceEnabled()) {
		protocol.trace("MATCHED " + name);
	    }
	    return true;
	}
	
	if (protocol.isTraceEnabled()) {
	    protocol.trace("FAILED " + name);
	}
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
	
	if (protocol.isTraceEnabled()) {
	    protocol.trace("GENERATE PACKET " + name);
	}

	for (Matcher matcher: matchers) {
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
            if (protocol.isTraceEnabled())
            {
                protocol.trace("FAILED " + name+"; "+matcher);
            }
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
	    if (protocol.isTraceEnabled()) {
		protocol.trace("MATCHED " + name);
	    }
	    return true;
	}
	
	if (protocol.isTraceEnabled()) {
	    protocol.trace("FAILED " + name);
	}
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
	Matcher matcher = new VariableMatcher(count, variable, size,
					      terminal, condition, optional);
	matchers.add(matcher);
	totalMinimumSize += matcher.minimumSize();
    }

    public void addConstantMatcher(int constant, int size,
				   Condition condition, boolean optional)
    {
	Matcher matcher = new ConstantMatcher(constant, size,
					      condition, optional);
	matchers.add(matcher); 
	totalMinimumSize += matcher.minimumSize();
    }
  
    public String getName()
    {
        return name;
    }
    
    public List<Matcher> getMatchers()
    {
        return Collections.unmodifiableList(matchers);
    }
    
    private String name;
    private int padding;
    private Protocol protocol;
    private List<Matcher> matchers = new LinkedList<Matcher>();
    private int totalMinimumSize;
}
