package net.sf.nmedit.jpatch.history;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;

public class ModuleAddEdit extends AbstractUndoableEdit
{

    private PModuleContainer container;
    private PModule module;
    private int index;
    private boolean inverseEdit;

    public ModuleAddEdit(PModuleContainer container,
            PModule module, int index, boolean inverseEdit)
    {
        this.container = container;
        this.module = module;
        this.index = index;
        this.inverseEdit = inverseEdit;
    }
    
    public String getPresentationName()
    {
        return (inverseEdit ? "remove " : "add ")+HistoryUtils.quote(module.getTitle());
    }

    public void undo() throws CannotUndoException
    {
        super.undo();
        doAdd(inverseEdit);
    }
    
    public void redo() throws CannotRedoException
    {
        super.redo();
        doAdd(!inverseEdit);
    }
    
    private void doAdd(boolean add)
    {
        if (add)
            container.add(index, module);
        else
            container.remove(module);
    }
    
}
