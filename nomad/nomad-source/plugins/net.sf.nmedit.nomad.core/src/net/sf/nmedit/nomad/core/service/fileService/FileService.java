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

import javax.swing.Icon;

import net.sf.nmedit.nomad.core.service.Service;

public interface FileService extends Service
{

    FSFileFilter getFileFilter();
    
    boolean isOpenFileOperationSupported();
    
    void open(File file);
    
    boolean isNewFileOperationSupported();
    
    void newFile();

    String getName();

    String getDescription();

    Icon getIcon();

    boolean isDirectSaveOperationSupported(Object source);
    
    boolean isSaveOperationSupported(Object source);

    File getAssociatedFile(Object source);
    
    void save(Object source, File as);
    
    boolean isEditPropertiesSupported(Object source);
    
    void editProperties(Object source);
    
}

