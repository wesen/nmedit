/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.nmedit.jtheme.component;

import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.plaf.mcui.ContainerAction;
import net.sf.nmedit.jtheme.help.HelpHandler;
import net.sf.nmedit.nmutils.swing.CopyCutPasteTarget;

public class JTPatch extends JComponent implements CopyCutPasteTarget
{

    /**
     * 
     */
    private static final long serialVersionUID = 794652007213898614L;
    private JTContext context;
    protected ArrayList<JTModuleContainer> moduleContainers;
    private HelpHandler helpHandler;
    
    
    public JTPatch(JTContext context)
    {
    	moduleContainers = new ArrayList<JTModuleContainer>();
        this.context = context;
    }
    
    public HelpHandler getHelpHandler()
    {
        return helpHandler;
    }
    
    public void setHelpHandler(HelpHandler h)
    {
        this.helpHandler = h;
    }
    
    public JTModuleContainer[] getModuleContainers() 
    {
    	return moduleContainers.toArray(new JTModuleContainer[moduleContainers.size()]);
    }
    
    public Collection<? extends JTModule> getSelectedModules() {
    	for (JTModuleContainer jtc : moduleContainers) {
    		if (jtc.getSelectionSize() > 0)
    			return jtc.getSelectedModules();
    	}
    	
    	return new HashSet<JTModule>();
    }

    public Collection<? extends PModule> getSelectedPModules() {
    	for (JTModuleContainer jtc : moduleContainers) {
    		if (jtc.getSelectionSize() > 0)
    			return jtc.getSelectedPModules();
    	}
    	
    	return new HashSet<PModule>();
    }

    
    public void addModuleContainer(JTModuleContainer container) {
    	moduleContainers.add(container);
    }

    public void removeModuleContainer(JTModuleContainer container) {
    	moduleContainers.remove(container);
    }

    public JTContext getContext()
    {
        return context;
    }

    public PPatch getPatch()
    {
        return null;
    }
    
    public PPatch newPatchWithModules(JTModule modules[]) {
    	return null;
    }

	public boolean canCopy() {
		Collection<? extends JTModule> modules = getSelectedModules();
		return modules.size() > 0;
	}

	public boolean canCut() {
		Collection<? extends JTModule> modules = getSelectedModules();
		return modules.size() > 0;
	}

	public boolean canPaste() {
		if (getFocusedContainer() != null)
			return true;
		else
			return false;
	}

	public JTModuleContainer getFocusedContainer() {
		for (JTModuleContainer mc : getModuleContainers()) {
			return mc;
		}
		return null;
	}
	
	private void performContainerActionOnSelected(String actionName, Clipboard clipBoard) {
		Collection<? extends JTModule> modules = getSelectedModules();
		ContainerAction action = new ContainerAction(getFocusedContainer(), actionName, clipBoard);
		action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}
	
	public void performCopy(Clipboard clipBoard) {
		performContainerActionOnSelected(ContainerAction.COPY, clipBoard);
	}

	public void performCut(Clipboard clipBoard) {
		performContainerActionOnSelected(ContainerAction.CUT, clipBoard);
	}

	public void performPaste(Clipboard clipBoard) {
		performContainerActionOnSelected(ContainerAction.PASTE, clipBoard);
	}

}

