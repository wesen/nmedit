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
import net.sf.nmedit.jpdl2.PDLPacket;

public class LightMessage extends MidiMessage
{

    // use arrays instead of string concatenation to make the message more efficient
    private static final int LIGHTCOUNT = 20;
    private static final String[] TABLE_LIGHTS;
    private static final String[] TABLE_PATHS;
    static {
        TABLE_LIGHTS = new String[LIGHTCOUNT];
        TABLE_PATHS = new String[LIGHTCOUNT];
        for (int i=0;i<LIGHTCOUNT;i++)
        {
            TABLE_LIGHTS[i] = "light"+i;
            TABLE_PATHS[i] = "data:data:l"+i;
        }
    }
    
    public LightMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("startIndex", "data:data:startIndex");
    for (int i=0;i<LIGHTCOUNT;i++)
        addParameter(TABLE_LIGHTS[i], TABLE_PATHS[i]);
    }

    public LightMessage(PDLPacket packet)
    {
	this();
	setAll(packet);
    }

    public int getStartIndex()
    {
        return get("startIndex");
    }
    
    public int getLight(int index)
    {
        return get(TABLE_LIGHTS[index]);
    }
    
    public int getLightCount()
    {
        return LIGHTCOUNT;
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
}
