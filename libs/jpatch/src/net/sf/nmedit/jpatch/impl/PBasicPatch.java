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
package net.sf.nmedit.jpatch.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import net.sf.nmedit.jpatch.LayoutTool;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PFactory;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PModuleMetrics;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PSettings;

/**
 * The reference implementation of interface {@link PPatch}.
 * 
 * TODO settings / history / meta data implementation
 * @author Christian Schneider
 */
public class PBasicPatch implements PPatch
{

    private ModuleDescriptions moduleDescriptions;
    private PFactory pfactory;
    private String name;
    private Object focusedComponent; 
    private UndoManager undoManager = new UndoManager();
    private UndoableEditSupport editSupport = new UndoableEditSupport();

    public PBasicPatch(ModuleDescriptions moduleDescriptions)
    {
        this(moduleDescriptions, PBasicFactory.getSharedInstance());
    }

    public PBasicPatch(ModuleDescriptions moduleDescriptions, PFactory pfactory)
    {
        this.moduleDescriptions = moduleDescriptions;
        this.pfactory = pfactory;
        editSupport.addUndoableEditListener(undoManager);
    }

    public UndoManager getUndoManager()
    {
        return undoManager;
    }

    public UndoableEditSupport getUndoableEditSupport()
    {
        return editSupport;
    }
    
    public ModuleDescriptions getModuleDescriptions()
    {
        return moduleDescriptions;
    }
    
    public PModule createModule(PModuleDescriptor d)
    {
        return getComponentFactory().createModule(d);
    }

    public PFactory getComponentFactory()
    {
        return pfactory;
    }

    public PModuleContainer getModuleContainer(int index)
    {
        throw new UnsupportedOperationException("not implemented");
    }

    public int getModuleContainerCount()
    {
        throw new UnsupportedOperationException("not implemented");
    }

    public PModuleMetrics getModuleMetrics()
    {
        return getModuleDescriptions().getMetrics();
    }

    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public PSettings getSettings()
    {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean setFocusedComponent(Object f)
    {
        if (focusedComponent != f)
        {
            if (f == null || (f != null && requestFocus(f)))
            {
                this.focusedComponent = f;
                return true;
            }
        }
        return false;
    }
    
    protected boolean requestFocus(Object f)
    {   
        return (f instanceof PParameter);
    }

    public Object getFocusedComponent()
    {
        return focusedComponent;
    }

	public String patchFileString() {
		return null;
	}

	public PPatch createEmptyPatch() {
		return null;
	}

	public PPatch createFromFile(File file) {
		return null;
	}

    
}
