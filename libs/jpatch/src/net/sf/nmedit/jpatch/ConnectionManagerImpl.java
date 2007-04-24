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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.event.ConnectionEvent;
import net.sf.nmedit.jpatch.event.ConnectionListener;
import net.sf.nmedit.nmutils.collections.UnmodifiableIterator;
import net.sf.nmedit.nmutils.iterator.BFSIterator;

public class ConnectionManagerImpl
        implements ConnectionManager
{
    
    private static final long serialVersionUID = 685046267882051424L;
    private Map<Connector, Node> nodemap = new HashMap<Connector, Node>();
    private int connections = 0;
    private transient int modCount;
    private ConnectionFactory factory;
    private ModuleContainer target;
    private EventListenerList eventListeners = new EventListenerList();

    public ConnectionManagerImpl(ModuleContainer target, ConnectionFactory factory)
    {
        this.target = target;
        this.factory = factory;
    }
    
    public void addConnectionListener( ConnectionListener l )
    {
        eventListeners.add(ConnectionListener.class, l);
    }

    public void removeConnectionListener( ConnectionListener l )
    {
        eventListeners.remove(ConnectionListener.class, l);
    }

    protected void fireConnectionAdded(Connection connection)
    {
        ConnectionEvent e = null;
        Object[] listenerList = eventListeners.getListenerList();
        for (int i=listenerList.length-2;i>=0;i-=2)
        {
            if (listenerList[i]==ConnectionListener.class)
            {
                if (e == null) e = new ConnectionEvent(this, connection);
                ((ConnectionListener) listenerList[i+1]).connectionAdded(e);
            }
        }
    }

    protected void fireConnectionRemoved(Connection connection)
    {
        ConnectionEvent e = null;
        Object[] listenerList = eventListeners.getListenerList();
        for (int i=listenerList.length-2;i>=0;i-=2)
        {
            if (listenerList[i]==ConnectionListener.class)
            {
                if (e == null) e = new ConnectionEvent(this, connection);
                ((ConnectionListener) listenerList[i+1]).connectionRemoved(e);
            }
        }
    }
    
    private Node nodeOrNull(Connector c)
    {
        return nodemap.get(c);
    }
    
    private Node getNode(Connector c)
    {
        Node node = nodemap.get(c);
        if (node == null)
            checkTarget(c);
        return node;
    }

    public Connection create(Connector a, Connector b)
    {
        return factory.create(a, b);
    }
    
    private boolean twoOutputs(Connector a, Connector b)
    {
        return (a.isOutput() && b.isOutput());
        //    throw new IllegalArgumentException("Cannot connect two outputs: "+a+", "+b);
    }

    public boolean add(Connection connection)
    {
        Connector a = connection.getSource();
        Connector b = connection.getDestination();

        if (a == b)
        {
            return false;
         //   throw new IllegalArgumentException("Cannot connect connector to itself:"+a);
        }

        Node na = nodeOrNull(a);
        Node nb = nodeOrNull(b);

        if (na == null) checkTarget(a);
        if (nb == null) checkTarget(b);
        
        
        {
            Connector checkout1 = a;
            Connector checkout2 = b;
    
            Node ra = null;
            Node rb = null;
            
            if (na != null)
            {
                ra = na.root();
                checkout1 = ra.connector;
            }
            if (nb != null)
            {
                rb = nb.root();
                checkout2 = rb.connector;
            }
            
            if (ra != null && ra == rb)
                // already connected
                return false;
            
            // check outputs:
            if (twoOutputs(checkout1, checkout2))
                return false;

            if (checkout2.isOutput())
            {
                Node swapn = nb;
                nb = na;
                na = swapn;
                
                Connector swapc = b;
                b = a;
                a = swapc;
            }   
        }
        
        // graph(b) contains no output => graph(a) eventually contains an output

        boolean updateA = false;
        boolean updateB = false;
        
        if (na == null)
        {
            na = new Node(a);
            nodemap.put(a, na);
            updateA = true;
        }
        if (nb == null)
        {
            nb = new Node(b);
            na.addChild(nb, connection);
            nodemap.put(b, nb);
            updateB = true;
        }
        else
        {   
            // nb must be root of graph(nb)
            nb.becomeRoot();
            na.addChild(nb, connection);
        }
        
        modCount++;
        if (updateA) a.updateConnectedState();
        if (updateB) b.updateConnectedState();
        fireConnectionAdded(connection);
        fireUpdateTree(nb); // update sub tree
        return true;
    }
    
    private void fireUpdateTree(Node root)
    {
        // TODO
    }
    
    private void disconnectParentFromChild(Node parent, Node child)
    {
        Connection removedConnection = child.parentConnection;
        parent.removeChild(child);

        if (!parent.isConnected()) nodemap.remove(parent.connector);
        if (!child.isConnected()) nodemap.remove(child.connector);

        modCount++;
        
        if (!parent.isConnected()) parent.connector.updateConnectedState();
        if (!child.isConnected()) child.connector.updateConnectedState();

        fireConnectionRemoved(removedConnection);

        // update disconnected subtree
        if (child.isConnected() && parent.root().isOutput())
            fireUpdateTree(child);
    }

    public boolean disconnect(Connector a, Connector b)
    {
        Node na = getNode(a);
        Node nb = getNode(b);
        
        if (na == null || nb == null)
            // not connected
            return false;
                
        if (na.parent == nb) disconnectParentFromChild(nb, na);
        else if (nb.parent == na) disconnectParentFromChild(na, nb);
        else return false; // not connected
        
        return true;        
    }

    public Signal getSignal(Connector c)
    {
        throw new UnsupportedOperationException("not implemented");
    }

    public ModuleContainer getTarget()
    {
        return target;
    }

    public boolean remove(Connection connection)
    {
        return disconnect(connection.getSource(), connection.getDestination());
    }

    public boolean canAddConnection(Connection connection)
    {
        return canConnect(connection.getSource(), connection.getDestination());
    }

    public boolean canConnect(Connector a, Connector b)
    {
        return connectionSupported(a, b, true);
    }

    public Connection getBreakableConnection(Connector connector)
    {
        Node node = getNode(connector);
        
        if (node == null)
            return null;
        
        return node.parent != null && node.root().isOutput()
            ? node.parentConnection : null;
    }
    
    public boolean breakConnection(Connector connector)
    {
        Connection con = getBreakableConnection(connector);
        return con != null && remove(con);
    }

    public boolean canBreakConnection(Connector connector)
    {
        return getBreakableConnection(connector) != null;
    }

    public Connection connect(Connector a, Connector b)
    {
        Connection con = create(a, b);
        if (add(con))
            return con;
        else
            return null;
    }

    public boolean disconnect(Connector connector)
    {
        return removeAll(getConnections(connector));
    }

    public boolean disconnect(Module module)
    {
        return removeAll(getConnections(module));
    }
    
    public void clear()
    {
        if (!nodemap.isEmpty())
        {
            nodemap.clear();
            modCount ++;
        }
    }

    public <K extends Connection> boolean removeAll(Collection<K> c)
    {
        if (c.isEmpty())
            return false;
        int knownMod = modCount;
        for (Connection con: c)
            remove(con);
        return knownMod != modCount;
    }
    
    public <K extends Connection> boolean retainAll(Collection<K> c)
    {
        if (c.isEmpty())
            return false;
        int knownMod = modCount;
        for (Connection con: getConnections())
            if (!c.contains(con))
                remove(con);
        return knownMod != modCount;
    }

    public <K extends Connection> boolean addAll(Collection<K> c)
    {
        if (c.isEmpty())
            return false;
        int knownMod = modCount;
        for (K con: c)
            if (canAddConnection(con))
                add(con);
        return knownMod != modCount;
    }

    private void addChildConnections(Collection<Connection> c, Node node)
    {
        for (Node child: node)
        {
            assert child.parentConnection != null;
            c.add(child.parentConnection);
        }
    }

    private void addChildren(Collection<Connector> c, Node node)
    {
        for (Node child: node)
        {
            assert child.connector != null;
            c.add(child.connector);
        }
    }
    
    public Collection<Connection> getChildConnections(Connector connector)
    {
        List<Connection> c = new LinkedList<Connection>();
        Node n = getNode(connector);
        if (n != null)
            addChildConnections(c, n);
        return Collections.<Connection>unmodifiableList(c);
    }

    public Collection<Connector> getChildConnectors(Connector connector)
    {
        List<Connector> c = new LinkedList<Connector>();
        Node n = getNode(connector);
        if (n != null)
            addChildren(c, n);
        return Collections.<Connector>unmodifiableList(c);
    }

    public int getChildCount(Connector connector)
    {
        Node n = nodeOrNull(connector);
        return n == null ? 0 : n.childcount();
    }
    
    public boolean isConnected(Connector connector)
    {
        return nodemap.containsKey(connector);
    }

    public Collection<Connector> getConnected(Connector connector)
    {
        Node n = getNode(connector);
        List<Connector> c = new LinkedList<Connector>();
        if (n != null)
        {
            Node p = n.parent;
            if (p != null)
            {
                assert p.connector != null;
                c.add(p.connector);
            }
            addChildren(c, n);
            c.add(n.connector);
        }
        return Collections.unmodifiableList(c);
    }

    public Collection<Connection> getConnections(Connector connector)
    {
        Node n = getNode(connector);
        List<Connection> c = new LinkedList<Connection>();
        if (n != null)
        {
            if (n.parent != null)
            {
                assert n.parentConnection != null;
                c.add(n.parentConnection);
            }
            addChildConnections(c, n);
        }
        
        return Collections.<Connection>unmodifiableList(c);
    }

    public Collection<Connection> getConnectionTree(Connector connector)
    {
        List<Connection> c = new LinkedList<Connection>();

        Node n = getNode(connector);
        if (n != null)
        {
            Iterator<Node> i=n.root().bfsIterator();
            
            assert !i.hasNext();
            i.next(); // the root has no parentConnection
            
            while(i.hasNext())
            {
                Connection connection = i.next().parentConnection;
                assert connection != null;
                c.add(connection);
            }
        }
        return Collections.<Connection>unmodifiableList(c);
    }

    public Collection<Connection> getConnectionTrees(Module module)
    {
        Set<Connection> set = new HashSet<Connection>();
        
        ModuleDescriptor md = module.getDescriptor();
        
        for (int i=md.getConnectorCount()-1;i>=0;i--)
        {
            Connector connector = module.getConnector(md.getConnectorDescriptor(i));
            Node n = getNode(connector);
            if (n!= null && (!set.contains(n.parentConnection)))
            {
                // if condition where false this implies that the connector belongs
                // to a graph that has already been added
                set.addAll(getConnectionTree(connector));
            }
        }
        
        return Collections.<Connection>unmodifiableSet(set);
    }

    public boolean isConnected(Module module, boolean testInputs)
    {
        ModuleDescriptor md = module.getDescriptor();
        for (int i=md.getConnectorCount()-1;i>=0;i--)
        {
            Connector connector = module.getConnector(md.getConnectorDescriptor(i));
            
            if (isConnected(connector))
            {
                if (connector.isOutput())
                    return true;
                else if (testInputs)
                    return true;
                
                Connector source = getSource(connector);
                if (source != null && source != connector && source.isOutput())
                    return true;
            }
        }
        return false;
    }
    
    public Collection<Connection> getConnections()
    {
        return new AbstractCollection<Connection>()
        {
            private Collection<Connection> buffer;
            
            private Iterator<Connection> iter()
            {
                return ConnectionManagerImpl.this.iterator();
            }
            
            private void checkBuffer()
            {
                if (buffer != null)
                    return;
                buffer = new LinkedList<Connection>();
                Iterator<Connection> iter = iter();
                while (iter.hasNext())
                    buffer.add(iter.next());
            }

            @Override
            public Iterator<Connection> iterator()
            {
                checkBuffer();
                return new UnmodifiableIterator<Connection>(buffer.iterator());
            }

            @Override
            public int size()
            {
                checkBuffer();
                return buffer.size();
            }
            
            public boolean isEmpty()
            {
                return (buffer != null) ? buffer.isEmpty() : iter().hasNext();
            }
            
        };
    }

    public Collection<Connection> getConnections(Module module)
    {
        Set<Connection> set = new HashSet<Connection>();
        
        ModuleDescriptor md = module.getDescriptor();
        
        for (int i=md.getConnectorCount()-1;i>=0;i--)
        {
            Connector connector = module.getConnector(md.getConnectorDescriptor(i));
            set.addAll(getConnections(connector));
        }
        
        return Collections.unmodifiableSet(set);
    }

    public Collection<Connector> getTreeNodes(Connector connector)
    {
        List<Connector> c = new LinkedList<Connector>();
        Node n = getNode(connector);
        if (n != null)
        {
            for(Iterator<Node> i=n.root().bfsIterator();i.hasNext();)
            {
                Connector node = i.next().connector;
                assert node != null;
                c.add(node);
            }
        }
        return Collections.unmodifiableList(c);
    }

    public Connector getParent(Connector connector)
    {
        Node n = nodeOrNull(connector);
        if (n == null)
        {
            checkTarget(connector);
            return null;
        }
        
        n = n.parent;
        return n != null ? n.connector : null;
    }

    public Connector getSource(Connector connector)
    {
        Connector source = getParent(connector);
        return (source != null && source.isOutput()) ? source : null;
    }
    
    public Connector getRoot(Connector connector)
    {
        Node n = nodeOrNull(connector);
        if (n == null)
        {
            checkTarget(connector);
            return null;
        }
        
        return n.root().connector;
    }

    public boolean isConnected(Connector a, Connector b)
    {
        return isConnected(nodeOrNull(a), nodeOrNull(b));
    }

    protected boolean isConnected(Node na, Node nb)
    {
        return na != null && nb != null && (na.root() == nb.root());
    }

    protected boolean isPathPresent(Node na, Node nb)
    {
        return na != null && nb != null && (na.root() == nb.root());
    }
    
    public boolean isPathPresent(Connector a, Connector b)
    {
        return isPathPresent(nodeOrNull(a), nodeOrNull(b));
    }

    public boolean isConnectedWithOutput(Connector connector)
    {
        return getSource(connector) != null;
    }

    public Collection<Connector> getShortestPathToRoot(Connector a, Connector b)
    {
        checkTarget(a);
        checkTarget(b);

        Node na = nodeOrNull(a);
        Node nb = nodeOrNull(b);
        
        if (na == null || nb == null) return Collections.<Connector>emptyList();

        LinkedList<Connector> c = new LinkedList<Connector>();
        if (!connectorPath(c, nb, na))
        {
            c.clear();
            if (!connectorPath(c, na, nb))
            {
                return Collections.<Connector>emptyList();
            }
        }
        return Collections.<Connector>unmodifiableCollection(c);
    }
    
    private boolean connectorPath(Collection<Connector> c, Node start, Node stop)
    {
        PathIterator path = new PathIterator(start, stop);
        while (path.hasNext())
            c.add(path.next().connector);
        return path.exists();
    }

    public Collection<Connection> getShortestPathToRootTransitions(Connector a, Connector b)
    {
        checkTarget(a);
        checkTarget(b);

        Node na = nodeOrNull(a);
        Node nb = nodeOrNull(b);
        
        if (na == null || nb == null) return Collections.<Connection>emptyList();

        LinkedList<Connection> c = new LinkedList<Connection>();
        if (!connectionPath(c, nb, na))
        {
            c.clear();
            if (!connectionPath(c, na, nb))
            {
                return Collections.<Connection>emptyList();
            }
        }
        return Collections.unmodifiableCollection(c);
    }
    
    private boolean connectionPath(LinkedList<Connection> c, Node start, Node stop)
    {
        PathIterator path = new PathIterator(start, stop);
        while (path.hasNext())
            c.add(path.next().parentConnection);
        if (!c.isEmpty())
            c.removeLast();
        return path.exists() && (!c.isEmpty());
    }

    private class PathIterator implements Iterator<Node>
    {

        private Node pos;
        private Node stop;
        private Node end;
        private boolean exists = false;
        
        public PathIterator(Node start, Node stop)
        {
            this.pos = start;
            this.stop = stop;
            this.end = stop.parent;
        }

        public boolean exists()
        {
            return exists;
        }
        
        public boolean hasNext()
        {
            if (pos == null || pos == end)
                return false;
            if (pos == stop)
                exists = true;
            return true;
        }

        public Node next()
        {
            if (!hasNext())
                throw new NoSuchElementException();
            
            Node next = pos;
            pos = pos.parent;
            return next;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
    }

    private void checkTarget(Connector c)
    {
        if (!inTarget(c))
            throw new IllegalArgumentException("connector not present in target: "+c);
    }
    
    public boolean inTarget(Connector c)
    {
        return c.getOwner().getParent() == getTarget();
    }
    
    private boolean connectionSupported(Connector a, Connector b, boolean checkState)
    {
        if (    a == b || 
                (a.isOutput() && b.isOutput()) ||
                (!inTarget(a)) || (!inTarget(b))) return false;
        
        Node na = nodeOrNull(a);
        Node nb = nodeOrNull(b);
        
        if (na == null || nb == null) return true;
        
        Node ra = na.root();
        Node rb = nb.root();
        
        boolean input = !(ra.isOutput() && rb.isOutput());
        
        if (checkState)
            return ra != rb && input;
        else 
            return ra == rb || input;
    }

    public boolean isConnectionSupported(Connector a, Connector b)
    {
        return connectionSupported(a, b, false);
    }

    public boolean isConnectionSupported(Connection connection)
    {
        return isConnectionSupported(connection.getSource(), connection.getDestination());
    }

    public int size()
    {
        return connections;
    }
    
    public Collection<Connector> connectors()
    {
        return new AbstractCollection<Connector>()
        {
            private Collection<Connector> buffer;
            
            private void checkBuffer()
            {
                if (buffer != null)
                    return;
                buffer = new ArrayList<Connector>(nodemap.size());
                Iterator<Node> iter = nodes();
                while (iter.hasNext())
                    buffer.add(iter.next().connector); 
            }

            @Override
            public Iterator<Connector> iterator()
            {
                checkBuffer();
                return new UnmodifiableIterator<Connector>(buffer.iterator());
            }

            @Override
            public int size()
            {
                return (buffer != null) ? buffer.size() : nodemap.size();
            }
            
            public boolean isEmpty()
            {
                return (buffer != null) ? buffer.isEmpty() : nodemap.isEmpty();
            }
            
        };
    }

    public Iterator<Connection> iterator()
    {
        return new Iterator<Connection>()
        {
            Iterator<Node> iter = nodes();
            Connection current;
            Connection removable;
            
            public boolean hasNext()
            {
                while (current == null && iter.hasNext())
                {
                    Node node = iter.next();
                    if (node.parentConnection != null)
                        current = node.parentConnection;
                }
                return current != null; 
            }
            
            public Connection next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                
                removable = current;
                current = null;
                return removable;
            }
            
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    private Iterator<Node> nodes()
    {
        return nodemap.values().iterator();
    }

    // **** nested classes ***************************************************

    @SuppressWarnings("unchecked")
    static class Node implements Iterable<Node>
    {
        // the connector
        Connector connector;
        // connection to the parent
        Connection parentConnection;
        // children
        LinkedListItem<Node> children;
        // parent node
        Node parent;
        
        Node(Connector connector)
        {
            this.connector = connector;
        }

        public void becomeRoot()
        {
            while (parent != null)
                becomeParent();

            assert root() == this;
        }
        
        public void becomeParent()
        {
            if (parent == null)
                return;
            
            Connection connection = parentConnection;
            parentConnection = null;
            
            Node subtree = parent;
            parent.removeChild(this);
            
            subtree.becomeRoot();
            addChild(subtree, connection);
        }
        
        boolean isOutput()
        {
            return connector.isOutput();
        }
        
        public Node root()
        {
            Node pos = this;
            while (pos.parent != null)
                pos = pos.parent;
            return pos;
        }

        public boolean isConnected()
        {
            return parent != null || children != null;
        }
        
        public void add(Node node, Connection parentConnection)
        {
            addChild(node, parentConnection);
        }

        private void addChild(Node child, Connection parentConnection)
        {
            // children==null-argument supported
            children = LinkedListItem.<Node>add(children, child);
            child.parent = this;
            child.parentConnection = parentConnection;
            assert parentConnection != null;
        }
        
        private void removeChild(Node child)
        {
            // children==null-argument supported
            children = LinkedListItem.<Node>remove(children, child);
            child.parent = null;
            child.parentConnection = null;
        }

        int childcount()
        {
            // children==null-argument supported
            return LinkedListItem.size(children);
        }

        public Iterator<Node> iterator()
        {
            return children();
        }

        Iterator<Node> children()
        {
            return new Iterator<Node>()
            {
                LinkedListItem<Node> pos = children;
                
                public boolean hasNext()
                {
                    return pos != null;
                }

                public Node next()
                {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    Node next = pos.element();
                    pos = pos.next;
                    return next;
                }

                public void remove()
                {
                    throw new UnsupportedOperationException();
                }
                
            };
        }
        
        Iterator<Node> bfsIterator()
        {
            return new BFSIterator<Node>(this)
            {
                protected void enqueueChildren(Queue<Node> queue, Node parent)
                {
                    for (Node child: parent)
                        queue.offer(child);
                }
            };
        }
    }

    static class LinkedListItem<E>
    {
        private LinkedListItem<E> next;
        private E element;
        
        LinkedListItem(LinkedListItem<E> next, E element)
        {
            this.element = element;
            this.next = next;
        }

        LinkedListItem<E> next() { return next; }
        E element() { return element; }
        
        static <E> LinkedListItem<E> add(LinkedListItem<E> list, E element)
        {
            return new LinkedListItem<E>(list, element);
        }
        
        static <E> LinkedListItem<E> remove(LinkedListItem<E> firstInList, E element)
        {
            if (firstInList == null)
                return null;
                                    
            LinkedListItem<E> pos = firstInList;
            LinkedListItem<E> prev = null;
            while (pos != null)
            {
                if (eq(pos.element, element))
                {
                    if (prev == null) firstInList = pos.next;
                    else prev.next = pos.next;
                    break;
                }
                prev = pos;
                pos = pos.next;
            }
            return firstInList;
        }
        
        static int size(LinkedListItem<?> list)
        {
            int size = 0;
            while (list != null)
            {
                size++;
                list = list.next;
            }
            return size;
        }
        
        static boolean eq(Object a, Object b)
        {
            return a==b/* || a.equals(b)*/;
        }
        
    }
    
}
