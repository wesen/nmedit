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

import net.sf.nmedit.jnmprotocol2.LoadPatchMessage;
import net.sf.nmedit.jnmprotocol2.StorePatchMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.Bank;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.worker.PatchLocation;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;

public class NMStorePatchWorker implements StorePatchWorker
{

    private NordModular synth;
    private PatchLocation dstLocation;    
    private PatchLocation srcLocation;
    private NMPatch srcPatch;

    public NMStorePatchWorker(NordModular synth)
    {
        this.synth = synth;
    }
    
    public void setSource(Object source)
    {
        srcLocation = null;
        srcPatch = null;
        if (source == null)
            return;
        else if (source instanceof NMPatch)
            srcPatch = (NMPatch) source;
        else if (source instanceof PatchLocation)
            srcLocation = (PatchLocation) source;
        else throw new ClassCastException("unsupported source: "+source);
    }

    public void setDestination(Object destination)
    {
        dstLocation = null;
        if (destination == null)
            return;
        else if (destination instanceof PatchLocation)
            dstLocation = (PatchLocation) destination;
        else throw new ClassCastException("unsupported destination: "+destination);
    }

    public void store() throws SynthException
    {
        if (dstLocation == null)
            throw new NullPointerException("destination not specified");
        
        if (srcLocation == null && srcPatch == null)
            throw new NullPointerException("source not specified");

        if (dstLocation != null) validate(dstLocation);
        if (srcLocation != null) validate(srcLocation);
        
        if (srcPatch != null)
        {
            NMPatch nmpatch = (NMPatch) srcPatch;
            Slot slot = nmpatch.getSlot();
            if (slot != null)
            {
                if (dstLocation.inSlot())
                {
                    nmpatch.setSlot(null); // delete reference to old slot
                }
                else
                {
                    srcLocation = new PatchLocation(slot.getSlotIndex());
                }
            }
        }
        if (dstLocation != null && srcLocation != null)
        {
            if (dstLocation.inSlot() && srcLocation.inSlot()) 
                throw new RuntimeException("cannot transfer a patch from one slot to another slot");
            if (dstLocation.inBank() && srcLocation.inBank())
                throw new RuntimeException("cannot transfer a patch from one bank position to another bank position");

            if (srcLocation.inSlot() && dstLocation.inBank())
            {
                StorePatchMessage message = new StorePatchMessage(
                        srcLocation.getSlot(), 
                        dstLocation.getBank(), 
                        dstLocation.getPosition());        
                synth.getScheduler().offer(new ScheduledMessage(synth, message));
            }
            if (srcLocation.inBank() && dstLocation.inSlot())
            {
                LoadPatchMessage message = new LoadPatchMessage(
                        dstLocation.getSlot(),
                        srcLocation.getBank(),
                        srcLocation.getPosition()
                );
                synth.getScheduler().offer(new ScheduledMessage(synth, message));
                
                NmSlot slot = synth.getSlot(dstLocation.getSlot());
                slot.setPatch(null);
                slot.requestPatch();
            }
            return;
        }
        
        if (srcPatch != null && dstLocation.inSlot())
            (new StorePatchInSlotWorker(synth, srcPatch, dstLocation.getSlot())).store();

    }
    
    private void validate(PatchLocation location)
    {
        if (location.inSlot())
        {
            if(location.getSlot()<0 || location.getSlot()>=synth.getSlotCount())
                throw new RuntimeException("invalid source slot: "+location.getSlot());
        }
        else if (location.inBank())
        {
            if (location.getBank()<0||location.getBank()>=synth.getBankCount())
                throw new RuntimeException("invalid source bank: bank "+location.getBank()+", position "+location.getPosition());
            Bank<?> b = synth.getBank(location.getBank());
            if (location.getPosition()<0||location.getPosition()>=b.getPatchCount())
                throw new RuntimeException("invalid source bank position: bank "+location.getBank()+", position "+location.getPosition());
        }
    }

}
