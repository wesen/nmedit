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

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sound.midi.MidiDevice;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMData;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchExporter;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchFileWriter;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.midi.MidiDescription;
import net.sf.nmedit.jsynth.midi.MidiID;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jtheme.ModulePane;
import net.sf.nmedit.nmutils.io.FileUtils;
import net.sf.nmedit.nomad.core.JPFUtil;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.jpf.TempDir;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.initService.InitService;
import net.sf.nmedit.nomad.core.swing.ExtensionFilter;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.explorer.FileContext;
import net.sf.nmedit.nomad.core.swing.tabs.JTabbedPane2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Installer implements InitService
{
    
    

    // TempDir temp = new TempDir(this);
    public Class<? extends Service> getServiceClass()
    {
        return InitService.class;
    }

    public void init()
    {
    	installPatches();
        readSynthConfiguration();
        
        NMContextData data = NMContextData.sharedInstance();
        NMData d = NMData.sharedInstance();
        
        ModulePane pane = ModulePane.getSharedInstance();
        pane.setModules(d.getModuleDescriptions());
        pane.setTheme(data.getJTContext());

        Nomad nomad = Nomad.sharedInstance();
        MLEntry mlKnobs = nomad.getMenuLayout().getEntry("nomad.menu.patch.knobs");
        
        DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
        KnobController kc = new KnobController();
        dm.addListener(kc);
        mlKnobs.addActionListener(kc);
        
        loadLastSession();
    }

    public void shutdown()
    {
        
        saveCurrentSession();
        
        List<NordModular> synthList = new ArrayList<NordModular>();
        
        JTabbedPane2 tb = Nomad.sharedInstance().getSynthTabbedPane();
        
        for (int i=0;i<tb.getTabCount();i++)
        {
            Component c = tb.getComponentAt(i);
            if (c instanceof NMSynthDeviceContext)
            {
                synthList.add(((NMSynthDeviceContext)c).getSynthesizer()); 
            }
        }
        
        storeSynthConfiguration(synthList);
        
    }
    
    public void installPatches() {
    	File pluginDir = JPFUtil.getPluginDirectory(this);
    	File patches = new File(pluginDir, "patches/");
    	TempDir tempDir = TempDir.generalTempDir();
    	File userPatches = tempDir.getTempFile("patches");
    	if (!userPatches.exists())
    		FileUtils.copy(patches, userPatches);
    	if (userPatches.exists()) {
    		ExplorerTree explorerTree = Nomad.sharedInstance().getExplorer();
    		FileContext libraryContext = new FileContext(explorerTree, new ExtensionFilter("Nord Modular Patch", "pch", true), userPatches);
    		libraryContext.setName("Library");
    		libraryContext.setNailed(true);
    		explorerTree.addRootNode(libraryContext, 0);
    	}

        

    }

    private File getSessionFile(TempDir dir)
    {
        return dir.getTempFile("session.properties");
    }
    
    
    private void loadLastSession()
    {
        TempDir tmpDir = TempDir.forObject(this);
        File sessionFile = getSessionFile(tmpDir);
        
        if (!sessionFile.exists())
            return;

        Properties props = new Properties();
        InputStream in;
        try
        {
            in = new BufferedInputStream(new FileInputStream(sessionFile));
            try
            {
                props.load(in);
            }
            finally
            {
                in.close();
            }
        } 
        catch (Exception e)
        {
            Log log = LogFactory.getLog(getClass());
            if (log.isDebugEnabled())
            {
                log.debug("exception while loading session file", e);
            }
            
            e.printStackTrace();
            // ignore
            // delete corrupt session file
            sessionFile.delete();
            return ;
        }
        
        loadSessionFromProperties(tmpDir, props);
        
    }
    
    private static final String SESSION_PATCHCOUNT = "session.patchcount";

    private void loadSessionFromProperties(TempDir tmpDir, Properties props)
    {
        String PCountString = props.getProperty(SESSION_PATCHCOUNT);
        if (PCountString == null) return;
        
        int patchcount;
        try
        {
            patchcount = Integer.parseInt(PCountString);
        }
        catch (NumberFormatException e)
        {
            Log log = LogFactory.getLog(getClass());
            if (log.isDebugEnabled())
            {
                log.debug("exception while loading session file", e);
            }
            
            return ;
        }
        
        for (int i=0;i<patchcount;i++)
        {
            File sourceFile = null;
            String keySourceFile = getSourceFileKey(i);
            String sourceFileString = props.getProperty(keySourceFile);
            if (sourceFileString != null) sourceFile = new File(sourceFileString);
            File tmpFile = getPatchTmpFile(tmpDir, i);
            String title = props.getProperty(getTitleKey(i));
            NMPatch patch = tryOpenSessionPatch(tmpFile, sourceFile, title);
        }
        
    }

    private String getTitleKey(int index)
    {
        return "patch."+index+".title";
    }
    
    private String getSourceFileKey(int index)
    {
        return "patch."+index+".sourcefile";
    }
    
    private File getPatchTmpFile(TempDir tmpDir, int index)
    {
        return tmpDir.getTempFile("temp_"+index+".pch");
    }

    private NMPatch tryOpenSessionPatch(File file, File sourceFile, String title)
    {
        if (!file.exists()) return null;
        return NmFileService.openPatch(file, sourceFile, title, false);
    }

    private void saveCurrentSession()
    {
        DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
        
        List<NMPatch> patches = new ArrayList<NMPatch>();
        
        for (Document doc : dm.getDocuments())
        {
            if (doc instanceof PatchDocument)   
            {
                patches.add(((PatchDocument)doc).getPatch());
            }
        }
        

        TempDir tmpDir = TempDir.forObject(this);
        
        Properties props = new Properties();
        int pcount = 0;
        
        for (NMPatch patch: patches)
        {
            File saveAs = getPatchTmpFile(tmpDir, pcount); 
            
            try
            {
                OutputStream out = new BufferedOutputStream(new FileOutputStream(saveAs));
                
                PatchFileWriter pfw = new PatchFileWriter(out);
                PatchExporter export = new PatchExporter();
                export.export(patch, pfw);
                
                out.flush();
                out.close();
            }
            catch (Exception e)
            {
                Log log = LogFactory.getLog(getClass());
                if (log.isDebugEnabled())
                {
                    log.debug("exception while storing session file", e);
                }
                
                e.printStackTrace();
                continue;
            }
            
            // tmp file written
            File sourceFile = patch.getFile();
            if (sourceFile != null)
                props.put(getSourceFileKey(pcount), sourceFile.getAbsolutePath());
            else
            {
                // source file unknown, since patch file contains no title we have to remember it
                String name = patch.getName();
                if (name != null) props.put(getTitleKey(pcount), name);
            }
            pcount++;
        }
        
        props.put(SESSION_PATCHCOUNT, String.valueOf(pcount));

        File sessionFile = getSessionFile(tmpDir);
        
        try
        {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(sessionFile));
            props.store(out, "generated file - do not edit manually");
            out.close();
        }
        catch (Exception e)
        {
            Log log = LogFactory.getLog(getClass());
            if (log.isDebugEnabled())
            {
                log.debug("exception while storing session file", e);
            }
            
            e.printStackTrace();
            // ignore   
        }
        
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
     
        
        for (int i=0;i<count;i++)
        {
            String prefix = "nordmodular."+i+"."; 

            String name = properties.getProperty(prefix+"name");
            
            MidiDescription in = readMidiInfo(properties, prefix+"midi.in.", true);
            MidiDescription out = readMidiInfo(properties, prefix+"midi.out.", false);

            MidiPlug pin = in == null ? null : new MidiPlug(in);
            MidiPlug pout = out == null ? null : new MidiPlug(out);
            
            NewNordModularService.newSynth(name, pin, pout);
        }
    }

    private MidiDescription readMidiInfo(Properties properties, String prefix,  boolean input)
    {
        int id = str2int(properties.getProperty(prefix+"id"), -1);
        String name = properties.getProperty(prefix+"name");
        String vendor = properties.getProperty(prefix+"vendor");
        String version = properties.getProperty(prefix+"version");
        String description = properties.getProperty(prefix+"description");
        int isInput = str2boolean(properties.getProperty(prefix+"input"));
        
        if (isInput < 0 || ((isInput==1)!=input) )
            return null;
        
        return new MidiDescription(name, vendor, version, description, isInput, id);
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
        if (info == null) return;

        properties.put(prefix+"id", Integer.toString(midiID.getID(info)));
        properties.put(prefix+"name", info.getName());
        properties.put(prefix+"vendor", info.getVendor());
        properties.put(prefix+"version", info.getVersion());
        properties.put(prefix+"description", info.getDescription());
        properties.put(prefix+"input", Boolean.toString(input));
    }

    private File getSynthPropertyFile()
    {
        return TempDir.forObject(this).getTempFile("synth.properties"); 
    }

}

