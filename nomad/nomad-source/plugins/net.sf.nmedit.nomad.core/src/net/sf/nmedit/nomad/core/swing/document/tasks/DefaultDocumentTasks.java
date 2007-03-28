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
 * Created on Jun 1, 2006
 */
package net.sf.nmedit.nomad.core.swing.document.tasks;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.nomad.core.swing.document.Document;

public abstract class DefaultDocumentTasks implements DocumentTasks
{
    
    private Map<Document, File> map = new HashMap<Document, File>();
    
    public void setAssociatedFile( Document d, File f )
    {
        if (f==null)
            map.remove(d);
        else
            map.put(d, f);
    }

    public File getAssociatedFile( Document d )
    {
        return map.get(d);
    }

    public int closeSavely( Document... documents )
    {
        int closed = 0;
        
        for (Document d : documents)
        {
            boolean closeConfirmed = false; 
            
            if (isModified(d))
            {
                File file = getAssociatedFile(d);
                if (file == null)
                {
                    // not yet associated with a file
                    // find an appropriate file to save the document in
                    file = getAssociateWithFile(d);
                }
                
                if (file != null)
                {
                    try
                    {
                        saveAs(d, file);
                        // if and only if the document is saved       
                        closeConfirmed = true;
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                }
            }
            else
            {
                closeConfirmed = true;
            }
            
            if (closeConfirmed)
            {
                closeImmediatelly(d);
            }
        }
        
        return closed;
    }

}
