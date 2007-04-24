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
package net.sf.nmedit.jpatch;

import java.util.Collection;

import net.sf.nmedit.jpatch.event.ConnectionListener;

public interface ConnectionManager
    extends ConnectionFactory, Iterable<Connection>
{
    
    void addConnectionListener( ConnectionListener l );

    void removeConnectionListener( ConnectionListener l );

    <K extends Connection> boolean removeAll(Collection<K> c);
    
    <K extends Connection> boolean retainAll(Collection<K> c);
    
    <K extends Connection> boolean addAll(Collection<K> c);
    
    /**
     * Returns the number of connections in the target module container.
     */
    int size();

    /**
     * Returns the target module container.
     */
    ModuleContainer getTarget();
    
    /**
     * Returns all connections in the target module container.
     */
    Collection<Connection> getConnections();
    
    /**
     * Returns all connections containing the specified connector.
     */
    Collection<Connection> getConnections(Connector connector);
    
    /**
     * Returns all connections of the graph the specified
     * connector belongs to.
     */
    Collection<Connection> getConnectionTree(Connector connector);
    
    /**
     * Returns all connections containing at least one of the connectors
     * of the specified module.
     */
    Collection<Connection> getConnections(Module module);
    
    /**
     * Returns all connections of all graphs to which at least
     * one connector of the specified module belongs to.
     */
    Collection<Connection> getConnectionTrees(Module module);

    /**
     * Returns all connections which contain the specified connector
     * and one of it's children.
     */
    Collection<Connection> getChildConnections(Connector connector);
    
    /** 
     * Returns all connectors in the target module container
     * which are connected.
     */
    Collection<Connector> connectors();

    /**
     * Returns the parent of the specified connector.
     */
    Connector getParent(Connector connector);
    
    /**
     * Returns the output connector connected with the specified
     * connector. If the specified connector is an output it will
     * be returned. If no output connector is connected to the 
     * specified connector then <code>null</code> will be returned.   
     */
    Connector getSource(Connector connector);
    
    /**
     * Returns the root connector. If the specified connector is part of 
     * a graph containing the output connector then the root connector is
     * also the source connector.
     */
    Connector getRoot(Connector connector);
    
    /**
     * Returns the number of children of the specified connector.
     */
    int getChildCount(Connector connector);

    /**
     * Returns <code>true</code> if the connector is connected with
     * at least one other connector, <code>false</code> otherwise.
     */
    boolean isConnected(Connector connector);
    
    /**
     * Returns true if at least one connector of the specified module is connected.
     */
    boolean isConnected(Module module, boolean testInputs);
    
    /**
     * Returns <code>true</code> if the specified connector is
     * connected direct or indirect with an output connector.
     */
    boolean isConnectedWithOutput(Connector connector);
    
    /**
     * Returns the children of the specified connector.
     */
    Collection<Connector> getChildConnectors(Connector connector);
    
    /**
     * Returns the connectors which are connected directly with 
     * the specified connector. 
     */
    Collection<Connector> getConnected(Connector connector);
    
    /**
     * Returns all connectors of the graph containing the specified connector.
     * @param connector
     * @return
     */
    Collection<Connector> getTreeNodes(Connector connector);
    
    /**
     * Removes all connections containing the specified connector.
     * @return returns the number of removed connections 
     */
    boolean disconnect(Connector connector);
    
    /**
     * Removes the connection to the parent connector of the specified connector
     * if and only if the connector is connected direct or indirect with an
     * output connector.
     * @return if the connection was removed
     */
    boolean breakConnection(Connector connector);
    
    Connection getBreakableConnection(Connector connector);
    
    /**
     * Returns <code>true</code> if {@link #breakConnections(Connector)}
     * will remove the connection to the parent connector of the specified connector.
     */
    boolean canBreakConnection(Connector connector);
    
    /**
     * Removes all connections containing at least connecter of
     * the specified module.
     * @return returns the number of removed connections
     */
    boolean disconnect(Module module);

    /**
     * Removes all connections.
     */
    void clear();
    
    /**
     * Connects the specified connectors with each other.
     * @return the created connection
     * 
     * @throws IllegalArgumentException if {@link #isConnectionSupported(Connector, Connector)} returns
     * <code>false</code>
     * @throws IllegalStateException if the connection already exists
     */
    Connection connect(Connector a, Connector b);
    
    /**
     * Returns <code>true</code> if the two specified connectors are connected with each other,
     * <code>false</code> otherwise.
     */
    boolean isConnected(Connector a, Connector b);
    
    /**
     * Returns <code>true</code> if the two specified connectors are nodes in the same graph,
     * <code>false</code> otherwise.
     */
    boolean isPathPresent(Connector a, Connector b);
    
    /**
     * Returns the shortest path from one of the specified connectors
     * to the other. 
     * 
     * The path ends at the connector which is nearest to the output connector.
     * If no output connector is present then the path starts from connector a or b.
     * 
     * If a==b then the resulting collection contains only a.
     * 
     * If a and b are not in the same graph an empty collection is returned.
     * 
     * @throws IllegalArgumentException if the specified connectors are not present
     * in the target module container
     */
    Collection<Connector> getShortestPathToRoot(Connector a, Connector b);
    
    /**
     * Returns the transitions/connections of the shortest path from
     * one of the specified connectors to the other. 
     * 
     * The path ends at the connector which is nearest to the output connector.
     * If no output connector is present then the path starts from connector a or b.
     * 
     * If a==b then the resulting collection will be empty.
     * 
     * If a and b are not in the same graph an empty collection is returned.
     * 
     * @throws IllegalArgumentException if the specified connectors are not present
     * in the target module container
     */
    Collection<Connection> getShortestPathToRootTransitions(Connector a, Connector b);
   
    /**
     * Returns true if the specified connector is present in the target
     * module container.
     */
    boolean inTarget(Connector c);
    
    /**
     * Removes the connection containing the specified connectors.
     * @return returns <code>true</code> if the connection existed and thus was removed,
     * <code>false</code> otherwise.
     */
    boolean disconnect(Connector a, Connector b);

    /**
     * @return returns <code>true</code> if {@link #connect(Connector, Connector)} will create a new connection,
     * <code>false</code> otherwise
     */
    boolean canConnect(Connector a, Connector b);

    /**
     * @return returns <code>true</code> if {@link #add(Connection)} will establish a new connection,
     * <code>false</code> otherwise
     */
    boolean canAddConnection(Connection connection);
    
    /**
     * Returns <code>true</code> if a connection between the two 
     * specified connectors is supported in the target module container,
     * <code>false</code> otherwise.
     * The return value is independent of the current connection state
     * between the two connectors.
     */
    boolean isConnectionSupported(Connector a, Connector b);

    /**
     * @see #isConnectionSupported(Connector, Connector)
     */
    boolean isConnectionSupported(Connection connection);

    /**
     * Establishes the specified connection.
     * @return if the operation was successfull
     * 
     * @throws IllegalArgumentException if {@link #isConnectionSupported(Connection)} returns <code>false</code>
     * @return returns <code>true</code> if the connection was established, <code>false</code> otherwise
     */
    boolean add(Connection connection);
    
    /**
     * If existing, the specified connection will be removed.
     * @return <code>true</code> if the connection was removed, <code>false</code> otherwise.
     */
    boolean remove(Connection connection);
    
    /**
     * Returns the signal associated with the graph containing the specified connector.
     */
    Signal getSignal(Connector c);

}
