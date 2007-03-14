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
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.event.ConnectionEvent;
import net.sf.nmedit.jpatch.event.ConnectionListener;
import net.sf.nmedit.nmutils.collections.Counter;
import net.sf.nmedit.nmutils.iterator.BFSIterator;

public abstract class AbstractConnectionManager implements ConnectionManager
{

    private ModuleContainer target;
    
    private EventListenerList eventListeners = new EventListenerList();

    public AbstractConnectionManager(ModuleContainer target)
    {
        this.target = target;
    }

    public ModuleContainer getTarget()
    {
        return target;
    }

    public int getConnectionCount()
    {
        return Counter.countIterator(getConnections());
    }

    public int getConnectorCount()
    {
        return Counter.countIterator(connectors());
    }
    
    public Iterator<Connector> connectors()
    {
        return new Iterator<Connector>()
        {

            Iterator<Module> modules = getTarget().iterator();
            Module module = null;
            int connectorIndex=-1;
            
            {
                nextModule();
            }
            
            void nextModule()
            {
                while (modules.hasNext())
                {
                    module = modules.next();
                    if ((connectorIndex = module.getConnectorCount()-1) >=0)
                        return;
                }
            }
            
            public Connector next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                
                ConnectorDescriptor descriptor =
                module.getDescriptor().getConnectorDescriptor(connectorIndex);
                
                Connector c = null;
                try
                {
                    c = module.getConnector(descriptor);
                }
                catch (InvalidDescriptorException e)
                {
                    throw new InternalError(e.getMessage());
                }
                
                connectorIndex--;
                if (connectorIndex<0)
                    nextModule();
                
                return c;
            }
            
            public boolean hasNext()
            {
                return connectorIndex>=0;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    public LightweightIterator<Connection> getConnections()
    {
        return new ConnectionIterator(this)
        {
            private Iterator<Connector> connectors = connectors();
            private boolean aligned = false;
            
            private void align()
            {
                if (!aligned)
                {
                    while (connectors.hasNext())
                    {
                        destination = connectors.next();
                        if (destination.getSource()!=null)
                        {
                            aligned = true;
                            return;
                        }
                    }
                    aligned = false;
                }
            }
            
            @Override
            protected void nextConnection()
            {
                align();
                aligned = false;
                source = destination.getSource();
            }

            @Override
            public boolean hasNext()
            {
                align();
                return aligned;
            }
            
        };
    }

    public LightweightIterator<Connection> getConnections( final Connector c )
    {
        return new ConnectionIterator(this)
        {
            int index;   
            {
                source = c;
                index = c.getChildConnectorCount()-1;
            }
            
            @Override
            protected void nextConnection()
            {
                if (index>=0)
                {
                    destination = source.getChildConnector(index);
                }
                else
                {
                    destination = source;
                    source = destination.getSource();
                }
                index --;
            }

            @Override
            public boolean hasNext()
            {
                return index>= 0 || (index == -1 && destination.getSource()!=null);
            }
            
        };
    }

    public LightweightIterator<Connection> getConnections( final Module module )
    {
        return new ConnectionIterator(this)
        {
            
            Module mod = module;
            ModuleDescriptor desc;
            LightweightIterator<Connection> iter;
            int index;
            
            {
                desc = mod.getDescriptor();
                index = desc.getConnectorCount()-1;
                iter = null;
            }
            
            void align()
            {
                if (iter==null)
                {
                    while (index>=0)
                    {
                        Connector c;
                        try
                        {
                            c = mod.getConnector(desc.getConnectorDescriptor(index--));
                        }
                        catch (InvalidDescriptorException e)
                        {
                            throw new InternalError();
                        }
                        iter = getConnections(c);
                        if (iter.hasNext())
                            return;
                    } 
                    iter = null;
                }
            }

            @Override
            protected void nextConnection()
            {
                align();                
                Connection c = iter.next();
                this.source = c.getSource();
                this.destination = c.getDestination();
            }

            @Override
            public boolean hasNext()
            {
                align();
                return iter != null;
            }
            
        };
    }

    protected boolean isSameModule(Connector a, Connector b)
    {
        return a.getOwner() == b.getOwner();
    }
    
    protected boolean isSameModuleContainer(Connector a, Connector b)
    {
        return isSameModule(a, b) && a.getOwner().getParent() == b.getOwner().getParent();
    }
    
    public boolean canConnect( Connector a, Connector b )
    {
        return (!isConnected(a, b)) && isSameModuleContainer(a, b);
    }

    public Iterator<Connector> bfsSearch( Connector start )
    {
        return new BFSIterator<Connector>(start)
        {
            @Override
            protected void enqueueChildren( Queue<Connector> queue, Connector parent )
            {
                for (Connector c : parent.getChildren())
                {
                    queue.add(c);
                }
            }   
        };
    }

    public void disconnect( Connector connector )
    {
        Iterator<? extends Connector> others = getConnected(connector);
        while (others.hasNext())
        {
            disconnect(connector, others.next());
        }
    }

    public boolean canDisconnect( Connector a, Connector b )
    {
        return isConnected(a, b);
    }

    public void remove( Connection connection )
    {
        disconnect(connection.getSource(), connection.getDestination());
    }

    public void add( Connection connection )
    {
        connect(connection.getSource(), connection.getDestination());
    }

    public void disconnectAll()
    {
        Iterator<Connection> connection = getConnections();
        while (connection.hasNext())
        {
            remove(connection.next());
        }
    }

    public void addConnectionListener( ConnectionListener l )
    {
        eventListeners.add(ConnectionListener.class, l);
    }

    public void removeConnectionListener( ConnectionListener l )
    {
        eventListeners.remove(ConnectionListener.class, l);
    }

    protected void fireConnectionAdded(ConnectionEvent e)
    {
        Object[] listenerList = eventListeners.getListenerList();
        for (int i=listenerList.length-2;i>=0;i-=2)
        {
            if (listenerList[i+1]==ConnectionListener.class)
            {
                ((ConnectionListener) listenerList[i]).connectionAdded(e);
            }
        }
    }

    protected void fireConnectionRemoved(ConnectionEvent e)
    {
        Object[] listenerList = eventListeners.getListenerList();
        for (int i=listenerList.length-2;i>=0;i-=2)
        {
            if (listenerList[i+1]==ConnectionListener.class)
            {
                ((ConnectionListener) listenerList[i]).connectionAdded(e);
            }
        }
    }
        
}
