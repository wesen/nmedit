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

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice.Info;

import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.nomad.synth.DeviceIOException;
import net.sf.nmedit.nomad.synth.SynthDevice;
import net.sf.nmedit.nomad.synth.nord.protocol.Protocol;

public abstract class AbstractNordModularDevice extends SynthDevice
{

    private Slot[] slots ;
    private int activeSlotID = 0;
    private Protocol protocol = null;
    
    private List<SlotListener> slotListenerList
        = new ArrayList<SlotListener>();
    
    public AbstractNordModularDevice()
    {
        slots = new Slot[getSlotCount()];
        for (int i=0;i<slots.length;i++)
            slots[i] = new Slot(i, this);
    }
    
    public Slot getSlot(int slotID)
    {
        return slots[slotID];
    }
    
    public int getActiveSlotID()
    {
        return activeSlotID;
    }
    
    void setActiveSlotID(int slotID)
    {
        if (slotID<0||slotID>=slots.length)
            throw new IllegalArgumentException("Slot ID out of range: "+slotID);
        
        this.activeSlotID = slotID;
    }
    
    public abstract int getSlotCount();

    @Override
    public String getVendor()
    {
        return "Clavia";
    }

    @Override
    public String getVersion()
    {
        // TODO return correct version
        return getName()+" 3.01";
    }

    void send( MidiMessage message )
        throws DeviceIOException
    {
        if (!isConnected())
            throw new DeviceIOException(this, "Not connected");
        
        try
        {
            protocol.send(message);
        }
        catch (Exception e)
        {
            throw new DeviceIOException(this, e);
        }
    }

    abstract Protocol createProtocol()
        throws DeviceIOException;

    @Override
    protected void connect( Info midiIn, Info midiOut ) throws DeviceIOException
    {
        try
        {
            protocol = createProtocol();
            protocol.addListener(new NmMessageDispatcher(this));
            protocol.start(midiIn, midiOut);
            protocol.send(new IAmMessage());
        }
        catch (DeviceIOException e)
        {
            if (protocol!=null)
                protocol.stop();
            protocol = null;
            throw e;
        }
        catch (Exception e)
        {
            if (protocol!=null)
                protocol.stop();
            protocol = null;
            throw new DeviceIOException(this,e);
        }
    }

    @Override 
    protected void disconnect()
    {
        if (protocol!=null)
        {
            protocol.stop();
            protocol = null;
        }
    }

    public void addSlotListener( SlotListener l )
    {
        if (!slotListenerList.contains(l))
            slotListenerList.add(l);
    }
    
    public void removeSlotListener( SlotListener l )
    {   
        slotListenerList.remove(l);
    }

    public void fireSlotSelectedMessage( Slot slot )
    {
        for (int i=slotListenerList.size()-1;i>=0;i--)
            slotListenerList.get(i).slotSelected(slot);
    }

    public void fireNewPatchInSlot( Slot slot )
    {
        for (int i=slotListenerList.size()-1;i>=0;i--)
            slotListenerList.get(i).newPatchInSlot(slot);
    }
    
    
}
