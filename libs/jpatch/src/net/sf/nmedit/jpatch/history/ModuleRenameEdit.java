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
import javax.swing.undo.CannotUndoException;

import net.sf.nmedit.jpatch.PModule;

public class ModuleRenameEdit extends AbstractUndoableEdit
{

    private PModule module;
    private String oldtitle;
    private String newtitle;

    public ModuleRenameEdit(PModule module, String oldtitle, String newtitle)
    {
        this.module = module;
        this.oldtitle = oldtitle;
        this.newtitle = newtitle;
    }
    
    public void undo() throws CannotUndoException
    {
        super.undo();
        module.setTitle(oldtitle);
    }

    public void redo() throws CannotUndoException
    {
        super.redo();
        module.setTitle(newtitle);
    }
    
    public String getPresentationName()
    {
        return "rename to "+HistoryUtils.quote(newtitle);
    }
    
}
