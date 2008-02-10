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
package net.sf.nmedit.nomad.core.service.fileService;

import java.io.File;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import net.sf.nmedit.nomad.core.service.ServiceRegistry;
import net.sf.nmedit.nomad.core.service.fileService.FileService;

public class FileServiceTool
{

    public static void addChoosableFileFilters(JFileChooser chooser)
    {
        for (Iterator<FileService> i = ServiceRegistry.getServices(FileService.class);
         i.hasNext();)
        {
            FileService service = i.next();
            if (service.isOpenFileOperationSupported())
                chooser.addChoosableFileFilter(service.getFileFilter());
        }
    }

    public static FileService lookupFileService(JFileChooser chooser)
    {
        FileFilter fileFilter = chooser.getFileFilter();
        return (fileFilter == null) ? null : lookupFileService(fileFilter);
    }

    private static FileService lookupFileService(FileFilter fileFilter)
    { 
        return (fileFilter instanceof FSFileFilter) ?
                ((FSFileFilter) fileFilter).getService() : null;
    }
    
    public static FileService lookupFileService(File file) {
    	for (Iterator<FileService> i = ServiceRegistry.getServices(FileService.class);
    	i.hasNext();)
    	{
    		FileService service = i.next();
    		FSFileFilter filter = service.getFileFilter();
    		if (filter.accept(file)) {
    			return service;
    		}
    	}
    	return null;
    }
    
}

