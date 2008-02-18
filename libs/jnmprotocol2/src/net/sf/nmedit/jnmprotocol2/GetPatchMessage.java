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

import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;

public class GetPatchMessage extends MidiMessage
{
    public static enum PatchPart {
	HEADER, POLY_MODULE, COMMON_MODULE, POLY_CABLE, COMMON_CABLE,
	    POLY_PARAMETER, COMMON_PARAMETER, MORPHMAP, KNOBMAP, CONTROLMAP,
	    POLY_NAMEDUMP, COMMON_NAMEDUMP, NOTE
	    }
    
    private static final PatchPart[] orderedParts = new PatchPart[PatchPart.values().length];
    static {
        int index = 0;
        orderedParts[index++] = PatchPart.HEADER;
        orderedParts[index++] = PatchPart.POLY_MODULE;
        orderedParts[index++] = PatchPart.COMMON_MODULE;
        orderedParts[index++] = PatchPart.POLY_CABLE;
        orderedParts[index++] = PatchPart.COMMON_CABLE;
        orderedParts[index++] = PatchPart.POLY_PARAMETER;
        orderedParts[index++] = PatchPart.COMMON_PARAMETER;
        orderedParts[index++] = PatchPart.MORPHMAP;
        orderedParts[index++] = PatchPart.KNOBMAP;
        orderedParts[index++] = PatchPart.CONTROLMAP;
        orderedParts[index++] = PatchPart.POLY_NAMEDUMP;
        orderedParts[index++] = PatchPart.COMMON_NAMEDUMP;
        orderedParts[index++] = PatchPart.NOTE;
        if (index<orderedParts.length)
            throw new InternalError("parts are missing");
    }
    
    public static GetPatchMessage[] forAllParts(int slot, int pid)
    {
        GetPatchMessage[] messages = new GetPatchMessage[orderedParts.length];
        for (int i=0;i<orderedParts.length;i++)
        {
            messages[i] = new GetPatchMessage(slot, pid, orderedParts[i]);
        }
        return messages;
    }

    private PatchPart part;

    public GetPatchMessage(int slot, int pid, PatchPart part)
    {
        this.part = part;
        addParameter("pid", "");
        set("cc", 0x17);
        expectsreply = true;
        setSlot(slot);
        set("pid", pid);
        this.part = part;
    }
    
    GetPatchMessage(PDLPacket packet)
    throws MidiException
    {
	throw new MidiException
	    ("GetPatchMessage(PDLPacket packet) not implemented", 0);
    }

    public int getPid()
    {
        return get("pid");
    }
    
    public BitStream getBitStream() throws MidiException 
    {
    	IntStream intStream = appendAll();
    	
    	switch (part)
    	{
    	    case HEADER: 
    	        intStream.append(0x20);
    	        intStream.append(0x28);
    	        break;
        	
        	case POLY_MODULE:
        	    intStream.append(0x4b);
        	    intStream.append(0x01);
        	    break;
        
        	case COMMON_MODULE:
        	    intStream.append(0x4b);
        	    intStream.append(0x00);
        	    break;
        
        	case POLY_CABLE:
        	    intStream.append(0x53);
        	    intStream.append(0x01);
        	    break;
        
        	case COMMON_CABLE:
        	    intStream.append(0x53);
        	    intStream.append(0x00);
        	    break;
        
        	case POLY_PARAMETER:
        	    intStream.append(0x4c);
        	    intStream.append(0x01);
        	    break;
        
        	case COMMON_PARAMETER:
        	    intStream.append(0x4c);
        	    intStream.append(0x00);
        	    break;
        
        	case MORPHMAP:
        	    intStream.append(0x66);
        	    break;
        
        	case KNOBMAP:
        	    intStream.append(0x63);
        	    break;
        	
        	case CONTROLMAP:
        	    intStream.append(0x61);
        	    break;
        
        	case POLY_NAMEDUMP:
        	    intStream.append(0x4e);
        	    intStream.append(0x01);
        	    break;
        
        	case COMMON_NAMEDUMP:
        	    intStream.append(0x4e);
        	    intStream.append(0x00);
        	    break;
        
        	case NOTE:
        	    intStream.append(0x68);
        	    break;
    
        }
        return getBitStream(intStream);
    }
    
}
