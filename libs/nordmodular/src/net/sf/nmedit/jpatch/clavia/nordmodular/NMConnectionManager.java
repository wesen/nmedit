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
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.Iterator;

import net.sf.nmedit.jpatch.AbstractConnectionManager;
import net.sf.nmedit.jpatch.Connection;
import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.LightweightIterator;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.event.ConnectionEvent;

public class NMConnectionManager extends AbstractConnectionManager implements ConnectionManager
{

    private transient ConnectionEvent cevent = null;
    
    public NMConnectionManager( VoiceArea voiceArea )
    {
        super( voiceArea );
    }
    
    private ConnectionEvent getEvent(Connector source, Connector destination)
    {
        if (cevent == null)
            cevent = new ConnectionEvent(source, destination);
        else
            cevent.setConnectors(source, destination);
        
        return cevent;
    }

    void fireConnectionAdded(Connector a, Connector b)
    {
        fireConnectionAdded(getEvent(a, b));
    }
    
    void fireConnectionRemoved(Connector a, Connector b)
    {
        fireConnectionRemoved(getEvent(a, b));
    }

    public void updateGraph( NMConnector updatePartialGraph )
    {
        // TODO Auto-generated method stub
        
    }

    public Connector getOutput( Connector c )
    {
        return c.getOutput();
    }

    public Connector getRoot( Connector c )
    {
        return c.getRoot();
    }

    public LightweightIterator<Connection> getAllConnections( Connector c )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public LightweightIterator<Connection> getAllConnections( Module module )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Iterator<Connector> getConnected( Connector c )
    {
        return (Iterator<Connector> ) ((NMConnector)c).getConnected();
    }

    public Iterator<Connector> getAllConnected( Connector c )
    {
        return (Iterator<Connector> ) ((NMConnector)c).getAllConnected();
    }

    public boolean isConnected( Connector connector )
    {
        return connector.isConnected();
    }

    public boolean isConnected( Connector a, Connector b )
    {
        return a.isConnectedWith(b);
    }

    public boolean connect( Connector a, Connector b )
    {
        return a.connectWith(b);
    }

    public boolean disconnect( Connector a, Connector b )
    {
        return a.disconnectFrom(b);
    }
    
}
