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

public class AckMessage extends MidiMessage
{
    public AckMessage()
    {
	super();

	addParameter("pid1", "data:pid1");
	addParameter("type", "data:type");
	addParameter("pid2", "data:pid2");
	set("cc", 0x16);
	set("type", 0x7f);

	isreply = true;
    }

    public AckMessage(PDLPacket packet)
    {
	this();
	setAll(packet);
    }
    
    public int getPid1()
    {
        return get("pid1");
    }
    
    public int getPid2()
    {
        return get("pid2");
    }
    public BitStream getBitStream() throws MidiException
    {
    return getBitStream(appendAll());
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
}
