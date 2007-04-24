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
 * Created on Dec 2, 2006
 */
package net.sf.nmedit.jpatch;

import java.util.Collection;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.event.ConnectorListener;
import net.sf.nmedit.jpatch.event.ConnectorStateEvent;

public abstract class AbstractConnector extends AbstractComponent implements Connector
{
    
    protected EventListenerList eventListenerList = new EventListenerList();
    private boolean connectedStateMemory = false;
    
    public void addConnectorListener(ConnectorListener l)
    {
        eventListenerList.add(ConnectorListener.class, l);
    }
    
    public void removeConnectorListener(ConnectorListener l)
    {
        eventListenerList.remove(ConnectorListener.class, l);   
    }
    
    public boolean canBreakConnection()
    {
        return getSource() != null;
    }

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
    public boolean breakConnection()
    {
        Connector source = getSource();
        
        return (source != null) && disconnectFrom( source );
    }

    public void updateConnectedState()
    {
        boolean isConnected = isConnected();
        if (connectedStateMemory != isConnected)
        {
            connectedStateMemory = isConnected;
            fireConnectorStateChanged();
        }
    }
    
    protected void fireConnectorStateChanged()
    {
        ConnectorStateEvent cse = null;
        Object[] list = eventListenerList.getListenerList();
        for (int i=list.length-2;i>=0;i-=2)
        {
            if (list[i]==ConnectorListener.class)
            {
                if (cse == null)
                    cse = new ConnectorStateEvent(this);
                ((ConnectorListener)list[i+1]).connectorStateChanged(cse);
            }
        }
    }

    /**
     * @deprecated
     */
    public Connector getOutput()
    {
        return getSource();
    }

    public Connector getRoot()
    {
        return getConnectionManager().getRoot(this);
    }

    public void disconnect()
    {
        getConnectionManager().disconnect(this);
    }

    public boolean isConnected()
    {
        return getConnectionManager().isConnected(this);
    }

    public boolean isOutput()
    {
        return getDescriptor().isOutput();
    }

    public boolean canConnectWith( Connector c )
    {
        return getConnectionManager().canConnect(this, c);
    }

    public boolean canDisconnectFrom( Connector c )
    {
        return getConnectionManager().isConnected(this, c);
    }

    public boolean isConnectedWith( Connector c )
    {
        return getConnectionManager().isConnected(this, c);
    }

    public Connection connectWith( Connector c ) 
    {
        return getConnectionManager().connect(this, c);
    }

    public boolean disconnectFrom( Connector c ) 
    {
        return getConnectionManager().disconnect(this, c);
    }

    public Connector getSource() 
    {
        return getConnectionManager().getSource(this);
    }

    public Connector getParent() 
    {
        return getConnectionManager().getParent(this);
    }
    
    public Collection<? extends Connector> getChildren()
    {
        return getConnectionManager().getChildConnectors(this);
    }

}
