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

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEditSupport;

public class PUndoableEditSupport extends UndoableEditSupport {
    

    public synchronized void beginUpdate(String name) 
    {
        if (updateLevel == 0) {
            compoundEdit = createNamedCompoundEdit(name);
        }
        updateLevel++;
    }
    
    /**
     * Called only from <code>beginUpdate(String)</code>.
     * Exposed here for subclasses' use.
     */
    protected CompoundEdit createNamedCompoundEdit(String name) 
    {
        return new NamedCompoundEdit(name);
    }
    
}
