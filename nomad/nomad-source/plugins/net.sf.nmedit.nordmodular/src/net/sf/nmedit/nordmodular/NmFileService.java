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

import java.io.File;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMData;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.forms.ExceptionDialog;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.fileService.FSFileFilter;
import net.sf.nmedit.nomad.core.service.fileService.FileService;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.DocumentManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NmFileService implements FileService
{
    
    private static final String PATCH_DESCRIPTION ="Nord Modular patch 3.0 (*.pch)";

    public FSFileFilter getFileFilter()
    {
        return new FSFileFilter(this, "pch");
    }
    
    public String getDescription()
    {
        return PATCH_DESCRIPTION;
    }
    
    public static NMPatch openPatch(File file, File sourceFile, final String title, boolean showExceptionDialog)
    {
        NMContextData data = NMContextData.sharedInstance();

        try
        {
        	boolean setFilePointerToNull = false;
        	if (isFileAlreadyOpen(file))
        	{
        		if (JOptionPane.showConfirmDialog(
        				Nomad.sharedInstance().getWindow().getRootPane(),
        				"File \""+file+"\" is already open.\nDo you want to open a copy of the file?",
        				"Open...",
        				JOptionPane.YES_NO_OPTION)
        				== JOptionPane.NO_OPTION)
        		{
        			Nomad.sharedInstance().setSelectedDocumentByFile(file);
        			return null;
        		}
        		
        		setFilePointerToNull = true;
        	}
        	
        	NMPatch patch = NMPatch.createPatchFromFile(file);
            patch.setEditSupportEnabled(false);

            if (title != null)
                patch.setName(title);
            
            final PatchDocument pd = createPatchDoc(patch);
            
            if (setFilePointerToNull)
            {
            	if (sourceFile != null)
            	{
	            	String name = sourceFile.getName();
	            	if (name.toLowerCase().endsWith(".pch"))
	            		name = name.substring(0, name.length()-4);
	            	patch.setName(name);
            	}
            }
            else
            {
	            if (sourceFile!=null)
	            {
	                patch.setProperty("file", sourceFile);
	                pd.setURI(sourceFile);
	            }
            }

            patch.setEditSupportEnabled(true);
            patch.setModified(false);
                    DocumentManager dm = 
                    Nomad.sharedInstance()
                    .getDocumentManager();
                    dm.add(pd);
                    dm.setSelection(pd);
                
            return patch;
        
        }
        catch (Exception e)
        {
            Log log = LogFactory.getLog(NmFileService.class);
            if (log.isWarnEnabled())
            {
                log.warn("open failed: "+file, e);
            }
            if (showExceptionDialog)
            {
                ExceptionDialog.showErrorDialog(Nomad.sharedInstance().getWindow().getRootPane(), 
                    "Could not open file '"+file+"' ("+e.getMessage()+")", "Could not open file", e);
            }
        }
        
        return null;
    }
    
    public void open(File file)
    {
        openPatch(file, file, null, true);
    }
    
    public static PatchDocument createPatchDoc(NMPatch patch) throws Exception
    {
        JTNMPatch jtp = JTNMPatch.createPatchUI(patch);
        jtp.setHelpHandler(NMContextData.sharedInstance().getHelpHandler());
        PatchDocument pd = new PatchDocument(jtp);
        return pd;
    }

    public Class<? extends Service> getServiceClass()
    {
        return FileService.class;
    }

    public boolean isNewFileOperationSupported()
    {
        return true;
    }

    public void newFile()
    {
        try
        {
            NMPatch patch = new NMPatch(NMData.sharedInstance().getModuleDescriptions());
            PatchDocument pd = createPatchDoc(patch);
            Nomad.sharedInstance()
            .getDocumentManager()
            .add(pd);
            patch.setEditSupportEnabled(true); // enable history
        }
        catch (Exception e)
        {
            Log log = LogFactory.getLog(getClass());
            if (log.isWarnEnabled())
            {
                log.warn(e);
            }
            return;
        }
    }

    public String getName()
    {
        return "Nord Modular patch 3.0";
    }

    public boolean isOpenFileOperationSupported()
    {
        return true;
    }

    public Icon getIcon()
    {
        return PatchDocument.pchIcon;
    }
    
    private NMPatch getPatch(Object source)
    {
        if (source instanceof NMPatch) return (NMPatch) source;
        if (source instanceof JTNMPatch) return ((JTNMPatch) source).getPatch();
        if (source instanceof PatchDocument) return ((PatchDocument)source).getComponent().getPatch();
        return null;
    }

    public File getAssociatedFile(Object source)
    {
        NMPatch patch = getPatch(source);
        if (patch == null)
            return null;
        return patch.getFile();
    }

    public static boolean isFileAlreadyOpen(File file)
    {
    	final DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
        for (Document d: dm.getDocuments())
        {
        	if (d instanceof PatchDocument)
        	{
        		if (file.equals(d.getFile()))
        			return true;
        	}
        }
        return false;
    }
    
    public static void selectOrOpen(NMPatch patch)
    {
        final DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
        for (Document d: dm.getDocuments())
        {
            if (d instanceof PatchDocument)
            {
                PatchDocument pd = (PatchDocument) d;
                if (pd.getPatch() == patch)
                {
                    dm.setSelection(pd);
                    return;
                }
            }
        }
        // document does not exist
        
        try
        {
            final Document d = NmFileService.createPatchDoc(patch);

            dm.add(d);
            dm.setSelection(d); // TODO selection does not work
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        
    }
    
    public boolean isDirectSaveOperationSupported(Object source)
    {
        NMPatch patch = getPatch(source);
        return patch != null && (patch.getFile()!=null);
    }
    
    public boolean isSaveOperationSupported(Object source)
    {
        return getPatch(source) != null;
    }
    
    private File getFileWithCorrectExtension(File file)
    {
    	String name = file.getName();
    	if (name.toLowerCase().endsWith(".pch")) return file;
    	return new File(file.getParentFile(), name+".pch");
    }

    public void save(Object source, File as)
    {
        NMPatch patch = getPatch(source);
        
        if (patch == null)
            throw new IllegalArgumentException("save operation not supported for source: "+source);

        File file = as != null ? as : patch.getFile();
        if (file == null)
            throw new RuntimeException("not file specified");
        file = getFileWithCorrectExtension(file);
        if (NmUtils.writePatchSavely(patch, file)) {
            patch.setProperty("file", file);
            patch.setModified(false);
        }
    }

    public void editProperties(Object source)
    {
        NMPatch patch = getPatch(source);        
        if (patch == null)
            throw new IllegalArgumentException("properties not supported for source: "+source);
        
        
        PatchSettingsDialog.invoke(this, patch);
    }

    public boolean isEditPropertiesSupported(Object source)
    {
        return getPatch(source) != null;
    }

}

