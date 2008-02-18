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

/**
 * 
 * TODO morph deassign !!!
 */
public class MorphAssignmentMessage extends MidiMessage
{

    // morphId = 4 deassigns a morph from a parameter
    public static final int MORPH_ID_DEASSIGN = 4;
    
    public MorphAssignmentMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
	addParameter("section", "data:data:section");
	addParameter("module", "data:data:module");
	addParameter("parameter", "data:data:parameter");
	addParameter("morph", "data:data:morph");
	set("cc", 0x17);
	set("sc", 0x64);
    }

    MorphAssignmentMessage(Packet packet)
    {
	this();
	setAll(packet);
    }

    public int getSection()
    {
        return get("section");
    }

    public int getModule()
    {
        return get("module");
    }
    
    public void deAssignMorph(int slot, int pid, int section, int module, int parameter)
    {
        setMorphAssignment(slot, pid, section, module, parameter, MORPH_ID_DEASSIGN);
    }
    
    public void setMorphAssignment(int slot, int pid, int section, int module, int parameter, int morph)
    {
        set("slot", slot);
        set("pid", pid);
        set("section", section);
        set("module", module);
        set("parameter", parameter);
        set("morph", morph);
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
