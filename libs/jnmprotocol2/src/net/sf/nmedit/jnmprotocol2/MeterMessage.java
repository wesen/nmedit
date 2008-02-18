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

public class MeterMessage extends MidiMessage
{
    // use arrays instead of string concatenation to make the message more efficient
    private static final int METER_COUNT = 5;
    private static final String[] TABLE_A = {
        "a0", "a1", "a2", "a3", "a4"};
    private static final String[] TABLE_B = {
        "b0", "b1", "b2", "b3", "b4"};
    private static final String[] TABLE_PATHS_A = {
        "data:data:a0", "data:data:a1", "data:data:a2", "data:data:a3", "data:data:a4"};
    private static final String[] TABLE_PATHS_B = {
        "data:data:b0", "data:data:b1", "data:data:b2", "data:data:b3", "data:data:b4"};
    
    public MeterMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("startIndex", "data:data:startIndex");
    for (int i=0;i<METER_COUNT;i++)
    {
        addParameter(TABLE_B[i], TABLE_PATHS_B[i]);
        addParameter(TABLE_A[i], TABLE_PATHS_A[i]);
    }
    }

    public MeterMessage(PDLPacket packet)
    {
	this();
	setAll(packet);
    }
    
    public int getStartIndex()
    {
        return get("startIndex");
    }
    
    public int getMeterA(int index)
    {
        return get(TABLE_A[index]);
    }
    
    public int getMeterB(int index)
    {
        return get(TABLE_B[index]);
    }
    
    public int getMeterCount()
    {
        return METER_COUNT;
    }

    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
}
