package net.sf.nmedit.jpatch.history.edit;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.history.HistoryUtils;

public class ModuleRenameEdit extends AbstractUndoableEdit
{

    private PModule module;
    private String oldtitle;
    private String newtitle;

    public ModuleRenameEdit(PModule module, String oldtitle, String newtitle)
    {
        this.module = module;
        this.oldtitle = oldtitle;
        this.newtitle = newtitle;
    }
    
    public void undo() throws CannotUndoException
    {
        super.undo();
        module.setTitle(oldtitle);
    }

    public void redo() throws CannotUndoException
    {
        super.redo();
        module.setTitle(newtitle);
    }
    
    public String getPresentationName()
    {
        return "rename to "+HistoryUtils.quote(newtitle);
    }
    
}
