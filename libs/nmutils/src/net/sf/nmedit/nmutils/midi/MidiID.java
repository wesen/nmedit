package net.sf.nmedit.nmutils.midi;

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
    
    public MidiDevice.Info findDeviceInfo(String name, String vendor, 
            String version, String description, boolean input, int ID)
    {
        ensureDataAvailable();
        
        List<MidiDevice.Info> list = new ArrayList<MidiDevice.Info>();
        
        for (MidiDevice.Info info: infoList)
        {
            if (MidiUtils.equals(info, name, vendor, version, description))
            {
                try
                {
                    if ((input && MidiUtils.isInputDevice(info)) || ((!input) && MidiUtils.isOutputDevice(info)))
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
        
        for (MidiDevice.Info info: list)
        {
            if (getID(info) == ID)
                return info;
        }
        
        return null;
    }
    
    public static void main(String[] args)
    {
        MidiID MID = new MidiID();
        
        for (MidiDevice.Info info: MidiSystem.getMidiDeviceInfo())
            System.out.println(info+" ID="+MID.getID(info));
        
    }
    
}
