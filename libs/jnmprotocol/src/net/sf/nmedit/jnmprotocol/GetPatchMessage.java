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

import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jpdl.*;

public class GetPatchMessage extends MidiMessage
{
    public enum PatchPart {
	ALL, HEADER, POLY_MODULE, COMMON_MODULE, POLY_CABLE, COMMON_CABLE,
	    POLY_PARAMETER, COMMON_PARAMETER, MORPHMAP, KNOBMAP, CONTROLMAP,
	    POLY_NAMEDUMP, COMMON_NAMEDUMP, NOTE
	    }
    
    private PatchPart part;


    public GetPatchMessage(int slot, int pid)
    {
        this(slot, pid, PatchPart.ALL);
    }
    
    public GetPatchMessage(int slot, int pid, PatchPart part)
    {
        this();
        setSlot(slot);
        set("pid", pid);
        this.part = part;
    }
    
    public GetPatchMessage()
    {
	super();

	this.part = PatchPart.ALL;

	addParameter("pid", "");
	set("cc", 0x17);

	expectsreply = true;
    }

    public GetPatchMessage(PatchPart part)
    {
	this();

	this.part = part;
    }

    GetPatchMessage(Packet packet)
    throws MidiException
    {
	throw new MidiException
	    ("GetPatchMessage(Packet packet) not implemented", 0);
    }

    public int getPid()
    {
        return get("pid");
    }
    
    public List<BitStream> getBitStream()
    throws MidiException
    {
	IntStream intStream;
	List<BitStream> bitStreamList = new LinkedList<BitStream>();
	
	if (part == PatchPart.ALL || part == PatchPart.HEADER) {
	    intStream = appendAll();
	    intStream.append(0x20);
	    intStream.append(0x28);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.POLY_MODULE) {	
	    intStream = appendAll();
	    intStream.append(0x4b);
	    intStream.append(0x01);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.COMMON_MODULE) {
	    intStream = appendAll();
	    intStream.append(0x4b);
	    intStream.append(0x00);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.POLY_CABLE) {
	    intStream = appendAll();
	    intStream.append(0x53);
	    intStream.append(0x01);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.COMMON_CABLE) {
	    intStream = appendAll();
	    intStream.append(0x53);
	    intStream.append(0x00);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.POLY_PARAMETER) {
	    intStream = appendAll();
	    intStream.append(0x4c);
	    intStream.append(0x01);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.COMMON_PARAMETER) {
	    intStream = appendAll();
	    intStream.append(0x4c);
	    intStream.append(0x00);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.MORPHMAP) {
	    intStream = appendAll();
	    intStream.append(0x66);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.KNOBMAP) {
	    intStream = appendAll();
	    intStream.append(0x63);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.CONTROLMAP) {
	    intStream = appendAll();
	    intStream.append(0x61);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.POLY_NAMEDUMP) {
	    intStream = appendAll();
	    intStream.append(0x4e);
	    intStream.append(0x01);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.COMMON_NAMEDUMP) {
	    intStream = appendAll();
	    intStream.append(0x4e);
	    intStream.append(0x00);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	if (part == PatchPart.ALL || part == PatchPart.NOTE) {
	    intStream = appendAll();
	    intStream.append(0x68);
	    appendChecksum(intStream);
	    bitStreamList.add(getBitStream(intStream));
	}

	return bitStreamList;
    }
    
}
