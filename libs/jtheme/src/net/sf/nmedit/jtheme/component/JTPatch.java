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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.nmutils.swing.CopyCutPasteTarget;

public class JTPatch extends JComponent implements CopyCutPasteTarget
{

    /**
     * 
     */
    private static final long serialVersionUID = 794652007213898614L;
    private JTContext context;
    protected ArrayList<JTModuleContainer> moduleContainers;

    public JTPatch(JTContext context)
    {
    	moduleContainers = new ArrayList<JTModuleContainer>();
        this.context = context;
    }
    
    public JTModuleContainer[] getModuleContainers() {
    	JTModuleContainer []res = new JTModuleContainer[1];
    	return (JTModuleContainer[]) moduleContainers.toArray(res);
    }
    
    public Collection<? extends JTModule> getSelectedModules() {
    	for (JTModuleContainer jtc : getModuleContainers()) {
    		if (jtc.getSelectionSize() > 0)
    			return jtc.getSelectedModules();
    	}
    	
    	return new HashSet<JTModule>();
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
		return true;
	}

	public boolean canCut() {
		return true;
	}

	public boolean canPaste() {
		return true;
	}

	public void performCopy(Clipboard clipBoard) {
		System.out.println("copy into clipboard " + clipBoard + " i am " + getPatch().getName());
		for (JTModule m : getSelectedModules()) {
			System.out.println("selected " + m);
		}
	}

	public void performCut(Clipboard clipBoard) {
		// no op
	}

	public void performPaste(Clipboard clipBoard) {
		// no op
	}

}

