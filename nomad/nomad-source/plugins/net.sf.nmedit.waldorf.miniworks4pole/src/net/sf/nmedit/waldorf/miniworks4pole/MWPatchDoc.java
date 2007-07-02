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
import javax.swing.JComponent;

import net.sf.nmedit.nomad.core.swing.document.Document;
import net.waldorf.miniworks4pole.jpatch.MWPatch;
import net.waldorf.miniworks4pole.jtheme.JTMWPatch;

public class MWPatchDoc implements Document
{
    
    private JTMWPatch jtpatch;
    private MWPatch patch;
    
    public MWPatchDoc(MWPatch patch)
    {
        this.patch = patch;
    }

    public JComponent getComponent()
    {
        if (jtpatch == null)
            jtpatch = MWData.createPatchUI(patch);
                
        return jtpatch;
    }

    public String getTitle()
    {
        return "MiniWorks";
    }

    public Icon getIcon()
    {
        return null;
    }

    public void dispose()
    {
        jtpatch = null;
    }

    public <T> T getFeature(Class<T> featureClass)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public File getFile()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getProperty(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
