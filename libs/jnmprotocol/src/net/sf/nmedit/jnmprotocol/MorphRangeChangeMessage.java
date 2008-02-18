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

public class MorphRangeChangeMessage extends MidiMessage
{
    public MorphRangeChangeMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
	addParameter("section", "data:data:section");
	addParameter("module", "data:data:module");
	addParameter("parameter", "data:data:parameter");
    addParameter("span", "data:data:span");
    addParameter("direction", "data:data:direction");
    set("cc", 0x14);
	set("sc", 0x43);
    }
    
    MorphRangeChangeMessage(Packet packet)
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

    public int getSpan()
    {
        return get("span");
    }

    public int getDirection()
    {
        return get("direction");
    }
    
    public void setMorphRange(int slot, int pid, int section, int module, int parameter, int span, int direction)
    {
        if (section<0 || section>1)
            throw new IllegalArgumentException("invalid section: "+section);

        set("cc", 0x13);
        setSlot(slot);
        set("pid", pid);
        set("section", section);
        set("module", module);
        set("parameter",parameter);
        set("span", span);
        set("direction", direction);
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
