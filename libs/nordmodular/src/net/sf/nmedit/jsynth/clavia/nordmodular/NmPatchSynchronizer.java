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
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.event.ConnectionEvent;
import net.sf.nmedit.jpatch.event.ModuleContainerEvent;
import net.sf.nmedit.jpatch.event.ModuleEvent;
import net.sf.nmedit.jpatch.event.ParameterEvent;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;

public class NmPatchSynchronizer extends AllEventsListener 
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

        uninstallModuleContainer(patch.getPolyVoiceArea());
        uninstallModuleContainer(patch.getCommonVoiceArea());
    }
    
    public void install()
    {
        if (installed)
            return;

        installModuleContainer(patch.getPolyVoiceArea());
        installModuleContainer(patch.getCommonVoiceArea());
    }

    public void moduleAdded(ModuleContainerEvent e)
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

    public void moduleRemoved(ModuleContainerEvent e)
    {
        super.moduleRemoved(e);
        PModule module = e.getModule();
        try
        {
        synth.getProtocol().send(NmUtils.createDeleteModuleMessage(slot.getPatchId(), module));
        }
        catch (Exception er)
        {
            // TODO handle error
        }
    }

    public void moduleRenamed(ModuleEvent e)
    {
        // NmUtils.create
    }

    public void moduleMoved(ModuleEvent e)
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

    public void parameterValueChanged(ParameterEvent e)
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

    public void connectionAdded(ConnectionEvent e)
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

    public void connectionRemoved(ConnectionEvent e)
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
   
}
