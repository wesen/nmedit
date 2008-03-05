package net.sf.nmedit.jpatch.history;

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
}
