/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.jpatch.history;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class PUndoManager extends UndoManager
{

    private boolean modified = false;
    
    public synchronized boolean isModified() 
    {
        return modified;
    }
    
    public synchronized void setModified(boolean modified) 
    {
        this.modified = modified;
    }
    
    public synchronized boolean addEdit(UndoableEdit anEdit) 
    {
        if (super.addEdit(anEdit))
        {
            this.modified = true;
            return true;
        }
        return false;
    }

}
