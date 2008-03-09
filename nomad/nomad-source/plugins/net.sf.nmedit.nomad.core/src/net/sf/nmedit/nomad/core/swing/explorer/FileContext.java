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
 * Created on Oct 29, 2006
 */
package net.sf.nmedit.nomad.core.swing.explorer;

import java.awt.Event;
import java.io.File;
import java.io.FileFilter;

import net.sf.nmedit.nomad.core.swing.ExtensionFilter;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;

public class FileContext extends FileNode implements TreeContext 
{

    private FileFilter fileFilter;
    private ExplorerTree tree;
    private String name = null;

    public FileContext( ExplorerTree etree, File file )
    {
        this(etree, null, file);
    }

    public FileContext( ExplorerTree etree, FileFilter fileFilter, File file )
    {
        super(etree.getRoot(), file );
        this.tree = etree;
        this.fileFilter = fileFilter;
    }
    
    public void processEvent(Event event)
    {
        if (event instanceof ContextEvent)
        {
            ContextEvent ce = (ContextEvent) event;
            
            if (ce.getNode() instanceof FileNode)
            {
                //File file = ((FileNode) ce.getNode()).getFile();
                /*
                if (file.isFile())
                    Nomad.sharedInstance().openPatchFiles(
                        new File[]{file});*/
            }
        }
    }
    public FileFilter getFileFilter()
    {
        return fileFilter;
    }
    
    public void setFileFilter(FileFilter f)
    {
        this.fileFilter = f;
        if (updateChildrenNodes(true)) {
        	tree.fireNodeStructureChanged(this);
        }
    }
    
    public String toString()
    {
    	if (getName() != null)
    		return getName();
        if (fileFilter != null && fileFilter instanceof ExtensionFilter)
        {
            ExtensionFilter ef = (ExtensionFilter) fileFilter;
            return super.toString() + " (*."+ef.getExtension()+")";
        }
        else
        {
            return super.toString() + " (*.*)";
        }
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
