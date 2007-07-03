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

public class IAmMessage extends MidiMessage
{
    public static final int PC = 0;
    public static final int MODULAR = 1;

    public IAmMessage()
    {
	super();

	addParameter("sender", "data:sender");
	addParameter("versionHigh", "data:versionHigh");
	addParameter("versionLow", "data:versionLow");
	set("cc", 0);
	set("sender", PC);
	set("versionHigh", 3);
	set("versionLow", 3);

	expectsreply = true;
	isreply = true;
    }

    IAmMessage(Packet packet)
    {
	this();
	setAll(packet);
	if (get("sender") == MODULAR) {
	    addParameter("unknown1", "data:unknown:unknown1");
	    addParameter("unknown2", "data:unknown:unknown2");
	    addParameter("unknown3", "data:unknown:unknown3");
	    addParameter("unknown4", "data:unknown:unknown4");
	}
	setAll(packet);
    }

    public List<BitStream> getBitStream()
	throws Exception
    {
        return createBitstreamList(getBitStream(appendAll()));
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
}
