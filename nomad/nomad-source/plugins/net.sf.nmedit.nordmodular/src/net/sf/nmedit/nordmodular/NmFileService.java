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
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNM1Context;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.fileService.FSFileFilter;
import net.sf.nmedit.nomad.core.service.fileService.FileService;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.document.Document;

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

    public void open(File file)
    {

        NMData data = NMData.sharedInstance();

        try
        {
            InputStream in = new FileInputStream(file);

            NMPatch patch = NmUtils.parsePatch(data.getModuleDescriptions(), in);
            in.close();
            
            patch.setProperty("file", file);

            patch.setName(NmUtils.getPatchNameFromfileName(file));
            final PatchDocument pd = createPatchDoc(patch);
            pd.setURI(file);

            SwingUtilities.invokeLater(new Runnable(){
                public void run()
                {
                    Nomad.sharedInstance()
                    .getDocumentManager()
                    .add(pd);
                }
            });
        
        }
        catch (Exception e)
        {
            Log log = LogFactory.getLog(getClass());
            if (log.isWarnEnabled())
            {
                log.warn("open failed: "+file, e);
            }
            return;
        }
    }
    
    public static PatchDocument createPatchDoc(NMPatch patch) throws Exception
    {
        JTNMPatch jtp = createPatchUI(patch);
        PatchDocument pd = new PatchDocument(jtp);
        return pd;
    }

    public static JTNMPatch createPatchUI(NMPatch patch) throws Exception
    {           
        JTNM1Context context = NMData.sharedInstance().getJTContext();
        synchronized(context.getLock())
        {
            return new JTNMPatch(context, patch);
        }
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
        NMData data = NMData.sharedInstance();

        try
        {
            NMPatch patch = new NMPatch(data.getModuleDescriptions());
            patch.getHistory().setEnabled(false);
            PatchDocument pd = createPatchDoc(patch);
            Nomad.sharedInstance()
            .getDocumentManager()
            .add(pd);
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

    public void save(Object source, File as)
    {
        NMPatch patch = getPatch(source);
        
        if (patch == null)
            throw new IllegalArgumentException("save operation not supported for source: "+source);

        File file = as != null ? as : patch.getFile();
        if (file == null)
            throw new RuntimeException("not file specified");
        if (NmUtils.writePatchSavely(patch, file))
            patch.setProperty("file", file);
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

