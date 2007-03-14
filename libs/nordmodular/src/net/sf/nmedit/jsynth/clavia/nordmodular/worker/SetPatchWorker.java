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
 */package net.sf.nmedit.jsynth.clavia.nordmodular.worker;

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;

public class SetPatchWorker extends NmProtocolListener implements ScheduledWorker
{

    private NordModular synth;
    private NMPatch patch;
    private int slotId;
    private MidiMessage message;
    private boolean aborted = false;
    private boolean sent = false;
    private long timeout;
    private boolean ackReply = false;

    public SetPatchWorker(NmSlot slot, NMPatch patch)
    {
        this(slot.getSynthesizer(), patch, slot.getSlotId());
    }
    
    public SetPatchWorker(NordModular synth, NMPatch patch, int slotId)
    {
        this.synth = synth;
        this.patch = patch;
        this.slotId = slotId;
    }
    
    public void messageReceived(AckMessage message) 
    {
        // int cc = message.get("cc");
        int slotId = message.get("slot");
        int pid1 = message.get("pid1");
        // int type = message.get("type"); // type=54, purpose ???
        // int pid2 = message.get("pid2");
        
        if (this.slotId == slotId)
        {
            ackReply = true;
            
            NmSlot slot = synth.getSlot(slotId);
            
            slot.setPatchId(pid1);
            slot.setPatch(patch);
        }
    }

    public void aborted()
    {
        aborted = true;
        dispose();
    }

    public boolean isWorkerFinished()
    {
        return aborted || (!synth.isConnected()) || ackReply;
    }

    public void runWorker() throws SynthException
    {
        if (!sent)
        {
            sent = true;
            synth.addProtocolListener(this);
            prepareMessage();
            sendMessage();
            timeout = System.currentTimeMillis()+5000;
            return ;
        }
        
        if (ackReply)
        {
            return;
        }
        
        if (timeout<System.currentTimeMillis())
        {
            throw new SynthException("timeout in "+this+" ack reply missing");
        }
    }
    
    public void dispose()
    {
        synth.removeProtocolListener(this);
        aborted = true;
    }

    private void sendMessage() throws SynthException
    {
        try
        {
            synth.getProtocol().send(message);
        }
        catch (Exception e)
        {
            throw NmUtils.transformException(e);
        }
    }

    public void forceCreateMessage()
        throws SynthException
    {
        if (message != null)
            return;
        
        prepareMessage();
    }
    
    private void prepareMessage() throws SynthException
    {
        try
        {
            message = NmUtils.createPatchMessage(patch, slotId);
        }
        catch (Exception e)
        {
            throw NmUtils.transformException(e);
        }
    }
    
    public String toString()
    {
        return getClass().getName()+"[patch="+patch+",slot="+slotId+"]";
    }
    
}
