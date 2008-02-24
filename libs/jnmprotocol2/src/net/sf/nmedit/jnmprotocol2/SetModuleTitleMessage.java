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

import net.sf.nmedit.jnmprotocol2.utils.NmCharacter;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;

public class SetModuleTitleMessage extends MidiMessage
{
    
    private String title = null;
    
    public SetModuleTitleMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
	addParameter("section", "data:data:section");
	addParameter("module", "data:data:module");
	addParameter("name", "data:data:name");
	set("cc", 0x17);
	set("sc", 0x33);
    }

    SetModuleTitleMessage(PDLPacket packet)
    {
	this();
	setAll(packet);
        title = NmCharacter.extractName(packet.getPacket("data:data:name"));
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(int slot, int pid, int section, int module, String title)
    {
        if (title == null) title = "";
        
        if (!NmCharacter.isValid(title))
            throw new IllegalArgumentException("invalid characters in title: "+title);
        if (title.length()>16)
            title = title.substring(0,16);
        setSlot(slot);
        set("pid", pid);
        set("section", section);
        set("module", module);
        this.title = title == null ? "" : title;
    }

    public BitStream getBitStream()
    throws MidiException
    {
        IntStream intStream = new IntStream();
        intStream.append(get("cc"));
        intStream.append(get("slot"));
        intStream.append(get("pid"));
        intStream.append(get("sc"));
        intStream.append(get("section"));
        intStream.append(get("module"));
        NmCharacter.appendString(intStream, title);
        
        return getBitStream(intStream);
    }
    
}
