package net.sf.nmedit.jpatch.history;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sf.nmedit.jpatch.PPatch;

public class PatchNameEdit extends AbstractUndoableEdit
{

    private PPatch patch;
    private String oldname;
    private String newname;

    public PatchNameEdit(PPatch patch, String oldname, String newname)
    {
        this.patch = patch;
        this.oldname = oldname;
        this.newname = newname;
    }
    
    public String getPresentationName()
    {
        return "rename patch to "+HistoryUtils.quote(newname);
    }
    
    public void undo() throws CannotUndoException
    {
        super.undo();
        patch.setName(oldname);
    }
    
    public void redo() throws CannotRedoException
    {
        super.redo();
        patch.setName(newname);
    }
    
}
