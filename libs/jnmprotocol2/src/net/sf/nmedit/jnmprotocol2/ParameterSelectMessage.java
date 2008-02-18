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

public class ParameterSelectMessage extends MidiMessage
{
    public ParameterSelectMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
    addParameter("unknown1", "data:data:section");
	addParameter("section", "data:data:section");
	addParameter("module", "data:data:module");
	addParameter("parameter", "data:data:parameter");
	set("cc", 0x14);
	set("sc", 0x2F);
    set("unknown1", 0x0);
    }

    public ParameterSelectMessage(PDLPacket packet)
    {
	this();
	setAll(packet);
    }

    public int getPid()
    {
        return get("pid");
    }

    public int getSection()
    {
        return get("section");
    }

    public int getModule()
    {
        return get("module");
    }

    public int getParameter()
    {
        return get("parameter");
    }
    
    public void select(int slot, int pid, int section, int module, int parameter)
    {
        set("cc", 0x13);
        setSlot(slot);
        set("pid", pid);
        set("unknown1", section); // synth sends only messages where unknown1==section ?
        set("section", section);
        set("module", module);
        set("parameter", parameter);
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
