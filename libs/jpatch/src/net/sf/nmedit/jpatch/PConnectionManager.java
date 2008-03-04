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

import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.event.PConnectionListener;


/**
 * Manages the connections of a module container. 
 * 
 * <h1>Connections</h1>
 * <p>
 *   Connected connectors imply a graph where the connectors are
 *   the nodes and the connections are the transitions.
 * </p>
 * <p>
 *   Because two outputs can not be connected with each other
 *   a graph contains one or none output connector. 
 * </p>
 * <p>
 *   The graph contains no circles. Thus a path
 *   <code>c[1], c[2], ..., c[n], c[1] where c[i]!=c[j] for i!=j and i=1..n, j=1..n </code>
 *   does not exist.
 * </p>
 * <p>
 *   For each pair (a,b), a!=b of connectors in a graph
 *   a path between both connectors exists. 
 * </p>
 * <p>
 *   Because of the properties of the graph the graph is a tree.
 * </p>
 * <p>
 *   If the tree contains an output connector then this connector
 *   is used as the root of the tree. 
 * </p>
 * 
 * @author Christian Schneider
 */
public interface PConnectionManager extends Iterable<PConnection>
{
    
    PModuleContainer getModuleContainer();
    
    /**
     * Creates the specified connection if it does not exist already.
     */
    boolean add(PConnection c);

    /**
     * Creates a connection between the two connectors if it does not 
     * exist already. 
     */
    boolean add(PConnector a, PConnector b);
    
    /**
     * Removed the specified connection.
     */
    boolean remove(PConnection c);

    /**
     * Removes the connection between the two connectors if
     * it exists.
     */
    boolean remove(PConnector a, PConnector b);
    
    /**
     * Removes all connections containing the specified connector.
     * 
     * @param c
     * @return
     */
    boolean remove(PConnector c);

    /**
     * Removes all connections containing the specified connector.
     */
    boolean removeAllConnectors(Collection<? extends PConnector> c);
    
    /**
     * Removes the specified connections. 
     */
    boolean removeAllConnections(Collection<? extends PConnection> c);

    /**
     * Returns true if the specified connection exists.
     */
    boolean contains(PConnection c);

    /**
     * Returns true if the specified connectors are connected
     * with each other.
     */
    boolean isConnected(PConnector a, PConnector b);

    /**
     * Returns true if both connectors are in the same graph/tree.
     */
    boolean pathExists(PConnector a, PConnector b);
    
    /**
     * Returns true if the specified connector is connected.
     */
    boolean isConnected(PConnector c);
    
    /**
     * Returns the number of connections in the module container.
     * @return the number of connections in the module container
     */
    int size();    
    
    /**
     * Returns true if no connections exist.
     * @return true if no connections exist
     */
    boolean isEmpty();
    
    void clear();

    PSignal getSignalType(PConnector connector);

    // collection connectors
    
    /**
     * Returns all connected connectors in the module container.
     */
    Collection<PConnector> connected();

    /**
     * Returns the parent and children of the specified connector.
     */
    Collection<PConnector> connected(PConnector c);

    /**
     * Returns all nodes in the tree containing the specified connector.
     */
    Collection<PConnector> graph(PConnector c);
    
    // collection of connections
    
    /**
     * Returns all connections in the module container.
     */
    Collection<PConnection> connections();
    
    /**
     * Returns all connections containing the specified connecter.
     * The connections to the child and parent connectors.
     */
    Collection<PConnection> connections(PConnector c);
    
    /**
     * Returns all connections containing at least one connector
     * of the specified module.
     * @param m
     * @return
     */
    Collection<PConnection> connections(PModule m); 

    /**
     * Returns all connections containing at least one connector
     * of the specified module.
     * @param m
     * @return
     */
    Collection<PConnection> connections(Collection<? extends PModule> ms); 

    /**
     * Returns all connections of the tree containing the specified connector.
     */
    Collection<PConnection> graphConnections(PConnector c);
    
    // connectors / graph

    /**
     * Returns the root of the graph the specified connector belongs to.
     * The specified connector will be returned if it is not connected 
     * or if it is an output connector. If the connector is in a graph
     * containing an output connector, the output connector will be returned.
     * If the connector is in a graph containing no output connector then
     * one of the input connectors will be returned (depends on the implementation). 
     * 
     * @param c
     * @return
     */
    PConnector root(PConnector c);
    
    /**
     * Returns the output connector of the graph containing the specified connector.
     * If the specified connector is an output it will be returned.
     * 
     * @param c
     * @return
     */
    PConnector output(PConnector c);

    /**
     * Returns the parent connector. The parent connector is the connector
     * next to the root of the graph the specified connector belongs to.
     * 
     * @param c
     * @return
     */
    PConnector parent(PConnector c);

    /**
     * Returns the children of this connector.
     * For each child the specified connector is the next connector
     * towards the root of the graph. 
     * 
     * @param c
     * @return
     */
    Collection<PConnector> children(PConnector c);

    /**
     * Returns the number of children of the specified connector.
     */
    int childCount(PConnector c);

    /**
     * Returns true if at least one connector of the specified module is connected.
     * @param m
     * @return
     */
    boolean isConnected(PModule m);
    
    /**
     * Adds the specified {@link PConnectionListener} to the listener list.
     * @param l the listener
     */
    void addConnectionListener(PConnectionListener l);

    /**
     * Removes the specified {@link PConnectionListener} from the listener list.
     * @param l the listener
     */
    void removeConnectionListener(PConnectionListener l);

    /**
     * Posts an edit of this component.
     * The edit will only be recognized if 
     * {@link #isUndoableEditSupportEnabled()}
     * is true.
     * @param edit
     */
    void postEdit(UndoableEdit edit);
    
    /**
     * True if undo support is enabled.
     * @return true if undo support is enabled
     */
    boolean isUndoableEditSupportEnabled();

    public PUndoableEditFactory getUndoableEditFactory();
    
}
