package net.sf.nmedit.jpatch.history;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sf.nmedit.jpatch.PParameter;

public class ParameterValueEdit extends AbstractUndoableEdit {
	
	private PParameter parameter;
	private int oldValue;
	private int newValue;
	
	public ParameterValueEdit(PParameter parameter, int oldValue, int newValue) {
		this.parameter = parameter;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

    public String getPresentationName()
    {
        return "modify " + parameter.getName();
    }
    
    public void undo() throws CannotUndoException
    {
        super.undo();
        parameter.setValue(oldValue);
    }
    
    public void redo() throws CannotRedoException
    {
        super.redo();
        parameter.setValue(newValue);
    }

}
