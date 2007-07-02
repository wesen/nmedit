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
package net.sf.nmedit.jpatch.event;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;

/**
 * Event sent by a {@link PModuleContainer module container}.
 * 
 * @author Christian Schneider
 */
public class PModuleContainerEvent extends PPatchEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = 3352798644873669518L;
    private PModuleContainer container;
    private PModule module;
    private int index = -1;

    protected PModuleContainerEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers, Object arg )
    {
        super( target, when, id, x, y, key, modifiers, arg );
    }

    protected PModuleContainerEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers )
    {
        super( target, when, id, x, y, key, modifiers );
    }

    protected PModuleContainerEvent( Object target, int id, Object arg )
    {
        super( target, id, arg );
    }
    
    public PModuleContainerEvent(PModuleContainer container)
    {
        this(null, -1, null);
        this.container = container;
    }

    public void moduleAdded(PModule module, int index)
    {
        this.id = MODULE_ADDED;
        setModule(module);
        this.index = index;
    }
    
    public int getIndex()
    {
        return index;
    }

    public void moduleRemoved(PModule module, int index)
    {
        this.id = MODULE_REMOVED;
        setModule(module);
        this.index = index;
    }
    
    public PModuleContainer getContainer()
    {
        return container;
    }

    public void setModule( PModule module )
    {
        this.module = module;
    }
    
    public PModule getModule()
    {
        return module;
    }
    
}
