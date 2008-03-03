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

/**
 * Event sent by a {@link PModule module}.
 * 
 * @author Christian Schneider
 */
public class PModuleEvent extends PPatchEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = 7313259477979546246L;
    private String oldName;

    protected PModuleEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers, Object arg )
    {
        super( target, when, id, x, y, key, modifiers, arg );
    }

    protected PModuleEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers )
    {
        super( target, when, id, x, y, key, modifiers );
    }

    protected PModuleEvent( Object target, int id, Object arg )
    {
        super( target, id, arg );
    }

    public PModuleEvent(PModule module)
    {
        super(module, 0, null);
    }
    
    /**
     * Returns the module that was changed.
     * @return the module that was changed
     */
    public PModule getModule()
    {
        return (PModule) target;
    }
    
    /**
     * Returns the previous name of the module.
     * @return the previous name of the module.
     * @throws UnsupportedOperationException if the event id is not {@link PPatchEvent#MODULE_RENAMED}
     */
    public String getOldName()
    {
        if (getId() != MODULE_RENAMED)
            throw new UnsupportedOperationException("undefined property");
        return oldName;
    }

    /**
     * Sets the event id to {@link PPatchEvent#MODULE_RENAMED} and 
     * the previous module name.
     *   
     * @param oldName the previous module name
     */
    public void moduleRenamed(String oldName)
    {
        this.id = MODULE_RENAMED;
        this.oldName = oldName;
    }
    
    /**
     * Sets the event id to {@link PPatchEvent#MODULE_REMOVED} and 
     * the previous screen location property.
     * 
     * @param oldScreenX the previous x-coordinate (screen) 
     * @param oldScreenY the previous y-coordinate (screen)
     */
    public void moduleMoved(int oldScreenX, int oldScreenY)
    {
        this.id = MODULE_MOVED;
        this.x = oldScreenX;
        this.y = oldScreenY;
    }
    
    /**
     * Returns the x-coordinate of the previous screen location 
     * @return x-coordinate of the previous screen location
     * @throws UnsupportedOperationException if the event id is not {@link PPatchEvent#MODULE_MOVED}
     */
    public int getOldScreenX()
    {
        if (getId() != MODULE_MOVED)
            throw new UnsupportedOperationException("undefined property");
        return x;
    }

    /**
     * Returns the y-coordinate of the previous screen location 
     * @return y-coordinate of the previous screen location
     * @throws UnsupportedOperationException if the event id is not {@link PPatchEvent#MODULE_MOVED}
     */
    public int getOldScreenY()
    {
        if (getId() != MODULE_MOVED)
            throw new UnsupportedOperationException("undefined property");
        return y;
    }
    
}
