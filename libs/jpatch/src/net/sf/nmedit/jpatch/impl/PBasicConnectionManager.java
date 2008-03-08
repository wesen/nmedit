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
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PSignal;
import net.sf.nmedit.jpatch.PUndoableEditFactory;
import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PConnectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The reference implementation of interface {@link PConnectionManager}.
 * @author Christian Schneider
 */
public class PBasicConnectionManager implements PConnectionManager
{

    private PBasicModuleContainer container;
    private Map<PConnector, Node> nodemap;
    private int size;
    private EventListenerList eventListeners = new EventListenerList();
    private transient volatile int modCount;

    private static final boolean DEBUG = true;
    
    
    public PBasicConnectionManager(PBasicModuleContainer container)
    {
        this.container = container;
        nodemap = new HashMap<PConnector, Node>();
    }

    public void postEdit(UndoableEdit edit)
    {
        container.postEdit(edit);
    }
    
    public boolean isUndoableEditSupportEnabled()
    {
        return container.isUndoableEditSupportEnabled();
    }
    
    public PUndoableEditFactory getUndoableEditFactory()
    {
        return container.getUndoableEditFactory();
    }
    

    public void addConnectionListener( PConnectionListener l )
    {
        eventListeners.add(PConnectionListener.class, l);
    }

    public void removeConnectionListener( PConnectionListener l )
    {
        eventListeners.remove(PConnectionListener.class, l);
    }

    protected void fireConnectionAdded(PConnector a, PConnector b)
    {
        connectionAddHappened(a, b);
        PConnectionEvent e = null;
        Object[] listenerList = eventListeners.getListenerList();
        for (int i=listenerList.length-2;i>=0;i-=2)
        {
            if (listenerList[i]==PConnectionListener.class)
            {
                if (e == null) e = new PConnectionEvent(this, a, b);
                ((PConnectionListener) listenerList[i+1]).connectionAdded(e);
            }
        }
    }

    protected void fireConnectionRemoved(PConnector a, PConnector b)
    {
        connectionRemoveHappened(a, b);
        PConnectionEvent e = null;
        Object[] listenerList = eventListeners.getListenerList();
        for (int i=listenerList.length-2;i>=0;i-=2)
        {
            if (listenerList[i]==PConnectionListener.class)
            {
                if (e == null) e = new PConnectionEvent(this, a, b);
                ((PConnectionListener) listenerList[i+1]).connectionRemoved(e);
            }
        }
    }

    protected void connectionRemoveHappened(PConnector a, PConnector b)
    {
        if (isUndoableEditSupportEnabled())
        {
            PUndoableEditFactory factory = getUndoableEditFactory();
            if (factory != null)
                postEdit(factory.createDisconnectEdit(a, b));
        }
    }
    
    protected void connectionAddHappened(PConnector a, PConnector b)
    {
        if (isUndoableEditSupportEnabled())
        {
            PUndoableEditFactory factory = getUndoableEditFactory();
            if (factory != null)
                postEdit(factory.createConnectEdit(a, b));
        }
    }
  
    private boolean validTarget(PConnector c)
    {
        PModule module = c.getParentComponent();
        return module != null && module.getParentComponent() == getModuleContainer(); 
    }
    
    private Log getLog()
    {
        return LogFactory.getLog(getClass());
    }

