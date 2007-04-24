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

import java.awt.Event;

import net.sf.nmedit.jpatch.Module;

public class ModuleEvent extends Event
{

    public static final int MODULE_MOVED = 100;
    public static final int MODULE_RENAMED = 101;
    
    private String oldName;

    protected ModuleEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers, Object arg )
    {
        super( target, when, id, x, y, key, modifiers, arg );
    }

    protected ModuleEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers )
    {
        super( target, when, id, x, y, key, modifiers );
    }

    protected ModuleEvent( Object target, int id, Object arg )
    {
        super( target, id, arg );
    }

    public ModuleEvent(Module module)
    {
        super(module, 0, null);
    }
    
    public int getId()
    {
        return id;
    }
    
    public Module getModule()
    {
        return (Module) target;
    }
    
    public void moduleRenamed(String oldName)
    {
        this.id = MODULE_RENAMED;
        this.oldName = oldName;
    }
    
    public String getOldName()
    {
        return oldName;
    }

    public void moduleMoved(int oldScreenX, int oldScreenY)
    {
        this.id = MODULE_MOVED;
        this.x = oldScreenX;
        this.y = oldScreenY;
    }
    
    public int getOldScreenX()
    {
        return x;
    }
    
    public int getOldScreenY()
    {
        return y;
    }
    
}
