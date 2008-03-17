/**
 * 
 */
package net.sf.nmedit.jtheme.component.plaf.mcui;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PatchUtils;
import net.sf.nmedit.jpatch.dnd.PModuleTransferDataWrapper;
import net.sf.nmedit.jpatch.history.PUndoableEditSupport;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.util.ModuleImageRenderer;

public class ContainerAction extends AbstractAction
{
    
    /**
	 * 
	 */
	private JTModuleContainer jmc;
	/**
     * 
     */
    private static final long serialVersionUID = 7135918324094843867L;
    public static final String DELETE_UNUSED = "delete.unused";
    public static final String DELETE = "delete";
    public static final String SELECT_ALL = "selectAll";
    public static final String COPY = "copy";
    public static final String PASTE = "paste";
    public static final String CUT = "cut";
    public static final String SHAKE = "Shake";
    public static final String ABORT_PASTE = "abortPaste";
    
    private Clipboard clipBoard = null;

    public ContainerAction(JTModuleContainer moduleContainer, String command) {
    	this(moduleContainer, command, null);
    }
    
    public ContainerAction(JTModuleContainer moduleContainer, String command, Clipboard clipBoard)
    {
    	this.clipBoard = clipBoard;
        this.jmc = moduleContainer;
        putValue(ACTION_COMMAND_KEY, command);
        if (command == DELETE_UNUSED)
        {
            putValue(NAME, "Delete Unused Modules");
        } 
        else if (command == DELETE)
        {
            putValue(NAME, "Delete");
        }
        else if (command == SHAKE)
        {
            JTCableManager cm = jmc.getCableManager();
            setEnabled(cm != null);
            putValue(NAME, "Shake");
        }
        else {
        	putValue(NAME, command);
        }
    }
    
    protected PModuleContainer getTarget()
    {
        return this.jmc.getModuleContainer();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (isEnabled())
        {
        	Object key = getValue(ACTION_COMMAND_KEY);
            if (key==DELETE_UNUSED)
            {
            	deleteUnusedModules();
            } else if (key == SELECT_ALL) {
            	if (jmc != null) {
            		for (JTModule module : jmc.getModules())
            			jmc.addSelection(module);
            	}
            		
            } else if (key == DELETE) {
            	delete();
            } else if (key == COPY) {
            	if (getClipBoard() != null)
            		copy();
            } else if (key == PASTE) {
            	if (getClipBoard() != null)
            		paste();
            } else if (key == CUT) {
            	if (getClipBoard() != null)
            		cut();
            } else if (key == ABORT_PASTE) {
            	jmc.getUI().abortPaste();
            }
            else if (key == SHAKE)
            {
                JTCableManager cm = jmc.getCableManager();
                if (cm != null) cm.shake();
            }
        }
    }
    
	private void delete()
    {
        Component[] components = jmc.getComponents();
        if (components.length>0)
        {
            /* Get UndoableEditSupport from any component in JPatch
             * (in this case from PModuleContainer).
             * ues may be null, usually when there is no undo support.
             */
            PUndoableEditSupport ues = jmc.getModuleContainer().getEditSupport();
            /* didBeginUpdate flag is used to determine if we  called
             * ues.beginUpdate() this when actially an edit happened.
             */
            boolean didBeginUpdate = false;
            
            try
            {
                
                for (JTModule mm : jmc.getModules())
                {
                    if (mm.isSelected())
                    {
                        /* Module mm is selected, thus we do an edit 
                         * and if not done already call ues.beginUpdate()
                         * (=>didBeginUpdate). ues still might be null.
                         * The beginUpdate() call causes that all
                         * edits until the next endUpdate() are collected
                         * into a single undo event.
                         */
                        if ((!didBeginUpdate) && ues != null)
                        {
                            // begin update
                        	if (jmc.getModules().size() > 1)
                        		ues.beginUpdate("delete modules");
                        	else
                        		ues.beginUpdate();
                            // Set beginUpdate flag. Important !!!
                            didBeginUpdate = true;
                        }
                        removeModule(mm);
                    }
                }
                
                // after the remove we have to update the container
                jmc.revalidate(); // revalidates the bounds of the container
                jmc.repaint(); // repaint
                
            }
            finally
            {
                if (didBeginUpdate && ues != null)
                {
                    /* Only if ues.beginUpdate() was called we 
                     * do the endUpdate(). The try/finally statements
                     * ensures the endUpdate() is called, even if 
                     * an exception occures. This is very important
                     * since otherwise an exception would cause the
                     * undo manager to enter an invalid state from
                     * which it will 'never' recover.
                     */
                    ues.endUpdate();
                }
            }
        }
    }

    public boolean isEnabled(Object sender) 
    {
    	Object key = getValue(ACTION_COMMAND_KEY);
        if (key==DELETE_UNUSED) {
            PModuleContainer t = getTarget();
            return t != null && PatchUtils.hasUnusedModules(t);
        } else if (key == DELETE) {
        	if (sender != null && (sender instanceof JTModule)
        			&& !((JTModule)sender).isEnabled())
        		return false;
        	else
        		return true;
        } else if (key == SELECT_ALL) {
        	return true;
        } else {
        	return true;
        }
    }

    private void deleteUnusedModules() {
        PModuleContainer mc = getTarget();
        if (mc != null)
        {
            PUndoableEditSupport ues = mc.getEditSupport();
            try
            {
                if (ues != null)
                    ues.beginUpdate("delete unused modules");
            
                while (PatchUtils.removeUnusedModules(mc)>0);
            } 
            finally
            {
                if (ues != null)
                    ues.endUpdate();
            }
        }
    }
    
    private boolean removeModule(JTModule m)
    {
        PModule nm = m.getModule();
        
        if (nm != null && nm.getParentComponent() != null)
        {
            return nm.getParentComponent().remove(nm);
        }
        return false;
    }

	public void setClipBoard(Clipboard clipBoard) {
		this.clipBoard = clipBoard;
	}

	public Clipboard getClipBoard() {
		return clipBoard;
	}

    private void cut() {
    	copy();
    	delete();
	}

	private void paste() {
		Transferable t = getClipBoard().getContents(this);
		if (t instanceof PModuleTransferDataWrapper) {
			PModuleTransferDataWrapper tdata = (PModuleTransferDataWrapper)t;
			jmc.getUI().startPaste(tdata);
		}
	}

	private void copy() {
    	PPatch newPatch = jmc.getModuleContainer().createPatchWithModules(jmc.getSelectedPModules());
		PModuleContainer newMc = null;
		
		for (int i = 0; i < newPatch.getModuleContainerCount(); i++) {
			newMc = newPatch.getModuleContainer(i);
			if (newMc.getModuleCount() > 0)
				break;
		}
		if (newMc == null)
			return;
		
    	PModuleTransferDataWrapper tdata =new PModuleTransferDataWrapper(newMc, newMc.getModules(), new Point(5, 5));
		ModuleImageRenderer mir = new ModuleImageRenderer(jmc.getSelectedModules());
        mir.setForDragAndDrop(true);
        mir.setPaintExtraBorder(true);
        tdata.setTransferImage(mir.render());

		getClipBoard().setContents(tdata, null);
	}    
}