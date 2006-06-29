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
package net.sf.nmedit.jsynth.clavia.nordmodular.v3_03;

import net.sf.nmedit.jnmprotocol.DeleteCableMessage;
import net.sf.nmedit.jnmprotocol.DeleteModuleMessage;
import net.sf.nmedit.jnmprotocol.GetPatchMessage;
import net.sf.nmedit.jnmprotocol.MidiException;
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

public class Slot
{

    private final int ID;
    private final NordModular device ;
    private Patch patch;
    private int pID;
    private AreaListener al = new AreaListener();
    private ParameterListener pl = new ParameterListener();
    private ParameterMessage parameterMessage = null;
    private int voiceCount = 0;
    private boolean selected = false;
    private ModuleListener ml = new ModuleListener();

    Slot(int ID, NordModular device)
    {
        this.ID = ID;
        this.device = device;
        this.patch = (Patch) device.getPatchImplementation().createPatch();
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
    
    public final NordModular getDevice()
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
        m.addListener(ml);
        for (int i=0;i<m.getParameterCount();i++)
            m.getParameter(i).addListener(pl);
    }
    
    private void uninstall(Module m)
    {
        m.removeListener(ml);
        for (int i=0;i<m.getParameterCount();i++)
            m.getParameter(i).removeListener(pl);
    }

    private class ModuleListener implements EventListener<ModuleEvent>
    {

        public void event( ModuleEvent event )
        {
            switch (event.getID())
            {
                case ModuleEvent.MODULE_MOVED:
                {
                    //if (isPatchInitialized())
                    {
                        try
                        {
                            MoveModuleMessage msg = new MoveModuleMessage();
                            Module m = event.getModule();

                            msg.set("slot", ID);
                            msg.set("pid", pID);
                            
                            msg.moveModule(Format.getVoiceAreaID(m.getVoiceArea().isPolyVoiceArea()),
                                    m.getIndex(), m.getX(), m.getY());

                            device.send(msg);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        
                    }
                }
            }
        }
        
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
                    
                    //if (isPatchInitialized())
                    {
                        //synchNewModule(event.getModule());
                    }
                }
                break;
                    
                case VoiceAreaEvent.VA_MODULE_REMOVED:
                {
                    uninstall(event.getModule());
                    //synchsafe();
                    
                    //if (isPatchInitialized())
                    {
                        synchRemoveModule(event.getModule());
                    }
                }
                break;

                case VoiceAreaEvent.VA_CONNECTED:
                {
                    NewCableMessage message;
                    try
                    {
                        message = new NewCableMessage();
                        message.set("slot", getID());
                        message.set("pid", pID);

                        Connector src = event.getSrc();
                        Connector dst = event.getDst();
                        int section = Format.getVoiceAreaID(event.getVoiceArea().isPolyVoiceArea());
                        int color = src.getConnectionColor().getSignalID();
                        message.newCable(section, color, 
                                dst.getModule().getIndex(), 
                                Format.getOutputID(dst.isOutput()),
                                dst.getID(),
                                src.getModule().getIndex(), 
                                Format.getOutputID(src.isOutput()),
                                src.getID());
                        
                        device.send(message);
                        
                    }
                    catch (Exception e)
                    {
                        // TODO  
                        e.printStackTrace();
                    }
                }
                break;
                case VoiceAreaEvent.VA_DISCONNECTED:
                {
                    try
                    {
                    DeleteCableMessage message = new DeleteCableMessage();
                    message.set("slot", getID());
                    message.set("pid", pID);

                    Connector src = event.getSrc();
                    Connector dst = event.getDst();
                    int section = Format.getVoiceAreaID(event.getVoiceArea().isPolyVoiceArea());
                    message.deleteCable(section,  
                            dst.getModule().getIndex(), 
                            Format.getOutputID(dst.isOutput()),
                            dst.getID(),
                            src.getModule().getIndex(), 
                            Format.getOutputID(src.isOutput()),
                            src.getID());
                    
                    device.send(message);
                    
                }
                catch (Exception e)
                {
                    // TODO  
                    e.printStackTrace();
                }
                }
                break;
            }
        }
        
    }

    private void synchNewModule( Module module )
    {
        NewModuleMessage msg;
        try
        {
            msg = new NewModuleMessage();
        }
        catch (Exception e)
        {
            System.out.println("Could not create NewModuleMessage");
            e.printStackTrace();
            return ;
        }

        msg.set("slot", getID());
        msg.set("pid", pID);
        
        // voice area
        int va = Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea());
        
        msg.newModule(
           va, module.getIndex(), module.getID(), module.getX(), module.getY()      
        );
        
        System.out.println
        ("newModule: voice area:"+va+" module-index:"+module.getIndex()+" module-id:"+module.getID()+" module.x:"+
                module.getX()+" module.y:"+module.getY());
        /*
        newModule: voice area:1 module-id:1 module.x:0 module.y:0
        Ignore Message: parse failed:  240 51 80 6 4 126 5 0 247
        newModule: voice area:1 module-id:1 module.x:0 module.y:2
        Ignore Message: parse failed:  240 51 88 6 5 127 5 247
*/
        try
        {
            device.send(msg);
        }
        catch (SynthException e)
        {
            e.printStackTrace();
        }
    }

    private void synchRemoveModule( Module module )
    {
        DeleteModuleMessage msg;
        try
        {
            msg = new DeleteModuleMessage();
        }
        catch (Exception e)
        {
            System.out.println("Could not create DeleteModuleMessage");
            e.printStackTrace();
            return ;
        }
        
        msg.set("slot", getID());
        msg.set("pid", pID);

        // voice area
        int va = Format.getVoiceAreaID(module.getVoiceArea().isPolyVoiceArea());
        msg.deleteModule(va, module.getIndex());

        try
        {
            device.send(msg);
        }
        catch (SynthException e)
        {
            e.printStackTrace();
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
        +", Device="+device.getInfo().getName()
        +", Version="+device.getInfo().getVersion()
        +", Vendor="+device.getInfo().getVendor()+"]";            
    }

    public String getName()
    {
        return "Slot "+((char)(ID+'A'));
    }
    
    public void synchronize()
        throws SynthException
    {
        if (patch == null)
            throw new SynthException("No patch in slot "+toString());
        
        PatchMessage message;
        try
        {
            message =
            NordUtilities.generatePatchMessage(patch, ID);    
        }
        catch (Exception e)
        {
            throw new SynthException(e);
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
       // packetCount = 0;
        setPatch((Patch) device.getPatchImplementation().createPatch());
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
/*
    public void signalPacketReceived()
    {
        packetCount ++;
    }

    public boolean canUpdateSlot()
    {
        return packetCount<13;
    }*/
    
}
