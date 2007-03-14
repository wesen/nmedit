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
import net.sf.nmedit.jnmprotocol.LightMessage;
import net.sf.nmedit.jnmprotocol.MeterMessage;
import net.sf.nmedit.jnmprotocol.NewPatchInSlotMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jnmprotocol.VoiceCountMessage;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.GetPatchWorker;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.ScheduledMessage;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;

public class NmMessageHandler extends NmProtocolListener
    implements SlotManagerListener, PropertyChangeListener, SlotListener
{

    private NordModular synth;

    public NmMessageHandler(NordModular synth)
    {
        this.synth = synth;
        synth.getSlotManager().addSlotManagerListener(this);
    }
    
    private boolean isValidSlot(int slotId)
    {
        return 0<=slotId && slotId<synth.getSlotCount();
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
    
    /**
     * TODO document for messageReceived(AckMessage) 
     */
    public void messageReceived(AckMessage message) 
    {
        int slotId = message.get("slot");
        int pid1 = message.get("pid1");
        //int type = message.get("type");
        //int pid2 = message.get("pid2");
        
        // check if slot is available <=> synth is connected
        // -> this is not implied by the received message
        if (!isValidSlot(slotId))
            return;
        
        NmSlot slot = synth.getSlot(slotId);
        if (slot.getPatchId() != pid1)
        {
            slot.setPatchId(pid1);
        }
    }

    public void messageReceived(NewPatchInSlotMessage message) 
    {
        // user changed the patch in one of the slots
        // trigger-source: synth
        
        int slotId = message.get("slot");
        int patchId = message.get("pid");
        
        if (!isValidSlot(slotId))
            return;
        
        NmSlot slot = synth.getSlot(slotId);
        slot.setPatch(null);
        slot.setPatchId(patchId);
        
        getPatch(slotId, patchId);
    }

    private void getPatch(int slot, int patchId)
    {
        if (!synth.isConnected())
            return;
        
        GetPatchWorker worker = new GetPatchWorker(synth, slot, patchId);
        synth.getScheduler().offer(worker);
    }
    
    private void requestPatch(int slotId)
    {
        synth.getScheduler().offer(new ScheduledMessage(synth, 
                NmUtils.createRequestPatchMessage(slotId)));
    }
    
    public void messageReceived(VoiceCountMessage message) 
    {
        for (int i=0;i<synth.getSlotCount();i++)
        {
            int vc = message.get("voiceCount"+i);
            synth.getSlot(i).setVoiceCount(vc);
        }
    }
    
    public void messageReceived(SlotsSelectedMessage message) 
    {
        for (int i=0;i<synth.getSlotCount();i++)
        {
            boolean selected = message.get("slot"+i+"Selected")==1;
            NmSlot slot = synth.getSlot(i);
            slot.setEnabled(selected);
        }
    }

    public void messageReceived(SlotActivatedMessage message) 
    {
        int slotId = message.get("activeSlot");

        if (!isValidSlot(slotId))
            return;
        
        synth.getNmSlotManager().setActiveSlot(slotId);
    }

    public void messageReceived(ParameterMessage message) 
    {
        int slotId = message.get("slot");
        
        if (!isValidSlot(slotId))
            return;
        
        int vaId = message.get("section");
        int moduleId = message.get("module");
        int paramId = message.get("parameter");
        int value = message.get("value");
        
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
        
        NMModule module = va.getModule(moduleId);
        
        ModuleDescriptor md = module.getDescriptor();
        Parameter p;
        try
        {
            p = module.getParameter(md.getParameter(paramId, "parameter"));
        }
        catch (InvalidDescriptorException e)
        {
            return;
        }
        p.setValue(value);        
    }
    
    public void messageReceived(MeterMessage message) 
    {
        // TODO implement message handling
    }
        
    public void messageReceived(LightMessage message) 
    {
        // TODO implement message handling
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
                requestPatch(slot.getSlotId());
            }
            else
            {
                // disabled
                slot.setPatch(null);   
            }
        }
    }

    public void newPatchInSlot(SlotEvent e)
    {
        NmSlot slot = (NmSlot) e.getSlot();
        
        uninstallSynchronizer(slot.getSlotId());
        
        NMPatch patch = slot.getPatch();
        if (patch != null)
        {
            installSynchronizer(slot, patch);
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

}
