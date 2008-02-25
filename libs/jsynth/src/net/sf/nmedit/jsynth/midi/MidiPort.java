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
 * Created on Dec 29, 2006
 */
package net.sf.nmedit.jsynth.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import net.sf.nmedit.jsynth.AbstractPort;
import net.sf.nmedit.jsynth.Plug;
import net.sf.nmedit.jsynth.Port;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;

public class MidiPort extends AbstractPort implements Port
{
    
    // MIDI In  = Receiver
    // MIDI Out = Transmitter
    
    public static final String DESCRIPTION = "MIDI Port";
    
    public static enum Type
    {
        RECEIVER,
        TRANSMITTER;
    }

    private MidiPlug mplug = null;
    private String name;
    private Type type;
    
    public MidiPort(Synthesizer synth, Type type, String name)
    {
        super(synth);
        this.type = type;
        this.name = name;
    }

    public Type getType()
    {
        return type;
    }
    
    public MidiPlug getPlug()
    {
        return mplug;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return MidiPort.DESCRIPTION;
    }

    public boolean isPluggable( Plug p )
    {
        if (p==null || ! ( p instanceof MidiPlug ))
            return false;
        MidiPlug mp = (MidiPlug) p;
        
        MidiDevice dev;
        try
        {
            MidiDevice.Info info = mp.getDeviceInfo();
            if (info == null)
                return false;
            
            dev = MidiSystem.getMidiDevice(info);
        }
        catch (MidiUnavailableException e)
        {
            return false;
        }
        
        int maxAvailable = (getType() == Type.RECEIVER) ?
                dev.getMaxTransmitters() : dev.getMaxReceivers();
        
        // -1 = unlimited 
        return maxAvailable == -1 || maxAvailable>0;
    }

    public void setPlug( Plug p ) throws SynthException
    {
        if (p!= null && !isPluggable(p))
            throw new SynthException("Invalid plug "+p+" for "+this+".");
        
        if (mplug != p)
        {
            if (getSynthesizer().isConnected())
                throw new SynthException("Cannot assign "+p+" to "+this+" while synthesizer is connected.");
            
            MidiPlug oldPlug = mplug;
            MidiPlug newPlug = p == null ? null : (MidiPlug) p;
            
            mplug = newPlug;
            firePlugAttached(oldPlug, newPlug);
        }
    }
    
    public String toString()
    {
        return getClass().getName()
        +"[name='"+name+"',type="+type+",plug="+mplug+"]";
    }
    
}
