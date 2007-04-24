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

/*
 * Created on Nov 30, 2006
 */
package net.sf.nmedit.jpatch;

import java.util.Collection;

import net.sf.nmedit.jpatch.event.ModuleContainerListener;

/**
 * The module container shows a module from it's inside.
 * 
 * @author Christian Schneider
 */
public interface ModuleContainer extends Component, Iterable<Module>
{

    /**
     * @throws UnsupportedOperationException if the operation is not supported
     */
    MoveOperation createMoveOperation();
    
    /**
     * Returns the patch this module container belongs to. 
     * @return the patch this module container belongs to.
     */
    Patch getPatch();
    
    /**
     * Creates a new module which is defined by the specified ModuleDescriptor. 
     * Only modules created with this method should be added to this container.
     * 
     * @param descriptor descriptor which defines the requested module
     * @return the module defined by the specified descriptor
     * @throws InvalidDescriptorException if the descriptor is invalid. For example
     *   when the described module is not compatible with this patch.
     */
    Module createModule(ModuleDescriptor descriptor)
        throws InvalidDescriptorException;
    
    /**
     * Adds the specified module to this container.
     * @param module the module to be added
     */
    void add(Module module);
    
    /**
     * Removes the specified module from this container
     * @param module the module to be removed
     */
    void remove(Module module);
    
    /**
     * Returns the number of modules which are added to this container. 
     * @return the number of modules which are added to this container.
     */
    int getModuleCount();
    
    /**
     * Returns true if the specified module exists in this container
     * @param module
     * @return
     */
    boolean contains(Module module);

    /**
     * Returns the ConnectionManager of this container.
     * If this container does not allow connections <code>null</code>
     * will be returned.
     * 
     * @return the ConnectionManager of this container
     */
    ConnectionManager getConnectionManager();

    /**
     * Adds the specified ModuleContainerListener.
     * @param l the listener
     */
    void addModuleContainerListener(ModuleContainerListener l);
    
    /**
     * Removes the specified ModuleContainerListener.
     * @param l the listener
     */
    void removeModuleContainerListener(ModuleContainerListener l);
    
}
