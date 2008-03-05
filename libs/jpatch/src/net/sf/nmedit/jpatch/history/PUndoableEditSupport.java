package net.sf.nmedit.jpatch.history;

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEditSupport;

public class PUndoableEditSupport extends UndoableEditSupport {
    

    public synchronized void beginUpdate(String name) 
    {
        if (updateLevel == 0) {
            compoundEdit = createNamedCompoundEdit(name);
        }
        updateLevel++;
    }
    
    /**
     * Called only from <code>beginUpdate(String)</code>.
     * Exposed here for subclasses' use.
     */
    protected CompoundEdit createNamedCompoundEdit(String name) 
    {
        return new NamedCompoundEdit(name);
    }
    
}
