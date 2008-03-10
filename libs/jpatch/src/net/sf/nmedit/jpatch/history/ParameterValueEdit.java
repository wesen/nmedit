package net.sf.nmedit.jpatch.history;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sf.nmedit.jpatch.PParameter;

public class ParameterValueEdit extends AbstractUndoableEdit {
	
	private PParameter parameter;
	private double oldValue;
	private double newValue;
	
	public ParameterValueEdit(PParameter parameter, double oldValue, double newValue) {
		this.parameter = parameter;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

    public String getPresentationName()
    {
        return "modify parameter " + parameter.getName();
    }
    
    public void undo() throws CannotUndoException
    {
        super.undo();
        parameter.setDoubleValue(oldValue);
    }
    
    public void redo() throws CannotRedoException
    {
        super.redo();
        parameter.setDoubleValue(newValue);
    }

}
