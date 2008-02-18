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

public class NewCableMessage extends MidiMessage
{
    private IntStream intStream;
    
    public NewCableMessage()
    {
	super();
	
	expectsreply = true;

	addParameter("pid", "data:data:pid");
	addParameter("sc", "data:data:sc");
	set("cc", 0x17);
	set("sc", 0x50);
    }

    NewCableMessage(Packet packet)
    throws MidiException
    {
	throw new MidiException
	    ("NewCableMessage(Packet packet) not implemented", 0);
    }

    public void setPid(int pid)
    {
        set("pid", pid);
    }

    public void newCable(int section, int color,
			 int module1, int type1, int connector1,
			 int module2, int type2, int connector2)
    {
        intStream = appendAll();
        intStream.append(section);
	intStream.append(color);
        intStream.append(module1);
        intStream.append(type1);
        intStream.append(connector1);
        intStream.append(module2);
        intStream.append(type2);
        intStream.append(connector2);
    }
    
    public List<BitStream> getBitStream()
    throws MidiException
    {
	appendChecksum(intStream);
	
    return createBitstreamList(getBitStream(intStream));
    }
}
