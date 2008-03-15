package net.sf.nmedit.jpatch.history;

import javax.swing.UIManager;
import javax.swing.undo.CompoundEdit;

public class NamedCompoundEdit extends CompoundEdit {
	protected String presentationName;

	public NamedCompoundEdit(String name) {
		super();
		this.presentationName = name;
	}

	public String getPresentationName() {
		return presentationName;
	}

	public String getUndoPresentationName() {
		String name = getPresentationName();
		if (!"".equals(name)) {
			name = UIManager.getString("AbstractUndoableEdit.undoText") +
			" " + name;
		} else {
			name = UIManager.getString("AbstractUndoableEdit.undoText");
		}

		return name;
	}

	public String getRedoPresentationName() {
		String name = getPresentationName();
		if (!"".equals(name)) {
			name = UIManager.getString("AbstractUndoableEdit.redoText") +
			" " + name;
		} else {
			name = UIManager.getString("AbstractUndoableEdit.redoText");
		}

		return name;
	}

}
