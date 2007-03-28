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

/*
 * Created on Jan 9, 2007
 */
package net.sf.nmedit.jsynth.clavia.nordmodular;

import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.AbstractSlot;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SlotManager;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.ReqPatchWorker;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.ScheduledMessage;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.SetPatchWorker;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;
import net.sf.nmedit.jsynth.worker.SendPatchWorker;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;

public class NmSlot extends AbstractSlot implements Slot
{

    public static final String PROPERTY_VOICECOUNT = "voicecount";
    public static final String PROPERTY_ACTIVESLOT = "active";
    public static final String PROPERTY_ENABLEDSLOT = "enabled";
    public static final String PROPERTY_PATCH_ID = "pid";

    private static final char[] slotIdLetter = new char[] {'A', 'B', 'C', 'D'};
    private int slotId;
    private int patchId;
    private NordModular synth;
    private NMPatch patch;
    private int voiceCount = 0;

    private boolean activated = false;
    private boolean enabled = false;
    
    public NmSlot(NordModular synth, int slotId)
    {
        if (!(slotId >= 0 && slotId<4))
            throw new IllegalArgumentException("invalid slot id: "+slotId);
        this.synth = synth;
        this.slotId = slotId;
    }
    
    public String getPatchName()
    {
        return patch!= null ? patch.getName() : null;
    }
    
    public int getVoiceCount()
    {
        return voiceCount;
    }
    
    public boolean isEnabled()
    {
        return enabled;
    }
    
    public boolean isActivated()
    {
        return activated;
    }
    
    void setEnabled(boolean enabled)
    {
        boolean oldValue = this.enabled;
        boolean newValue = enabled;
        
        if (oldValue != newValue)
        {
            this.enabled = enabled;
            fireSlotEnabledChange(oldValue, newValue);
        }
    }
    
    public void setActivated(boolean activated)
    {
        boolean oldValue = this.activated;
        boolean newValue = activated;
        
        if (oldValue != newValue)
        {
            this.activated = activated;
            fireActiveSlotChange(oldValue, newValue);
        }
    }

    public void requestEnableSlot(boolean enable)
    {
        // enable == selected
        
        if (isEnabled() == enable)
            return;
        
        boolean[] selection = new boolean[]{false, false, false, false};
        SlotManager<NmSlot> manager = synth.getSlotManager();
        for (int i=Math.min(manager.getSlotCount(), 4)-1;i>=0;i--)
            selection[i] = manager.getSlot(i).isEnabled();
        selection[getSlotIndex()] = enable;

        MidiMessage message = NmUtils.createSlotsSelectedMessage(
                selection[0], selection[1], 
                selection[2], selection[3]);
        
        synth.getScheduler().offer(new ScheduledMessage(synth, message));
    }

    public void requestSelectSlot()
    {
        // select == activated
        
        if (!isActivated())
        {
            synth.getScheduler().offer(new ScheduledMessage(synth, 
                    NmUtils.createSlotsActivatedMessage(getSlotId())
            ));
        }
    }
    
    void setVoiceCount(int voiceCount)
    {
        if (voiceCount<0)
            throw new IllegalArgumentException("invalid voice count: "+voiceCount);
        int oldValue = this.voiceCount;
        int newValue = voiceCount;
        if (oldValue != newValue)
        {
            this.voiceCount = voiceCount;
            fireVoiceCountChange(oldValue, newValue);
        }
    }
    
    public NordModular getSynthesizer()
    {
        return synth;
    }
    
    public int getSlotId()
    {
        return slotId;
    }

    public String getName()
    {
        return "Slot "+slotIdLetter[slotId];
    }

    public String toString()
    {
        return getClass().getName()+"[slotid="+slotId+",name='"+getName()+"']";
    }

    public int getPatchId()
    {
        return patchId;
    }

    public void setPatchId(int pid)
    {
        int oldValue = this.patchId;
        int newValue = pid;
        if (oldValue != newValue)
        {
            this.patchId = pid;
            setEnabled(pid>0);//this is wrong:pid=0 is valid
            firePatchIdChange(oldValue, newValue);
        }
    }
    

    public RequestPatchWorker createRequestPatchWorker()
    {
        return new ReqPatchWorker(synth, getSlotId(), patch!=null);
    }
    
    public NMPatch getPatch()
    {
        return patch;
    }
    
    public void setPatch(NMPatch patch)
    {
        if (this.patch != patch)
        {
            this.patch = patch;
            fireNewPatchInSlotEvent();
        }
    }
    
    public int getSlotIndex()
    {
        return slotId;
    }

    public StorePatchWorker createStorePatchWorker()
    {
  //TODO
        return new 
        net.sf.nmedit.jsynth
        .clavia.nordmodular
        .worker.StorePatchWorker(this, 0, 0);
    }
 
    public SendPatchWorker createSendPatchWorker()
    {
        return new SendPatchToSlotPatchWorker(this);
    }

    /**
     * Implementation of the SendPatchWorker interface.
     * The worker sends the patch asynchronously and performs
     * following steps:
     * 
     * 1. install to scheduler
     * 2. install protocol listener/build/send patch message
     * 
     * @author christian
     */
    private static class SendPatchToSlotPatchWorker
        implements SendPatchWorker
    {
        private NmSlot target;
        private NMPatch patch;
        private boolean sent = false;

        public SendPatchToSlotPatchWorker(NmSlot target)
        {
            this.target = target;
        }
        
        public NordModular getSynth()
        {
            return target.getSynthesizer();
        }

        public void setPatch(Object patch) throws SynthException
        {
            this.patch = (NMPatch) patch;
        }

        public void send() throws SynthException
        {
            if (patch == null)
                throw new SynthException("patch is not set");

            if (sent)
                throw new SynthException("cannot resent patch");
            sent = true;
            
            SetPatchWorker worker = new SetPatchWorker(target.getSynthesizer(), 
                    patch, target.getSlotId());
            
            // we want to create the midi message in the current thread
            // instead of the protocol thread so the latter thread
            // is not blocked by this operation
            worker.forceCreateMessage();
            
            getSynth().getScheduler().offer(worker);
        }

    }

    protected void fireVoiceCountChange(int oldVoiceCount, int newVoiceCount)
    {
        changeSupport.firePropertyChange(PROPERTY_VOICECOUNT, oldVoiceCount, newVoiceCount);
    }

    protected void fireActiveSlotChange(boolean oldActivated, boolean newActivated)
    {
        changeSupport.firePropertyChange(PROPERTY_ACTIVESLOT,  oldActivated, newActivated);
    }

    protected void fireSlotEnabledChange(boolean oldEnabled, boolean newEnabled)
    {
        changeSupport.firePropertyChange(PROPERTY_ENABLEDSLOT,  oldEnabled, newEnabled);
    }

    protected void firePatchIdChange(int oldPid, int newPid)
    {
        changeSupport.firePropertyChange(PROPERTY_PATCH_ID,  oldPid, newPid);
    }

}
