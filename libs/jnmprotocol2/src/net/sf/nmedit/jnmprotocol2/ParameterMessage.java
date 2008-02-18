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

public class ParameterMessage extends MidiMessage
{
    public ParameterMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
	addParameter("section", "data:data:section");
	addParameter("module", "data:data:module");
	addParameter("parameter", "data:data:parameter");
	addParameter("value", "data:data:value");
	set("cc", 0x13);
	set("sc", 0x40);
    }

    public ParameterMessage(PDLPacket packet)
    {
	this();
	setAll(packet);
    }

    public void setPid(int pid)
    {
        set("pid", pid);
    }

    public void parameterChanged(int section, int module, int parameter, int value)
    {
        set("section", section);
        set("module", module);
        set("parameter", parameter);
        set("value", value);
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
    
    public int getValue()
    {
        return get("value");
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
