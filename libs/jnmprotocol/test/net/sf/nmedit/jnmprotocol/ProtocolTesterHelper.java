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
package net.sf.nmedit.jnmprotocol;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

public class ProtocolTesterHelper
{
    
    private static boolean available(int count)
    {
        return count == -1 || count > 0;
    }
    
    public static MidiDevice.Info[] getMidiDevicePair() throws MidiUnavailableException
    {
        // return MidiSystem.getMidiDeviceInfo();
        
        MidiDevice.Info[] pair = new MidiDevice.Info[] {
                getFirstInputDevice(), getFirstOutputDevice()
        };
        return pair;
    }
    
    public static MidiDevice.Info getFirstInputDevice() throws MidiUnavailableException
    {
        MidiDevice.Info[] hwlist = getHardwareDevices();
        
        for (int i=0;i<hwlist.length;i++)
        {
            MidiDevice.Info info = hwlist[i];
            MidiDevice device = MidiSystem.getMidiDevice(info);
            
            if (available(device.getMaxTransmitters()))
                return info;
        }
        return null;
    }
    
    public static MidiDevice.Info getFirstOutputDevice() throws MidiUnavailableException
    {
        MidiDevice.Info[] hwlist = getHardwareDevices();
        
        for (int i=0;i<hwlist.length;i++)
        {
            MidiDevice.Info info = hwlist[i];
            MidiDevice device = MidiSystem.getMidiDevice(info);

            if (available(device.getMaxReceivers()))
                return info;
        }
        return null;
    }

    public static MidiDevice.Info[] getHardwareDevices()
    {
        MidiDevice.Info[] infoList = MidiSystem.getMidiDeviceInfo();
        List<MidiDevice.Info> hwList = new ArrayList<MidiDevice.Info>();
        
        for (int i=0;i<infoList.length;i++)
        {
            MidiDevice.Info candidateInfo = infoList[i];
            try
            {
                MidiDevice candidate = MidiSystem.getMidiDevice(candidateInfo);
                if (isHardwareDevice(candidate))
                    hwList.add(candidateInfo);
            }
            catch (MidiUnavailableException e)
            {
                // ignore
            }
        }
        
        return hwList.toArray(new MidiDevice.Info[hwList.size()]);
    }

    public static boolean isHardwareDevice( MidiDevice dev )
    {
        return !(dev instanceof Sequencer || dev instanceof Synthesizer);
    }

}
