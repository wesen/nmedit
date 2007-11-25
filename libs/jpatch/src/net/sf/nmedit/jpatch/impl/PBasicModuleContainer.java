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

import java.util.Iterator;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleContainerDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PModuleMetrics;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
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
        
        int limit = module.getDescriptor().getLimit();
        if (limit>=0)
        {
            for (PModule m: this)
            {
                if (m.getDescriptor().equals(module.getDescriptor()))
                    if (--limit<=0) break;
            }
            return  limit>0;
        }
        return true;
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

}
