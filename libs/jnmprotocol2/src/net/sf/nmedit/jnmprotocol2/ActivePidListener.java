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

import net.sf.nmedit.jnmprotocol2.AckMessage;
import net.sf.nmedit.jnmprotocol2.LightMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;

public class ActivePidListener extends NmProtocolListener
{
    public void messageReceived(AckMessage message)
    {
        setPid(message.get("slot"), message.get("pid1"));
    }
    
    public void messageReceived(LightMessage message)
    {
        setPid(message.get("slot"), message.get("pid"));
    }
    
    public int getActivePid(int slot)
    {
	return activePidMap[slot];
    }
    
    private void setPid(int slotId, int pid)
    {
        int oldPid = activePidMap[slotId];
        int newPid = pid;
        if (oldPid != newPid)
        {
            activePidMap[slotId] = pid;
            pidChanged(slotId, pid);
        }
    }
    
    protected void pidChanged(int slotId, int pid)
    {
        // no op
    }

    private int[] activePidMap = {0,0,0,0};
}
