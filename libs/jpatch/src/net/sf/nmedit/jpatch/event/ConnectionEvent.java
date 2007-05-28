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

import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;

public class ConnectionEvent extends JPatchEvent
{

    private PConnector source;
    private PConnector destination;
    private transient PConnection connection;

    protected ConnectionEvent(  Object target, long when, int id, int x, int y, int key,
            int modifiers, Object arg )
    {
        super( target, when, id, x, y, key, modifiers, arg );
    }

    protected ConnectionEvent( Object target, long when, int id, int x, int y, int key,
            int modifiers )
    {
        super( target, when, id, x, y, key, modifiers );
    }

    protected ConnectionEvent( Object target, int id, Object arg )
    {
        super( target, id, arg );
    }
    
    public ConnectionEvent(PConnectionManager cm, PConnection c)
    {
        this(cm, c.getA(), c.getB());
        this.connection = c;
    }
    
    public ConnectionEvent(PConnectionManager cm, PConnector source, PConnector destination)
    {
        this(cm, 0, null);
        this.source = source;
        this.destination = destination;
    }

    public PConnectionManager getConnectionManager()
    {
        return (PConnectionManager) target;
    }
    
    public PConnection getConnection()
    {
        if (connection == null)
            connection = new PConnection(source, destination);
        return connection;
    }
    
    public PConnector getSource()
    {
        return source;
    }
    
    public PConnector getDestination()
    {
        return destination;
    }

    public void setConnectors( PConnector source, PConnector destination )
    {
        this.source = source;
        this.destination = destination;
    }
    
    public String toString()
    {
        return getClass().getName()+"[id="+id+",src="+source+",dst="+destination+"]";
    }
    
}
