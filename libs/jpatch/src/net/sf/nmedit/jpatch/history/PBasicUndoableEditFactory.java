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

import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PUndoableEditFactory;

public class PBasicUndoableEditFactory implements PUndoableEditFactory
{
    
    private boolean ignoreEdit = false;
    
    public synchronized boolean isIgnoreEditEnabled()
    {
        return ignoreEdit;
    }
    
    private synchronized void setIgnoreEditEnabled(boolean enable)
    {
        this.ignoreEdit = enable;
    }

    public UndoableEdit createRenameEdit(PModule module, String oldtitle, String newtitle)
    {
        return wrap(new ModuleRenameEdit(module, oldtitle, newtitle));
    }

    public UndoableEdit createMoveEdit(PModule module, int oldScreenX, int oldScreenY, int newScreenX, int newScreenY)
    {
        return wrap(new MoveEdit(module, oldScreenX, oldScreenY, newScreenX, newScreenY));
    }

    public UndoableEdit createConnectEdit(PConnector a, PConnector b)
    {
        return wrap(new ConnectEdit(a, b, false));
    }

    public UndoableEdit createDisconnectEdit(PConnector a, PConnector b)
    {
        return wrap(new ConnectEdit(a, b, true));
    }

    public UndoableEdit createAddEdit(PModuleContainer container, PModule module, int index)
    {
        return wrap(new ModuleAddEdit(container, module, index, false));
    }

    public UndoableEdit createRemoveEdit(PModuleContainer container, PModule module, int index)
    {
        return wrap(new ModuleAddEdit(container, module, index, true));
    }

    public UndoableEdit createPatchNameEdit(PPatch patch, String oldname, String newname)
    {
        return wrap(new PatchNameEdit(patch, oldname, newname));
    }

    public UndoableEdit createParameterValueEdit(PParameter parameter, int oldValue, int newValue)
    {
        return wrap(new ParameterValueEdit(parameter, oldValue, newValue));
    }

    private <E extends UndoableEdit> UndoableEdit wrap(E edit)
    {
        // wrap atomic edits so they don't mess up the undo/redo history
        return new AtomicEdit<E>(edit);
    }
    
    private class AtomicEdit<E extends UndoableEdit> extends WrappedEdit<E>
    {

        public AtomicEdit(E edit)
        {
            super(edit);
        }

        public void die()
        {
            super.die();
            if (HistoryUtils.DEBUG) System.out.println("dead: "+this);
        }
        public void undo() throws CannotUndoException
        {
            try
            {
                if (HistoryUtils.DEBUG) System.out.println("undo: "+this);
                setIgnoreEditEnabled(true);
                super.undo();
            }
            finally
            {
                setIgnoreEditEnabled(false);   
            }
        }
        
        public void redo() throws CannotRedoException
        {
            try
            {
                if (HistoryUtils.DEBUG) System.out.println("redo: "+this);
                setIgnoreEditEnabled(true);
                super.redo();
            }
            finally
            {
                setIgnoreEditEnabled(false);   
            }
        }
        
    }

}
