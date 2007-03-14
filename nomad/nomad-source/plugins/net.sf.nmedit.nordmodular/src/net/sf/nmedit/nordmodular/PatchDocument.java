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
import java.net.URI;

import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.jtheme.component.JTPatch;
import net.sf.nmedit.nomad.core.util.document.Document;

public class PatchDocument implements Document
{
    
    private JTNMPatch jtpatch;
    private URI uri;

    public PatchDocument(JTNMPatch patch)
    {
        this.jtpatch = patch;
    }
    
    public void setURI(File file)
    {
        setURI(file.toURI());
    }
    
    public void setURI(URI uri)
    {
        this.uri = uri;
    }
    
    public URI getURI()
    {
        return uri;
    }

    public JTPatch getComponent()
    {
        return jtpatch;
    }

    public String getTitle()
    {
        System.out.println("getTitle: "+jtpatch.getPatch().getName());
        
        return jtpatch.getPatch().getName();
    }

}

