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

import net.sf.nmedit.jpatch.PConnector;

public class ConnectEdit extends AbstractUndoableEdit
{

    private PConnector a;
    private PConnector b;
    private boolean inverseEdit;

    public ConnectEdit(PConnector a, PConnector b, boolean inverseEdit)
    {
        this.a = a;
        this.b = b;
        this.inverseEdit = inverseEdit;
    }
    
    public String getPresentationName()
    {
        return inverseEdit ? "disconnect" : "connect";
    }
    
    public void undo() throws CannotUndoException
    {
        super.undo();
        doEdit(inverseEdit);
    }
    
    public void redo() throws CannotRedoException
    {
        super.redo();
        doEdit(!inverseEdit);
    }
    
    private boolean doEdit(boolean connect)
    {
        if (connect)
            return a.connect(b);
        else
            return a.disconnect(b);
    }
    
}
