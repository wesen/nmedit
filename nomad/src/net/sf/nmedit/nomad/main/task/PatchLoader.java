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

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.io.FileSource;
import net.sf.nmedit.jpatch.io.PatchDecoder;
import net.sf.nmedit.jpatch.spi.PatchDecoderProvider;
import net.sf.nmedit.jpatch.spi.PatchImplementation;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.patch.ui.PatchDocument;
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
        int sep = patchName.lastIndexOf('/');
        if (sep<0) sep = patchName.lastIndexOf('\\');
        if (sep>=0)
            patchName = patchName.substring(sep+1);
        
        return patchName;
    }

    public void loadPatch(String[] files) {
        PatchImplementation impl = nomad.getPatchImplementation();
        PatchDecoderProvider provider;
        try
        {
            provider = impl.getPatchDecoderProvider(FileSource.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ;
        }
        
        PatchDocument first = null;
        
        for (String name:files)
        {
            try 
            {
                PatchDecoder decoder = provider.createDecoder(impl);
                
                FileReader fr = new FileReader(name);
                decoder.decode( new FileSource(fr) );
                
                Patch patch = (Patch) decoder.getPatch();
                
                PatchUI ui = PatchUI.newInstance(patch);
                PatchDocument doc = new PatchDocument(ui);
                if (first==null) first = doc;
                doc.setFile(new File(name));
                String pName = extractPatchName(name);

                patch.setName(pName);
                patch.getHistory().clear();
                patch.getHistory().setModified(false);
                
                synchronized (nomad)
                {
                    nomad.addPatchDocument(doc);
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
        
        if (first!=null)
        {
            nomad.getDocumentContainer().setSelection(first);
        }
        
    }
    
}