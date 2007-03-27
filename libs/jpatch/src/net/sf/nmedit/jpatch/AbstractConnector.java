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

import java.util.Iterator;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.event.ConnectorListener;
import net.sf.nmedit.jpatch.event.ConnectorStateEvent;

public abstract class AbstractConnector extends AbstractComponent implements Connector
{
    
    private EventListenerList eventListenerList = null;
    private transient ConnectorStateEvent cse = null;
    
    public void addConnectorListener(ConnectorListener l)
    {
        if (eventListenerList == null)
            eventListenerList = new EventListenerList();
        eventListenerList.add(ConnectorListener.class, l);
    }
    
    public void removeConnectorListener(ConnectorListener l)
    {
        if (eventListenerList != null)
            eventListenerList.remove(ConnectorListener.class, l);   
    }
    
    protected void fireConnectorStateChanged()
    {
        if (eventListenerList != null)
        {
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
    }

    public Connector getOutput()
    {
        return getConnectionManager().getOutput(this);
    }

    public Connector getRoot()
    {
        return getConnectionManager().getRoot(this);
    }

    public Iterator<Connector> bfsSearch()
    {
        return getConnectionManager().bfsSearch(this);
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
        return getConnectionManager().canDisconnect(this, c);
    }

    public boolean isConnectedWith( Connector c )
    {
        return getConnectionManager().isConnected(this, c);
    }

    public boolean connectWith( Connector c ) 
    {
        return getConnectionManager().connect(this, c);
    }

    public boolean disconnectFrom( Connector c ) 
    {
        return getConnectionManager().disconnect(this, c);
    }

}
