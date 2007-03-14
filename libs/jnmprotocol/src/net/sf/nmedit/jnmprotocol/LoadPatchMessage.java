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

public class LoadPatchMessage extends MidiMessage
{
    public LoadPatchMessage()
    {
	super();

	addParameter("pp", "");
	addParameter("ssc", "");
	addParameter("loadslot", "data:data:command:data:slot");
	addParameter("section",  "data:data:command:data:section");
	addParameter("position",  "data:data:command:data:position");
	set("cc", 0x17);
	set("pp", 0x41);
	set("ssc", 0x0a);

	expectsreply = true;
    }

    LoadPatchMessage(Packet packet)
	throws Exception
    {
	throw new MidiException
	    ("LoadPatchMessage(Packet packet) not implemented", 0);
    }

    public List getBitStream()
	throws Exception
    {
	IntStream intStream = appendAll();
	appendChecksum(intStream);
	
	LinkedList bitStreamList = new LinkedList();
	bitStreamList.add(getBitStream(intStream));
	return bitStreamList;
    }
    
    public void notifyListener(NmProtocolListener listener)
	throws Exception
    {
	throw new MidiException
	    ("LoadPatchMessage.notifyListener() not implemented", 0);
    }
}
