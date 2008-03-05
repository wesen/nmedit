package net.sf.nmedit.jpatch.history;

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEditSupport;

public class NamedUndoableEditSupport extends UndoableEditSupport {
	public NamedUndoableEditSupport() {
		super();
	}
	
    public synchronized void beginUpdate(String name) {
    	if (updateLevel == 0) {
    	    compoundEdit = new NamedCompoundEdit(name);
    	}
    	updateLevel++;
    }

}
