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
 * Created on Oct 30, 2006
 */
package net.sf.nmedit.nomad.core.swing;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFilter extends javax.swing.filechooser.FileFilter implements FileFilter
{

    private String extension;
    private String suffix;
    private boolean ignoreHidden;
    private String description;
    
    public ExtensionFilter(String description, String extension, boolean ignoreHidden)
    {
        this.description = description;
        this.extension = extension;
        this.suffix = "."+extension.toLowerCase();
        this.ignoreHidden = ignoreHidden;
    }

    public boolean accept( File pathname )
    {
        if (pathname.isHidden() && ignoreHidden)
            return false;
        if (pathname.isDirectory())
            return true;
        
        return pathname.getName().toLowerCase().endsWith(suffix);
    }

    public String getExtension()
    {
        return extension;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

}
