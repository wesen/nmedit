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
package net.sf.nmedit.waldorf.miniworks4pole;

import java.io.File;

import javax.swing.Icon;

import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.fileService.FSFileFilter;
import net.sf.nmedit.nomad.core.service.fileService.FileService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WMFileService implements FileService
{

    public FSFileFilter getFileFilter()
    {
        throw new UnsupportedOperationException();
    }

    public void open(File file)
    {
        throw new UnsupportedOperationException();
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
            Nomad.sharedInstance()
            .getDocumentManager()
            .add(new MWPatchDoc(MWData.createPatch()));
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
        return "Miniworks 4Pole Patch";
    }

    public boolean isOpenFileOperationSupported()
    {
        return false;
    }

    public String getDescription()
    {
        return "";
    }

    public Icon getIcon()
    {
        return null;
    }

    public boolean isDirectSaveOperationSupported(Object source)
    {
        return false;
    }
    
    public boolean isSaveOperationSupported(Object source)
    {
        return false;
    }

    public void save(Object source, File as)
    {
        throw new UnsupportedOperationException("save not supported");
    }

    public void editProperties(Object source)
    {
        // TODO Auto-generated method stub
        
    }

    public boolean isEditPropertiesSupported(Object source)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public File getAssociatedFile(Object source)
    {
        // TODO Auto-generated method stub
        return null;
    }

}

