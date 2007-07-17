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
package net.sf.nmedit.nordmodular;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.tree.TreeNode;

import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jtheme.ModulePane;
import net.sf.nmedit.nmutils.midi.MidiID;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.jpf.TempDir;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.initService.InitService;

import org.java.plugin.PluginManager;

public class Installer implements InitService
{

    // TempDir temp = new TempDir(this);
    public Class<? extends Service> getServiceClass()
    {
        return InitService.class;
    }

    public void init()
    {
        readSynthConfiguration();
        
        NMData data = NMData.sharedInstance();
        
        ModulePane pane = ModulePane.getSharedInstance();
        pane.setModules(data.getModuleDescriptions());
        pane.setTheme(data.getJTContext());
        
    }

    public void shutdown()
    {
        List<NordModular> synthList = new ArrayList<NordModular>();
        
        Enumeration<TreeNode> en = Nomad.sharedInstance().getExplorer().getRoot().children();
        while (en.hasMoreElements())
        {
            TreeNode node = en.nextElement();
            
            if (node instanceof NMSynthDeviceContext)
            {
                synthList.add(((NMSynthDeviceContext)node).getSynthesizer()); 
            }
        }
        
        storeSynthConfiguration(synthList);
        
    }

    private void readSynthConfiguration()
    {
        Properties properties = new Properties();
        
        InputStream in = null;
        try
        {
            in = new BufferedInputStream(new FileInputStream(getSynthPropertyFile()));
            properties.load(in);
        }
        catch (IOException e)
        {
            // ignore
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    // ignore
                }
            }
        }
        
        readSynthConfiguration(properties);
    }

    private void readSynthConfiguration(Properties properties)
    {
        int count = str2int(properties.getProperty("nordmodular.count"), 0);
     
        if (count>100)
        {
            count = 10; // just in case the number messed up
        }
        
        if (count <= 0)
            return ;
     
        MidiDevice.Info[] list = MidiSystem.getMidiDeviceInfo();

        MidiID midiID = new MidiID(list);
        
        for (int i=0;i<count;i++)
        {
            String prefix = "nordmodular."+i+"."; 

            String name = properties.getProperty(prefix+"name");
            
            MidiDevice.Info in = readMidiInfo(properties, prefix+"midi.in.", list, midiID, true);
            MidiDevice.Info out = readMidiInfo(properties, prefix+"midi.out.", list, midiID, false);

            MidiPlug pin = in == null ? null : new MidiPlug(in);
            MidiPlug pout = out == null ? null : new MidiPlug(out);
            
            NewNordModularService.newSynth(name, pin, pout);
        }
    }

    private Info readMidiInfo(Properties properties, String prefix, Info[] list, MidiID midiID, boolean input)
    {
        int id = str2int(properties.getProperty(prefix+"id"), -1);
        String name = properties.getProperty(prefix+"name");
        String vendor = properties.getProperty(prefix+"vendor");
        String version = properties.getProperty(prefix+"version");
        String description = properties.getProperty(prefix+"description");
        int isInput = str2boolean(properties.getProperty(prefix+"input"));
        
        if (isInput < 0 || ((isInput==1)!=input) )
            return null;

        return midiID.findDeviceInfo(name, vendor, version, description, input, id);
    }
    
    private int str2boolean(String value)
    {
        try
        {
            return Boolean.parseBoolean(value) ? 1 : 0;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    private int str2int(String value, int alt)
    {
        if (value == null)
            return alt;
        
        try
        {
            return Integer.parseInt(value.trim());
        }
        catch (NumberFormatException e)
        {
            return alt;
        }
    }
    
    private void storeSynthConfiguration(List<NordModular> synthList)
    {
        MidiID midiID = new MidiID();
        
        Properties properties = new Properties();
        properties.put("nordmodular.count", Integer.toString(synthList.size()));
        for (int i=0;i<synthList.size();i++)
        {
            String prefix = "nordmodular."+i+"."; 
            NordModular synth = synthList.get(i);
            properties.put(prefix+"name", synth.getName());
            putMidiInfo(properties, prefix+"midi.in.", synth.getPCInPort().getPlug(), midiID, true);
            putMidiInfo(properties, prefix+"midi.out.", synth.getPCOutPort().getPlug(), midiID, false);
        }
        
        OutputStream out = null;
        
        try
        {
            out = new BufferedOutputStream(new FileOutputStream(getSynthPropertyFile()));
        
            properties.store(out, "generated file, do not edit");
        }
        catch (IOException e)
        {
            // ignore
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    // ignore
                }
            }
        }
    }
    
    private void putMidiInfo(Properties properties, String prefix, MidiPlug plug, MidiID midiID, boolean input)
    {
        if (plug == null) return;
        MidiDevice.Info info = plug.getDeviceInfo();

        properties.put(prefix+"id", Integer.toString(midiID.getID(info)));
        properties.put(prefix+"name", info.getName());
        properties.put(prefix+"vendor", info.getVendor());
        properties.put(prefix+"version", info.getVersion());
        properties.put(prefix+"description", info.getDescription());
        properties.put(prefix+"input", Boolean.toString(input));
    }

    private File getSynthPropertyFile()
    {
        TempDir temp = new TempDir(PluginManager.lookup(this).getPluginFor(this));
        return temp.getTempFile("synth.properties"); 
    }

}

