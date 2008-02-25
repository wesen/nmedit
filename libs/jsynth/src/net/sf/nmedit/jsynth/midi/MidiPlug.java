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
 * Created on Jan 2, 2007
 */
package net.sf.nmedit.jsynth.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import net.sf.nmedit.jsynth.Plug;

public class MidiPlug implements Plug
{

    private MidiDescription synthDesc;

    public MidiPlug(MidiDescription devInfo)
    {
        this.synthDesc = devInfo;
    }

    public String getName()
    {
        return synthDesc.getName();
    }

    public String getDescription()
    {
        return synthDesc.getDescription();
    }

    public String getVendor()
    {
        return synthDesc.getVendor();
    }

    public String getVersion()
    {
        return synthDesc.getVersion();
    }
    
    public MidiDevice.Info getDeviceInfo()
    {
        MidiDevice.Info[] list = MidiSystem.getMidiDeviceInfo();
        MidiID midiID = new MidiID(list);
        return midiID.findDeviceInfo(synthDesc);
    }

    public boolean isHardwareDevice() throws MidiUnavailableException
    {
        return MidiUtils.isHardwareDevice(getDeviceInfo());
    }
    
    public int hashCode()
    {
        return synthDesc.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof MidiPlug))
            return false;
        return getDeviceInfo().equals(((MidiPlug) o).getDeviceInfo()); 
    }

    public String toString()
    {
        return "MidiPlug[name="+getName()+",vendor="+getVendor()+",description="+getDescription()+"]"; 
    }
        
}
