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
package net.sf.nmedit.jpatch;

import java.util.Collection;

import net.sf.nmedit.jpatch.event.PModuleContainerListener;

public interface PModuleContainer extends PComponent, Iterable<PModule>
{

    PConnectionManager getConnectionManager();

    PPatch getPatch();
    
    /**
     * Adds a module to this container.
     * 
     * @param module
     * @return
     */
    boolean add(PModule module);
    
    boolean add(int index, PModule module);
    
    boolean remove(PModule module);
    
    int getModuleCount();
    
    PModule getModule(int index);
    
    int indexOf(PModule module);

    boolean contains(PModule module);
    
    public Collection<? extends PModule> getModules();
    public Collection<? extends PModule> getModulesWithDescriptor(PModuleDescriptor descriptor);

    void addModuleContainerListener(PModuleContainerListener l);
    void removeModuleContainerListener(PModuleContainerListener l);
    
    PModule createModule(PModuleDescriptor d);
    
    PModuleMetrics getModuleMetrics();

    MoveOperation createMoveOperation();
    CopyOperation createCopyOperation();
    
    PPatch createPatchWithModules(Collection<? extends PModule> collection);

    boolean canAdd(PModuleDescriptor descriptor);
    
}
