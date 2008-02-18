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

public class IAmMessage extends MidiMessage
{
    public static final int PC = 0;
    public static final int MODULAR = 1;

    public static final int NORD_MODULAR_KEYBOARD = 0x00;
    public static final int NORD_MODULAR_RACK = 0x01;
    public static final int MICRO_MODULAR = 0x02;
    
    public IAmMessage()
    {
	super();

	addParameter("sender", "data:sender");
	addParameter("versionHigh", "data:versionHigh");
	addParameter("versionLow", "data:versionLow");
	set("cc", 0);
	set("sender", PC);
	set("versionHigh", 3);
	set("versionLow", 3);

	expectsreply = true;
	isreply = true;
    }
    
    public IAmMessage(PDLPacket packet)
    {
    this();
    setAll(packet);
    if (get("sender") == MODULAR) {
        addParameter("reserved", "data:identification:reserved");
        addParameter("serial1", "data:identification:serial1");
        addParameter("serial2", "data:identification:serial2");
        addParameter("deviceId", "data:identification:deviceId");
    }
    setAll(packet);
    }
    
    /**
     * Returns the last four digits of the serial number.
     * If the sender of this message is not the synthesizer,
     * then -1 is returned.
     * @return the last four digits of the serial number.
     */
    public int getSerial()
    {
        if (get("sender") == MODULAR) 
        {
            int s1 = get("serial1")&0x7F;
            int s2 = get("serial2")&0x7F;
            return (s1<<7)|s2;
        }
        return -1;
    }
    
    /**
     * Returns the device id. 
     * If the sender of this message is not the synthesizer
     * then the device id is unavailable and -1 is returned.
     * Otherwise one of the following values is returned
     * <ul>
     *  <li>NORD_MODULAR_KEYBOARD</li>
     *  <li>NORD_MODULAR_RACK</li>
     *  <li>MICRO_MODULAR</li>
     *  <li>3..127</li>
     * </ul>
     * 
     * @return the device id
     */
    public int getDeviceId()
    {
        if (get("sender") == MODULAR) 
        {
            return get("deviceId")&0x7F;
        }
        return -1;
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
