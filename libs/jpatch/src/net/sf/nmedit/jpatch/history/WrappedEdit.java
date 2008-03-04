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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

public class WrappedEdit<E extends UndoableEdit> implements UndoableEdit
{
    
    private E edit;
    
    public WrappedEdit(E edit)
    {
        this.edit = edit;
    }

    public boolean addEdit(UndoableEdit anEdit)
    {
        return edit.addEdit(anEdit);
    }

    public boolean canRedo()
    {
        return edit.canRedo();
    }

    public boolean canUndo()
    {
        return edit.canUndo();
    }

    public void die()
    {
        edit.die();
    }

    public String getPresentationName()
    {
        return edit.getPresentationName();
    }

    public String getRedoPresentationName()
    {
        return edit.getRedoPresentationName();
    }

    public String getUndoPresentationName()
    {
        return edit.getUndoPresentationName();
    }

    public boolean isSignificant()
    {
        return edit.isSignificant();
    }

    public void redo() throws CannotRedoException
    {
        edit.redo();
    }

    public boolean replaceEdit(UndoableEdit anEdit)
    {
        return edit.replaceEdit(anEdit);
    }

    public void undo() throws CannotUndoException
    {
        edit.undo();
    }
    
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (o instanceof WrappedEdit) return ((WrappedEdit)o).edit.equals(edit);
        return edit.equals(o);
    }
    
    public int hashCode()
    {
        return edit.hashCode();
    }
    
    public String toString()
    {
        return getClass().getSimpleName()+"["+edit.toString()+"]";
    }

}
