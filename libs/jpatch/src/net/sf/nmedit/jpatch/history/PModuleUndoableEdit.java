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
    private String oldtitle;
    private String newtitle;
    private Point oldlocation;
    private Point newlocation;
    
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
    
    private PModuleUndoableEdit(int id)
    {
        this.id = id;
    }
    
    private PModuleUndoableEdit(PModule module, Point oldScreenLocation)
    {
        this(MOVE);
        this.module = module;
        this.oldlocation = oldScreenLocation;
        this.newlocation = new Point(module.getScreenX(), module.getScreenY());
    }
    
    private PModuleUndoableEdit(PModule module, String oldTitle)
    {
        this(RENAME);
        this.module = module;
        this.oldtitle = oldTitle;
        this.newtitle = module.getTitle();
    }

    protected PModuleUndoableEdit(PModuleContainer container, PModule module, int id)
    {
        this(id);
        this.container = container;
        this.module = module;
    }

    private String getTitleInQuotes(String title)
    {
        if (title == null) return "\"\"";
        return "\""+title+"\"";
    }
    
    public void redo() throws CannotRedoException 
    {
        super.redo();
        switch (id)
        {
            case ADD:
                container.add(module);
                break;
            case REMOVE:
                container.remove(module);
                break;
            case MOVE:
                module.setScreenLocation(newlocation);
                break;
            case RENAME:
                module.setTitle(newtitle);
                break;
            default:
                throw new CannotRedoException();
        }
    }

    public void undo() throws CannotUndoException 
    {
        super.undo();
        switch (id)
        {
            case ADD:
                container.remove(module);
                break;
            case REMOVE:
                container.add(module);
                break;
            case MOVE:
                module.setScreenLocation(oldlocation);
                break;
            case RENAME:
                module.setTitle(oldtitle);
                break;
            default:
                throw new CannotUndoException();
        }
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
                return "rename "+getTitleInQuotes(oldtitle)+" to "+getTitleInQuotes(newtitle);
            default:
                return "";
        }
    }

}
