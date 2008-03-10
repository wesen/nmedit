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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.CopyOperation;
import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleContainerDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PModuleMetrics;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PUndoableEditFactory;
import net.sf.nmedit.jpatch.dnd.ModulesBoundingBox;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jpatch.history.PUndoableEditSupport;
import net.sf.nmedit.nmutils.collections.ArrayMap;

/**
 * The reference implementation of interface {@link PModuleContainer}.
 * @author Christian Schneider
 */
public class PBasicModuleContainer extends PBasicComponent<PModuleContainerDescriptor> implements
        PModuleContainer
{
    
    private ArrayMap<PModule> modules;
    private PPatch patch;
    private PConnectionManager connectionManager;
    private EventListenerList listenerList = new EventListenerList();
    private transient PModuleContainerEvent mcEvent;
    private transient PModuleMetrics moduleMetrics;

    public PBasicModuleContainer(PPatch patch, String name, int componentIndex)
    {
        this(patch, new PBasicModuleContainerDescriptor(name, componentIndex), componentIndex);
    }
    
    public PBasicModuleContainer(PPatch patch, PModuleContainerDescriptor descriptor, int componentIndex)
    {
        super(descriptor, componentIndex);
        modules = new ArrayMap<PModule>();
        modules.setMinKey(1);
        this.patch = patch;
        this.connectionManager = createConnectionManager();
    }

    public PUndoableEditSupport getEditSupport()
    {
        return patch != null ? patch.getEditSupport() : null;
    }
    
    public void postEdit(UndoableEdit edit)
    {
        if (patch != null)
            patch.postEdit(edit);
    }

    public PUndoableEditFactory getUndoableEditFactory()
    {
        return patch.getUndoableEditFactory();
    }
    
    public boolean isUndoableEditSupportEnabled()
    {
        return patch != null && patch.isUndoableEditSupportEnabled();
    }
    
    protected PConnectionManager createConnectionManager()
    {
        return new PBasicConnectionManager(this);
    }

    public void addModuleContainerListener(PModuleContainerListener l)
    {
        listenerList.add(PModuleContainerListener.class, l);
    }
    
    public void removeModuleContainerListener(PModuleContainerListener l)
    {
        listenerList.remove(PModuleContainerListener.class, l);   
    }

    protected void fireModuleAdded(PModule module)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        if (mcEvent != null)
            mcEvent.moduleAdded(module, module.getComponentIndex());

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PModuleContainerListener.class) 
            {
                // Lazily create the event:
                if (mcEvent == null)
                {
                    mcEvent = new PModuleContainerEvent(this);
                    mcEvent.moduleAdded(module, module.getComponentIndex());
                }
                ((PModuleContainerListener)listeners[i+1]).moduleAdded(mcEvent);
            }
        }
    }

    protected void registerModule(PModule module)
    {
        // no op
    }
    
    protected void unregisterModule(PModule module)
    {
        // no op
    }

    protected void fireModuleRemoved(PModule module, int oldIndex)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        if (mcEvent != null)
            mcEvent.moduleRemoved(module, oldIndex);

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PModuleContainerListener.class) 
            {
                // Lazily create the event:
                if (mcEvent == null)
                {
                    mcEvent = new PModuleContainerEvent(this);
                    mcEvent.moduleRemoved(module, oldIndex);
                }
                ((PModuleContainerListener)listeners[i+1]).moduleRemoved(mcEvent);
            }
        }
    }
    
    protected void moduleRemoved(PModule module, int index)
    {
        if (isUndoableEditSupportEnabled())
        {
            PUndoableEditFactory factory = getUndoableEditFactory();
            if (factory != null)
            {
                UndoableEdit edit = factory.createRemoveEdit(this, module, index);
                if (edit != null) postEdit(edit);
            }
        }
    }
    
    protected void moduleAdded(PModule module, int index)
    {
        if (isUndoableEditSupportEnabled())
        {
            PUndoableEditFactory factory = getUndoableEditFactory();
            if (factory != null)
            {
                UndoableEdit edit = factory.createAddEdit(this, module, index);
                if (edit != null) postEdit(edit);
            }
        }
    }

    public boolean add(PModule module)
    {
        return add(modules.generateIndex(), module);
    }

    public boolean add(int index, PModule module)
    {
        if (!canAdd(index, module))
            return false;
        configure(module, index);
        modules.put(index, module);
        registerModule(module);
        
        
        moduleAdded(module, index);
        fireModuleAdded(module);
        
        /*
        MoveOperation move = createMoveOperation();
        if (move != null)
        {
            move.setScreenOffset(0, 0);
            move.add(module);
            move.move();
        }*/
        
        
        
        return true;   
    }
    
    protected void configure(PModule module, int index)
    {
        if (module instanceof PBasicModule)
        {
            ((PBasicModule) module).setComponentIndex(index);
            ((PBasicModule) module).setParent(this); 
        }
        else throw new IllegalArgumentException("incompatible module: "+module);
    }
    
    protected void revertConfigure(PModule module)
    {
        if (module instanceof PBasicModule)
        {
            ((PBasicModule) module).setComponentIndex(-1);
            ((PBasicModule) module).setParent(null); 
        }
        else throw new IllegalArgumentException("incompatible module: "+module);
    }
    
    protected boolean canAdd(int index, PModule module)
    {
        if (modules.get(index)!=null)
            return false;
        return canAdd(module.getDescriptor());
    }

    public PConnectionManager getConnectionManager()
    {
        return connectionManager;
    }
    
    public PModule getModule(int index)
    {
        return modules.get(index);
    }

    public int getModuleCount()
    {
        return modules.size();
    }
    
    public Collection<? extends PModule> getModules() {
    	ArrayList<PModule> result = new ArrayList<PModule>();
    	for (PModule m : modules)
    		if (m != null)
    			result.add(m);
    	return result;
    }

    public PPatch getPatch()
    {
        return patch;
    }

    public boolean contains(PModule module)
    {
        return indexOf(module)>=0;
    }
    
    public int indexOf(PModule module)
    {
        int index = module.getComponentIndex();
        if (index>=0 && modules.get(index) == module)
        {
            return index;
        }
        index = modules.indexOf(module);
        if (module instanceof PBasicModule)
            ((PBasicModule) module).setComponentIndex(index);
        
        return index;
    }

    public boolean remove(PModule module)
    {
        int index = indexOf(module);
        if (index>=0)
        {
            module.removeAllConnections();
            modules.remove(index);
            unregisterModule(module);
            moduleRemoved(module, index);
            fireModuleRemoved(module, index);
            revertConfigure(module);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Iterator<PModule> iterator()
    {
        return modules.iterator();
    }

    public PModule createModule(PModuleDescriptor d)
    {
        return getPatch().createModule(d);
    }

    public PModuleMetrics getModuleMetrics()
    {
        if (moduleMetrics == null)
        {
            PPatch p = getPatch();
            if (p != null) moduleMetrics = p.getModuleMetrics();
        }
        return moduleMetrics;
    }

    public MoveOperation createMoveOperation()
    {
        return null;
    }

    public CopyOperation createCopyOperation()
    {
        return null;
    }

	public PPatch createPatchWithModules(Collection<? extends PModule> modules) {
		PPatch patch = getPatch();
		int mcIdx = patch.getModuleContainerIndex(this);
		PPatch newPatch = patch.createEmptyPatch();
		PModuleContainer dstMc = newPatch.getModuleContainer(mcIdx);
		
		CopyOperation copy = createCopyOperation();
		copy.setDestination(dstMc);
        for (PModule module: modules) {
            copy.add(module);
        }
        ModulesBoundingBox bbox = new ModulesBoundingBox(modules, new Point(0, 0));
        Rectangle r = bbox.getBoundingBox();
        copy.setScreenOffset(-r.x, -r.y);
        copy.copy();
		return newPatch;
	}

    public boolean canAdd(PModuleDescriptor descriptor)
    {
        int limit = descriptor.getLimit();
        if (limit>=0)
        {
            for (PModule m: this)
            {
                if (m.getDescriptor().equals(descriptor))
                    if (--limit<=0) break;
            }
            return  limit>0;
        }
        return true;
    }

	public Collection<? extends PModule> getModulesWithDescriptor(PModuleDescriptor descriptor) {
		ArrayList<PModule> result = new ArrayList<PModule>();
		for (PModule m : this) { 
			if (m.getDescriptor().equals(descriptor))
				result.add(m);
		}
		
		return result;
	}

}
