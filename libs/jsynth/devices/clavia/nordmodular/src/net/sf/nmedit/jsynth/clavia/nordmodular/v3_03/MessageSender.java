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
 * Created on Jun 29, 2006
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.v3_03;

import net.sf.nmedit.jnmprotocol.DeleteCableMessage;
import net.sf.nmedit.jnmprotocol.DeleteModuleMessage;
import net.sf.nmedit.jnmprotocol.GetPatchMessage;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.MoveModuleMessage;
import net.sf.nmedit.jnmprotocol.NewCableMessage;
import net.sf.nmedit.jnmprotocol.NewModuleMessage;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.VoiceAreaEvent;
import net.sf.nmedit.jsynth.SynthException;

/**
 * Sends changes made to the patch to the synthesizer. 
 * 
 * @author Christian Schneider
 */
public class MessageSender
{
    
    private final NordModular device;
    private final Slot slot;
    private Patch patch;
    private VoiceAreaEventBroadcaster vaBroadcaster;
    private ModuleEventBroadcaster modBroadcaster;
    private ParameterEventBroadcaster paBroadcaster;

    MessageSender(Slot slot)
    {
        this.slot = slot;
        this.device = slot.getDevice();
        this.patch = null;
        this.vaBroadcaster = new VoiceAreaEventBroadcaster();
        this.modBroadcaster = new ModuleEventBroadcaster();
        this.paBroadcaster = new ParameterEventBroadcaster();
    }
    
    public NordModular getDevice()
    {
        return device;
    }
    
    public boolean isInstalled()
    {
        return patch != null;
    }
    
    public void install(Patch patch)
    {
        if (isInstalled()) throw new IllegalStateException(getClass().getSimpleName()+" can only be installed to a single patch");
        if (patch == null) throw new NullPointerException("patch");
        this.patch = patch;

        for (Module m : patch.getPolyVoiceArea()) 
            installListeners(m);
        for (Module m : patch.getCommonVoiceArea())
            installListeners(m);
        patch.getPolyVoiceArea().addListener(vaBroadcaster);
        patch.getCommonVoiceArea().addListener(vaBroadcaster);
    }

    public void uninstall()
    {
        if (!isInstalled()) throw new IllegalStateException(getClass().getSimpleName()+" has not installed");

        for (Module m : patch.getPolyVoiceArea())
            uninstallListeners(m);
        for (Module m : patch.getCommonVoiceArea())
            uninstallListeners(m);
        patch.getPolyVoiceArea().removeListener(vaBroadcaster);
        patch.getCommonVoiceArea().removeListener(vaBroadcaster);
        patch = null;
    }

    private void installListeners(Module m)
    {
        m.addListener(modBroadcaster);
        for (int i=m.getParameterCount()-1;i>=0;i--)
            m.getParameter(i).addListener(paBroadcaster);
    }
    
    private void uninstallListeners(Module m)
    {
        m.removeListener(modBroadcaster);
        for (int i=m.getParameterCount()-1;i>=0;i--)
            m.getParameter(i).removeListener(paBroadcaster);
    }
    
