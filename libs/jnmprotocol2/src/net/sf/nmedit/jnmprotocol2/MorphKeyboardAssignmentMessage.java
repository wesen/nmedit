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

public class MorphKeyboardAssignmentMessage extends MidiMessage
{

    public static final int KEYBOARD_DISABLE = 0x00;
    public static final int KEYBOARD_VELOCITY = 0x01;
    public static final int KEYBOARD_NOTE     = 0x02;

    public MorphKeyboardAssignmentMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
	addParameter("morph", "data:data:morph");
	addParameter("keyboard", "data:data:keyboard");
	set("cc", 0x17);
	set("sc", 0x64);
    }

    MorphKeyboardAssignmentMessage(PDLPacket packet)
    {
	this();
	setAll(packet);
    }
    
    public int getPid()
    {
        return get("pid");
    }
    
    public int getMorph()
    {
        return get("morph");
    }
    
    public int getKeyboardAssignment()
    {
        return get("keyboard");
    }

    public void setMorphAssignment(int slot, int pid, int morph, int keyboard)
    {
        set("slot", slot);
        set("pid", pid);
        // multiple (morph, keyboard) pairs possible
        set("morph", morph);
        set("keyboard", keyboard);
    }

    public BitStream getBitStream() throws MidiException
    {
    return getBitStream(appendAll());
    }
    
}
