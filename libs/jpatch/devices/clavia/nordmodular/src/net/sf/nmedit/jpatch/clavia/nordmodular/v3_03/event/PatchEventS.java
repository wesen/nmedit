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
 * Created on Jul 6, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;

public class PatchEventS extends PatchEvent
{

    private String oldValue = null;
    private String newValue = null;
    private Patch patch;

    public PatchEventS( Patch patch )
    {
        this.patch = patch;
    }

    public Patch getPatch()
    {
        return patch;
    }
    
    public void noteChanged(String oldNote, String newNote)
    {
        setID(PATCH_NOTE_CHANGED);
        this.oldValue = oldNote;
        this.newValue = newNote;
    }

    public void nameChanged(String oldName, String newName)
    {
        setID(PATCH_NAME_CHANGED);
        this.oldValue = oldName;
        this.newValue = newName;
    }
    
    public String getOldValue()
    {
        return oldValue;
    }
    
    public String getNewValue()
    {
        return newValue;
    }

}
