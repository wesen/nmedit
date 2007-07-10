/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2007 Marcus Andersson

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

import net.sf.nmedit.jnmprotocol.utils.NmCharacter;
import net.sf.nmedit.jpdl.*;

public class SynthSettingsMessage extends MidiMessage
{

    private BitStream settingsStream;
    private List<BitStream> bitStreamList;
    private Map<String, Object> parameterMap;
    
    private static final String[] setting_parameters = {
        "midiClockSource",
        "midiVelScaleMin",
        "ledsActive",
        "midiVelScaleMax",
        "midiClockBpm",
        "local",
        "keyboardMode",
        "pedalPolarity",
        "globalSync",
        "masterTune",
        "programChangeSend",
        "knobMode",
        "midiChannelsSlotA",
        "midiChannelsSlotB",
        "midiChannelsSlotC",
        "midiChannelsSlotD"
    };
    
    // TODO use correct defaults
    private static final int[] defaults = {
        0, // midiClockSource
        0, // midiVelScaleMin
        1, // ledsActive
        127, // midiVelScaleMax
        120, // midiClockBpm
        1, // local
        0, // keyboardMode
        0, // pedalPolarity
        3, // globalSync
        0, // masterTune
        3, // programChangeSend
        0, // knobMode
        0, // midiChannelsSlotA
        1, // midiChannelsSlotB
        2, // midiChannelsSlotC
        3  // midiChannelsSlotD
    };
    
    public static Map<String, Integer> createParamMap()
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i=0;i<setting_parameters.length;i++)
            map.put(setting_parameters[i], defaults[i]);
        return map;
    }
    
    private static void append(IntStream dst, int i, Map<String, Object> params)
    {
        Object value = params.get(setting_parameters[i]);
        if (value == null)
            throw new RuntimeException("parameter missing: "+setting_parameters[i]);
        if (!(value instanceof Integer))
            throw new RuntimeException("parameter not ant int: "+value);
        dst.append(((Integer)value).intValue());
    }
    
    public static IntStream createIntStream(Map<String, Object> params)
    {
        Object name = params.get("name");
        if (name == null || (!(name instanceof String)) || !NmCharacter.isValid((String)name))
            throw new RuntimeException("invalid parameter 'name': "+name);

        IntStream is = new IntStream();
        is.append(0x03); // type = 3 <= SynthSettings
        
        int i=0;
        append(is, i++, params); // midiClockSource
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params); // midiClockBpm
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params);
        append(is, i++, params); // masterTune
        append(is, i++, params);
        append(is, i++, params); // knobMode
        is.append(0);
        NmCharacter.appendString(is, (String)name); // String$name
        is.append(0);
        append(is, i++, params); // midiChannelsSlotA
        is.append(27);
        is.append(0);
        append(is, i++, params); // midiChannelsSlotB
        is.append(27);
        is.append(0);
        append(is, i++, params); // midiChannelsSlotC
        is.append(27);
        is.append(0);
        append(is, i++, params); // midiChannelsSlotD
        is.append(27);
        
        return is;
    }
    
    public static BitStream createBitStream(IntStream data)
    {
        PacketParser patchParser =  PDLData.getPatchParser();
        BitStream result = new BitStream();
        boolean success = patchParser.generate(data, result);
        if (!success)
            throw new RuntimeException("could not generate bitstream");
        return result;
    }

    private SynthSettingsMessage()
    {
        super();
        settingsStream = new BitStream();
        bitStreamList = new LinkedList<BitStream>();

        addParameter("pid", "data:pid");
        set("cc", 0x1c);
        set("pid", 0);
    
        expectsreply = false; // true ???
        isreply = true;
    }

    public SynthSettingsMessage(Packet packet)
    {
    this();
    setAll(packet);
    
    packet = packet.getPacket("data:next");
    while (packet != null) {
        settingsStream.append(packet.getVariable("data"), 7);
        packet = packet.getPacket("next");
    }
    // Remove checksum
    settingsStream.setSize(settingsStream.getSize()-7);
    // Remove padding
    settingsStream.setSize((settingsStream.getSize()/8)*8);
    
    }

    public SynthSettingsMessage(Map<String, Object> params)
    throws Exception
    {
        this();
        set("slot", 0);
        this.parameterMap = params;
        IntStream is = createIntStream(params);
        BitStream bs = createBitStream(is);
     
        final int first = 1;
        final int last = 1;
        
        // Generate sysex bistream
        IntStream intStream = new IntStream();
        intStream.append(get("cc") + first + 2*last);
        intStream.append(get("slot"));
        intStream.append(0x01); // command
        intStream.append(0x01); // pp = 1

        // Pad. Extra bits are ignored later.
        bs.append(0, 6);
        while (bs.isAvailable(7)) {
        intStream.append(bs.getInt(7));
        }
        
        appendChecksum(intStream);
        
        settingsStream = getBitStream(intStream);
        
        // Generate sysex bitstream
        bitStreamList.add(settingsStream);
    }

    public Map<String, Object> getParamMap()
    {
        if (parameterMap != null)
            return parameterMap;
        
        parameterMap = new HashMap<String, Object>();
        
        PacketParser parser = PDLData.getPatchParser();
    
        Packet packet = new Packet();
        boolean success = false;
        
        try
        {
            settingsStream.setPosition(0);
            success = parser.parse(settingsStream, packet);
        }
        finally
        {
            settingsStream.setPosition(0);
        }
        
        if (!success)
            throw new RuntimeException("could not parse settings");
        
        Packet section = packet.getPacket("section");
        int type = section.getVariable("type");
        
        if (type != 0x03)
            throw new RuntimeException("wrong type: 0x"+Integer.toHexString(type));
        
        Packet data = section.getPacket("data");
        
        for (int i=0;i<setting_parameters.length;i++)
        {
            String name = setting_parameters[i];
            parameterMap.put(name, data.getVariable(name));
        }

        String name = NmCharacter.extractName(data.getPacket("name"));
        parameterMap.put("name", name);
        
        return parameterMap;
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
    
    public List<BitStream> getBitStream()
    throws Exception
    {
    return bitStreamList;
    }
    
    public void notifyListener(NmProtocolListener listener)
    throws Exception
    {
    listener.messageReceived(this);
    }
    
    public BitStream getSynthSettingsStream()
    {
        return settingsStream;
    }
    
}
