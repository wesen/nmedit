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
 * Created on Dec 11, 2006
 */
package net.sf.nmedit.jpatch;

import java.util.NoSuchElementException;

public abstract class ConnectionIterator implements LightweightIterator<Connection>, Connection
{

    protected Connector source = null;
    protected Connector destination = null;
    private ConnectionManager connectionManager;
    protected boolean nextCalled = false;
    
    public ConnectionIterator(ConnectionManager connectionManager)
    {
        this.connectionManager = connectionManager;
    }

    public ConnectionManager getManager()
    {
        return connectionManager;
    }
    
    protected abstract void nextConnection();
    
    public Connection next()
    {
        if (!hasNext())
            throw new NoSuchElementException();
        
        nextConnection();
        nextCalled = true;
        return this;
    }

    public abstract boolean hasNext();
    
    public void remove()
    {
        if (!nextCalled)
        {
            throw new IllegalStateException();
        }
        getManager().remove(this);
        nextCalled = false;
    }

    public Connector getSource()
    {
        return source;
    }

    public Connector getDestination()
    {
        return destination;
    }

    public Module getSourceModule()
    {
        return getSource().getOwner();
    }

    public Module getDestinationModule()
    {
        return getDestination().getOwner();
    }

    public boolean contains( Connector c )
    {
        return source == c || destination == c;
    }

}
