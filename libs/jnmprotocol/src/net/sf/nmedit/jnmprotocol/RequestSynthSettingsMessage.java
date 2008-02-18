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

public class RequestSynthSettingsMessage extends MidiMessage
{
    public RequestSynthSettingsMessage()
    {
	super();

	addParameter("pp", "");
	set("cc", 0x17);
	set("pp", 0x44);

	expectsreply = true;
    }

    RequestSynthSettingsMessage(Packet packet)
    throws MidiException
    {
	throw new MidiException
	    ("RequestPatchMessage(Packet packet) not implemented", 0);
    }

    public List<BitStream> getBitStream()
    throws MidiException
    {
	IntStream intStream = appendAll();
	appendChecksum(intStream);
    return createBitstreamList(getBitStream(intStream));
    }
    
}
