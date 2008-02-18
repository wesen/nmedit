/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.nmedit.jsynth.clavia.nordmodular;

import net.sf.nmedit.jnmprotocol2.NewPatchInSlotMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;

public class NewPatchInSlotHandler
{
    
    private NMPatch[] slots = new NMPatch[4];

    public void setSendingPatch(int slotIndex, NMPatch patch)
    {
        slots[slotIndex] = patch;
    }
    
    public NMPatch getSendingPatch(int slotIndex)
    {
        return slots[slotIndex];
    }
    
    public void handle(NewPatchInSlotMessage message)
    {
        /*
        int slotIndex = message.get("slot");
        int pid = message.get("pid");
        // int cc = message.get("cc");
        */
        
        
    }
    
}
