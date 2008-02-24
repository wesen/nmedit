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

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;

/**
 * Parent class of all events in the JPatch API.
 * 
 * @author Christian Schneider
 */
public class PPatchEvent extends Event
{
    /**
     * 
     */
    private static final long serialVersionUID = 8994239811441141439L;
    /**
     * The value of a {@link PParameter parameter} was changed.
     */
    public static final int VALUE_CHANGED = 0;
    public static final int PARAMETER_FOCUS_REQUEST = 111;
    
    /**
     * A {@link PModule module} was added to the {@link PModuleContainer module container}.
     */
    public static final int MODULE_ADDED = 1;

    /**
     * A {@link PModule module} was removed from {@link PModuleContainer module container}.
     */
    public static final int MODULE_REMOVED = 2;

    /**
     * The location of a {@link PModule module} changed.
     */
    public static final int MODULE_MOVED = 100;

    /**
     * The title/name property of a {@link PModule module} was changed.
     */
    public static final int MODULE_RENAMED = 101;

    /**
     * The title/name property of a {@link PModule module} was changed.
     */
    public static final int MODULE_COLOR_CHANGED = 102;

    /**
     * Value of a {@link PLight light} was changed.
     */
    public static final int LIGHT = 110;
    
    /**
     * first id of custom events
     */
    public static final int CUSTOM_EVENT_START = 3000;
    
    public static final int MORPH_GROUP_CHANGED = 3001;
    
    protected PPatchEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers, Object arg )
    {
        super( target, when, id, x, y, key, modifiers, arg );
    }

    protected PPatchEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers )
    {
        super( target, when, id, x, y, key, modifiers );
    }

    protected PPatchEvent( Object target, int id, Object arg )
    {
        super( target, id, arg );
    }

    /**
     * Returns the id of this event.
     * @return the id of this event
     */
    public int getId()
    {
        return id;
    }
}
