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

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;

public class PModuleUndoableEdit extends AbstractUndoableEdit
{

    public static final int ADD = 0;
    public static final int REMOVE = 1;
    public static final int RENAME = 2;
    public static final int MOVE = 3;

    protected int id;
    protected PModule module;
    private PModuleContainer container;
    private String title;
    private Point location;
    
    public static UndoableEdit editAdd(PModule module)
    {
        return new PModuleUndoableEdit(module.getParentComponent(), module, ADD);
    }
    
    public static UndoableEdit editRemove(PModuleContainer parent, PModule module)
    {
        return new PModuleUndoableEdit(parent, module, REMOVE);
    }

    public static UndoableEdit editMove(PModule module, Point oldScreenLocation)
    {
        return new PModuleUndoableEdit(module, oldScreenLocation);
    }

    public static UndoableEdit editRename(PModule module, String oldTitle)
    {
        return new PModuleUndoableEdit(module, oldTitle);
    }
    
    private PModuleUndoableEdit(PModule module, Point oldScreenLocation)
    {
        this.module = module;
        this.location = oldScreenLocation;
        this.id = MOVE;
    }
    
    private PModuleUndoableEdit(PModule module, String oldTitle)
    {
        this.module = module;
        this.title = oldTitle;
        this.id = RENAME;
    }

    protected PModuleUndoableEdit(PModuleContainer container, PModule module, int id)
    {
        this.container = container;
        this.module = module;
        this.id = id;
    }

    private String getTitleInQuotes(String title)
    {
        if (title == null) return "\"\"";
        return "\""+title+"\"";
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
            case ADD:
                return "add "+getTitleInQuotes(module.getTitle());
            case REMOVE:
                return "remove "+getTitleInQuotes(module.getTitle());
            case MOVE:
                return "move "+getTitleInQuotes(module.getTitle());
            case RENAME:
                return "rename "+getTitleInQuotes(module.getTitle())+" to "+getTitleInQuotes(title);
            default:
                return "";
        }
    }

    private boolean undo_or_redo(boolean isUndo)
    {
        switch (id)
        {
            case ADD:
                removeModule();
                return true;
            case REMOVE:
                addModule();
                return true;
            case MOVE:
                moveModule();
                return true;
            case RENAME:
                renameModule();
                return true;
            default:
                return false;
        }
    }

    private void renameModule()
    {
        module.setTitle(title);
        die();
    }

    private void moveModule()
    {
        module.setScreenLocation(location);
        die();
    }

    private void addModule()
    {
        container.add(module);
        die();
    }

    private void removeModule()
    {
        container.remove(module);
        die();
    }

}
