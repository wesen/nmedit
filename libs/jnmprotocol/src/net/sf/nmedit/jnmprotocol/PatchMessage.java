/*
    Nord Modular Midi Protocol 3.03 Library
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

package net.sf.nmedit.jnmprotocol;

import java.util.*;
import net.sf.nmedit.jpdl.*;

public class PatchMessage extends MidiMessage
{
    private BitStream patchStream;
    private List<BitStream> bitStreamList;
    
    public PatchMessage()
    {
	super();

	patchStream = new BitStream();
	bitStreamList = new LinkedList<BitStream>();

	addParameter("pid", "data:pid");
	set("cc", 0x1c);
	set("pid", 0);

	expectsreply = true;
	isreply = true;
    }

    public PatchMessage(Packet packet)
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

    public PatchMessage(BitStream patchStream, List<Integer> sectionEndPositions,
			int slot)
    throws MidiException
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
		if (n == ((sectionEndPositions.get(0))
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
	throws MidiException
    {
	return bitStreamList;
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }

    public BitStream getPatchStream()
    {
	return patchStream;
    }
}
