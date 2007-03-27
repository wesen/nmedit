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

import java.util.Iterator;

import net.sf.nmedit.jpatch.event.ConnectorListener;

/**
 * A connector of a module.
 * 
 * @see net.sf.nmedit.jpatch.ConnectionManager
 * @author Christian Schneider
 */
public interface Connector extends Composite
{
    
    Signal getDefaultSignal();
    
    Signal getSignal();
    
    /**
     * @see Component#getDescriptor()
     */
    ConnectorDescriptor getDescriptor();

    /**
     * Returns <code>true</code> if this connector is connected with at least one other connector.
     * @return <code>true</code> if this connector is connected with at least one other connector.
     */
    boolean isConnected();

    /**
     * Returns <code>true</code> if this connector is an output connector.
     * @return <code>true</code> if this connector is an output connector.
     */
    boolean isOutput();

    /**
     * Returns the connection manager which manages this connector.
     * The return value is never <code>null</code>
     * @return the connection manager which manages this connector
     */
    ConnectionManager getConnectionManager();

    /**
     * Same as <code>getConnectionManager().getOutput(this);</code>
     * @see ConnectionManager#getOutput(Connector)
     */
    Connector getOutput();
    
    /**
     * Same as <code>getConnectionManager().getRoot(this);</code>
     * @see ConnectionManager#getRoot(Connector)
     */
    Connector getRoot(); 

    /**
     * Same as <code>getConnectionManager().bfsSearch(this);</code>
     * @see ConnectionManager#bfsSearch(Connector)
     */
    Iterator<Connector> bfsSearch();

    /**
     * Same as <code>getConnectionManager().disconnect(this);</code>
     * @see ConnectionManager#disconnect(Connector)
     */
    void disconnect();

    /**
     * Same as <code>getConnectionManager().canConnect(this, c);</code>
     * @see ConnectionManager#canConnect(Connector, Connector)
     */
    boolean canConnectWith(Connector c);

    /**
     * Same as <code>getConnectionManager().canDisconnect(this, c);</code>
     * @see ConnectionManager#canDisconnect(Connector, Connector)
     */
    boolean canDisconnectFrom(Connector c);

    /**
     * Same as <code>getConnectionManager().isConnected(this, c);</code>
     * @see ConnectionManager#isConnected(Connector, Connector)
     */
    boolean isConnectedWith(Connector c);

    /**
     * Same as <code>getConnectionManager().connect(this, c);</code>
     * @see ConnectionManager#connect(Connector, Connector)
     */
    boolean connectWith(Connector c);

    /**
     * Same as <code>getConnectionManager().disconnect(this, c);</code>
     * @see ConnectionManager#disconnect(Connector, Connector)
     */
    boolean disconnectFrom(Connector c);
    
    /**
     * Returns the source/parent connector
     * @return
     */
    Connector getSource();
    
    /**
     * Returns the number of child connectors
     * @return
     */
    int getChildConnectorCount();
    
    /**
     * Returns the child connector at the specified index
     * @param index
     * @return
     */
    Connector getChildConnector(int index);
    
    /**
     * Returns all child connectors
     * @return
     */
    Connector[] getChildren();
    
    void addConnectorListener(ConnectorListener l);
    void removeConnectorListener(ConnectorListener l);
    
}
