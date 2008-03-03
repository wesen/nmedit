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
package net.sf.nmedit.jpatch.history2;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.PConnector;

public class PConnectionEdit extends AbstractUndoableEdit
{

    public static final int CONNECT = 0;
    public static final int DISCONNECT = 1;
    
    protected int id;

    private PConnector a;
    private PConnector b;

    public static UndoableEdit editConnect(PConnector a, PConnector b)
    {
        return new PConnectionEdit(a, b, CONNECT);
    }

    public static UndoableEdit editDisconnect(PConnector a, PConnector b)
    {
        return new PConnectionEdit(a, b, DISCONNECT);
    }
    
    protected PConnectionEdit(PConnector a, PConnector b, int id)
    {
        this.id = id;
        this.a = a;
        this.b = b;
    }

    public void redo() throws CannotRedoException 
    {
        super.redo();
        if (!undo_or_redo(false))
            throw new CannotRedoException();
    }

    public void undo() throws CannotUndoException 
    {
        super.undo();
        if (!undo_or_redo(true))
            throw new CannotUndoException();
    }
    
    public String getPresentationName()
    {
        switch (id)
        {
            case CONNECT:
                return "connect";
            case DISCONNECT:
                return "disconnect";
            default:
                return "";
        }
    }

    private boolean undo_or_redo(boolean isUndo)
    {
        switch (id)
        {
            case CONNECT:
                if (a.disconnect(b))
                {
                    die();
                    return true;
                }
                return false;
            case DISCONNECT:
                if (a.connect(b))
                {
                    die();
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
