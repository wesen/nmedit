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
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMConnector;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.event.ConnectionEvent;
import net.sf.nmedit.jpatch.event.ConnectionListener;
import net.sf.nmedit.jpatch.event.ModuleContainerEvent;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.event.ModuleEvent;
import net.sf.nmedit.jpatch.event.ModuleListener;
import net.sf.nmedit.jpatch.event.ParameterEvent;
import net.sf.nmedit.jpatch.event.ParameterValueChangeListener;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;

public class NmPatchSynchronizer
{

    private NordModular synth;
    private NMPatch patch;
    private NmSlot slot;
    private boolean installed;

    private VoiceAreaSynchronizer commonVaSync;
    private VoiceAreaSynchronizer polyVaSync;

    public NmPatchSynchronizer(NordModular synth, NMPatch patch, NmSlot slot)
    {
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
        
        commonVaSync.uninstall();
        polyVaSync.uninstall();
        
        commonVaSync = null;
        polyVaSync = null;
    }
    
    public void install()
    {
        if (installed)
            return;

        commonVaSync = new VoiceAreaSynchronizer(patch.getCommonVoiceArea());
        polyVaSync = new VoiceAreaSynchronizer(patch.getPolyVoiceArea());
    }

    private class VoiceAreaSynchronizer
        implements ModuleContainerListener, ModuleListener, 
        ParameterValueChangeListener, ConnectionListener
    {
        
        private VoiceArea va;
        
        public VoiceAreaSynchronizer(VoiceArea va)
        {
            this.va = va;
            install();
        }

        public void install()
        {
            va.getConnectionManager().addConnectionListener(this);
            va.addModuleContainerListener(this);
            for (Module m: va)
                install(m);
        }

        public void uninstall()
        {
            va.getConnectionManager().removeConnectionListener(this);
            va.removeModuleContainerListener(this);
            for (Module m: va)
                uninstall(m);
        }

        private void install(Module m)
        {
            m.addModuleListener(this);
            ModuleDescriptor md = m.getDescriptor();
            for (int i=0;i<md.getParameterCount();i++)
            {
                try
                {
                    Parameter p = m.getParameter(md.getParameterDescriptor(i));
                    install(p);
                }
                catch (InvalidDescriptorException e)
                {
                    // TODO
                    e.printStackTrace();
                }
            }
        }

        private void uninstall(Module m)
        {
            m.removeModuleListener(this);
            ModuleDescriptor md = m.getDescriptor();
            for (int i=0;i<md.getParameterCount();i++)
            {
                try
                {
                    Parameter p = m.getParameter(md.getParameterDescriptor(i));
                    uninstall(p);
                }
                catch (InvalidDescriptorException e)
                {
                    // TODO
                    e.printStackTrace();
                }
            }
        }

        private void install(Parameter p)
        {
            p.addParameterValueChangeListener(this);
        }

        private void uninstall(Parameter p)
        {
            p.removeParameterValueChangeListener(this);
        }
        
        public void moduleAdded(ModuleContainerEvent e)
        {
            NMModule module = (NMModule) e.getModule();
            
            install(module);
            
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
            NMModule module = (NMModule) e.getModule();
            
            uninstall(module);
            try
            {
            synth.getProtocol().send(NmUtils.createDeleteModuleMessage(slot.getPatchId(), module));
            }
            catch (Exception er)
            {
                // TODO handle error
            }
        }

        public void moduleMoved(ModuleEvent e)
        {
            try
            {
                MidiMessage message =
                    NmUtils.createMoveModuleMessage((NMModule)e.getModule(), slot.getSlotIndex(), 
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
            Parameter parameter = e.getParameter();

            try
            {
                MidiMessage message =
                    NmUtils.createParameterChangedMessage((NMParameter) parameter, 
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
                MidiMessage message =
                NmUtils.createNewCableMessage(va, 
                        (NMConnector) e.getSource(), 
                        (NMConnector) e.getDestination(),
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
                synth.getProtocol().send(
                        NmUtils.createDeleteCableMessage(va, 
                                (NMConnector) e.getSource(), 
                                (NMConnector) e.getDestination(),
                        slot.getSlotId(), slot.getPatchId()
                ));
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
        
    }
    
}
