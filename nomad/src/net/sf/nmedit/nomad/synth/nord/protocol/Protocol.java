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
package net.sf.nmedit.nomad.synth.nord.protocol;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice.Info;

import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.nomad.synth.DeviceIOException;
import net.sf.nmedit.nomad.synth.SynthDevice;

public abstract class Protocol
{

    private List<NmProtocolListener> messageListenerList;
    protected MidiDriver driver = null;
    private SynthDevice device;
    
    protected Protocol(SynthDevice device)
    {
        this.device = device;
        messageListenerList = new ArrayList<NmProtocolListener>();
    }
    
    protected SynthDevice getDevice()
    {
        return device;
    }

    protected void notifyListeners(MidiMessage m)
    {
        try
        {
            for (int i=messageListenerList.size()-1;i>=0;i--)
                m.notifyListener(messageListenerList.get(i));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void addListener(NmProtocolListener l)
    {
        if (!messageListenerList.contains(l))
            messageListenerList.add(l);
    }
    
    public void removeListener(NmProtocolListener l)
    {
        messageListenerList.remove(l);
    }
    
    public abstract void send(MidiMessage message) throws Exception;
    
    public void stop()
    {
        driver.disconnect();
        driver = null;
    }

    public void start( Info midiIn, Info midiOut ) throws DeviceIOException
    {
        try
        {
            driver = createDriver();
            driver.connect(midiIn, midiOut);
        }
        catch (Exception e)
        {
            driver = null;
            throw new DeviceIOException(device, e);
        }
    }

    protected MidiDriver createDriver()
        throws Exception
    {
        return new MidiDriver();
    }
    
}
