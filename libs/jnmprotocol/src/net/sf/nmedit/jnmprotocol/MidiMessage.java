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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.ErrorMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.KnobAssignmentMessage;
import net.sf.nmedit.jnmprotocol.LightMessage;
import net.sf.nmedit.jnmprotocol.MeterMessage;
import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.MorphRangeChangeMessage;
import net.sf.nmedit.jnmprotocol.NewPatchInSlotMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.NoteMessage;
import net.sf.nmedit.jnmprotocol.PDLData;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.ParameterSelectMessage;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.SetPatchTitleMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jnmprotocol.SynthSettingsMessage;
import net.sf.nmedit.jnmprotocol.VoiceCountMessage;
import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.IntStream;
import net.sf.nmedit.jpdl.Packet;

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
        try
        {
            return createImpl(bitStream);
        }
        catch (MidiException e)
        {
            // set the midi message which caused the exception
            e.setMidiMessage(bitStream.toByteArray());
            // rethrow exception
            throw e;
        }   
    }
    
    
    private static MidiMessage createImpl(BitStream bitStream)
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

            {
            	// check for type = 0x03 = (data1 << 1) | ((data2 >>> 6) & 0x1)
            	
                Packet pp = packet.getPacket("data:next");
                int data1 = pp == null ? -1 : pp.getVariable("data");
                if (data1 == 0x01)
                {
                	pp = pp.getPacket("next");
                    int data2 = pp == null ? -1 : pp.getVariable("data");
                    if ((data2 & 0x40) > 0) 
                    {
                        return new SynthSettingsMessage(packet);
                    }
                }
            }
            
		    return new PatchMessage(packet);
		}	  
		
		error = "unsupported packet";
	    }
	    else {
		error = "checksum error";
	    }
	}
	else {
	    error = "parse failed";
	}

	throw new MidiException(error, 0);
    }

    public List<BitStream> getBitStream()
	throws MidiException
    {
        throw new MidiException(
                getClass().getName()+
                ".getBitStream() not implemented.", 0);
    }

    public void notifyListener(NmProtocolListener listener)
    {
        throw new UnsupportedOperationException(
                getClass().getName()+".notifyListener not implemented");
    }

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
        if (isParameter(name))
            throw new IllegalArgumentException("parameter already exists:"+name);

        Parameter p = new Parameter(lastParameter, name, path);
        if (firstParameter == null)
            firstParameter = p;
        lastParameter = p;
        parameters.put(name, p);
    }

    protected boolean isParameter(String name)
    {
        return parameterForName(name) != null;
    }

    private void checkParameter(Parameter p, String parameter)
    {
        if (p == null) 
            throw new RuntimeException("Unsupported paramenter: " + parameter);
    }
    
    public void set(String parameter, int value)
    {
        Parameter p = parameterForName(parameter);
        checkParameter(p, parameter);
        p.setValue(value);
    }

    public Iterator<String> parameterNames()
    {
        // can't use parameters.keySet().iterator()
        // because it does not respect the order
        return new Iterator<String>()
        {
            Parameter pos = firstParameter;

            public boolean hasNext()
            {
                return pos != null;
            }

            public String next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                Parameter res = pos;
                pos = pos.next;
                return res.name;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
            
        };
    }
    
    public int get(String parameter, int defaultValue)
    {
        Parameter p = parameterForName(parameter);
        checkParameter(p, parameter);
        return p.valueSet ? p.value : defaultValue;
    }

    private void checkValue(Parameter p)
    {
        if (!p.valueSet)
            throw new RuntimeException("Missing parameter value: " + p.name);
    }

    public int get(String parameter)
    {
        Parameter p = parameterForName(parameter);
        checkParameter(p, parameter);
        checkValue(p);
        return p.value;
    }

    public void setAll(Packet packet)
    {
    	for (Parameter p: parameters.values()) 
        {
            p.setValue(packet.getVariable(p.path));
        }
    }

    public IntStream appendAll()
    {
    	IntStream intStream = new IntStream(parameters.size()+10);
        Parameter p = firstParameter;
        while (p != null) 
        {
            checkValue(p);
    	    intStream.append(p.value);
            p = p.next;
    	}
    	return intStream;
    }

    protected MidiMessage()
    {
        parameters = new HashMap<String, Parameter>();
	expectsreply = false;
	isreply = false;
	
	addParameter("cc", "cc");
	addParameter("slot", "slot");
	set("slot", 0);
    }
  
    protected BitStream getBitStream(IntStream intStream)
	throws MidiException
    {
        
	BitStream bitStream = new BitStream();
	boolean success = PDLData
        .getMidiSysexParser()
        .generate(intStream, bitStream);

	if (!success || intStream.isAvailable(1)) {
	    throw new MidiException("Information mismatch in generate. In "+this,
				    intStream.getSize() - intStream.getPosition());
	}

	return bitStream;
    }

    public int getSlot()
    {
        return get("slot");
    }
    
    public void setSlot(int slot)
    {
        if (slot<0 || slot>=4)
            throw new IllegalArgumentException("invalid slot: "+slot);
        set("slot", slot);
    }
    
    protected void appendChecksum(IntStream intStream)
        throws MidiException
    {
	int checksum = 0;
	intStream.append(checksum);
	
	BitStream bitStream = getBitStream(intStream);
	checksum = calculateChecksum(bitStream);
	
	intStream.setSize(intStream.getSize()-1);
	intStream.append(checksum);
	intStream.setPosition(0);
    }
    
    protected static List<BitStream> createBitstreamList()
    {
        return new LinkedList<BitStream>();
    }
    
    protected List<BitStream> createBitstreamList(BitStream bs)
    {
        List<BitStream> list = createBitstreamList();
        list.add(bs);
        return list;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("[");
        
        Iterator<Parameter> params = parameters.values().iterator();
        
        if (params.hasNext())
        {
            sb.append(params.next());
        }
        
        while (params.hasNext())
        {
            sb.append(",");
            sb.append(params.next());
        }
        
        sb.append("]");
        return sb.toString();
    }

    private Parameter parameterForName(String name)
    {
        return parameters.get(name);
    }
    
    protected boolean isreply;
    protected boolean expectsreply;

    private Map<String, Parameter> parameters;
    private Parameter firstParameter;
    private Parameter lastParameter;

    private static class Parameter
    {
        String name;
        String path;
        int value;
        boolean valueSet = false;
        Parameter next;
        public Parameter(Parameter previous, String name, String path)
        {
            if (previous != null)
                previous.next = this;
            
            this.name = name;
            this.path = path;
        }
        public void setValue(int value)
        {
            this.value = value;
            this.valueSet = true;
        }
        public String toString()
        {
            return name+"="+(valueSet?Integer.toString(value):"?");
        }
    }
    
}
