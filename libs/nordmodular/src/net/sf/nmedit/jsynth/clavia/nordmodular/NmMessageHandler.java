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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.ErrorMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.KnobAssignmentMessage;
import net.sf.nmedit.jnmprotocol.LightMessage;
import net.sf.nmedit.jnmprotocol.MeterMessage;
import net.sf.nmedit.jnmprotocol.MorphRangeChangeMessage;
import net.sf.nmedit.jnmprotocol.NewPatchInSlotMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.NoteMessage;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.ParameterSelectMessage;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.jnmprotocol.SetPatchTitleMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jnmprotocol.SynthSettingsMessage;
import net.sf.nmedit.jnmprotocol.VoiceCountMessage;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.Helper;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.GetPatchWorker;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.ScheduledMessage;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;

public class NmMessageHandler extends NmProtocolListener
    implements SlotManagerListener, PropertyChangeListener, SlotListener, SynthesizerStateListener
{

    private NordModular synth;

    public NmMessageHandler(NordModular synth)
    {
        this.synth = synth;
        synth.addSynthesizerStateListener(this);
        synth.getSlotManager().addSlotManagerListener(this);
    }
    
    private boolean isValidSlot(int slotId)
    {
        return 0<=slotId && slotId<synth.getSlotCount();
    }

    public void messageReceived(SynthSettingsMessage msg)
    {
        synth.setSettings(msg);
    }

    public void messageReceived(MorphRangeChangeMessage message) 
    { 
        int slotId = message.getSlot();
        
        if (!isValidSlot(slotId))
            return;
        
        int vaId = message.getSection();
        int moduleId = message.getModule();
        int paramId = message.getParameter();
        int span = message.getSpan();
        int direction = message.getDirection(); // +==0/-==1
        
        NmSlot slot = synth.getSlot(slotId);
        NMPatch patch = slot.getPatch();
        
        VoiceArea va = null;
        if (vaId == Format.VALUE_SECTION_VOICE_AREA_POLY)
            va = patch.getPolyVoiceArea();
        else if (vaId == Format.VALUE_SECTION_VOICE_AREA_COMMON)
            va = patch.getCommonVoiceArea();
        
        if (va == null)
            return;
        
        PModule module = va.getModule(moduleId);
        
        PParameter p;
        try
        {
            p = Helper.getParameter(module, "morph", paramId);
        }
        catch (InvalidDescriptorException e)
        {
            return;
        }
        p.setValue(direction==1?-span:+span);
    }

    public void messageReceived(NoteMessage message) 
    { 
        System.out.println("note: "+message);
    }
    
    /**
     * This message is ignored. NordModular.connect() causes this message and
     * handles it itself.
     */
    public void messageReceived(IAmMessage message) 
    { 
        // no op
    }
    
    /**
     * This message is ignored. The GetPatchWorker causes this kind
     * of messages and handles them itself.
     */
    public void messageReceived(PatchMessage message) 
    {
        // no op 
    }

    /**
     * This message is ignored.
     */
    public void messageReceived(PatchListMessage message) 
    {
        // no op
    }

    public void messageReceived(SetPatchTitleMessage message) 
    {
        int slotId = message.getSlot();
        //int pid = message.get("pid");
        
        // check if slot is available <=> synth is connected
        // -> this is not implied by the received message
        if (!isValidSlot(slotId))
            return;

        NmSlot slot = synth.getSlot(slotId);
        NMPatch patch = slot.getPatch();
        String title = message.getTitle();
        if (patch != null)
        patch.setName(title);
    }
    
    /**
     * TODO document for messageReceived(AckMessage) 
     */
    public void messageReceived(AckMessage message) 
    {
        int slotId = message.getSlot();
        
        // check if slot is available <=> synth is connected
        // -> this is not implied by the received message
        if (!isValidSlot(slotId))
            return;
        
        /*
        NmSlot slot = synth.getSlot(slotId);
        if (slot.getPatchId() != message.getPid1())
        {
            slot.setPatchId(message.getPid1());
        }*/
    }

    public void messageReceived(NewPatchInSlotMessage message) 
    {
        // user changed the patch in one of the slots
        // trigger-source: synth
        
        int slotId = message.getSlot();
        
        if (!isValidSlot(slotId))
            return;

        int patchId = message.getPid();
        
        NmSlot slot = synth.getSlot(slotId);

        slot.setPatch(null);
        //slot.setPatchId(patchId);
        
        getPatch(slotId, patchId);
    }

    private void getPatch(int slot, int patchId)
    {
        if (!synth.isConnected())
            return;
        
        GetPatchWorker worker = new GetPatchWorker(synth, slot, patchId);
        synth.getScheduler().offer(worker);
    }
    
    public static void requestPatch(NordModular synth, int slotId)
    {
        synth.getScheduler().offer(new ScheduledMessage(synth, 
                new RequestPatchMessage(slotId)));
    }
    
    public void messageReceived(VoiceCountMessage message) 
    {
        for (int i=0;i<synth.getSlotCount();i++)
        {
            synth.getSlot(i).setVoiceCount(message.getVoiceCount(i));
        }
    }
    
    // slot selected => led on
    // slot activated => led blinking
    
    public void messageReceived(SlotsSelectedMessage message) 
    {
        for (int i=0;i<synth.getSlotCount();i++)
        {
            synth.getSlot(i).setEnabled(message.isSlotSelected(i));
        }
    }

    public void messageReceived(SlotActivatedMessage message) 
    {
        int slotId = message.getActiveSlot();

        if (!isValidSlot(slotId))
            return;
        
        synth.getNmSlotManager().setActiveSlot(slotId);
    }

    public void messageReceived(LightMessage message) 
    {
        int slotId = message.getSlot();
        
        if (!isValidSlot(slotId))
            return;
        NmSlot slot = synth.getSlot(slotId);
        NMPatch patch = slot.getPatch();
        if (patch == null) return;
        patch.getLightProcessor().processLightMessage(message);
    }

    public void messageReceived(MeterMessage message) 
    {
        int slotId = message.getSlot();
        
        if (!isValidSlot(slotId))
            return;
        NmSlot slot = synth.getSlot(slotId);
        NMPatch patch = slot.getPatch();
        if (patch == null) return;
        patch.getLightProcessor().processMeterMessage(message);
    }

    public void messageReceived(KnobAssignmentMessage message) 
    { 
        int slotId = message.getSlot();
        
        if (!isValidSlot(slotId))
            return;
        
        int prevKnob = message.get("prevknob");
        int vaId = message.get("section");
        int moduleId = message.get("module");
        int paramId = message.get("parameter");
        int knob = message.get("knob");

        NmSlot slot = synth.getSlot(slotId);
        NMPatch patch = slot.getPatch();
        
        if (prevKnob>=0)
        {
            Knob k = patch.getKnobs().getByID(prevKnob);
            if (k != null)
                k.setParameter(null);
        }
        
        if (knob>=0)
        {
            VoiceArea va = null;
            if (vaId == Format.VALUE_SECTION_VOICE_AREA_POLY)
                va = patch.getPolyVoiceArea();
            else if (vaId == Format.VALUE_SECTION_VOICE_AREA_COMMON)
                va = patch.getCommonVoiceArea();
            
            if (va == null)
                return;
            
            PModule module = va.getModule(moduleId);
            
            PParameter p;
            try
            {
                p = Helper.getParameter(module, "parameter", paramId);
            }
            catch (InvalidDescriptorException e)
            {
                return;
            }
            
            Knob k = patch.getKnobs().getByID(knob);
            if (k != null)
                k.setParameter(p);
        }
    }
    
    public void messageReceived(ParameterSelectMessage message) 
    { 
        int slotId = message.getSlot();
        if (!isValidSlot(slotId))
            return;
        
        int vaId = message.getSection();
        int moduleId = message.getModule();
        int paramId = message.getParameter();
        
        // addParameter("pid", "data:pid");
        // addParameter("sc", "data:sc");
        
        NmSlot slot = synth.getSlot(slotId);
        NMPatch patch = slot.getPatch();
        
        VoiceArea va = null;
        if (vaId == Format.VALUE_SECTION_VOICE_AREA_POLY)
            va = patch.getPolyVoiceArea();
        else if (vaId == Format.VALUE_SECTION_VOICE_AREA_COMMON)
            va = patch.getCommonVoiceArea();
        
        if (va == null)
            return;
        
        PModule module = va.getModule(moduleId);
        
        PParameter p;
        try
        {
            p = Helper.getParameter(module, "parameter", paramId);
        }
        catch (InvalidDescriptorException e)
        {
            return;
        }
        p.requestFocus();
    }
    
    public void messageReceived(ParameterMessage message) 
    {
        int slotId = message.getSlot();
        
        if (!isValidSlot(slotId))
            return;
        
        int vaId = message.getSection();
        int moduleId = message.getModule();
        int paramId = message.getParameter();
        int value = message.getValue();
        
        // addParameter("pid", "data:pid");
        // addParameter("sc", "data:sc");
        
        NmSlot slot = synth.getSlot(slotId);
        NMPatch patch = slot.getPatch();
        
        VoiceArea va = null;
        if (vaId == Format.VALUE_SECTION_VOICE_AREA_POLY)
            va = patch.getPolyVoiceArea();
        else if (vaId == Format.VALUE_SECTION_VOICE_AREA_COMMON)
            va = patch.getCommonVoiceArea();
        
        if (va == null)
            return;
        
        PModule module = va.getModule(moduleId);
        
        PParameter p;
        try
        {
            p = Helper.getParameter(module, "parameter", paramId);
        }
        catch (InvalidDescriptorException e)
        {
            return;
        }
        p.setValue(value);        
    }
    
    public void messageReceived(ErrorMessage message) 
    {
        throw new RuntimeException(message.toString());
    }

    public void slotAdded(SlotEvent e)
    {
        installSlot((NmSlot) e.getSlot());
    }

    public void slotRemoved(SlotEvent e)
    {
        uninstallSlot((NmSlot) e.getSlot());
    }

    private void installSlot(NmSlot slot)
    {
        slot.addSlotListener(this);
        slot.addPropertyChangeListener(NmSlot.PROPERTY_ENABLEDSLOT, this);
    }

    private void uninstallSlot(NmSlot slot)
    {
        slot.removeSlotListener(this);
        slot.removePropertyChangeListener(this);        
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        NmSlot slot = (NmSlot) evt.getSource();
        
        if (evt.getPropertyName() == NmSlot.PROPERTY_ENABLEDSLOT)
        {
            if ((Boolean)evt.getNewValue())
            {
                // enabled
                // TODO should we call request patch first to get the pid ?
                //getPatch(slot.getSlotId(), 0);
                
                //requestPatch(synth, slot.getSlotId());
            }
            else
            {
                // disabled
                //slot.setPatch(null);   
            }
        }
    }

    public void newPatchInSlot(SlotEvent e)
    {
        NmSlot slot = (NmSlot) e.getSlot();
        NMPatch oldPatch = (NMPatch) e.getOldPatch();
        NMPatch newPatch = (NMPatch) e.getNewPatch();
        
        uninstallSynchronizer(slot.getSlotId());
     
        if (newPatch != null)
        {
            installSynchronizer(slot, newPatch);
        }
    }
    
    private void installSynchronizer(NmSlot slot, NMPatch patch)
    {
        NmPatchSynchronizer sync = new NmPatchSynchronizer(slot.getSynthesizer(), patch, slot);
        synchronizerList[slot.getSlotIndex()] = sync;
        sync.install();
    }

    private void uninstallSynchronizer(int slotId)
    {
        NmPatchSynchronizer sync = synchronizerList[slotId];
        if (sync!=null)
        {
            synchronizerList[slotId] = null;
            sync.uninstall();
        }
    }
    
    private NmPatchSynchronizer[] synchronizerList = new NmPatchSynchronizer[4];

    public void synthConnectionStateChanged(SynthesizerEvent e)
    {
        if (synth.isConnected())
        {
            // reply will update the slot.enabled properties
            for (int i=0;i<synth.getSlotCount();i++)
                NmMessageHandler.requestPatch(synth, i);
        }
    }

}
