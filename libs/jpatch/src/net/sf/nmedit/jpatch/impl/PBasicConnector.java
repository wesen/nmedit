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
package net.sf.nmedit.jpatch.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PSignal;
import net.sf.nmedit.jpatch.PSignalTypes;
import net.sf.nmedit.jpatch.event.PConnectorListener;
import net.sf.nmedit.jpatch.event.PConnectorStateEvent;

/**
 * The reference implementation of interface {@link PConnector}.
 * @author Christian Schneider
 */
public class PBasicConnector extends PBasicComponent<PConnectorDescriptor> implements PConnector
{
    
    private PModule parent;
    private boolean connected;
    private EventListenerList eventListenerList;

    public PBasicConnector(PModule parent, PConnectorDescriptor descriptor, int componentIndex)
    {
        super(descriptor, componentIndex);
        this.parent = parent;
        this.connected = false;
        this.eventListenerList = new EventListenerList();
    }

    public void addConnectorListener(PConnectorListener l)
    {
        eventListenerList.add(PConnectorListener.class, l);
    }
    
    public void removeConnectorListener(PConnectorListener l)
    {
        eventListenerList.remove(PConnectorListener.class, l);   
    }

    public boolean canBreakConnection()
    {
        return getOutputConnector() != null;
    }

    public boolean disconnect(PConnector c)
    {
        PConnectionManager m = getConnectionManager();
        return (m != null && m.remove(this, c));
    }
    
    public boolean connect(PConnector c)
    {
        PConnectionManager m = getConnectionManager();
        return (m != null && m.add(this, c));
    }
    
    
    public boolean breakConnection()
    {
        PConnector source = getOutputConnector();
        
        return (source != null) && disconnect( source );
    }

    protected void fireConnectorStateChanged()
    {
        PConnectorStateEvent cse = null;
        Object[] list = eventListenerList.getListenerList();
        for (int i=list.length-2;i>=0;i-=2)
        {
            if (list[i]==PConnectorListener.class)
            {
                if (cse == null)
                    cse = new PConnectorStateEvent(this);
                ((PConnectorListener)list[i+1]).connectorStateChanged(cse);
            }
        }
    }

    public PConnectionManager getConnectionManager()
    {
        PModule module = getParentComponent();
        if (module != null)
        {
            PModuleContainer container = module.getParentComponent();
            if (container != null)
                return container.getConnectionManager();
        }
        return null;
    }

    public PSignal getDefaultSignalType()
    {
        return getDescriptor().getDefaultSignalType();
    }

    public PSignalTypes getDefinedSignals()
    {
        return getDescriptor().getDefinedSignals();
    }
    
    public PSignal getSignalType()
    {
        return getConnectionManager().getSignalType(this);
    }

    public boolean isOutput()
    {
        return getDescriptor().isOutput();
    }

    @Override
    public PModule getParentComponent()
    {
        return parent;
    }

    public boolean isConnected()
    {
        return connected;
    }
    
    protected void setConnected(boolean connected)
    {
        if (this.connected != connected)
        {
            this.connected = connected;
            fireConnectorStateChanged();
        }
    }

    public void verifyConnectedState()
    {
        PConnectionManager m = getConnectionManager();
        setConnected(m != null && m.isConnected(this));
    }

    public Collection<PConnector> getChildren()
    {
        PConnectionManager m = getConnectionManager();
        return m == null ? Collections.<PConnector>emptyList() : m.children(this);
    }

    public Collection<PConnection> getGraphConnections()
    {
        PConnectionManager m = getConnectionManager();
        return m == null ? Collections.<PConnection>emptyList() : m.graphConnections(this);
    }

    public Collection<PConnector> getGraphNodes()
    {
        PConnectionManager m = getConnectionManager();
        
        if (m == null)
        {
            Collection<PConnector> c = new ArrayList<PConnector>(1);
            c.add(this);
            return Collections.<PConnector>unmodifiableCollection(c);
        }
        else
        {
            return m.graph(this);
        }
    }

    public PConnector getOutputConnector()
    {
        if (isOutput()) return this;
        PConnectionManager m = getConnectionManager();
        return m == null ? null : m.output(this);
    }

    public PConnector getParentConnector()
    {
        PConnectionManager m = getConnectionManager();
        return m == null ? null : m.parent(this);
    }

    public PConnector getRootConnector()
    {
        PConnectionManager m = getConnectionManager();
        return m == null ? this : m.root(this);
    }

    public boolean disconnect()
    {
        PConnectionManager m = getConnectionManager();
        return m != null && m.remove(this);
    }

    public boolean isConnected(PConnector c)
    {
        PConnectionManager m = getConnectionManager();
        return m != null && m.isConnected(this, c);
    }

}
