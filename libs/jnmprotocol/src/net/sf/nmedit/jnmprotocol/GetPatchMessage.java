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

public class GetPatchMessage extends MidiMessage
{
    public GetPatchMessage()
	throws Exception
    {
	super();

	addParameter("pid", "");
	set("cc", 0x17);

	expectsreply = true;
    }

    GetPatchMessage(Packet packet)
	throws Exception
    {
	throw new MidiException
	    ("GetPatchMessage(Packet packet) not implemented", 0);
    }

    public List getBitStream()
	throws Exception
    {
	IntStream intStream;
	LinkedList bitStreamList = new LinkedList();
	    
	intStream = appendAll();
	intStream.append(0x20);
	intStream.append(0x28);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x4b);
	intStream.append(0x01);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x4b);
	intStream.append(0x00);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x53);
	intStream.append(0x01);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x53);
	intStream.append(0x00);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x4c);
	intStream.append(0x01);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x4c);
	intStream.append(0x00);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x66);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x63);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x61);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x4e);
	intStream.append(0x01);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x4e);
	intStream.append(0x00);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	intStream = appendAll();
	intStream.append(0x68);
	appendChecksum(intStream);
	bitStreamList.add(getBitStream(intStream));

	return bitStreamList;
    }
    
    public void notifyListener(NmProtocolListener listener)
	throws Exception
    {
	throw new MidiException
	    ("GetPatchMessage.notifyListener() not implemented", 0);
    }
}
