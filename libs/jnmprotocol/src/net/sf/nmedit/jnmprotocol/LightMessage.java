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

public class LightMessage extends MidiMessage
{
    public LightMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("startIndex", "data:data:startIndex");
	addParameter("light0", "data:data:l0");
	addParameter("light1", "data:data:l1");
	addParameter("light2", "data:data:l2");
	addParameter("light3", "data:data:l3");
	addParameter("light4", "data:data:l4");
	addParameter("light5", "data:data:l5");
	addParameter("light6", "data:data:l6");
	addParameter("light7", "data:data:l7");
	addParameter("light8", "data:data:l8");
	addParameter("light9", "data:data:l9");
	addParameter("light10", "data:data:l10");
	addParameter("light11", "data:data:l11");
	addParameter("light12", "data:data:l12");
	addParameter("light13", "data:data:l13");
	addParameter("light14", "data:data:l14");
	addParameter("light15", "data:data:l15");
	addParameter("light16", "data:data:l16");
	addParameter("light17", "data:data:l17");
	addParameter("light18", "data:data:l18");
	addParameter("light19", "data:data:l19");
    }

    LightMessage(Packet packet)
    {
	this();
	setAll(packet);
    }

    public List getBitStream()
	throws Exception
    {
	throw new MidiException("LightMessage::getBitStream not implemented.",
				0);
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
}
