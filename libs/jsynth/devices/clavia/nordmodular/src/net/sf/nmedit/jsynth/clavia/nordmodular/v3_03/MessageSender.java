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
import net.sf.nmedit.jnmprotocol.MidiException;
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
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.AssignmentChangeListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.HeaderListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.MorphListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.PatchListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.VoiceAreaListener;
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
    private Broadcast Broadcast;

    MessageSender(Slot slot)
    {
        this.slot = slot;
        this.device = slot.getDevice();
        this.patch = null;
        this.Broadcast = new Broadcast();
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

        Broadcast.install(patch);
    }

    public void uninstall()
    {
        if (!isInstalled()) throw new IllegalStateException(getClass().getSimpleName()+" has not installed");

        Broadcast.uninstall(patch);
        patch = null;
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
    
    private void sendNewModule( Module module )
    {
        // if (false) return ; // do not send message at the moment
        
        // get message instance
        NewModuleMessage msg = null;// (NewModuleMessage) createMessage(NewModuleMessage.class);
        try
        {
            msg = new NewModuleMessage();
            msg.set("pid", slot.getPID());
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        
        if (msg == null) return;

        // get data
        int section = Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea());
        
        // set data
        try
        {
            /*
            System.out.println
            (
                    "module-id:"+module.getID()+
                    " section:"+section+
                    " index:"+module.getIndex()+
                    " x:"+module.getX()+ 
                    " y:"+module.getY()+
                    " name:"+module.getName()+
                    " param:"+int2str(module.getParameterValues())+
                    " custom:"+int2str(module.getCustomValues())        
            );
            */
            
            msg.newModule 
            (
                module.getID(),
                section, 
                module.getIndex(),
                module.getX(), 
                module.getY(),
                "",// module.getName(),
                module.getParameterValues(),
                module.getCustomValues()
            );
        }
        catch (MidiException e)
        {
            e.printStackTrace();
            return ; 
        }
        
        // send message
        send(msg);
    }
    
    private String int2str(int[] array)
    {
        StringBuffer sb = new StringBuffer("{");
        for (int i=0;i<array.length;i++)
        {
            sb.append(Integer.toString(array[i]));
            if (i<array.length-1)
                sb.append(',');
        }
        sb.append('}');
        return sb.toString();
    }

    private void sendDeleteModule( Module module )
    {
        // get message instance
        DeleteModuleMessage msg = (DeleteModuleMessage) createMessage(DeleteModuleMessage.class);
        if (msg == null) return;

        // get data
        int section = Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea());
        
        // set data
        msg.deleteModule
        (
            section, 
            module.getIndex()
        );
        
        // send message
        send(msg);
    }

    private void sendDeleteCable( VoiceArea va, Connector a, Connector b )
    {
        // get message instance
        DeleteCableMessage msg = (DeleteCableMessage) createMessage(DeleteCableMessage.class);
        if (msg == null) return;

        // get data
        Connector src = a;
        Connector dst = b;
        int section = Format.getVoiceAreaID(va.isPolyVoiceArea());

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

    private void sendNewCable( VoiceArea va, Connector a, Connector b )
    {
        // get message instance
        NewCableMessage msg = (NewCableMessage) createMessage(NewCableMessage.class);
        if (msg == null) return;

        // get data
        Connector src = a;
        Connector dst = b;
        int section = Format.getVoiceAreaID(va.isPolyVoiceArea());
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

    private void sendMovedModule( Module module )
    {
        // get message instance
        MoveModuleMessage msg = (MoveModuleMessage) createMessage(MoveModuleMessage.class);
        if (msg == null) return;

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
    
    private void sendParameterChanged( Parameter parameter )
    {
        // get message instance
        ParameterMessage msg = (ParameterMessage) createMessage(ParameterMessage.class);
        if (msg == null) return;

        // get data
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

    private class Broadcast implements 
        HeaderListener, ModuleListener, MorphListener,
        ParameterListener, PatchListener, VoiceAreaListener,
        AssignmentChangeListener
    {

        public void install( Patch patch )
        {
            patch.addPatchListener(this);
            patch.getHeader().addHeaderListener(this);
            for (Morph m : patch.getMorphs())
                m.addMorphListener(this);
            patch.getMidiControllers().addAssignmentChangeListener(this);
            patch.getKnobs().addAssignmentChangeListener(this);
            install(patch.getPolyVoiceArea());
            install(patch.getCommonVoiceArea());
        }

        public void uninstall( Patch patch )
        {
            patch.removeListener(this);
            patch.getHeader().removeListener(this);
            for (Morph m : patch.getMorphs())
                m.removeMorphListener(this);
            patch.getMidiControllers().removeAssignmentChangeListener(this);
            patch.getKnobs().removeAssignmentChangeListener(this);
            uninstall(patch.getPolyVoiceArea());
            uninstall(patch.getCommonVoiceArea());
        }

        private void install( VoiceArea va )
        {
            va.addVoiceAreaListener(this);
            for (Module m : va)
                install(m);
        }

        private void uninstall( VoiceArea va )
        {
            va.removeVoiceAreaListener(this);
            for (Module m : va)
                uninstall(m);
        }
        
        private void install( Module m )
        {
            m.addModuleListener(this);
            for (int i=0;i<m.getParameterCount();i++)
                m.getParameter(i).addParameterListener(this);
            for (int i=0;i<m.getCustomCount();i++)
                m.getParameter(i).addParameterListener(this);
        }

        private void uninstall( Module m )
        {
            m.removeModuleListener(this);
            for (int i=0;i<m.getParameterCount();i++)
                m.getParameter(i).removeParameterListener(this);
            for (int i=0;i<m.getCustomCount();i++)
                m.getParameter(i).removeParameterListener(this);
        }

        /*public void connectorStateChanged( Event e )
        {
            history.setChanged(true);
        }*/

        public void headerValueChanged( Event e )
        {
            
        }

        public void moduleRenamed( Event e )
        {
            
        }

        public void moduleMoved( Event e )
        {
            sendMovedModule(e.getModule());
        }

        public void morphAssigned( Event e )
        {
            
        }

        public void morphDeassigned( Event e )
        {
            
        }

        public void morphKeyboardAssignmentChanged( Event e )
        {
            
        }

        public void morphValueChanged( Event e )
        {
            
        }

        public void parameterValueChanged( Event e )
        {
            //FIXME: can be custom param
            sendParameterChanged(e.getParameter());
        }

        public void parameterMorphValueChanged( Event e )
        {
            
        }

        public void parameterKnobAssignmentChanged( Event e )
        {
            
        }

        public void parameterMorphAssignmentChanged( Event e )
        {
            
        }

        public void parameterMidiCtrlAssignmentChanged( Event e )
        {

        }

        public void patchHeaderChanged( Event e )
        {
            
        }

        public void patchPropertyChanged( Event e )
        {
            if (Patch.NAME.equals(e.getPropertyName()))
            {
                
            }
            else if (Patch.NOTE.equals(e.getPropertyName()))
            {
                
            }   
            else
            {
                return ;
            }
        }

        public void moduleAdded( Event e )
        {
            install(e.getModule());
            sendNewModule(e.getModule());
        }

        public void moduleRemoved( Event e )
        {
            uninstall(e.getModule());
            sendDeleteModule(e.getModule());
        }

        public void voiceAreaResized( Event e )
        {
            // nothing to send
        }

        public void cablesAdded( Event e )
        {
            sendNewCable(e.getVoiceArea(), e.getConnector1(), e.getConnector2());
        }

        public void cablesRemoved( Event e )
        {
            sendDeleteCable(e.getVoiceArea(), e.getConnector1(), e.getConnector2());
        }

        public void assignmentChanged( Event e )
        {
            
        }

        public void cableGraphUpdated( Event e )
        {
            // TODO Auto-generated method stub
            
        }
        
    }

}
