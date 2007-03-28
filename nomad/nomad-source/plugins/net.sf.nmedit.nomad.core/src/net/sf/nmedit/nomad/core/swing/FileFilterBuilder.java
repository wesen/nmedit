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
 * Created on Jul 5, 2006
 */
package net.sf.nmedit.nomad.core.swing;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileFilterBuilder
{

    public static FileFilter createFileFilter(String extension, String description)
    {
        return new FileFilterImplementation(extension, description);
    }
    
    private static class FileFilterImplementation
    extends FileFilter
    {
        
        private String ext;
        private String lext;
        private String dsc;

        public FileFilterImplementation( String extension, String description )
        {
            this.ext = extension;
            this.lext = ext.toLowerCase();
            this.dsc = description;
        }

        @Override
        public boolean accept( File f )
        {
            return f.isDirectory() || f.toString().toLowerCase().endsWith('.'+lext);
        }

        @Override
        public String getDescription()
        {
            return dsc+" (*."+ext+")";
        }
        
    }

}
