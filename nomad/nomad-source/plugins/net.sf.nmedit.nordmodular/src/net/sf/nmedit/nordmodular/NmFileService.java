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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.fileService.FSFileFilter;
import net.sf.nmedit.nomad.core.service.fileService.FileService;

public class NmFileService implements FileService
{
    
    private static final String PATCH_DESCRIPTION ="Nord Modular patch 3.0 (*.pch)";

    public FSFileFilter getFileFilter()
    {
        return new NmFileFilter(this);
    }
    
    public String getDescription()
    {
        return PATCH_DESCRIPTION;
    }

    public void open(File file)
    {
        NMContext nmc=
        Nordmodular.sharedContext();
        
        try
        {
            InputStream in = new FileInputStream(file);

            NMPatch patch = NmUtils.parsePatch(nmc.getModuleDescriptions(), in);
            in.close();

            patch.setName(NmUtils.getPatchNameFromfileName(file));
            PatchDocument pd = createPatchDoc(patch);
            pd.setURI(file);

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
    
    public static PatchDocument createPatchDoc(NMPatch patch) throws Exception
    {
        JTNMPatch jtp = createPatchUI(patch);
        PatchDocument pd = new PatchDocument(jtp);
        return pd;
    }

    public static JTNMPatch createPatchUI(NMPatch patch) throws Exception
    {
        NMContext nmc=
            Nordmodular.sharedContext();
            
        return new JTNMPatch(nmc.getStorageContext(), nmc.getContext(), patch);
    }
    
    private static class NmFileFilter extends FSFileFilter
    {

        public NmFileFilter(FileService service)
        {
            super(service);
        }

        @Override
        public boolean accept(File f)
        {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".pch");
        }

        @Override
        public String getDescription()
        {
            return PATCH_DESCRIPTION;
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
        NMContext nmc=
            Nordmodular.sharedContext();

        try
        {
            NMPatch patch = new NMPatch(nmc.getModuleDescriptions());
            
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

}
