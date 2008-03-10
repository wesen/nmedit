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
package net.sf.nmedit.jpatch;

import javax.swing.undo.UndoableEdit;

public interface PUndoableEditFactory
{

    UndoableEdit createRenameEdit(PModule module, String oldtitle, String newtitle);
    UndoableEdit createMoveEdit(PModule module, int oldScreenX, int oldScreenY,
            int newScreenX, int newScreenY);
    UndoableEdit createConnectEdit(PConnector a, PConnector b);
    UndoableEdit createDisconnectEdit(PConnector a, PConnector b);
    UndoableEdit createAddEdit(PModuleContainer container,
            PModule module, int index);
    UndoableEdit createRemoveEdit(PModuleContainer container,
            PModule module, int index);
    UndoableEdit createPatchNameEdit(PPatch patch, String oldvalue, String newvalue);
    UndoableEdit createParameterValueEdit(PParameter parameter, int oldValue, int newValue);
    
}
