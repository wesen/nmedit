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

public class NewModuleMessage extends MidiMessage
{
    static {
	try {
	    usePdlFile("/patch.pdl", null);
	}
	catch (Exception e) {
	    System.out.println("NewModuleMessage: /patch.pdl not found.");
	}
    }

    private static Protocol protocol;
    private static String pdlFile;
    private static PacketParser packetParser;

    public static void usePdlFile(String filename, Tracer tracer)
	throws Exception
    {
	pdlFile = filename;
	protocol = new Protocol(pdlFile);
	packetParser = protocol.getPacketParser("Patch");
	protocol.useTracer(tracer);
    }
    
    private IntStream intStream;

    public NewModuleMessage()
	throws Exception
    {
	super();
	
	expectsreply = true;

	addParameter("command", "data:command");
	addParameter("pid", "data:data:data:pid");
	set("command", 0);
	set("cc", 0x1f);
    }
    
    NewModuleMessage(Packet packet)
	throws Exception
    {
	throw new MidiException
	    ("NewModuleMessage(Packet packet) not implemented", 0);
    }
    
    public void newModule(int type,
			  int section,
			  int index,
			  int xpos,
			  int ypos,
			  String name,
			  int[] parameterValues,
			  int[] customValues)
	throws MidiException
    {
	IntStream patchData = new IntStream();
	
	patchData.append(48);
	patchData.append(type);
	patchData.append(section);
	patchData.append(index);
	patchData.append(xpos);
	patchData.append(ypos);
	appendName(name, patchData);

	patchData.append(82);
	patchData.append(section);
	patchData.append(0);

	patchData.append(77);
	patchData.append(section);
	if (parameterValues.length > 0) {
	    patchData.append(1);
	    patchData.append(index);
	    patchData.append(type);
	    for (int i=0; i < parameterValues.length; i++) {
		patchData.append(parameterValues[i]);
	    }
	}
	else {
	    patchData.append(0);
	}

	patchData.append(91);
	patchData.append(section);
	if (customValues.length > 0) {
	    patchData.append(1);
	    patchData.append(index);
	    patchData.append(customValues.length);
	    for (int i=0; i < customValues.length; i++) {
		patchData.append(customValues[i]);
	    }
	}
	else {
	    patchData.append(0);
	}

	patchData.append(90);
	patchData.append(section);
	patchData.append(1);
	patchData.append(index);
	appendName(name, patchData);

	

	// Encode patch data
	BitStream patchStream = new BitStream();
	boolean success = packetParser.generate(patchData, patchStream);
	
	if (!success || patchData.isAvailable(1)) {
	    throw new MidiException("Information mismatch in generate.",
				    patchData.getSize() -
				    patchData.getPosition());
	}
	
	// Pad. Extra bits are ignored later.
	patchStream.append(0, 6);

	// Throw away first 7 bits, already encoded with 'sc' parameter.
        patchStream.getInt(7);
	
	// Generate message
        intStream = appendAll();
	while (patchStream.isAvailable(7)) {
	    intStream.append(patchStream.getInt(7));
	}
    }

    private void appendName(String stringName, IntStream patchData)
    {
	byte[] name = stringName.getBytes();
	int i = 0;
	for (i = 0; i < 16 && i < name.length; i++) {
	    patchData.append(name[i]);
	}
	if (i < 16) {
	    patchData.append(0);
	}
    }

    public List getBitStream()
	throws Exception
    {
	appendChecksum(intStream);
	
	LinkedList bitStreamList = new LinkedList();
	bitStreamList.add(getBitStream(intStream));
	return bitStreamList;
    }
    
    public void notifyListener(NmProtocolListener listener)
	throws Exception
    {
	throw new MidiException
	    ("NewModuleMessage.notifyListener() not implemented", 0);
    }
}
