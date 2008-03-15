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

package net.sf.nmedit.jnmprotocol2;

import net.sf.nmedit.jnmprotocol2.utils.NmCharacter;
import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLMessage;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.PDLPacketParser;
import net.sf.nmedit.jpdl2.dom.PDLDocument;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;

public class PatchMessage extends MidiMessage
{
    private BitStream bitstream;
    private BitStream patchStream;
    private boolean isFirstInSequence;
    private boolean isLastInSequence;
    
    private PatchMessage()
    {
	super();
	addParameter("pid", "data:pid");
	set("cc", 0x1c);
	set("pid", 0);

	expectsreply = true;
	isreply = true;
    }

    public static BitStream getEmbeddedStream(PDLPacket packet)
    {
        int[] embedded_stream = packet.getVariableList("data:embedded_stream");
        
        BitStream bitStream = new BitStream(embedded_stream.length/4);
        for (int value: embedded_stream)
            bitStream.append(value&0x7F, 7);
        // Remove padding
        bitStream.setSize((bitStream.getSize()/8)*8);
        return bitStream;
    }
    
    public PatchMessage(PDLPacket packet)
    {
	this();
	bitstream = new BitStream(); // not really ok
    patchStream = getEmbeddedStream(packet);
	setAll(packet);
	
    isFirstInSequence = packet.getVariable("first")>0;
    isLastInSequence = packet.getVariable("last")>0;

    /*
	packet = packet.getPacket("data:next");
	while (packet != null) {
	    patchStream.append(packet.getVariable("data"), 7);
	    packet = packet.getPacket("next");
	}
	// Remove padding
	patchStream.setSize((patchStream.getSize()/8)*8);
    */
    }

    public PatchMessage(BitStream section, int slot, int sectionIndex, int sectionCount) throws MidiException
    {
	this();
	set("slot", slot);

	// Create sysex messages

	    int first = sectionIndex == 0 ? 1 : 0;
	    int last = sectionIndex == (sectionCount-1) ? 1 : 0;
	    isFirstInSequence = first>0;
	    isLastInSequence = last>0;
	    
	    int sectionsEnded = sectionIndex+1;
        this.patchStream = BitStream.copyOf(section);
        BitStream partialPatchStream = patchStream;
        partialPatchStream.setSize((partialPatchStream.getSize()/8)*8);
        // Pad. Extra bits are ignored later.
        partialPatchStream.append(0, 6);

        // Generate sysex bistream
        IntStream intStream = new IntStream();
        intStream.append(get("cc") + first + 2*last);
        first = 0;
        intStream.append(get("slot"));
        intStream.append(0x01);
        intStream.append(sectionsEnded);
        partialPatchStream.setPosition(0);
        while (partialPatchStream.isAvailable(7)) {
        intStream.append(partialPatchStream.getInt(7));
        }
        
        intStream.setPosition(0);
        
        // Generate sysex bitstream
        this.bitstream = getBitStream(intStream);
    }

    public boolean isFirstInSequence()
    {
        return this.isFirstInSequence;
    }

    public boolean isLastInSequence()
    {
        return this.isLastInSequence;
    }
    
    public boolean isSingleMessage()
    {
        return isFirstInSequence && isLastInSequence;
    }
    
    public BitStream getBitStream() 
    {
    return bitstream;
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
    
    private PDLMessage patchMessage = null;
    
    public PDLMessage getPatchMessage() throws PDLException
    {
        if (patchMessage == null)
        {
            PDLDocument doc = PDLData.getPatchDoc();
            PDLPacketParser parser = new PDLPacketParser(doc);
            patchStream.setPosition(0);
            try
            {
                patchMessage = parser.parseMessage(patchStream);
            }
            finally
            {
                patchStream.setPosition(0); 
            }
        }
        return patchMessage;
    }

    public BitStream getPatchStream()
    {
        return patchStream;
    }

    public boolean containsSection(int sectionId)
    {
        try
        {
            PDLMessage message = getPatchMessage();
            PDLPacket packet = message.getPacket();
            do 
            {
                PDLPacket section = packet.getPacket("section");
                // PDLPacket sectionData = section.getPacket("data");
                int foundSectionId = section.getVariable("type");

                if (sectionId == foundSectionId)
                    return true;
                packet = packet.getPacket("next");
                
            } 
            while (packet != null);
        }
        catch (PDLException e)
        {
            // ignore
        }
        return false;
    }

    public final static int S_NAME_1 = 55;
    public final static int S_NAME_2 = 39;
    private String patchName;
    private static final String NO_PATCHNAME_PRESENT = "------------------";
    public String getPatchNameIfPresent()
    {
        if (patchName == NO_PATCHNAME_PRESENT)
            return null;
        if (patchName != null)
            return patchName;
     
        try
        {
            PDLMessage message = getPatchMessage();
            PDLPacket packet = message.getPacket();
            do 
            {
                PDLPacket section = packet.getPacket("section");
                PDLPacket sectionData = section.getPacket("data");
                
                switch (section.getVariable("type"))
                {
                    // Name section
                    case S_NAME_1:
                    case S_NAME_2:
                        // patch name
                        patchName = NmCharacter.extractName(sectionData.getPacket("name"));
                        return patchName;
                }
                
                packet = packet.getPacket("next");
            } 
            while (packet != null);
        }
        catch (PDLException e)
        {
            // ignore
        }
        patchName = NO_PATCHNAME_PRESENT;
        return null;
    }

}
