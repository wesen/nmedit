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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sf.nmedit.jpatch.PModule;

public class MoveEdit extends AbstractUndoableEdit
{

    private PModule module;
    private int oldScreenX;
    private int oldScreenY;
    private int newScreenX;
    private int newScreenY;

    public MoveEdit(PModule module, int oldScreenX, int oldScreenY,
            int newScreenX, int newScreenY)
    {
        this.module = module;
        this.oldScreenX = oldScreenX;
        this.oldScreenY = oldScreenY;
        this.newScreenX = newScreenX;
        this.newScreenY = newScreenY;
    }
    
    public void undo() throws CannotUndoException
    {
        super.undo();
        module.setScreenLocation(oldScreenX, oldScreenY);
    }
    
    public void redo() throws CannotRedoException
    {
        super.redo();
        module.setScreenLocation(newScreenX, newScreenY);
    }
    
    public String getPresentationName()
    {
        return "move "+HistoryUtils.quote(module.getTitle());
    }
    
}