    public boolean add(PConnector a, PConnector b)
    {
        if (DEBUG)
        {
            Log log = getLog();
            if (log.isDebugEnabled())
            {
                log.debug("adding connectors a,b="+a+","+b);
            }
        }
        
        if (a == b)
        {
            if (DEBUG)
            {
                Log log = getLog();
                if (log.isDebugEnabled())
                {
                    log.debug("connectors are equal: a,b="+a+","+b);
                }
            }
            
            return false;
         //   throw new IllegalArgumentException("Cannot connect connector to itself:"+a);
        }

        Node na = nodemap.get(a);
        Node nb = nodemap.get(b);

        if ((na == null && (!validTarget(a)))
                ||(nb == null && (!validTarget(b)))) 
        {

            if (DEBUG)
            {
                Log log = getLog();
                if (log.isDebugEnabled())
                {
                    log.debug("null/valid-target check failed: "+a+","+b);
                }
            }
            
            return false;
        }
        
        {
            PConnector checkout1 = a;
            PConnector checkout2 = b;
    
            Node ra = null;
            Node rb = null;
            
            if (na != null)
            {
                ra = na.root();
                checkout1 = ra.c;
            }
            if (nb != null)
            {
                rb = nb.root();
                checkout2 = rb.c;
            }
            
            if (ra != null && ra == rb)
            {
                // already connected

                if (DEBUG)
                {
                    Log log = getLog();
                    if (log.isDebugEnabled())
                    {
                        log.debug("already connected a,b="+a+","+b);
                    }
                }
                return false;
            }
            
            // two outputs ?
            if (checkout1.isOutput() && checkout2.isOutput())   
            {
                if (DEBUG)
                {
                    Log log = getLog();
                    if (log.isDebugEnabled())
                    {
                        log.debug("cannot connect two outputs: a,b="+a+","+b);
                    }
                }
                return false;
            }

            if (checkout2.isOutput())
            {
                Node swapn = nb;
                nb = na;
                na = swapn;
                
                PConnector swapc = b;
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
            na.addChild(nb);
            nodemap.put(b, nb);
            updateB = true;
        }
        else
        {   
            // nb must be root of graph(nb)
            nb.becomeRoot();
            na.addChild(nb);
        }
        
        modCount++;
        size++;
        if (updateA) a.verifyConnectedState();
        if (updateB) b.verifyConnectedState();
        fireConnectionAdded(a, b);
        fireUpdateTree(nb); // update sub tree
        return true;
    }
    
    private void fireUpdateTree(Node root)
    {
        // TODO
    }
    
    private void disconnectParentFromChild(Node parent, Node child)
    {
        parent.removeChild(child);
        
        if (!parent.isConnected()) nodemap.remove(parent.c);
        if (!child.isConnected()) nodemap.remove(child.c);

        modCount++;
        size--;
        
        if (!parent.isConnected()) parent.c.verifyConnectedState();
        if (!child.isConnected()) child.c.verifyConnectedState();

        fireConnectionRemoved(child.c, parent.c);

        // update disconnected subtree
        if (child.isConnected() && parent.root().isOutput())
            fireUpdateTree(child);
    }

    protected boolean remove(Node a, Node b)
    {
        if (a.p == b) 
        {
            disconnectParentFromChild(b, a);
            return true;
        }
        else if (b.p == a) 
        {
            disconnectParentFromChild(a, b);
            return true;
        }
        else return false; // not connected
    }

    public void clear()
    {
        if (!isEmpty())
        {
            removeAllConnections(connections());
        }
    }

    public boolean add(PConnection c)
    {
        return add(c.getA(), c.getB());
    }

    public int childCount(PConnector c)
    {
        Node n = nodemap.get(c);
        return n == null ? 0 : n.childCount();
    }

    public Collection<PConnector> children(PConnector c)
    {
        Node n = nodemap.get(c);
        if (n == null || n.childCount() <= 0)
            return Collections.<PConnector>emptyList();
        Collection<PConnector> children = new LinkedList<PConnector>();
        n.addChildConnectors(children);
        return Collections.<PConnector>unmodifiableCollection(children);
    }

    public Collection<PConnector> connected()
    {
        int s = size();
        if (s <= 0)
            return Collections.<PConnector>emptyList();
        List<PConnector> a = new ArrayList<PConnector>(s);
        for (Node n: nodemap.values())
            a.add(n.c);
        return Collections.<PConnector>unmodifiableCollection(a);
    }
    
    public Collection<PConnector> connected(PConnector c)
    {
        Node n = nodemap.get(c);
        if (n == null || n.childCount() <= 0)
            return Collections.<PConnector>emptyList();
        List<PConnector> a = new LinkedList<PConnector>();
        n.addChildConnectors(a);
        Node p = n.p;
        if (p != null) a.add(p.c);
        return Collections.<PConnector>unmodifiableCollection(a);
    }

    public Collection<PConnection> connections()
    {
        if (isEmpty())
            return Collections.<PConnection>emptyList();
        
        Collection <PConnection> r = new ArrayList<PConnection>(size);
        for (PConnection connection: this)
            r.add(connection);
        return Collections.<PConnection>unmodifiableCollection(r);
    }
    
    protected void addConnections(Collection<PConnection> c, Node n)
    {
        PConnector p = n.parent();
        if (p != null) 
            c.add(new PConnection(n.c, p));
        n.addChildParentConnections(c);
    }

    public Collection<PConnection> connections(PConnector c)
    {
        Node n = nodemap.get(c);
        if (n == null)
            return Collections.<PConnection>emptyList();

        Collection<PConnection> r = new ArrayList<PConnection>(n.childCount()+1);
        addConnections(r, n);
        return Collections.<PConnection>unmodifiableCollection(r);
    }
    
    public Collection<PConnection> graphConnections(PConnector c)
    {
        Node n = nodemap.get(c);
        if (n == null)
            return Collections.<PConnection>emptyList();
        
        Queue<Node> queue = new LinkedList<Node>();
        Collection<PConnection> g = new LinkedList<PConnection>();
        n.addChildNodes(queue);
        while (!queue.isEmpty())
        {
            n = queue.remove();
            g.add(new PConnection(n.c, n.parent()));
            n.addChildNodes(queue);
        }
        return Collections.<PConnection>unmodifiableCollection(g);
    }

    public Collection<PConnection> connections(PModule m)
    {
        Collection<PConnection> r = new LinkedList<PConnection>();
        for (int i=m.getConnectorCount()-1;i>=0;i--)
        {
            PConnector c = m.getConnector(i);
            Node n = nodemap.get(c);
            if (n != null) addConnections(r, n);
        }
        return Collections.<PConnection>unmodifiableCollection(r);
    }

    public Collection<PConnection> connections(Collection<? extends PModule> ms) {
    	Collection<PConnection> r = new LinkedList<PConnection>();
    	
    	for (PModule m : ms) {
    		if (m != null)
    			r.addAll(connections(m));
    	}
        
    	return Collections.<PConnection>unmodifiableCollection(r);
	}
    

    public boolean isConnected(PModule m)
    {
        for (int i=m.getConnectorCount()-1;i>=0;i--)
        {
            PConnector c = m.getConnector(i);
            if (nodemap.get(c)!=null)
                return true;
        }
        return false;
    }
    
    public boolean contains(PConnection c)
    {
        return isConnected(c.getA(), c.getB());
    }

    public PModuleContainer getModuleContainer()
    {
        return container;
    }

    public PSignal getSignalType(PConnector connector)
    {
        Node n = nodemap.get(connector);
        if (n == null)
            return connector.getDefinedSignals().noSignal();
        n = n.root();
        return n.isOutput() ? n.c.getDefaultSignalType() : connector.getDefinedSignals().noSignal();
    }

    public Collection<PConnector> graph(PConnector c)
    {
        Node n = nodemap.get(c);
        if (n == null)
            return Collections.<PConnector>emptyList();
        
        Queue<Node> queue = new LinkedList<Node>();
        Collection<PConnector> g = new LinkedList<PConnector>();
        queue.offer(n.root());
        while (!queue.isEmpty())
        {
            n = queue.remove();
            g.add(n.c);
            n.addChildNodes(queue);
        }
        return Collections.<PConnector>unmodifiableCollection(g);
    }

    public boolean isConnected(PConnector a, PConnector b)
    {
        Node na = nodemap.get(a);
        if (na == null) return false ;
        if (na.isParent(b)) return true;
        Node nb = nodemap.get(b);
        return nb != null && nb.p == na;
    }

    public boolean isConnected(PConnector c)
    {
        return nodemap.containsKey(c);
    }

    public boolean isEmpty()
    {
        return nodemap.isEmpty();
    }

    public PConnector output(PConnector c)
    {
        PConnector root = root(c);
        return root.isOutput() ? root : null;
    }

    public PConnector parent(PConnector c)
    {
        Node n = nodemap.get(c);
        return n == null ? null : n.parent();
    }

    public PConnector root(PConnector c)
    {
        Node n = nodemap.get(c);
        return n == null ? c : n.root().c;
    }

    public boolean pathExists(PConnector a, PConnector b)
    {
        Node na = nodemap.get(a);
        Node nb = nodemap.get(b);
        return na != null && nb != null && na.root() == nb.root();
    }

    public boolean remove(PConnection c)
    {
        return remove(c.getA(), c.getB());
    }

    public boolean removeAllConnectors(Collection<? extends PConnector> c)
    {
        boolean modified = false;
        for (PConnector connector: c)
            modified |= removeAllConnections(connections(connector));
        return modified;
    }
    
    public boolean removeAllConnections(Collection<? extends PConnection> c)
    {
        boolean modified = false;
        for (PConnection connection: c)
            modified |= remove(connection);
        return modified;
    }

    public boolean remove(PConnector a, PConnector b)
    {
        Node na = nodemap.get(a);
        Node nb = nodemap.get(b);
        return na != null && nb != null && remove(na, nb);
    }
    
    public boolean remove(PConnector c)
    {
        PConnector parent = c.getParentConnector();
        Collection<PConnector> children = c.getChildren();
        
        boolean removed = false;
        
        // remove connection to parent
        if (parent != null)
            removed |= remove(c, parent);
        // remove connections to children
        for (PConnector child: children)
            removed |= remove(c, child);
        
        if (parent != null)
        {
            // connect children with parent
            for (PConnector child: children)
                add(child, parent);
        }
        else
        {
            // connect children with each other
            PConnector previous = null;
            for (PConnector child: children)
            {
                if (previous != null)
                    add(previous, child);
                previous = child;
            }
        }
        return removed;
    }

    public int size()
    {
        return size;
    }

    public Iterator<PConnection> iterator()
    {
        return new Iterator<PConnection>()
        {
            Iterator<Node> iter = nodemap.values().iterator();
            Node next;
            int expectedModCount = modCount;
            
            void align()
            {
                if (next == null)
                {
                    Node n;
                    while (iter.hasNext())
                    {
                        n = iter.next();
                        if (n.p != null)
                        {
                            next = n;
                            break;
                        }
                    }
                }
            }
            
            public boolean hasNext()
            {
                align();
                return next != null;
            }
            
            public PConnection next()
            {              
                if (expectedModCount != modCount)
                    throw new ConcurrentModificationException();
                
                if (!hasNext())
                    throw new NoSuchElementException();
                PConnection c = new PConnection(next.c, next.parent());
                next = null;
                return c;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    protected Node createNode(PConnector c)
    {
        return new Node(c);
    }

    protected class Node
    {
        public PConnector c;
        public Node p;
        private LinkedListItem<Node> children;
        
        public Node(PConnector c)
        {
            this.c = c;
        }
        public void addChildParentConnections(Collection<PConnection> collection)
        {
            LinkedListItem<Node> pos = children;
            while (pos != null)
            {
                collection.add(new PConnection(c, pos.element.c));
                pos = pos.next;
            }
        }
        public void addChildConnectors(Collection<PConnector> collection)
        {
            LinkedListItem<Node> pos = children;
            while (pos != null)
            {
                collection.add(pos.element.c);
                pos = pos.next;
            }
        }
        public void addChildNodes(Collection<Node> collection)
        {
            LinkedListItem<Node> pos = children;
            while (pos != null)
            {
                collection.add(pos.element);
                pos = pos.next;
            }
        }
        public boolean isConnected()
        {
            return p!=null || childCount()>0;
        }
        public PConnection parentConnection()
        {
            return p == null ? null : new PConnection(c, p.c);
        }
        public PConnector parent()
        {
            return p == null ? null : p.c;
        }
        public int childCount()
        {
            return children == null ? 0 : LinkedListItem.size(children);
        }
        /*
        public boolean isChild(Object o)
        {
            return LinkedListItem.
        }*/
        public int hashCode()
        {
            return c.hashCode();
        }
        public boolean equals(Object o)
        {
            return o == this || o == c;
        }
        public Node root()
        {
            Node r = this;
            while (r.p != null)
                r = r.p;
            return r;
        }        
        public boolean isParent(PConnector c)
        {
            return p == null ? false : p.c == c;
        }

        public void becomeRoot()
        {
            while (p != null)
                becomeParent();

            assert root() == this;
        }
        
        public void becomeParent()
        {
            if (p == null)
                return;
            
            Node subtree = p;
            p.removeChild(this);
            
            subtree.becomeRoot();
            addChild(subtree);
        }
        
        boolean isOutput()
        {
            return c.isOutput();
        }

        private void addChild(Node child)
        {
            // children==null-argument supported
            children = LinkedListItem.<Node>add(children, child);
            child.p = this;
        }
        
        private void removeChild(Node child)
        {
            // children==null-argument supported
            children = LinkedListItem.<Node>remove(children, child);
            child.p = null;
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
