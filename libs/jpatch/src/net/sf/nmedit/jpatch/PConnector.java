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

import net.sf.nmedit.jpatch.event.PConnectorListener;
import net.sf.nmedit.jpatch.impl.PBasicConnectionManager;

/**
 * Connector of a {@link PModule module}.
 * 
 * @author Christian Schneider
 */
public interface PConnector extends PComponent
{
    
    /**
     * Returns the parent module. The return values
     * is always non-null.
     */
    PModule getParentComponent();
    
    /**
     * Returns the descriptor of this component.
     */
    PConnectorDescriptor getDescriptor();
    
    /**
     * Returns the connection manager of the parent module
     * container. If the container does not exist or this
     * feature is not supported <code>null</code> is returned.
     */
    PConnectionManager getConnectionManager();

    /**
     * Returns true if this connector is an output or false
     * if it is an input. The return value is specified 
     * in {@link PConnectorDescriptor#isOutput()} of the
     * {@link #getDescriptor() descriptor}.
     */
    boolean isOutput();
    
    /**
     * Returns true if at least one connection containing
     * this connector exists.
     */
    boolean isConnected();
    
    /**
     * If {@link #getRootConnector()} is not null and an output, then it
     * will be returned, otherwise <code>null</code> is returned.
     */
    PConnector getOutputConnector();
    
    /**
     * Returns the root connector. If {@link #getParentConnector()}
     * returns <code>null</code> then this connector is returned.
     */
    PConnector getRootConnector();
    
    /**
     * Returns the parent connector. The parent connector is
     * the next connector on the path towards the root of the
     * connection tree
     * If {@link #isOutput()} is false then <code>null</code>
     * is returned.
     */
    PConnector getParentConnector();
    
    /**
     * Returns all child connectors. For each
     * child connector this connector is the next on the path
     * towards the root of the connection tree.
     */
    Collection<PConnector> getChildren();

    /**
     * Returns all nodes in the tree containing this connector.
     */
    Collection<PConnector> getGraphNodes();

    /**
     * Returns all connections of the tree containing this connector.
     */
    Collection<PConnection> getGraphConnections();
    
    /**
     * Returns the current signal type
     * @return
     */
    PSignal getSignalType();
    
    /**
     * Returns the default signal as specified by the descriptor
     * attached to this connector
     * @return the default signal
     */
    PSignal getDefaultSignalType();

    /**
     * Returns the defined signals.
     * @return the defined signals
     */
    PSignalTypes getDefinedSignals();
    
    /**
     * Checks if the connector state changed, thus
     * if it was not connected and now is or if it
     * was connected and now isn't. Then the
     * listeners attached to this connector are notified
     * of the changed state.
     * 
     * Generally this does not have to be called, it is
     * used internally by the {@link PBasicConnectionManager}.
     */
    void verifyConnectedState();
    
    /**
     * Removes the connection containing this and the specified connector.
     * 
     * @param c the connector
     * @return true if the connection was removed, false otherwise 
     * (generally when the connection did not exist)
     */
    boolean disconnect(PConnector c);

    /**
     * Connects this connector with the specified connector.
     * If the connection already exists, nothing will happen. 
     * @param c the connector  
     * @return return true if the connection was created, false if
     * no new connection was created
     */
    boolean connect(PConnector c);

    /**
     * Returns true if this connector is connected with the specified connector, false otherwise.
     * @param c the other connector
     * @return true if this connector is connected with the specified connector
     */
    boolean isConnected(PConnector c);
    
    /**
     * Removes all connections to/from this connector.
     * @return removes all connections 
     */
    boolean disconnect();
    
    /**
     * Returns true if {@link #breakConnection()} will remove at least one connection.
     * @return true if the connection towards the output connector exists and can be removed
     */
    boolean canBreakConnection();
    
    /**
     * Removes the link that heads from the connector towards the output
     * connector that belongs to the same connection. If this connector is an
     * output connector or if there is no connection with an output connector,
     * the break operation is not feasible. <img
     * src="doc-files/cable-op-break.png" width="528" height="223" alt="An
     * illustration of the break operation." />
     * 
     * @return <code>true</code> if the connection changed
     */
    boolean breakConnection();

    /**
     * Adds the specified {@link PConnectorListener} to the listener list.
     * @param l the listener
     */
    void addConnectorListener(PConnectorListener l);
    
    /**
     * Removes the specified {@link PConnectorListener} from the listener list.
     * @param l the listener
     */
    void removeConnectorListener(PConnectorListener l);
    
}
