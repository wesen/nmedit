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

package net.sf.nmedit.jnmprotocol2;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLMessage;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.PDLPacketParser;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;

public abstract class MidiMessage
{

    private static Map<String, Constructor<? extends MidiMessage>> messageConstructors = new HashMap<String, Constructor<? extends MidiMessage>>();
    
    static {
        installMessage("iam", IAmMessage.class);
        installMessage("lights", LightMessage.class);
        installMessage("meters", MeterMessage.class);
        installMessage("voicecount", VoiceCountMessage.class);
        installMessage("slotsselected", SlotsSelectedMessage.class);
        installMessage("slotactivated", SlotActivatedMessage.class);
        installMessage("newPatchInSlot", NewPatchInSlotMessage.class);
        installMessage("knobChange", ParameterMessage.class);
        installMessage("morphRangeChange", MorphRangeChangeMessage.class);
        installMessage("knobAssignment", KnobAssignmentMessage.class);
        installMessage("PatchListResponse", PatchListMessage.class);
        installMessage("ack", AckMessage.class);
        installMessage("noteEvent", NoteMessage.class);
        installMessage("SetPatchTitle", SetPatchTitleMessage.class);
        installMessage("ParameterSelect", ParameterSelectMessage.class);
        installMessage("error", ErrorMessage.class);
        installMessage("PatchPacket", PatchMessage.class);
    }
    
    private static void installMessage(String key, Class<? extends MidiMessage> mclass)
    {
        Class<?>[] args = {PDLPacket.class};
        Constructor<? extends MidiMessage> constructor;
        try
        {
            constructor = mclass.getConstructor(args);
        } 
        catch (Exception e)
        {
            throw new RuntimeException("constructor not found: "+mclass, e);
        }
        messageConstructors.put(key, constructor);
    }
    
    private static BitStream ensureIsValidSysex(BitStream stream)
    {
        stream.setPosition(0);
        
        // find first 0xF0 and drop previous bytes 
        int StatusStartPosition = -1;
        int BehindStatusEndPosition = -1;
        
        while (stream.isAvailable(8))
        {
            int value = stream.getInt(8);
            if ((value & 0x80) > 0) // check bit 8
            {
                if (value == 0xF7)
                    BehindStatusEndPosition = stream.getPosition();
                else if (value == 0xF0) 
                    StatusStartPosition = stream.getPosition()-8;
                else
                {
                    // crappy message, set invalid position values
                    StatusStartPosition = -1;
                }
            }
        }
        
        if (!(StatusStartPosition>=0 && StatusStartPosition<BehindStatusEndPosition))
            return null; // corrupted message
        
        if (StatusStartPosition>0 || BehindStatusEndPosition<stream.getSize())
        {
            // copy sysex 0xF0 ... 0xF7 from original message
            stream.setPosition(StatusStartPosition);
            BitStream sysex = new BitStream();
            while (stream.getPosition()<BehindStatusEndPosition)
                sysex.append(stream.getInt(8), 8);
            stream = sysex;
        }
        stream.setPosition(0);
        return stream;
    }
    
    public static MidiMessage create(BitStream bitStream)
        throws MidiException
    {   
        {
            BitStream sysex = ensureIsValidSysex(bitStream);
            if (sysex == null)
            {
                MidiException me = new MidiException("corrupted message", MidiException.INVALID_MIDI_DATA);
                me.setMidiMessage(bitStream);
                throw me;
            }
            bitStream = sysex;
        }
        
        PDLPacketParser parser = new PDLPacketParser(PDLData.getMidiDoc());
    	
    	PDLMessage message;
    	try
    	{
    	    message = parser.parseMessage(bitStream);
    	}
    	catch (PDLException e)
    	{
    	    MidiException me = new MidiException("parse failed", MidiException.MIDI_PARSE_ERROR);
    	    me.setMidiMessage(bitStream.toByteArray());
    	    me.initCause(e);
    	    throw me;
    	}
    	finally
    	{
    	    bitStream.setPosition(0);
    	}
    	
    	/*
    	if (!("meters".equals(message.getMessageId())||"lights".equals(message.getMessageId())))
    	{
    	    System.out.println(message.getMessageId()+": "+PDLUtils.toString(message.getPacket()));
    	    System.out.println(PDLUtils.toHexadecimal(bitStream.toByteArray()));
    	}*/
    	
    	
    	/*if ("unknownNMInfo".equals(message.getMessageId()))
    	{
    	    System.out.println(PDLUtils.toString(message.getPacket()));
    	}*/
    	
    	if ("PatchPacket".equals(message.getMessageId()))
    	{
    	    PDLPacket packet = message.getPacket();
            {
                // check for type = 0x03 = (data1 << 1) | ((data2 >>> 6) & 0x1)
                /*
                PDLPacket pp = packet.getPacket("data:next");
                int data1 = pp == null ? -1 : pp.getVariable("data");
                if (data1 == 0x01)
                {
                    pp = pp.getPacket("next");
                    int data2 = pp == null ? -1 : pp.getVariable("data");
                    if ((data2 & 0x40) > 0) 
                    {
                        return new SynthSettingsMessage(packet);
                    }
                }*/
                PDLPacket pp = packet.getPacket("data");
                if (pp != null)
                {
                    int[] embedded_stream = pp.getVariableList("embedded_stream");
                    if (embedded_stream.length>=2 && embedded_stream[0] == 0x01 && (embedded_stream[1]&0x40)>0)
                    {
                        return new SynthSettingsMessage(packet);
                    }
                }
            }
            return new PatchMessage(packet); 
    	}

    	Constructor<? extends MidiMessage> mconstructor = messageConstructors.get(message.getMessageId());
    	if (mconstructor == null)
    	    throw new MidiException("unsupported packet "+message, 0);
        	
    	try
        {
            return mconstructor.newInstance(new Object[]{message.getPacket()});
        } catch (Exception e)
        {
            MidiException me = new MidiException("could not create instance for messageId "+message.getMessageId(), MidiException.UNKNOWN_MIDI_MESSAGE);
            me.initCause(e);
            throw me;
        }
	
    }

    public BitStream getBitStream()
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

    public void setAll(PDLPacket packet)
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
        PDLPacketParser parser = new PDLPacketParser(PDLData.getMidiDoc());

        BitStream bitStream = null;
        try
        {
            parser.parse(intStream);
            
            bitStream = parser.getBitStream();
        }
        catch (PDLException e)
        {
            MidiException me = new MidiException("Could not generate message: "+this+", "+(
                        intStream.getSize() - intStream.getPosition()), MidiException.MIDI_PARSE_ERROR);
            me.initCause(e);
            throw me;
        }

        if (intStream.isAvailable(1))
        {
            throw new MidiException("Information mismatch in generate. In "+this+", "+(
                    intStream.getSize() - intStream.getPosition()), MidiException.MIDI_PARSE_ERROR);
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
    /*
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
    */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("[");
        toStringArgs(sb);
        sb.append("]");
        return sb.toString();
    }
    
    protected void toStringArgs(StringBuilder sb)
    {
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
