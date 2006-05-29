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
 * Created on May 17, 2006
 */
package net.sf.nmedit.nomad.synth.nord;

import net.sf.nmedit.jnmprotocol.GetPatchMessage;
import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.nomad.patch.Format;
import net.sf.nmedit.nomad.patch.virtual.Module;
import net.sf.nmedit.nomad.patch.virtual.Parameter;
import net.sf.nmedit.nomad.patch.virtual.Patch;
import net.sf.nmedit.nomad.patch.virtual.event.EventListener;
import net.sf.nmedit.nomad.patch.virtual.event.ParameterEvent;
import net.sf.nmedit.nomad.patch.virtual.event.VoiceAreaEvent;
import net.sf.nmedit.nomad.synth.DeviceIOException;

public class Slot
{

    private final int ID;
    private final AbstractNordModularDevice device ;
    private Patch patch;
    private int pID;
    private AreaListener al = new AreaListener();
    private ParameterListener pl = new ParameterListener();
    private ParameterMessage parameterMessage = null;
    private int voiceCount = 0;
    private boolean selected = false;
    private int packetCount = 0;

    Slot(int ID, AbstractNordModularDevice device)
    {
        this.ID = ID;
        this.device = device;
        this.patch = new Patch();
        pID = -1;

        try {
            parameterMessage = new ParameterMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean isSelected()
    {
        return selected;
    }
  
    public void setSelected(boolean a)
    {
        this.selected = a;
        if (device.getActiveSlotID()<0)
        {
            device.setActiveSlotID(ID);
        }
    }

    public final int getID()
    {
        return ID;
    }
    
    public final AbstractNordModularDevice getDevice()
    {
        return device;
    }

    void setPID(int pID)
    {
        this.pID = pID;
    }
    
    public int getPID()
    {
        return pID;
    }

    public Patch getPatch()
    {
        return patch;
    }
 
    public void setPatch(Patch patch)
    {
        if (this.patch!=patch)
        {
            uninstall();
            this.patch = patch;
            install();
        }
    }
    
    private void uninstall()
    {
        if (patch!=null) {
            for (Module m : patch.getPolyVoiceArea())
                uninstall(m);
            for (Module m : patch.getCommonVoiceArea())
                uninstall(m);
            patch.getPolyVoiceArea().removeListener(al);
            patch.getCommonVoiceArea().removeListener(al);
        }
    }

    private void install()
    {
        if (patch!=null) {
            for (Module m : patch.getPolyVoiceArea())
                install(m);
            for (Module m : patch.getCommonVoiceArea())
                install(m);
            patch.getPolyVoiceArea().addListener(al);
            patch.getCommonVoiceArea().addListener(al);
        }
    }

    private void install(Module m)
    {
        for (int i=0;i<m.getParameterCount();i++)
            m.getParameter(i).addListener(pl);
    }
    
    private void uninstall(Module m)
    {
        for (int i=0;i<m.getParameterCount();i++)
            m.getParameter(i).removeListener(pl);
    }

    private class AreaListener implements EventListener<VoiceAreaEvent>
    {
        public void event( VoiceAreaEvent event )
        {
            switch (event.getID())
            {
                case VoiceAreaEvent.VA_MODULE_ADDED:
                {
                    install(event.getModule());
                    //synchsafe();
                }
                break;
                    
                case VoiceAreaEvent.VA_MODULE_REMOVED:
                {
                    uninstall(event.getModule());
                    //synchsafe();
                }
                break;

                case VoiceAreaEvent.VA_CONNECTED:
                case VoiceAreaEvent.VA_DISCONNECTED:
                {
                    //synchsafe();
                }
                break;
            }
        }
        
    }
    
    /*private void synchsafe()
    {
        if (packetCount==13)
        {
                try
                {
                    synchronize();
                }
                catch (DeviceIOException e)
                {
                    e.printStackTrace();
                }
        }
    }*/
    
    private class ParameterListener implements EventListener<ParameterEvent> 
    {

        public void event( ParameterEvent event )
        {
            Parameter parameter = event.getParameter();
            Module module = parameter.getModule();
            
            parameterMessage.set("slot", ID);
            parameterMessage.set("pid", pID);
            parameterMessage.set("module", module.getIndex());
            parameterMessage.set("section", Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea()));
            parameterMessage.set("parameter",parameter.getDefinition().getContextId());
            parameterMessage.set("value",parameter.getValue());
            try {
                device.send(parameterMessage);
            } catch (Exception e) {
                //System.err.println(e);
                e.printStackTrace();
                
                if (e instanceof MidiException) {
                    System.err.println("MidiException:code="+((MidiException)e).getError());
                }
            }
        }
        
    }
    
    public String toString()
    {
        return "Slot[ID="+ID
        +", Device="+device.getName()
        +", Version="+device.getVersion()
        +", Vendor="+device.getVendor()+"]";            
    }

    public String getName()
    {
        return "Slot "+((char)(ID+'A'));
    }
    
    public void synchronize()
        throws DeviceIOException
    {
        if (patch == null)
            throw new DeviceIOException(device, "No patch in slot "+toString());
        
        PatchMessage message;
        try
        {
            message =
            NordUtilities.generatePatchMessage(patch, ID);    
        }
        catch (Exception e)
        {
            throw new DeviceIOException(device, e);
        }
        
        device.send(message);        
    }

    public void setVoiceCount( int vc )
    {
        this.voiceCount  = vc;
    }
    
    public int getVoiceCount()
    {
        return voiceCount;
    }

    public void synchRequestPatch()
    {
        RequestPatchMessage requestPatchMessage;
        try 
        {
            requestPatchMessage = new RequestPatchMessage();
            requestPatchMessage.set("slot", ID);
            device.send(requestPatchMessage);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void synchGetPatch()
    {
        packetCount = 0;
        setPatch(new Patch());
        device.fireNewPatchInSlot(this);
        GetPatchMessage msg;
        try {
            msg = new GetPatchMessage();
            msg.set("slot", ID);
            msg.set("pid", pID);
            device.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signalPacketReceived()
    {
        packetCount ++;
    }

    public boolean canUpdateSlot()
    {
        return packetCount<13;
    }
    
}
