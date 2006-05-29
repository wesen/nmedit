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
 * Created on Jan 23, 2006
 */
package net.sf.nmedit.nomad.main.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.patch.builder.VirtualBuilder;
import net.sf.nmedit.nomad.patch.parser.PatchFileParser;
import net.sf.nmedit.nomad.patch.transcoder.PatchParserTranscoder;
import net.sf.nmedit.nomad.patch.ui.PatchUI;


public class PatchLoader {

	private final Nomad nomad;
	//private ArrayList<PatchFile> fileList = new ArrayList<PatchFile>();
	
	public PatchLoader(Nomad nomad) {
		this.nomad = nomad; }
	public void loadPatch(String file) {
		loadPatch(new String[]{file});
	}

	public void loadPatch(File[] files) {
		String[] sfiles = new String[files.length];
		for (int i=files.length-1;i>=0;i--)
			sfiles[i]=files[i].getAbsolutePath();
		
		loadPatch(sfiles);
	}
    
    public static String extractPatchName(String fileName)
    {
        String patchName = fileName.substring(0,fileName.lastIndexOf(".pch"));
        patchName = patchName.substring(patchName.lastIndexOf('/')+1);
        return patchName;
    }

    public void loadPatch(String[] files) {
        for (String name:files)
        {
            try 
            {
                FileReader fr = new FileReader(name);
                PatchFileParser parser = new PatchFileParser(fr);
                VirtualBuilder builder = new VirtualBuilder();
                PatchParserTranscoder t = new PatchParserTranscoder();
                t.transcode(parser, builder);                
                PatchUI tab = PatchUI.newInstance(builder.getPatch());
                builder.getPatch().setName(extractPatchName(name));
                tab.setName(extractPatchName(name));
                synchronized (nomad)
                {
                    nomad.addPatchUI(tab.getName(), tab);
                }
                
                fr.close();
                
            } catch (FileNotFoundException e) 
            {
                e.printStackTrace();
            } 
            catch (Throwable t) 
            {
                System.out.println("Error while loading patch.");
                t.printStackTrace();
            }
        }
        
    }
    
    /*
	
	public void loadPatch(final String[] files) {
		for (String fileName : files) {
			fileList.add(new PatchFile(fileName));
		}
        
        final TaskModel model = new TaskModel(){
            
            int taskCount = fileList.size();
            int selection = nomad.getDocumentContainer().getDocumentCount();
            
            public String getDescription() {
                return "Loading Nord Modular patch...";
            }

            public int getTaskCount() {
                return taskCount;
            }

            public String getTaskName(int taskIndex) {
                return fileList.get(taskIndex).patchName;
            }

            public void run(int taskIndex) throws Throwable {
                final PatchFile task = fileList.get(taskIndex);
                try {
                    
                    FileReader fr = new FileReader(task.fileName);
                    
                    PatchFileParser parser = new PatchFileParser(fr);
                    
                    VirtualBuilder builder = new VirtualBuilder();
                    
                    PatchParserTranscoder t = new PatchParserTranscoder();
                    t.transcode(parser, builder);
                    
                    // builder.getPatch().setName(cons);
                    
                    // PatchFile303 reader = new PatchFile303(task.fileName, cons);
                    
                    final PatchUI tab = PatchUI.newInstance(builder.getPatch());
                    
                    synchronized (nomad)
                    {
                        nomad.addPatchUI(task.patchName, tab);
                    }
                    
                    fr.close();
                    
                } catch (FileNotFoundException e) {
                    throw e;
                } catch (Throwable t) {
                    System.out.println("Error while loading patch.");
                    t.printStackTrace();
                }

                if (taskIndex==getTaskCount()-1) {
  //                  if (selection>=0 && selection<nomad.getDocumentContainer().getDocumentCount())
//                        nomad.getDocumentContainer().setSelection(selection);
                        
                    fileList.clear();
                }
            }
            
        };
		
        if (model.getTaskCount() <= 1)
        {
        for (int i=0;i<model.getTaskCount();i++)
        {
            try
            {
                model.run(i);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        } else
        {
            NomadTaskDialog dlg = new NomadTaskDialog(model);
            dlg.invoke();
        }
	}*/
		
}