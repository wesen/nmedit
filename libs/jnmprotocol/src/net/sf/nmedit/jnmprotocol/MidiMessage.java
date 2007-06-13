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

import net.sf.nmedit.jnmprotocol.utils.StringUtils;
import net.sf.nmedit.jpdl.*;

public abstract class MidiMessage
{

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
	bitStream.setPosition(0);
	
	if (messageChecksum == checksum) {
	    return true;
	}
	
	System.out.println("Checksum mismatch " + messageChecksum +
			   " != " + checksum + ".");
	return false;
    }

    public static MidiMessage create(BitStream bitStream)
        throws MidiException
    {
	String error;
	Packet packet = new Packet();
	boolean success = 
	    PDLData
        .getMidiSysexParser()
        .parse(bitStream, packet);
	bitStream.setPosition(0);
	
	if (success) {
	    if (packet.contains("IAm")) { 
		return new IAmMessage(packet);
	    }
	    
	    if (checksumIsCorrect(bitStream)) {

        if (packet.contains("Lights")) {
            return new LightMessage(packet);
        }
        
        if (packet.contains("Meters")) {
            return new MeterMessage(packet);
        }
            
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

        if (packet.contains("KnobChange")) {
            return new ParameterMessage(packet);
        }
        if (packet.contains("MorphRangeChange")) {
            return new MorphRangeChangeMessage(packet);
        }
        if (packet.contains("KnobAssignment")||packet.contains("KnobAssignmentChange"))
        {
            return new KnobAssignmentMessage(packet);
        }
		if (packet.contains("PatchListResponse")) {
		    return new PatchListMessage(packet);
		}
		if(packet.contains("ACK")) {
		    return new AckMessage(packet);
		}
        if (packet.contains("NoteEvent")) {
            return new NoteMessage(packet);
        }   
        if (packet.contains("SetPatchTitle")) {
            return new SetPatchTitleMessage(packet);
        }
        if (packet.contains("ParameterSelect")) {
            return new ParameterSelectMessage(packet);
        }
		if(packet.contains("Error")) {
		    return new ErrorMessage(packet);
		}
		
		if (packet.contains("PatchPacket")) {
		    return new PatchMessage(packet);
		}	  
		
		error = " unsupported packet: ";
	    }
	    else {
		error = " checksum error: ";
	    }
	}
	else {
	    error = " parse failed: ";
	}

    byte[] message =  bitStream.toByteArray();
    error += StringUtils.toHexadecimal(message)+" ("+StringUtils.toText(message)+")";
    
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

    public Iterator parameterNames()
    {
        return parameters.iterator();
    }
    
    public int get(String parameter, int defaultValue)
    {
    if (!parameters.contains(parameter)) {
        throw new RuntimeException("Unsupported paramenter: " + parameter);
    }
    Object value = values.get(parameter);
    return value == null ? defaultValue : ((Integer) value).intValue();
    }

    public int get(String parameter)
    {
	if (!parameters.contains(parameter)) {
	    throw new RuntimeException("Unsupported paramenter: " + parameter);
	}
	if (values.get(parameter) == null) {
	    throw new RuntimeException("Missing parameter value: " + parameter);
	}
	return ((Integer)values.get(parameter)).intValue();
    }

    public void setAll(Packet packet)
    {
	for (Iterator i = parameters.iterator(); i.hasNext(); ) {
	    String name = (String)i.next();
	    if (paths.get(name) != null) {
		set(name, packet.getVariable((String)paths.get(name)));
	    }
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
    {
	parameters = new LinkedList();
	paths = new HashMap();
	values = new HashMap();
	expectsreply = false;
	isreply = false;
	
	addParameter("cc", "cc");
	addParameter("slot", "slot");
	set("slot", 0);
    }
  
    protected BitStream getBitStream(IntStream intStream)
	throws Exception
    {
	BitStream bitStream = new BitStream();
	boolean success = PDLData
        .getMidiSysexParser()
        .generate(intStream, bitStream);

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
	List chars = name.getVariableList("chars");
    StringBuilder sbuilder = new StringBuilder(chars.size()); 
	for (Iterator i = chars.iterator(); i.hasNext(); ) {
	    int data = ((Integer)i.next()).intValue();
	    if (data != 0) {
            sbuilder.append((char)data);
	    }
	}
	return sbuilder.toString();
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("[");
        
        Iterator params = parameters.iterator();
        
        if (params.hasNext())
        {
            String param = (String) params.next();
            sb.append(param);
            sb.append("="+get(param));
        }
        
        while (params.hasNext())
        {
            sb.append(",");
            String param = (String) params.next();
            sb.append(param);
            sb.append("="+values.get(param));
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    protected boolean isreply;
    protected boolean expectsreply;

    private LinkedList parameters;
    private HashMap paths;
    private HashMap values;

}
