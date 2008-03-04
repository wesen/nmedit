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

import net.sf.nmedit.jnmprotocol2.ErrorMessage;
import net.sf.nmedit.jnmprotocol2.GetPatchMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jnmprotocol2.PatchMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ParseException;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchBuilder;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;

public class GetPatchWorker extends NmProtocolListener implements ScheduledWorker
{
    
    private NordModular synth;
    private int slotId;
    private int patchId;
    private int messageCount = 0;
    private boolean error = false;
    private PatchBuilder patchBuilder;
    private boolean slotSet = false;
    private boolean messageSent = false;
    private long timeout;

    private static final int PATCH_MESSAGE_COUNT = 13;
    
    public GetPatchWorker(NordModular synth, 
            int slotId, int patchId)
    {
        this.synth = synth;
        this.slotId = slotId;
        this.patchId = patchId;
    }
    
    public void messageReceived(ErrorMessage error)
    {
        aborted();
    }

    public void aborted()
    {
        error = true;
        synth.removeProtocolListener(this);
    }

    public boolean isWorkerFinished()
    {
        return error || (!synth.isConnected()) || (messageCount>= PATCH_MESSAGE_COUNT && slotSet);
    }

    public void runWorker() throws SynthException
    {
        if (!messageSent)
        {
            messageSent = true;
            synth.addProtocolListener(this);
            sendGetPatchMessage();
            
            timeout = System.currentTimeMillis()+5000;
            return;
        }

        if (messageCount<PATCH_MESSAGE_COUNT)
        {
            if (System.currentTimeMillis()>timeout)
            {
                throw new SynthException("timeout in "+this);
            }
            
            return;
        }
        
        slotSet = true;
        
        NmSlot slot = synth.getSlot(slotId);
        //slot.setPatchId(patchId);
        NMPatch patch = patchBuilder.getPatch();
        slot.setPatch(patch);
        patch.setEditSupportEnabled(true); // enable history
    }

    private void sendGetPatchMessage() throws SynthException
    {
        try
        {
            for (GetPatchMessage m: GetPatchMessage.forAllParts(slotId, patchId))
                synth.getProtocol().send(m);
        }
        catch (Exception e)
        {
            throw NmUtils.transformException(e);
        }
    }

    public void messageReceived(PatchMessage message) 
    {
        int slotId = message.get("slot");
        int patchId = message.get("pid");

        if (this.slotId != slotId || this.patchId != patchId)
            return;
        
        if (isWorkerFinished())
            return;

        messageCount ++;
        
        if (messageCount>=PATCH_MESSAGE_COUNT)
        {
            synth.removeProtocolListener(this);
            
        }
        try
        {
            if (patchBuilder == null)
            {
                patchBuilder = NmUtils.parsePatchMessage(message, getModuleDescriptions());
            }
            else
            {
                NmUtils.parsePatchMessage(message, patchBuilder);
            }
        }
        catch (ParseException e)
        {
            error = true;
            // TODO log error
            e.printStackTrace();
        }
    }

    private NM1ModuleDescriptions getModuleDescriptions()
    {
        return synth.getModuleDescriptions();
    }
    
    public String toString()
    {
        return getClass().getName()+"[slot="+slotId+",pid="+patchId+"]";
    }
    
}
