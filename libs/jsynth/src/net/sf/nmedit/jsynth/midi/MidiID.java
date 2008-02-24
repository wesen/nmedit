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
package net.sf.nmedit.jsynth.midi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;


public class MidiID
{
    
    private MidiDevice.Info[] infoList;

    private Map<MidiDevice.Info, Integer> idMap;

    public MidiID()
    {
        super();
    }
    
    public MidiID(MidiDevice.Info[] infoList)
    {
        this.infoList = infoList;
    }

    private void ensureDataAvailable()
    {
        if (infoList == null)
            infoList = MidiSystem.getMidiDeviceInfo();

        if (idMap == null)
        {
            idMap = new HashMap<MidiDevice.Info, Integer>(infoList.length*2);
            
            List<MidiDevice.Info> list = new ArrayList<MidiDevice.Info>(infoList.length);
            
            // inputs
            MidiUtils.collectMidiDeviceInfo(list, true, true, true, false, false);
            generateIDs(list);
            list.clear();
            // outputs
            MidiUtils.collectMidiDeviceInfo(list, true, true, false, true, false);
            generateIDs(list);
        }
    }
    
    private void generateIDs(List<MidiDevice.Info> list)
    {
        if (list.isEmpty())
            return;
        
        Collection<MidiDevice.Info> trace = new ArrayList<MidiDevice.Info>(list.size());
        
        while (!list.isEmpty())
        {
            int id = 0;
            MidiDevice.Info first = list.remove(0);
            idMap.put(first, id++);
            
            for (int j=0;j<list.size();j++)
            {
                MidiDevice.Info b = list.get(j);
                
                if (MidiUtils.equals(first, b))
                {
                    idMap.put(b, id++);
                    trace.add(b);
                }
            }
            
            list.removeAll(trace);
        }
    }

    public void reset()
    {
        infoList = null;
        idMap = null;
    }
    
    public int getID(MidiDevice.Info info)
    {
        ensureDataAvailable();
        Integer ID = idMap.get(info);
        return ID != null ? ID.intValue() : -1;
    }
    
    public MidiDevice.Info findDeviceInfo(MidiDescription description)
    {
        ensureDataAvailable();
        
        List<MidiDevice.Info> list = new ArrayList<MidiDevice.Info>();
        
        for (MidiDevice.Info info: infoList)
        {
            if (MidiUtils.equals(info, description.getName(), description.getVendor(), description.getVersion(), description.getDescription()))
            {
                try
                {
                    if ((description.isInput() && MidiUtils.isInputDevice(info)) || ((!description.isInput()) && MidiUtils.isOutputDevice(info)))
                    {
                        list.add(info);
                    }
                }
                catch (MidiUnavailableException e)
                {
                    // ignore
                }
            }
        }
        
        MidiDevice.Info firstInfo = null;
        for (MidiDevice.Info info: list)
        {
        	if (firstInfo == null)
        		firstInfo = info;
            if (getID(info) == description.getId())
                return info;
        }
        
        return firstInfo;
    }
    
    public static void main(String[] args)
    {
        MidiID MID = new MidiID();
        
        for (MidiDevice.Info info: MidiSystem.getMidiDeviceInfo())
            System.out.println(info+" ID="+MID.getID(info));
        
    }
    
}
