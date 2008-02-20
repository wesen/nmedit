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

import net.sf.nmedit.jnmprotocol2.GetPatchMessage;
import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jnmprotocol2.SlotsSelectedMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.AbstractSlot;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SlotManager;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.ReqPatchWorker;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.ScheduledMessage;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;

public class NmSlot extends AbstractSlot implements Slot
{

    public static final String PROPERTY_VOICECOUNT = "voicecount";
    public static final String PROPERTY_PATCH_ID = "pid";

    private static final char[] slotIdLetter = new char[] {'A', 'B', 'C', 'D'};
    private int slotId;
    private NordModular synth;
    private NMPatch patch;
    private int voiceCount = 0;
    private String patchName;

    private boolean enabled = false;
    
    private boolean patchRequestInProgress = false;
    
    // for internal use only
    public boolean isPatchRequestInProgress()
    {
        return patchRequestInProgress;
    }

    // for internal use only
    public void setPatchRequestInProgress(boolean prip)
    {
        this.patchRequestInProgress = prip;
    }
    
    public NmSlot(NordModular synth, int slotId)
    {
        if (!(slotId >= 0 && slotId<4))
            throw new IllegalArgumentException("invalid slot id: "+slotId);
        this.synth = synth;
        this.slotId = slotId;
    }
    
    void updatePatchName()
    {
        if (isEnabled() && patchName == null)
            requestPatchName();
    }

    public boolean isPropertyModifiable(String propertyName)
    {
        if (ENABLED_PROPERTY.equals(propertyName))
            return true;
        
        return false;
    }
    
    public String getPatchName()
    {
        return patchName;
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
        return synth.getActiveSlot() == slotId;
    }
    
    public void setEnabled(boolean enabled)
    {
        boolean oldValue = this.enabled;
        boolean newValue = enabled;
        
        if (oldValue != newValue)
        {
            requestEnableSlot(newValue);
        }
    }

    void setEnabledValue(boolean enabled)
    {
        boolean oldValue = this.enabled;
        boolean newValue = enabled;
        
        if (oldValue != newValue)
        {
            this.enabled = enabled;
            fireSlotEnabledChange(oldValue, newValue);
            synth.fireSlotEnabledChange(getSlotIndex(), oldValue, newValue);
        }
    }
    
    public void setActivated(boolean activated)
    {
        if (activated) synth.setActiveSlot(slotId);
    }

    private void requestEnableSlot(boolean enable)
    {
        // enable == selected
        
        boolean[] selection = new boolean[]{false, false, false, false};
        SlotManager<NmSlot> manager = synth.getSlotManager();
        for (int i=Math.min(manager.getSlotCount(), 4)-1;i>=0;i--)
            selection[i] = manager.getSlot(i).isEnabled();
        selection[getSlotIndex()] = enable;

        MidiMessage message = new SlotsSelectedMessage(
                selection[0], selection[1], 
                selection[2], selection[3]);
        
        synth.getScheduler().offer(new ScheduledMessage(synth, message));
    }
    
    public boolean isSelected()
    {
        return isActivated();
    }
    
    public void setSelected(boolean selected)
    {
        setActivated(selected);
    }

    void requestPatchName()
    {
        synth.getScheduler().offer(new ScheduledMessage(synth, 
                new GetPatchMessage(getSlotIndex(), getPatchId(),
                GetPatchMessage.PatchPart.HEADER)));
    }

    void setPatchNameValue(String patchName)
    {
        String oldValue = this.patchName;
        String newValue = patchName;
        if (oldValue != newValue || (oldValue != null && oldValue.equals(newValue)))
        {
            this.patchName = newValue;
            firePropertyChange(Slot.PATCHNAME_PROPERTY, oldValue, newValue);
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
        return ""+slotIdLetter[slotId];
    }

    public String toString()
    {
        return getClass().getName()+"[slotid="+slotId+",name='"+getName()+"']";
    }

    public int getPatchId()
    {
        return synth.getPId(getSlotId());
    }
/*
    public void setPatchIds(int pid)
    {
        int oldValue = this.patchId;
        int newValue = pid;
        if (oldValue != newValue)
        {
            this.patchId = pid;
            setEnabled(pid>0);//this is wrong:pid=0 is valid
            firePatchIdChange(oldValue, newValue);
        }
    }*/
    
    public RequestPatchWorker createRequestPatchWorker()
    {
        return new ReqPatchWorker(synth, getSlotId(), patch!=null);
    }

    public void requestPatch() throws SynthException
    {
        createRequestPatchWorker().requestPatch();
    }
    
    public NMPatch getPatch()
    {
        return patch;
    }
    
    public void setPatch(NMPatch patch)
    {
        NMPatch old = this.patch;
        if (old != patch)
        {
            if (old != null)
                old.setSlot(null);
            this.patch = patch;
            if (patch != null)
            {
                Slot osl = patch.getSlot();
                if (osl != null)
                    ((NmSlot)osl).setPatch(null); // remove patch from prev. slot
                
                patch.setSlot(this);
            }
            
            if (patch != null)
            {
                String name = patch.getName();
                if (name == null) name = "";
                setPatchNameValue(name);
            }

            fireNewPatchInSlotEvent(old, patch);
        }
    }
    
    public int getSlotIndex()
    {
        return slotId;
    }

    protected void fireVoiceCountChange(int oldVoiceCount, int newVoiceCount)
    {
        changeSupport.firePropertyChange(PROPERTY_VOICECOUNT, oldVoiceCount, newVoiceCount);
    }

    protected void fireSelectedSlotChange(boolean oldActivated, boolean newActivated)
    {
        changeSupport.firePropertyChange(SELECTED_PROPERTY,  oldActivated, newActivated);
    }

    protected void fireSlotEnabledChange(boolean oldEnabled, boolean newEnabled)
    {
        changeSupport.firePropertyChange(ENABLED_PROPERTY,  oldEnabled, newEnabled);
    }

    protected void firePatchIdChange(int oldPid, int newPid)
    {
        changeSupport.firePropertyChange(PROPERTY_PATCH_ID,  oldPid, newPid);
    }

}
