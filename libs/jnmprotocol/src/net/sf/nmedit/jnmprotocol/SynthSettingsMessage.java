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
import net.sf.nmedit.jpdl.*;

public class SynthSettingsMessage extends MidiMessage
{
    private BitStream patchStream;
    private List<BitStream> bitStreamList;
    private String name;

    public SynthSettingsMessage()
	throws Exception
    {
	super();

	patchStream = new BitStream();
	bitStreamList = new LinkedList<BitStream>();

	addParameter("pid", "data:pid");
	set("cc", 0x1c);
	set("pid", 0);

	addParameter("type", "section:type");
	addParameter("midiClockSource", "section:data:midiClockSource");
	addParameter("midiVelScaleMin", "section:data:midiVelScaleMin");
	addParameter("ledsActive", "section:data:ledsActive");
	addParameter("midiVelScaleMax", "section:data:midiVelScaleMax");
	addParameter("midiClockBpm", "section:data:midiClockBpm");
	addParameter("local", "section:data:local");
	addParameter("keyboardMode", "section:data:keyboardMode");
	addParameter("pedalPolarity", "section:data:pedalPolarity");
	addParameter("globalSync", "section:data:globalSync");
	addParameter("masterTune", "section:data:masterTune");
	addParameter("programChangeSend", "section:data:programChangeSend");
	addParameter("knobMode", "section:data:knobMode");
	addParameter("midiChannelsSlotA", "section:data:midiChannelsSlotA");
	addParameter("midiChannelsSlotB", "section:data:midiChannelsSlotB");
	addParameter("midiChannelsSlotC", "section:data:midiChannelsSlotC");
	addParameter("midiChannelsSlotD", "section:data:midiChannelsSlotD");
	set("type", 3);
	set("midiClockSource", 1);
	set("midiVelScaleMin", 0);
	set("ledsActive", 1);
	set("midiVelScaleMax", 127);
	set("midiClockBpm", 120);
	set("local", 0);
	set("keyboardMode", 0);
	set("pedalPolarity", 0);
	set("globalSync", 4);
	set("masterTune", 0);
	set("programChangeSend", 0);
	set("knobMode", 0);
	set("midiChannelsSlotA", 0);
	set("midiChannelsSlotB", 1);
	set("midiChannelsSlotC", 2);
	set("midiChannelsSlotD", 3);

	expectsreply = true;
	isreply = true;
    }

    public SynthSettingsMessage(Packet packet)
	throws Exception
    {
	this();
	setAll(packet);
	
	packet = packet.getPacket("data:next");
	while (packet != null) {
	    patchStream.append(packet.getVariable("data"), 7);
	    packet = packet.getPacket("next");
	}
	// Remove checksum
	patchStream.setSize(patchStream.getSize()-7);
	// Remove padding
	patchStream.setSize((patchStream.getSize()/8)*8);
	
	
	
    }

    public SynthSettingsMessage(BitStream patchStream, List sectionEndPositions,
			int slot)
	throws Exception
    {
	this();
	set("slot", slot);

	// Create sysex messages
	int first = 1;
	int last = 0;
	int messageNumber = 0;
	while (patchStream.isAvailable(8)) {
	    
	    // Get data for one sysex packet
	    BitStream partialPatchStream = new BitStream();
	    int sectionsEnded = 0;
	    int n = 0;
	    while (patchStream.isAvailable(8) && n < 166) {
		partialPatchStream.append(patchStream.getInt(8), 8);
		if (n == (((Integer)sectionEndPositions.get(0)).intValue()
			  - 166*messageNumber)) {
		    sectionsEnded++;
		    sectionEndPositions.remove(0);
		}
		n++;
	    }
	    messageNumber++;
	    
	    if (!patchStream.isAvailable(8)) {
		last = 1;
	    }
	    
	    // Pad. Extra bits are ignored later.
	    partialPatchStream.append(0, 6);
	    
	    // Generate sysex bistream
	    IntStream intStream = new IntStream();
	    intStream.append(get("cc") + first + 2*last);
	    first = 0;
	    intStream.append(get("slot"));
	    intStream.append(0x01);
	    intStream.append(sectionsEnded);
	    while (partialPatchStream.isAvailable(7)) {
		intStream.append(partialPatchStream.getInt(7));
	    }
	    appendChecksum(intStream);
	    
	    // Generate sysex bitstream
	    bitStreamList.add(getBitStream(intStream));
	}
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

    public String getName()
    {
	return name;
    }
}
