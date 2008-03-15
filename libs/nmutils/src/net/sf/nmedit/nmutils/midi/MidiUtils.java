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
 */package net.sf.nmedit.nmutils.midi;

import java.util.Collection;
import java.util.Iterator;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;


public class MidiUtils
{

    private static boolean eq(String s, String compare)
    {
        return compare == null || compare.equals(s);
    }

    public static boolean equals(MidiDevice.Info info, String name, String vendor, 
            String version, String description)
    {
        return eq(info.getName(), name)
            && eq(info.getVendor(), vendor)
            && eq(info.getVersion(), version)
            && eq(info.getDescription(), description);
    }

    public static boolean equals(MidiDevice.Info a, MidiDevice.Info b)
    {
        return equals(a, b.getName(), b.getVendor(), b.getVersion(), b.getDescription());
    }
    
    public static void collectMidiDeviceInfo(Collection<MidiDevice.Info> c,
            boolean hardware, boolean software, boolean inputs, boolean outputs,
            boolean onlyAvailable) 
    {
        if ((!(hardware || software))||(!(inputs||outputs)))
        {
            // empty set, nothing to add to the collection
            return;
        }

        MidiDevice.Info[] list = MidiSystem.getMidiDeviceInfo();
        
        for (int i=0;i<list.length;i++)
        {
            
            MidiDevice.Info info = list[i];

            try
            {
                if (!((hardware && isHardwareDevice(info)) || (software && isSoftwareDevice(info))))
                {
                    continue;
                }
            }
            catch (MidiUnavailableException e)
            {
                // do not add info in this case
                continue;
            }
            
            try
            {
                if (onlyAvailable)
                {
                    if ((inputs && isInputDeviceAvailable(info)) || (outputs && isOutputDeviceAvailable(info)))
                    {
                        c.add(info);
                    }
                }
                else
                {
                    if ((inputs && isInputDevice(info)) || (outputs && isOutputDevice(info)))
                    {
                        c.add(info);
                    }
                }
            }
            catch (MidiUnavailableException e)
            {
                // do not add info in this case
            }
        }
    }
    
    public static void removeInputDevices(Collection<MidiDevice.Info> c) throws MidiUnavailableException
    {
        Iterator<MidiDevice.Info> i = c.iterator();
        while (i.hasNext())
        {
            if (isInputDevice(i.next()))
                i.remove();
        }
    }

    public static void removeOutputDevices(Collection<MidiDevice.Info> c) throws MidiUnavailableException
    {
        Iterator<MidiDevice.Info> i = c.iterator();
        while (i.hasNext())
        {
            if (isOutputDevice(i.next()))
                i.remove();
        }
    }

    public static void removeHardwareDevices(Collection<MidiDevice.Info> c) throws MidiUnavailableException
    {
        Iterator<MidiDevice.Info> i = c.iterator();
        while (i.hasNext())
        {
            if (isHardwareDevice(i.next()))
                i.remove();
        }
    }

    public static void removeSoftwareDevices(Collection<MidiDevice.Info> c) throws MidiUnavailableException
    {
        Iterator<MidiDevice.Info> i = c.iterator();
        while (i.hasNext())
        {
            if (isSoftwareDevice(i.next()))
                i.remove();
        }
    }

    public static boolean isHardwareDevice(MidiDevice device)
    {
        return !isSoftwareDevice(device);
    }

    public static boolean isSoftwareDevice(MidiDevice device)
    {
        return device instanceof javax.sound.midi.Sequencer
        || device instanceof javax.sound.midi.Synthesizer;
    }

    public static boolean isInputDevice(MidiDevice.Info deviceInfo) throws MidiUnavailableException
    {
        return isInputDevice(getMidiDevice(deviceInfo));
    }

    public static boolean isOutputDevice(MidiDevice.Info deviceInfo) throws MidiUnavailableException
    {
        return isOutputDevice(getMidiDevice(deviceInfo));
    }
    
    public static MidiDevice getMidiDevice(MidiDevice.Info deviceInfo) throws MidiUnavailableException
    {
        try
        {
            return MidiSystem.getMidiDevice(deviceInfo);
        }
        catch (IllegalArgumentException e)
        {
            // the device does not exist anymore - 
            // this seems to be a bug because MidiUnavailableException should
            // be thrown instead
            MidiUnavailableException m = new MidiUnavailableException();
            m.initCause(e);
            throw m;
        }
    }

    public static boolean isInputDevice(MidiDevice device) 
    {
        return allowsTransmitters(device);
    }

    public static boolean isOutputDevice(MidiDevice device)
    {
        return allowsReceivers(device);
    }

    public static boolean isInputDeviceAvailable(MidiDevice.Info deviceInfo) throws MidiUnavailableException
    {
        return isInputDeviceAvailable(getMidiDevice(deviceInfo));
    }

    public static boolean isOutputDeviceAvailable(MidiDevice.Info deviceInfo) throws MidiUnavailableException
    {
        return isOutputDeviceAvailable(getMidiDevice(deviceInfo));
    }

    public static boolean isInputDeviceAvailable(MidiDevice device) 
    {
        return hasAvailableTransmitters(device);
    }

    public static boolean isOutputDeviceAvailable(MidiDevice device)
    {
        return hasAvailableReceivers(device);
    }
    
    public static boolean isHardwareDevice(MidiDevice.Info deviceInfo) 
        throws MidiUnavailableException
    {
        return isHardwareDevice(getMidiDevice(deviceInfo));        
    }

    public static boolean isSoftwareDevice(MidiDevice.Info deviceInfo) 
        throws MidiUnavailableException
    {
        return isSoftwareDevice(getMidiDevice(deviceInfo));
    }
    
    public static boolean allowsReceivers(MidiDevice.Info deviceInfo) 
        throws MidiUnavailableException
    {
        return allowsReceivers(getMidiDevice(deviceInfo));
    }
    
    public static boolean allowsTransmitters(MidiDevice.Info deviceInfo) 
        throws MidiUnavailableException
    {
        return allowsTransmitters(getMidiDevice(deviceInfo));
    }

    public static boolean hasAvailableReceivers(MidiDevice device)
    {
        return probe(device.getMaxReceivers());
    }

    public static boolean hasAvailableTransmitters(MidiDevice device)
    {
        return probe(device.getMaxTransmitters());
    }
    
    public static boolean allowsReceivers(MidiDevice device)
    {
        return probe(device.getMaxReceivers())
            || probe(device.getReceivers());
    }

    public static boolean allowsTransmitters(MidiDevice device)
    {
        return probe(device.getMaxTransmitters())
            || probe(device.getTransmitters());
    }
    
    private static boolean probe(Collection<?> receiverTransmitterList)
    {
        return !receiverTransmitterList.isEmpty();
    }
    
    private static boolean probe(int receivertransmitter)
    {
        return receivertransmitter == -1 || receivertransmitter > 0; 
    }

    public static MidiDevice getDeviceOrNull(MidiDevice.Info deviceInfo)
    {
        try
        {
            return getMidiDevice(deviceInfo);
        }
        catch (MidiUnavailableException e)
        {
            return null;
        }
    }
    
}
