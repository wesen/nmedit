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

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleContainer;

public class ModuleContainerEvent extends JPatchEvent
{

    private ModuleContainer container;
    private Module module;

    protected ModuleContainerEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers, Object arg )
    {
        super( target, when, id, x, y, key, modifiers, arg );
    }

    protected ModuleContainerEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers )
    {
        super( target, when, id, x, y, key, modifiers );
    }

    protected ModuleContainerEvent( Object target, int id, Object arg )
    {
        super( target, id, arg );
    }
    
    public ModuleContainerEvent(ModuleContainer container)
    {
        this(null, 0, null);
        this.container = container;
    }

    public ModuleContainer getContainer()
    {
        return container;
    }

    public void setModule( Module module )
    {
        this.module = module;
    }
    
    public Module getModule()
    {
        return module;
    }
    
}
