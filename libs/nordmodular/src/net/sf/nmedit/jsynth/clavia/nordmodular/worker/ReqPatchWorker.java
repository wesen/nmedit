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

import net.sf.nmedit.jnmprotocol2.AckMessage;
import net.sf.nmedit.jnmprotocol2.ErrorMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jnmprotocol2.RequestPatchMessage;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;

public class ReqPatchWorker extends
    NmProtocolListener implements RequestPatchWorker, ScheduledWorker
{

    private NordModular synth;
    private int slotId;
    private int patchId = 0;
    private boolean fakeWorker;
    private boolean called = false;  
    private boolean error = false;
    
    private int state = START;
    private static final int START = 0;
    private static final int WAIT_FOR_ACK = 1;
    private static final int ACK_RECEIVED = 2;
    private static final int DONE = 3;
    
    public ReqPatchWorker(NordModular synth, int slotId, boolean fakeWorker)
    {
        this.synth = synth;
        this.slotId = slotId;
        this.fakeWorker = fakeWorker;
    }

    public void requestPatch() throws SynthException
    {
        if (!synth.isConnected())
            throw new SynthException("not connected");
        
        if (called)
            return; // "worker already used
        
        called = true;
        
        if (fakeWorker) return;
        
        NmSlot slot = getSlotOrNull();
        if (slot == null) return;
        slot.setPatchRequestInProgress(true);
        synth.getScheduler().offer(this);
    }
    
    private NmSlot getSlotOrNull()
    {
        if (slotId>=0 && slotId<synth.getSlotCount())
            return synth.getSlot(slotId);
        return null;
    }

    public void aborted()
    {
        NmSlot slot = getSlotOrNull();
        if (slot != null) slot.setPatchRequestInProgress(false);
        error = true;
        synth.removeProtocolListener(this);
    }

    public boolean isWorkerFinished()
    {
        boolean finished = error || (!synth.isConnected()) || state>=DONE;
        if (finished)
        {
            NmSlot slot = getSlotOrNull();
            if (slot != null) slot.setPatchRequestInProgress(false);
        }
        return finished;
    }
    
    private long ackTimeout = 0;

    public void runWorker() throws SynthException
    {      
        if (state==START)
        {
            ackTimeout = System.currentTimeMillis()+2000;
            state = WAIT_FOR_ACK;
            synth.addProtocolListener(this);
            try
            {
                synth.getProtocol().send(new RequestPatchMessage(slotId));
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        if (state == WAIT_FOR_ACK)
        {
            if (ackTimeout<System.currentTimeMillis())
            {
                throw new SynthException("timeout: ack message");
            }
            
            return;
        }
        if (state == ACK_RECEIVED)
        {
            synth.removeProtocolListener(this);
            state = DONE;

            NmSlot slot = getSlotOrNull();
            if (slot != null) slot.setPatchRequestInProgress(false);
            
            GetPatchWorker worker = new GetPatchWorker(synth, slotId, patchId);
            synth.getScheduler().offer(worker);
        }
    }

    public void messageReceived(ErrorMessage message)
    {
        aborted();
    }
    
    public void messageReceived(AckMessage message)
    {
        if (slotId == message.get("slot"))
        {
            patchId = message.get("pid1");
            state = ACK_RECEIVED;
        }
    }
    
}