    private MidiMessage createMessage(Class<? extends MidiMessage> messageClass)
    {
        try
        {
            MidiMessage message = messageClass.newInstance();
            message.set("slot", slot.getID());
            message.set("pid", slot.getPID());
            return message;
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

    private void assureIsInstalled()
    {
        if (!isInstalled())
        {
            try
            {
                throw new SynthException("No patch installed");
            }
            catch (SynthException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void sendPatchMessageInternal() throws Exception
    {
        assureIsInstalled();
        PatchMessage msg = NordUtilities.generatePatchMessage(patch, slot.getID());
        send(msg);
    }
    
    public void sendPatchMessage()
    {
        try
        {
            sendPatchMessageInternal();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendRequestPatchMessageInternal() throws Exception
    {
        RequestPatchMessage msg = new RequestPatchMessage();   
        msg.set("slot", slot.getID());    
        send(msg);  
    }

    public void sendRequestPatchMessage()
    {
        try 
        {
            sendRequestPatchMessageInternal();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void sendGetPatchMessage()
    {
        // get message instance
        GetPatchMessage msg = (GetPatchMessage) createMessage(GetPatchMessage.class);
        if (msg == null) return;

        // send message
        send(msg);
    }
    
    private void sendNewModule( VoiceAreaEvent event )
    {
        if (false) return ; // do not send message at the moment
        
        // get message instance
        NewModuleMessage msg = (NewModuleMessage) createMessage(NewModuleMessage.class);
        if (msg == null) return;

        // get data
        Module module = event.getModule();
        int section = Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea());
        
        // install listeners
        installListeners(module);

        // set data
        msg.newModule
        (
            section, 
            module.getIndex(), 
            module.getID(), module.getX(), 
            module.getY()      
        );
        
        // send message
        send(msg);
    }

    private void sendDeleteModule( VoiceAreaEvent event )
    {
        // get message instance
        DeleteModuleMessage msg = (DeleteModuleMessage) createMessage(DeleteModuleMessage.class);
        if (msg == null) return;

        // get data
        Module module = event.getModule();
        int section = Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea());
        
        // uninstall listeners
        uninstallListeners(module);
        
        // set data
        msg.deleteModule
        (
            section, 
            module.getIndex()
        );
        
        // send message
        send(msg);
    }

    private void sendDeleteCable( VoiceAreaEvent event )
    {
        // get message instance
        DeleteCableMessage msg = (DeleteCableMessage) createMessage(DeleteCableMessage.class);
        if (msg == null) return;

        // get data
        Connector src = event.getSrc();
        Connector dst = event.getDst();
        int section = Format.getVoiceAreaID(event.getVoiceArea().isPolyVoiceArea());

        // set data
        msg.deleteCable
        (
            section,  
            
            dst.getModule().getIndex(), 
            Format.getOutputID(dst.isOutput()),
            dst.getID(),
            
            src.getModule().getIndex(), 
            Format.getOutputID(src.isOutput()),
            src.getID()
        );
        
        // send message
        send(msg);
    }

    private void sendNewCable( VoiceAreaEvent event )
    {
        // get message instance
        NewCableMessage msg = (NewCableMessage) createMessage(NewCableMessage.class);
        if (msg == null) return;

        // get data
        Connector src = event.getSrc();
        Connector dst = event.getDst();
        int section = Format.getVoiceAreaID(event.getVoiceArea().isPolyVoiceArea());
        int color = src.getConnectionColor().getSignalID();
        
        // set data
        msg.newCable
        (
            section,
            color, 
            
            dst.getModule().getIndex(), 
            Format.getOutputID(dst.isOutput()),
            dst.getID(),
            
            src.getModule().getIndex(), 
            Format.getOutputID(src.isOutput()),
            src.getID()
        );
        
        // send message
        send(msg);
    }

    private void sendMovedModule( ModuleEvent event )
    {
        // get message instance
        MoveModuleMessage msg = (MoveModuleMessage) createMessage(MoveModuleMessage.class);
        if (msg == null) return;

        // get data
        Module module = event.getModule();
        
        // set data
        msg.moveModule
        (
            Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea()),
            module.getIndex(), 
            module.getX(), 
            module.getY()
        );

        // send message
        send(msg);
    }

    private void sendParameterChanged( ParameterEvent event )
    {
        // get message instance
        ParameterMessage msg = (ParameterMessage) createMessage(ParameterMessage.class);
        if (msg == null) return;

        // get data
        Parameter parameter = event.getParameter();
        Module module = parameter.getModule();
        
        // set data
        msg.set("module", module.getIndex());
        msg.set("section", Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea()));
        msg.set("parameter", parameter.getDefinition().getContextId());
        msg.set("value", parameter.getValue());

        // send message
        send(msg);
    }
    
    private void send(MidiMessage msg)
    {
        try
        {
            device.send(msg);
        }
        catch (SynthException e)
        {
            e.printStackTrace();
        }
    }

    private class VoiceAreaEventBroadcaster implements EventListener<VoiceAreaEvent>
    {
        public void event( VoiceAreaEvent event )
        {
            switch (event.getID())
            {
                case VoiceAreaEvent.VA_MODULE_ADDED:
                {
                    sendNewModule(event);
                }
                break;
                case VoiceAreaEvent.VA_MODULE_REMOVED:
                {
                    sendDeleteModule(event);
                }
                break;
                case VoiceAreaEvent.VA_CONNECTED:
                {
                    sendNewCable(event);
                }
                break;
                case VoiceAreaEvent.VA_DISCONNECTED:
                {
                    sendDeleteCable(event);
                }
                break;
            }
        }
    }
    
    private class ModuleEventBroadcaster implements EventListener<ModuleEvent>
    {
        public void event( ModuleEvent event )
        {
            switch (event.getID())
            {
                case ModuleEvent.MODULE_MOVED:
                {
                    sendMovedModule(event);
                }                
                break;
                /* case ModuleEvent.MODULE_RENAMED:
                {
                    sendModuleRenamed(event);
                }                
                break; */
            }
        }
    }
    
    private class ParameterEventBroadcaster implements EventListener<ParameterEvent> 
    {

        public void event( ParameterEvent event )
        {
            switch (event.getID())
            {
                case ParameterEvent.PARAMETER_VALUE_CHANGED:
                {
                    sendParameterChanged(event);
                }
                break;
            }
        }
        
    }
    
}
