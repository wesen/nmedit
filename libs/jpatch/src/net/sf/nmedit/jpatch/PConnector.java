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

import net.sf.nmedit.jpatch.event.ConnectorListener;


public interface PConnector extends PComponent
{
    
    PModule getParentComponent();
    
    PConnectorDescriptor getDescriptor();
    
    PConnectionManager getConnectionManager();

    boolean isOutput();
    
    boolean isConnected();
    
    PConnector getOutputConnector();
    
    PConnector getRootConnector();
    
    PConnector getParentConnector();
    
    Collection<PConnector> getChildren();
    
    Collection<PConnector> getGraphNodes();
    
    Collection<PConnection> getGraphConnections();
    
    PSignalType getDefaultSignalType();
    
    PSignalType getSignalType();
    
    void verifyConnectedState();
    
    boolean disconnect(PConnector c);
    
    boolean connect(PConnector c);

    boolean canBreakConnection();


    boolean isConnected(PConnector c);
    boolean disconnect();
    

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

    void addConnectorListener(ConnectorListener l);
    void removeConnectorListener(ConnectorListener l);
    
}
