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

import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jpatch.AllEventsListener;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PAssignmentEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PAssignmentListener;
import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PParameterEvent;
import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;

public class NmPatchSynchronizer extends AllEventsListener implements PAssignmentListener
{

    private NordModular synth;
    private NMPatch patch;
    private NmSlot slot;
    private boolean installed;

    public NmPatchSynchronizer(NordModular synth, NMPatch patch, NmSlot slot)
    {
        listenConnections = true;
        listenModules = true;
        listenParameters = true;
        
        this.synth = synth;
        this.patch = patch;
        this.slot = slot;
    }
    
    public NMPatch getPatch()
    {
        return patch;
    }
    
    public Slot getSlot()
    {
        return slot;
    }
    
    public boolean isInstalled()
    {
        return installed;
    }
    
    public void uninstall()
    {
        if (!installed)
            return;
        
        patch.removeAssignmentListener(this);

        uninstallModuleContainer(patch.getPolyVoiceArea());
        uninstallModuleContainer(patch.getCommonVoiceArea());
    }
    
    public void install()
    {
        if (installed)
            return;

        patch.addAssignmentListener(this);
        
        installModuleContainer(patch.getPolyVoiceArea());
        installModuleContainer(patch.getCommonVoiceArea());
    }

    public void moduleAdded(PModuleContainerEvent e)
    {
        super.moduleAdded(e);
        PModule module = e.getModule();            
        try
        {
            synth.getProtocol().send(NmUtils.createNewModuleMessage(slot.getPatchId(), module));
        }
        catch (Exception er)
        {
            // TODO handle error
        }
    }

    public void moduleRemoved(PModuleContainerEvent e)
    {
        super.moduleRemoved(e);
        PModule module = e.getModule();
        int index = e.getIndex();
        
        //System.out.println("delete@"+index);
        try
        {
        synth.getProtocol().send(NmUtils.createDeleteModuleMessage(slot.getPatchId(), module, index));
        }
        catch (Exception er)
        {
            // TODO handle error
        }
    }

    public void moduleRenamed(PModuleEvent e)
    {
        // NmUtils.create
    }

    public void moduleMoved(PModuleEvent e)
    {
        try
        {
            MidiMessage message =
                NmUtils.createMoveModuleMessage(e.getModule(), slot.getSlotIndex(), 
                    slot.getPatchId());
            
            // TODO if (synth.isConnected())
            synth.getProtocol().send(message);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    public void parameterValueChanged(PParameterEvent e)
    {
        PParameter parameter = e.getParameter();

        try
        {
            MidiMessage message =
                NmUtils.createParameterChangedMessage(parameter, 
                        slot.getSlotIndex(), slot.getPatchId());

            // TODO if (synth.isConnected())
            synth.getProtocol().send(message);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    public void connectionAdded(PConnectionEvent e)
    {
        try
        {
            VoiceArea va = (VoiceArea) e.getConnectionManager().getModuleContainer();
            
            MidiMessage message =
            NmUtils.createNewCableMessage(va, 
                    e.getSource(), 
                    e.getDestination(),
                    slot.getSlotId(), slot.getPatchId());
                    
            synth.getProtocol().send(message);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    public void connectionRemoved(PConnectionEvent e)
    {
        try
        {
            VoiceArea va = (VoiceArea) e.getConnectionManager().getModuleContainer();
            
            synth.getProtocol().send(
                    NmUtils.createDeleteCableMessage(va, 
                            e.getSource(), 
                            e.getDestination(),
                    slot.getSlotId(), slot.getPatchId()
            ));
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    public void parameterAssigned(PAssignmentEvent e)
    {

      //  System.out.println("user assign:" +e);
        
        MidiMessage msg = null;
        
        switch (e.getId())
        {
            case PAssignmentEvent.MORPH_ASSIGNED:
                break;
            case PAssignmentEvent.KNOB_ASSIGNED:
                msg = NmUtils.createKnobAssignmentMessage(e.getParameter(), -1, e.getKnobId(),
                        slot.getSlotIndex(), slot.getPatchId());
                break;
            case PAssignmentEvent.MIDICTRL_ASSIGNED:
                break;
        }
        
        if (msg != null)
        {
            try
            {
                synth.getProtocol().send(msg);
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
        
    }

    public void parameterDeassigned(PAssignmentEvent e)
    {

       // System.out.println("user deassign:" +e);
        
        MidiMessage msg = null;
        
        switch (e.getId())
        {
            case PAssignmentEvent.MORPH_DEASSIGNED:
                break;
            case PAssignmentEvent.KNOB_DEASSIGNED:
                msg = NmUtils.createKnobDeAssignmentMessage(e.getKnobId(), 
                        slot.getSlotIndex(), slot.getPatchId());
                
       //         System.out.println("user deassign:"+msg);
                
                break;
            case PAssignmentEvent.MIDICTRL_DEASSIGNED:
                break;
        }
        
        if (msg != null)
        {
            try
            {
                synth.getProtocol().send(msg);
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
        
    }
   
}
