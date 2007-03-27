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

import net.sf.nmedit.jpatch.event.ConnectionListener;

public interface ConnectionManager
{
    
    /**
     * Returns the module container which is managed by this connection manager. 
     * @return the module container
     */
    ModuleContainer getTarget();
    
    /**
     * Returns the number of connections between connectors in the target module container
     * @return the number of connections
     */
    int getConnectionCount(); 

    /**
     * Returns <code>true</code> when the specified connectors can be connected
     * @param a 
     * @param b
     * @return 
     */
    boolean canConnect(Connector a, Connector b);

    /**
     * Returns the output connector to which the specified connector is connected to.
     * If the specified connector is an output it is returned.
     * If the specified connector is not connected with an output connector <code>null</code>
     * is returned.
     * 
     * @return the output connector to which the specified connector is connected to
     */
    Connector getOutput(Connector c);
    
    /**
     * Returns the root of the tree representing the connection between a set of connectors. 
     * The return value is the same as the argument if the specified connector is the root
     * or of it is an output connector (what implies that it is the root of that tree).
     * 
     * The return value is either an input or an output connector.
     * The return value is never <code>null</code>.
     * 
     * @param c
     * @return
     */
    Connector getRoot(Connector c);
    
    /**
     * Returns a lightweight iterator over all connections in the target module container.
     * @return a lightweight iterator over all connections in the target module container
     */
    LightweightIterator<Connection> getConnections();

    /**
     * Returns a lightweight iterator over all connections containing the specified connector.
     * @return a lightweight iterator over all connections containing the specified connector
     */
    LightweightIterator<Connection> getConnections(Connector c);

    /**
     * Returns a lightweight iterator over all connections which are directly
     * or indirectly linked with the specified connector.
     * @return a lightweight iterator over all connections linked with the specified connector
     */
    LightweightIterator<Connection> getAllConnections(Connector c);

    /**
     * Returns a lightweight iterator over all connections containing connectors of the specified module.
     * @return a lightweight iterator over all connections containing connectors of the specified module
     */
    LightweightIterator<Connection> getConnections(Module module);

    /**
     * Returns a lightweight iterator over all connections which are directly or indirectly linked
     * with connectors of the specified module.
     * @return a lightweight iterator over all connections linked with the connectors of the specified module
     */
    LightweightIterator<Connection> getAllConnections(Module module);

    /**
     * Returns the iteration over all connectors which are directly connected with the specified connector. 
     * @return the iteration over all connectors which are directly connected with the specified connector
     */
    Iterator<Connector> getConnected(Connector c);

    /**
     * Returns the iteration over all connectors which are directly or indirectly connected with the specified connector. 
     * @return the iteration over all connectors which are directly or indirectly connected with the specified connector
     */
    Iterator<Connector> getAllConnected(Connector c);
    
    /**
     * Returns the iteration over all connectors which are connected directly or indirectly with this
     * connector and where this connector is the signal source.
     * The first element of the iteration is always the specified connector.
     * 
     * @return
     */
    Iterator<Connector> bfsSearch(Connector start);

    /**
     * Removes all connections containing the specified connector  
     * @param connector
     */
    void disconnect( Connector connector );

    /**
     * Returns <code>true</code> if the specified connector is connected
     */
    boolean isConnected( Connector connector );

    /**
     * Returns <code>true</code> if the specified connectors can be connected with each other.
     * @param a
     * @param b
     * @return
     */
    boolean canDisconnect(Connector a, Connector b);
    
    /**
     * Returns true if a direct connection between the specified connectors exists
     * @param a
     * @param b
     * @return
     */
    boolean isConnected(Connector a, Connector b);
    
    /**
     * Establishes a direct connection between the specified connectors.
     * @param a
     * @param b
     */
    boolean connect(Connector a, Connector b);

    /**
     * Removes a direct connection between the specified connectors.
     * @param a
     * @param b
     */
    boolean disconnect(Connector a, Connector b);
    
    /**
     * Adds a connection listener which want's to be notified when connections are created or removed
     * @param l the listener
     */
    void addConnectionListener(ConnectionListener l);
    
    /**
     * Removes the specified connection listener
     * @param l the listener
     */
    void removeConnectionListener(ConnectionListener l);

    /**
     * Removes the specified connection
     * @param connection
     */
    void remove(Connection connection);
    
    /**
     * Creates the specified connection.
     * @param connection
     */
    void add(Connection connection);

    /**
     * Removes all connections in the target container.
     */
    void disconnectAll();
    
}
