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

import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jpdl2.*;

public class ErrorMessage extends MidiMessage
{
    // net.sf.nmedit.jnmprotocol2.ErrorMessage[sc=126,pid=1,code=5,slot=0,cc=20] / NM Display shows "No Slot Focused"
    
    // checksum error: sc=126,pid=16,code=4,slot=0,cc=20
    public ErrorMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
	addParameter("code", "data:data:code");
	set("cc", 0x14);
	set("sc", 0x7e);
	set("code", 0);

	isreply = true;
    }

    public ErrorMessage(PDLPacket packet)
    {
	this();
	setAll(packet);
    }
    
    public int getError()
    {
        return get("code");
    }

    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
}
