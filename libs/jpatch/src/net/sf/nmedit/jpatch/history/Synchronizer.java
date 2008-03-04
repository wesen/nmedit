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
package net.sf.nmedit.jpatch.history;

import java.awt.Point;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import net.sf.nmedit.jpatch.AllEventsListener;
import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PParameterEvent;

public class Synchronizer extends AllEventsListener
{
    
    private UndoableEditSupport editSupport;
    private UndoManager undoManager;
    private boolean ignoreEdit = false;
    
    private boolean getIgnoreEdit()
    {
        return ignoreEdit;
    }
    
    private void setIgnoreEdit(boolean ignore)
    {
        this.ignoreEdit = ignore;
    }
    
    private boolean isHistoryEnabled()
    {
        return false;// disabled while broken
    }

    public Synchronizer(UndoManager undoManager, UndoableEditSupport editSupport)
    {
        this.undoManager = undoManager;
        this.editSupport = editSupport;
        listenConnections = true;
        listenModules = true;
        listenParameters = false;
    }
    
    public void moduleAdded(PModuleContainerEvent e)
    {
        super.moduleAdded(e);
        if (isHistoryEnabled())
            postEdit(PModuleUndoableEdit.editAdd(e.getModule()));
    }

    public void moduleRemoved(PModuleContainerEvent e)
    {
        super.moduleRemoved(e);
        if (isHistoryEnabled())
            postEdit(PModuleUndoableEdit.editRemove(e.getContainer(), e.getModule()));
    }

    public void connectionAdded(PConnectionEvent e)
    {
        if (isHistoryEnabled())
            postEdit(PConnectionEdit.editConnect(e.getSource(), e.getDestination()));
    }

    public void connectionRemoved(PConnectionEvent e)
    {
        if (isHistoryEnabled())
            postEdit(PConnectionEdit.editDisconnect(e.getSource(), e.getDestination()));
    }

    public void parameterValueChanged(PParameterEvent e)
    {
        // no op
    }

    public void moduleMoved(PModuleEvent e)
    {
        if (isHistoryEnabled())
            postEdit(PModuleUndoableEdit.editMove(e.getModule(), new Point(e.getOldScreenX(), e.getOldScreenY())));
    }

    public void moduleRenamed(PModuleEvent e)
    {
        if (isHistoryEnabled())
            postEdit(PModuleUndoableEdit.editRename(e.getModule(), e.getOldName()));
    }
    
    private void postEdit(UndoableEdit edit)
    {
        //if (!getIgnoreEdit())
            editSupport.postEdit(new SynchronizerEdit(this, edit));
    }
    
    private static class SynchronizerEdit implements UndoableEdit
    {

        private Synchronizer synchronizer;
        private UndoableEdit edit;
        private boolean hasBeenDone = true;

        public SynchronizerEdit(Synchronizer synchronizer, UndoableEdit edit)
        {
            this.synchronizer = synchronizer;
            this.edit = edit;
        }
        
        public boolean addEdit(UndoableEdit anEdit)
        {
            return false;
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

        public boolean replaceEdit(UndoableEdit anEdit)
        {
            return edit.replaceEdit(anEdit);
        }

        public boolean canUndo()
        {
            return edit.canUndo() && hasBeenDone;
        }

        public boolean canRedo()
        {
            return edit.canRedo() && !hasBeenDone;
        }

        public void undo() throws CannotUndoException
        {
            try
            {
                hasBeenDone = true;
                //synchronizer.setIgnoreEdit(true);
                edit.undo();
            }
            finally
            {
                synchronizer.setIgnoreEdit(false);
            }
        }

        public void redo() throws CannotRedoException
        {
            try
            {
                hasBeenDone = false;
                //synchronizer.setIgnoreEdit(true);
                edit.redo();
            }
            finally
            {
                synchronizer.setIgnoreEdit(false);
            }
        }
        
    }
    
}
