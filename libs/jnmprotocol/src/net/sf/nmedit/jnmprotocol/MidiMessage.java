/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003 Marcus Andersson

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

package net.sf.nmedit.jnmprotocol;

import java.util.*;
import net.sf.nmedit.jpdl.*;

public abstract class MidiMessage
{
    static {
	try {
	    usePdlFile("/midi.pdl", null);
	}
	catch (Exception e) {
	    System.out.println("MidiMessage: /midi.pdl not found.");
	}
    }

    public static void usePdlFile(String filename, Tracer tracer)
	throws Exception
    {
	pdlFile = filename;
	protocol = new Protocol(pdlFile);
	packetParser = protocol.getPacketParser("Sysex");
	protocol.useTracer(tracer);
    }
    
    public static int calculateChecksum(BitStream bitStream)
    {
	int checksum = 0;
	bitStream.setPosition(0);
	
	while (bitStream.isAvailable(24)) {
	    checksum += bitStream.getInt(8);
	}
	checksum = checksum % 128;
	
	return checksum;
    }

    public static boolean checksumIsCorrect(BitStream bitStream)
    {
	int checksum = calculateChecksum(bitStream);
	
	bitStream.setPosition(bitStream.getSize()-16);
	int messageChecksum = bitStream.getInt(8);
	
	if (messageChecksum == checksum) {
	    return true;
	}
	
	System.out.println("Checksum mismatch " + messageChecksum +
			   " != " + checksum + ".");
	return false;
    }

    public static MidiMessage create(BitStream bitStream)
	throws Exception
    {
	String error;
	Packet packet = new Packet();
	boolean success = packetParser.parse(bitStream, packet);
	bitStream.setPosition(0);
	
	if (success) {
	    if (packet.contains("IAm")) { 
		return new IAmMessage(packet);
	    }
	    
	    if (checksumIsCorrect(bitStream)) {
		
		if (packet.contains("VoiceCount")) {
		    return new VoiceCountMessage(packet);
		}
		if (packet.contains("SlotsSelected")) {
		    return new SlotsSelectedMessage(packet);
		}
		if (packet.contains("SlotActivated")) {
		    return new SlotActivatedMessage(packet);
		}
		if (packet.contains("NewPatchInSlot")) {
		    return new NewPatchInSlotMessage(packet);
		}
		if (packet.contains("Lights")) {
		    return new LightMessage(packet);
		}
		if (packet.contains("KnobChange")) {
		    return new ParameterMessage(packet);
		}
		if (packet.contains("PatchListResponse")) {
		    return new PatchListMessage(packet);
		}
		if(packet.contains("ACK")) {
		    return new AckMessage(packet);
		}
		if(packet.contains("NewModuleResponse")) {
		    return new NewModuleResponseMessage(packet);
		}
		
		if (packet.contains("PatchPacket")) {
		    return new PatchMessage(packet);
		}	  
	    }
	    error = "unsupported packet: ";
	}
	else {
	    error = "parse failed: ";
	}
	
	while (bitStream.isAvailable(8)) {
	    error += " " + bitStream.getInt(8);
	}

	throw new MidiException(error, 0);
    }

    public abstract List getBitStream()
	throws Exception;

    public abstract void notifyListener(NmProtocolListener listener)
	throws Exception;

    public boolean expectsReply()
    {
	return expectsreply;
    }

    public boolean isReply()
    {
	return isreply;
    }

    protected void addParameter(String name, String path)
    {
	parameters.add(name);
	paths.put(name, path);
    }

    protected boolean isParameter(String name)
    {
	return paths.keySet().contains(name);
    }

    public void set(String parameter, int value)
    {
	if (!parameters.contains(parameter)) {
	    throw new RuntimeException("Unsupported paramenter: " + parameter);
	}
	values.put(parameter, new Integer(value));
    }

    public int get(String parameter)
    {
	if (!parameters.contains(parameter)) {
	    throw new RuntimeException("Unsupported paramenter: " + parameter);
	}
	return ((Integer)values.get(parameter)).intValue();
    }

    public void setAll(Packet packet)
    {
	for (Iterator i = parameters.iterator(); i.hasNext(); ) {
	    String name = (String)i.next();
	    set(name, packet.getVariable((String)paths.get(name)));
	}	
    }

    public IntStream appendAll()
    {
	IntStream intStream = new IntStream();

	for (Iterator i = parameters.iterator(); i.hasNext(); ) {
	    intStream.append(get((String)i.next()));
	}

	return intStream;
    }
    
    protected MidiMessage()
	throws Exception
    {
	parameters = new LinkedList();
	paths = new HashMap();
	values = new HashMap();
	expectsreply = false;
	isreply = false;
	
	if (protocol == null) {
	    protocol = new Protocol(pdlFile);
	    packetParser = protocol.getPacketParser("Sysex");
	}

	addParameter("cc", "cc");
	addParameter("slot", "slot");
	set("slot", 0);
    }
  
    protected BitStream getBitStream(IntStream intStream)
	throws Exception
    {
	BitStream bitStream = new BitStream();
	boolean success = packetParser.generate(intStream, bitStream);
	
	if (!success || intStream.isAvailable(1)) {
	    throw new MidiException("Information mismatch in generate.",
				    intStream.getSize() - intStream.getPosition());
	}

	return bitStream;
    }

    protected void appendChecksum(IntStream intStream)
	throws Exception
    {
	int checksum = 0;
	intStream.append(checksum);
	
	BitStream bitStream = getBitStream(intStream);
	
	checksum = calculateChecksum(bitStream);
	
	intStream.setSize(intStream.getSize()-1);
	intStream.append(checksum);
	intStream.setPosition(0);
    }
    
    protected String extractName(Packet name)
    {
	String result = "";
	List chars = name.getVariableList("chars");
	for (Iterator i = chars.iterator(); i.hasNext(); ) {
	    int data = ((Integer)i.next()).intValue();
	    if (data != 0) {
		result += (char)data;
	    }
	}
	return result;
    }
    
    protected boolean isreply;
    protected boolean expectsreply;

    private LinkedList parameters;
    private HashMap paths;
    private HashMap values;

    private static Protocol protocol;
    private static String pdlFile;
    private static PacketParser packetParser;
}
