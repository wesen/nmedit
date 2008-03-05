package net.sf.nmedit.jpatch.history;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class PUndoManager extends UndoManager
{

    private boolean modified = false;
    
    public synchronized boolean isModified() 
    {
        return modified;
    }
    
    public synchronized void setModified(boolean modified) 
    {
        this.modified = modified;
    }
    
    public synchronized boolean addEdit(UndoableEdit anEdit) 
    {
        if (super.addEdit(anEdit))
        {
            this.modified = true;
            return true;
        }
        return false;
    }

}
