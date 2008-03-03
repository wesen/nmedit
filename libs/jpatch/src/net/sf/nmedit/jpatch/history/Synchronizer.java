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

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import net.sf.nmedit.jpatch.AllEventsListener;
import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PParameterEvent;
import net.sf.nmedit.jpatch.history2.PConnectionEdit;
import net.sf.nmedit.jpatch.history2.PModuleUndoableEdit;

public class Synchronizer extends AllEventsListener
{
    
    private UndoManager undoableEditSupport;
    
    private boolean isHistoryEnabled()
    {
        return true;
    }

    public Synchronizer(UndoManager undoableEditSupport)
    {
        this.undoableEditSupport = undoableEditSupport;
        listenConnections = true;
        listenModules = true;
        listenParameters = false;
    }
    
    public void moduleAdded(PModuleContainerEvent e)
    {
        super.moduleAdded(e);
        if (isHistoryEnabled())
            undoableEditSupport.addEdit(PModuleUndoableEdit.editAdd(e.getModule()));
    }

    public void moduleRemoved(PModuleContainerEvent e)
    {
        super.moduleRemoved(e);
        if (isHistoryEnabled())
            undoableEditSupport.addEdit(PModuleUndoableEdit.editRemove(e.getContainer(), e.getModule()));
    }

    public void connectionAdded(PConnectionEvent e)
    {
        if (isHistoryEnabled())
            undoableEditSupport.addEdit(PConnectionEdit.editConnect(e.getSource(), e.getDestination()));
    }

    public void connectionRemoved(PConnectionEvent e)
    {
        if (isHistoryEnabled())
            undoableEditSupport.addEdit(PConnectionEdit.editDisconnect(e.getSource(), e.getDestination()));
    }

    public void parameterValueChanged(PParameterEvent e)
    {
        super.parameterValueChanged(e);
        if (!isHistoryEnabled())
            return;
        // not supported
    }

    public void moduleMoved(PModuleEvent e)
    {
        if (!isHistoryEnabled())
            return;
        if (isHistoryEnabled())
            undoableEditSupport.addEdit(PModuleUndoableEdit.editMove(e.getModule(), new Point(e.getOldScreenX(), e.getOldScreenY())));
    }

    public void moduleRenamed(PModuleEvent e)
    {
        if (!isHistoryEnabled())
            return;
        if (isHistoryEnabled())
            undoableEditSupport.addEdit(PModuleUndoableEdit.editRename(e.getModule(), e.getOldName()));
    }
    
}
