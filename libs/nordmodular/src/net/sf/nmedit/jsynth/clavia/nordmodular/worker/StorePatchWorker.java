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
package net.sf.nmedit.jsynth.clavia.nordmodular.worker;

import net.sf.nmedit.jnmprotocol.StorePatchMessage;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;

public class StorePatchWorker
  implements net.sf.nmedit.jsynth.worker.StorePatchWorker
{

    private NmSlot slot;
    private int section;
    private int position;

    public StorePatchWorker(NmSlot source, int section, int position)
    {
        this.slot = source;
        this.section = section;
        this.position = position;
    }

    public NordModular getSynth()
    {
        return slot.getSynthesizer();
    }

    public void store() throws SynthException
    {
        StorePatchMessage message = new StorePatchMessage();

        message.set("storeslot", slot.getSlotId());
        message.set("section", section);
        message.set("position", position);
        
        getSynth().getScheduler().offer(new ScheduledMessage(getSynth(), message));
    }
    
}
