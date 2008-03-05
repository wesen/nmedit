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

import net.sf.nmedit.jpatch.PConnector;

/**
 * Event sent by a {@link PConnector connector}.
 * 
 * @author Christian Schneider
 */
public class PConnectorStateEvent extends PPatchEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = 9119044209104155846L;
    
    protected PConnectorStateEvent(  Object target, long when, int id, int x, int y, int key,
            int modifiers, Object arg )
    {
        super( target, when, id, x, y, key, modifiers, arg );
    }

    protected PConnectorStateEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers )
    {
        super( target, when, id, x, y, key, modifiers );
    }

    protected PConnectorStateEvent( Object target, int id, Object arg )
    {
        super( target, id, arg );
    }
    
    public PConnectorStateEvent(PConnector c)
    {
        this(c, 0, null);
    }
    
    public PConnector getConnector()
    {
        return (PConnector)target;
    }
    
}
