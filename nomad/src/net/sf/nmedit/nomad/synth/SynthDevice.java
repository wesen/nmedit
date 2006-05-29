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
package net.sf.nmedit.nomad.synth;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;

public abstract class SynthDevice
{

    private MidiDevice.Info midiIn = null;
    private MidiDevice.Info midiOut = null;
    private boolean connected = false;

    public abstract String getName();
    public abstract String getVendor();
    public abstract String getVersion();
    
    private List<SynthDeviceStateListener> connectionListenerList
        = new ArrayList<SynthDeviceStateListener>();
    
    public void setIn(MidiDevice.Info in)
    {
        if (isConnected())
            throw new IllegalStateException("Cannot set MIDI device while connected");
        
        this.midiIn = in;
    }
    
    public void setOut(MidiDevice.Info out)
    {
        if (isConnected())
            throw new IllegalStateException("Cannot set MIDI device while connected");
        
        this.midiOut = out;
    }
    
    public MidiDevice.Info getIn()
    {
        return midiIn;
    }
    
    public MidiDevice.Info getOut()
    {
        return midiOut;
    }

    public boolean isConnected()
    {
        return connected;
    }
    
    public void setConnected(boolean connect) throws DeviceIOException
    {
        if (this.connected == connect)
            return ;
        
        if (connect)
        {
            
            if (midiIn == null)
                throw new DeviceIOException(this, "MIDI input device not set.");
            
            if (midiOut == null)
                throw new DeviceIOException(this, "MIDI output device not set.");
            
            try
            {
                connect(midiIn, midiOut);
            }
            catch (DeviceIOException e)
            {
                throw e;
            }
            
            this.connected = true;
            fireConnectionStateChanged();
        }
        else
        {
            disconnect();
            this.connected = false;
            fireConnectionStateChanged();
        }
    }
    
    protected abstract void connect(Info midiIn, Info midiOut)
        throws DeviceIOException;
    
    protected abstract void disconnect();

    public String toString()
    {
        return SynthDevice.class.getName()
            +"[Name="+getName()
            +", Version="+getVersion()
            +", Vendor="+getVendor()
            +"]";
    }
    
    public void addStateListener( SynthDeviceStateListener l )
    {
        if (!connectionListenerList.contains(l))
            connectionListenerList.add(l);
    }
    
    public void removeStateListener( SynthDeviceStateListener l )
    {
        connectionListenerList.remove(l);
    }

    protected void fireConnectionStateChanged()
    {
        for (int i=connectionListenerList.size()-1;i>=0;i--)
            connectionListenerList.get(i)
                .synthConnectionStateChanged(this);
    }

}
