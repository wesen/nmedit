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
package net.sf.nmedit.jsynth.nomad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.worker.SendPatchWorker;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.registry.GlobalRegistry;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.initService.InitService;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.document.Document;

public class Installer implements InitService
{
    private static final String MENU_FILE_SAVEINSYNTH = "nomad.menu.file.save.saveinsynth";

    public void init()
    {
        
        GlobalRegistry.getInstance().put(Synthesizer.class, SynthRegistry.sharedInstance());
        
        MenuLayout menuLayout = Nomad.sharedInstance().getMenuLayout();
        
        // TODO Auto-generated method stub

        MLEntry e = menuLayout.getEntry(MENU_FILE_SAVEINSYNTH);
        e.setEnabled(true);
        e.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                fileSaveInSynth();
            }});

    }

    void fileSaveInSynth()
    {
        
        DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
        
        Document d = dm.getSelection();
        if (d == null) return;
        
        Object patch = d.getProperty("patch");
        if (patch == null) return;
        

        // TODO use synth registry
        
        SynthRegistry registry = SynthRegistry.sharedInstance();
        
        List<Synthesizer> synthList = new ArrayList<Synthesizer>();
        for (Synthesizer synth: registry)
        {
            if (synth.isConnected())
                synthList.add(synth);
        }
        
        if (synthList.isEmpty())
            return;
        
        SaveInSynthDialog ssd = new SaveInSynthDialog(synthList);
        
        ssd.invoke();
        
        if (!ssd.isSaveOption())
            return;

        Synthesizer synth = ssd.getSelectedSynthesizer();
        Slot slot = ssd.getSelectedSlot();
        
        if (synth == null || slot == null || (!synth.isConnected()))
            return;
        
        SendPatchWorker spw = slot.createSendPatchWorker();
        try
        {
            spw.setPatch(patch);
            spw.send();
        }
        catch (SynthException e)
        {
            e.printStackTrace();
        }
        
        
/*        
        Iterator<FileService> iter = ServiceRegistry.getServices(synt.class);
        while (iter.hasNext())
        {
            FileService fs = iter.next();
            
            boolean add = 
                (saveAs && fs.isSaveOperationSupported(d))
                || ((!saveAs)&&fs.isSaveOperationSupported(d));
            
            if (add)
                chooser.addChoosableFileFilter(fs.getFileFilter());
        }

        
        if (!(chooser.showSaveDialog(mainWindow)==JFileChooser.APPROVE_OPTION)) return;
        
        FileService service = FileServiceTool.lookupFileService(chooser);
        
        if (service != null)
        {
            File newFile = chooser.getSelectedFile();
            if (newFile == null) return;
            service.save(d, newFile);
        }*/
    }

    public void shutdown()
    {
        // TODO Auto-generated method stub

    }

    public Class<? extends Service> getServiceClass()
    {
        return InitService.class;
    }

}
