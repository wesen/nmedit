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
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jpdl.*;

public class SlotsSelectedMessage extends MidiMessage
{
    
    public SlotsSelectedMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
	addParameter("slot0Selected", "data:data:slot0");
	addParameter("slot1Selected", "data:data:slot1");
	addParameter("slot2Selected", "data:data:slot2");
	addParameter("slot3Selected", "data:data:slot3");
	set("cc", 0x17);
	set("pid", 0x41);
	set("sc", 0x07);
    }

    SlotsSelectedMessage(Packet packet)
    {
    this();
    setAll(packet);
    }

    public SlotsSelectedMessage(boolean s0, boolean s1, boolean s2, boolean s3)
    {
        this();
        setSlotsSelected(s0, s1, s2, s3);
    }
    
    public boolean isSlotSelected(int slot)
    {
        return get("slot"+slot+"Selected")>0;
    }
    
    public void setSlotsSelected(boolean s0, boolean s1, boolean s2, boolean s3)
    {
        set("slot0Selected", s0?1:0);
        set("slot1Selected", s1?1:0);
        set("slot2Selected", s2?1:0);
        set("slot3Selected", s3?1:0);
    }

    public List<BitStream> getBitStream()
    throws MidiException
    {
	IntStream intStream = appendAll();
	appendChecksum(intStream);
    return createBitstreamList(getBitStream(intStream));
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
}
