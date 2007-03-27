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
package net.waldorf.miniworks4pole.jsynth;

import net.sf.nmedit.jsynth.AbstractSlot;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;
import net.sf.nmedit.jsynth.worker.SendPatchWorker;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;
import net.waldorf.miniworks4pole.jpatch.MWPatch;
import net.waldorf.miniworks4pole.jprotocol.MiniworksMidiMessage;

public class MWSlot extends AbstractSlot implements Slot
{
    
    private Miniworks4Pole synth;
    private MWPatch patch;

    public MWSlot(Miniworks4Pole synth)
    {
        this.synth = synth;
    }

    public void setPatch(MWPatch patch)
    {
        if (this.patch != patch)
        {
            this.patch = patch;
            fireNewPatchInSlotEvent();
        }
    }
    
    public RequestPatchWorker createRequestPatchWorker()
    {
        return new RequestPatchWorker()
        {
            public void requestPatch() throws SynthException
            {
                MiniworksMidiMessage message =
                    MiniworksMidiMessage.createProgramDumpRequestMessage(0, 0);
                
                synth.send(message);
            }   
        };
    }

    public SendPatchWorker createSendPatchWorker()
    {
        throw new UnsupportedOperationException();
    }

    public StorePatchWorker createStorePatchWorker()
    {
        throw new UnsupportedOperationException();
    }

    public String getName()
    {
        return "Slot "+getSlotIndex();
    }

    public String getPatchName()
    {
        return "Patch";
    }

    public int getSlotIndex()
    {
        return 1;
    }

    public Miniworks4Pole getSynthesizer()
    {
        return synth;
    }

    public MWPatch getPatch()
    {
        return patch;
    }

}
