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

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jsynth.event.SynthStateChangeEvent;
import net.sf.nmedit.jsynth.event.SynthStateListener;

public class Slot implements SynthStateListener
{

    private final int ID;
    private final NordModular device ;
    private Patch patch;
    private int pID;
    private int voiceCount = 0;
    private boolean selected = false;
    private MessageSender messageSender;

    Slot(int ID, NordModular device)
    {
        this.ID = ID;
        this.device = device;
        device.addSynthStateListener(this);
        // must be initialized after device was set
        this.messageSender = new MessageSender(this);
        
        this.patch = (Patch) device.getPatchImplementation().createPatch();
        pID = -1;
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
            if (messageSender.isInstalled())
            {
                messageSender.uninstall();
            }
            this.patch = patch;
            if (patch!=null) 
            {
                messageSender.install(patch);
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

    public void setVoiceCount( int vc )
    {
        this.voiceCount  = vc;
    }
    
    public int getVoiceCount()
    {
        return voiceCount;
    }
    
    public void sendPatchMessage()
    {
        messageSender.sendPatchMessage();     
    }

    public void sendRequestPatchMessage()
    {
        messageSender.sendRequestPatchMessage();
    }

    public void sendGetPatchMessage()
    {
        setPatch((Patch) device.getPatchImplementation().createPatch());
        messageSender.sendGetPatchMessage();
    }

    public void synthStateChanged( SynthStateChangeEvent e )
    {
        if (device.isConnected())
        {
            if (!messageSender.isInstalled())
            {
                if (patch!=null)
                    messageSender.install(patch);
            }
        }
        else
        {
            if (messageSender.isInstalled())
            {
                messageSender.uninstall();
            }
        }
    }

}
