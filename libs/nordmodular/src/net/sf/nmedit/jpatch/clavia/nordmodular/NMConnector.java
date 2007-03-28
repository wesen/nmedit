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
 * Created on Apr 7, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.sf.nmedit.jpatch.AbstractConnector;
import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.nmutils.collections.UnmodifiableIterator;
import net.sf.nmedit.nmutils.iterator.BFSIterator;

/**
 * Connector implementation.
 * <h1>Cabling</h1>
 * <p>
 * <i>Convention:</i>
 * <ul>
 * <li>The connection between connectors <code>a</code> and <code>b</code>
 * is identified by the (undirected) transition <code>(a,b)</code>.</li>
 * </ul>
 * <i>Rules:</i>
 * <ul>
 * <li>The transition <code>(a,a)</code> is not allowed</li>
 * <li>A connection contains no circles.</li>
 * <li>The connectors <code>a</code> and <code>b</code> belong to the same
 * connection when a path from <code>a</code> to <code>b</code> exists.</li>
 * <li>A connection contains none or at most one output connector.</li>
 * </ul>
 * <i>Implications/Implementation:</i>
 * <ul>
 * <li>The graph representing a connection is a tree (circles are not
 * possible).</li>
 * <li>An output connector is always the root of the tree.</li>
 * <li>In a connection that contains no output connector, any connector can
 * become the root.</li>
 * </ul>
 * 
 * <i>Note:</i> The operations modifying a connection assure
 * that the rules are not violated.
 * 
 * </p>
 * 
 * @author Christian Schneider
 */
public class NMConnector extends AbstractConnector implements Connector
{

    /**
     * parent connector - directed towards the output connector if part of the connection
     */
    private NMConnector parent;

    /**
     * child connectors - against the direction to the output connector if part
     * of the connection
     */
    //private HashSet<Connector> children;
    private List<NMConnector> children;

    /**
     * graph/connection the connector belongs to
     */
    private Graph graph;

    /**
     * the module this connector is part of
     */
    private final NMModule module;

    /**
     * definition of the connector
     */
    private final ConnectorDescriptor descriptor;

    /**
     * Creates a new connector.
     * 
     * @param definition definition of the connector
     * @param owner module the connector is part of
     */
    public NMConnector( ConnectorDescriptor descriptor, NMModule owner )
    {
        this.module = owner;
        this.descriptor = descriptor;
        parent = null;
        children = new LinkedList<NMConnector>();// new HashSet<Connector>();
        graph = new Graph();
        graph.add( this );
        if (descriptor.isOutput())
        {
            graph.setOutput( this );
        }
    }

    /**
     * Returns the module this connector is part of.
     * @return the module this connector is part of
     */
    public NMModule getModule()
    {
        return module;
    }

    /**
     * Returns the parent <code>p</code> of the connector.
     * If the return value is not <code>null</code> indicates that the
     * cable <code>(connector, connector.getParent())</code> exists.
     * 
     * @return the parent connector
     */
    /*public NMConnector getParent()
    {
        return parent;
    }-*/

    /**
     * Returns the output connector of the connection this connector is part of.
     * If no output connector is part of the connection <code>null</code> is returned.
     * 
     * @return output connector of the connection
     */
    public NMConnector getOutput()
    {
        return graph.getOutput();
    }

    /**
     * Returns the root of the tree representing the connection this connector
     * is part of. This is usefull if over each connector in a connection should
     * be iterated.
     * 
     * <pre><code>
     * Connector c = someConnector();
     * Connector root = c.getRoot(); // root of the tree representing a connection
     * Iterator&lt;Connector&gt; iterator = root.breadthFirstSearch();
     * </code></pre>
     * 
     * @return
     */
    public NMConnector getRoot()
    {
        NMConnector c = this;
        while (c.parent != null)
        {
            c = c.parent;
        }
        return c;
    }

    /**
     * Returns an iterator performing a breadth first search starting from this
     * connector.
     * 
     * @return breadth first search iteration starting from this connector
     * @see #getRoot()
     */
    public Iterator<NMConnector> breadthFirstSearch()
    {
        return new ConnectionBFSIterator( this );
    }

    /**
     * Returns an iteration over the child connectors.
     * @return iteration over the child connectors
     */
    public Iterator<NMConnector> childIterator()
    {
        return children.iterator();
    }
    
    /**
     * Returns the number of children of the connector.
     * 
     * @return number of children
     */
    public int getChildCount()
    {
        return children.size();
    }

    /**
     * Returns true if this connector has a connection to another connector.
     * 
     * @return true if this connector has a connection to another connector
     */
    public boolean isConnected()
    {
        return graph.size() > 1;
    }

    /**
     * Creates a connection between the connector and the given connector.
     * Returns <code>true</code> when the connectors became connected. <br />
     * Calling <code>a.connect(b)</code> is equivalent to
     * <code>b.connect(a)</code>.
     * 
     * @param c a connector
     * @return <code>true</code> when the connectors became connected
     * @see #connect(NMConnector, NMConnector)
     */
    public boolean connectWith( Connector c )
    {
        return NMConnector.connect( this, (NMConnector) c, null );
    }

    // overwrites  color
    public boolean connectWith( NMConnector c, Signal color )
    {
        return NMConnector.connect( this, c, color );
    }

    /**
     * Disconnects the connector from the given connector. Returns
     * <code>true</code> if the connection could be removed or
     * <code>false</code> if there was no connection. <br />
     * Calling <code>a.disconnect(b)</code> is equivalent to
     * <code>b.disconnect(a)</code>.
     * 
     * @param c a connector
     * @return <code>true</code> if the link was removed
     * @see #disconnect(NMConnector, NMConnector)
     */
    public boolean disconnectFrom( Connector c )
    {
        return disconnect( this, (NMConnector) c );
    }
    
    private static NMConnector connectHelper(NMConnector a, NMConnector b, Signal color)
    {
        // PRECONDITION: b.graph.getOutput() == null
        NMConnector updatePartialGraph = null;

        // b becomes root of it's graph and will be added to a
        b.rotate();

        // we drop graph(b). thus we don't have
        // to set the output of the graph

        for (NMConnector c : b.graph)
        {
            // note: b is element of b.graph
            a.graph.add( c );
            // update graph property for all new connectors
            c.graph = a.graph;
        }

        // set custom color
        if (color!=null)
            a.graph.setColor(color);
        
        // add child and set parent
        a.children.add( b );
        b.parent = a;
        
        // graph to update
        updatePartialGraph = b;
        return updatePartialGraph;
    }

    /**
     * Connects two connectors. Returns true if the connection could be
     * established or false if this was not possible due to a violation of the
     * rules of a connection.
     * 
     * @param a a connector
     * @param b a connector
     * @return true if the connection could be established
     */
    public static boolean connect( NMConnector a, NMConnector b, Signal color )
    {
        if (a == b)
            return false;
        
        if (a.graph == b.graph)
        {
            // both are part of the same graph:
            // <=> (a==b || a.graph.contains(b) || b.graph.contains(a))
            return false;
        }
        
        if (a.graph.getOutput() != null && b.graph.getOutput() != null)
        {
            // can't connect two outputs
            return false;
        }

        boolean aconnected = a.isConnected();
        boolean bconnected = b.isConnected();
        NMConnector updatePartialGraph = null;

        if (a.graph.getOutput() != null)
        {
            // connected
            updatePartialGraph = connectHelper(a, b, color);
        }
        else
        {
            // in -> out ==> error

            if (b.graph.getOutput() != null)
            {
                updatePartialGraph = connectHelper(b, a, color);
            }
            else
            {
                // both have no output, make a parent of b

                if (b.graph.size() > a.graph.size())
                {
                    // the graph c with less nodes
                    // becomes the child graph to
                    // reduce costs for c.rotate()
                    // and merging both graphs
                    NMConnector t = b;
                    b = a;
                    a = t;
                }

                connectHelper(a, b, color);
            }
        }

        if (aconnected != a.isConnected()) a.fireConnectorStateChanged();
        if (bconnected != b.isConnected()) b.fireConnectorStateChanged();

        VoiceArea va = a.getModule().getParent();

        if (va != null)
        {
            va.getConnectionManager().fireConnectionAdded( a, b );
            if (updatePartialGraph != null)
            {
                va.getConnectionManager().updateGraph( updatePartialGraph );
            }
        }
        else
        {
            // error
        }

        /* post conditions
        if (a.getSource()!=b && b.getSource()!=a)
            throw new ...("connector source not set");

        if (!(a.children.contains(b) || b.children.contains(a)))
            throw new ...("connector child not set");
        */

        return true;
    }

    /**
     * Disconnects two connectors.
     * 
     * @param a a connector
     * @param b a connector
     * @return true if the operation resulted in a modified connection
     */
    public static boolean disconnect( NMConnector a, NMConnector b )
    {
        if (( a.graph != b.graph ) || ( a == b ))
        {
            // can't seperate different graphs
            // can't disconnect from itself
            return false;
        }

        if (a.parent == b)
        {
            NMConnector t = b;
            b = a;
            a = t;
        }
        else if (b.parent != a)
        {
            // one connecter must be parent of the other
            return false;
        }

        // now: b.parent == a

        b.parent = null; // disconnect graphs
        a.children.remove( b );

        Graph g = new Graph();
        g.setColor(a.graph.getColor());
        for (Iterator<NMConnector> iter = new ConnectionBFSIterator( b ); iter
                .hasNext();)
        {
            NMConnector c = iter.next();
            a.graph.remove( c );
            g.add( c );
            c.graph = g;
        }

        // g has no output because b was child of a
        // and a output connector is only at the root of the tree

        if (!a.isConnected()) a.fireConnectorStateChanged();
        if (!b.isConnected()) b.fireConnectorStateChanged();

        VoiceArea va = a.getModule().getParent();

        if (va != null)
        {
            va.getConnectionManager().fireConnectionRemoved( a, b );
            if (a.graph.getOutput() != null)
            {
                va.getConnectionManager().updateGraph( b );
            }
            else if (b.graph.getOutput() != null)
            {
                va.getConnectionManager().updateGraph( a );
            }
        }
        
        return true;
    }
    
    /**
     * Rotates the tree so that the connector on which the operation was called
     * becomes it's root. <img src="doc-files/cable-op-rotate.png" width="528"
     * height="223" alt="An illustration of a rotation." />
     * <p>
     * In the illustration <code>rotation()</code> is called on connector
     * <code>#4</code> which becomes the root of the tree. <br />
     * Note the changed direction of the transitions <code>(4,2)</code> and
     * <code>(2,1)</code>.
     * </p>
     */
    private void rotate()
    {
        NMConnector n; // current child
        NMConnector p; // current parent of n
        NMConnector newn; // new parent of n

        NMConnector tmp;

        n = this; // start from current node
        p = n.parent;
        newn = null; // n will become root

        while (p != null)
        {
            tmp = p.parent; // old parent of p

            p.children.remove( n ); // remove child n
            n.parent = newn; // set new parent of n

            n.children.add( p ); // make p child of n
            p.parent = n;

            newn = n;
            n = p;
            p = tmp;
        }
    }

    /**
     * Returns true if {@link #breakCable()} would result in a modified
     * connection.
     * 
     * @return <code>true</code> if the connection changed
     * @see #breakCable()
     */
    public boolean canBreak()
    {
        return parent != null;
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
    public boolean breakCable()
    {
        if (canBreak())
        {
            // disconnect will return true
            return disconnect( this, parent );
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes the connector from the connection it is part of. Thus the
     * transitions to it's parent and children are removed. <img
     * src="doc-files/cable-op-disconnect.png" width="528" height="223" alt="An
     * illustration of the disconnect operation." />
     */
    public void disconnectCables()
    {
        if (!children.isEmpty())
        {
            NMConnector[] list = children
                    .toArray( new NMConnector[children.size()] );
            for (NMConnector c : list)
            {
                disconnect( this, c );
            }
        }

        if (parent != null) disconnect( this, parent );
    }

    public final ConnectorDescriptor getDescriptor()
    {
        return descriptor;
    }

    /**
     * @see DConnector#getName()
     */
    public String getName()
    {
        return getDescriptor().getComponentName();
    }

    public int getSignalId()
    {
        return getDescriptor().getSignalId();
    }

    public boolean isOutput()
    {
        return getDescriptor().isOutput();
    }

    /**
     * Returns the signal of the connection this connector is part of.
     * 
     * @return the signal of the connection
     */
    public Signal getConnectionColor()
    {
        return graph.getColor();
    }
    
    public void setConnectionColor(Signal s)
    {
        graph.setColor(s);
    }

    /**
     * Graph representing a connection.
     * 
     * @author Christian Schneider
     */
    private static final class Graph extends HashSet<NMConnector>
    {

        /**
         * The output connector of the graph. Can be <code>null</code>.
         */
        private NMConnector out;
        
        private Signal userColor = null;

        public Graph()
        {
            super();
            this.out = null;
        }

        /**
         * Returns the signal of the connection
         * 
         * @return the signal of the connection
         */
        public Signal getColor()
        {
            if (userColor != null)
                return userColor;
            
            // default: no signal
            return out == null ? Signal.NONE : Signal.bySignalID( out
                    .getSignalId() );
        }
        
        public void setColor(Signal s)
        {
            if (Signal.USER1.getSignalID()==s.getSignalID()||Signal.USER2.getSignalID()==s.getSignalID())
                userColor = s;
            else
                userColor = null;
        }

        /**
         * Returns the output connector of the connection. The return value is
         * <code>null</code> when no output connector is part of a connection.
         * 
         * @return the output connector of the connection
         */
        public NMConnector getOutput()
        {
            return out;
        }

        /**
         * Sets the output connector property
         * 
         * @param out the output connector
         */
        public void setOutput( NMConnector out )
        {
            this.out = out;
        }

    }
    
    private static final class ConnectionBFSIterator extends BFSIterator<NMConnector>
    {

        public ConnectionBFSIterator( NMConnector start )
        {
            super( start );
        }

        @Override
        protected void enqueueChildren( Queue<NMConnector> queue, NMConnector parent )
        {
            queue.addAll(parent.children);
        }
        
    }

    public NMModule getOwner()
    {
        return module;
    }

    public ConnectionManager getConnectionManager()
    {
        VoiceArea va = getOwner().getParent();
        return va != null ? va.getConnectionManager() : null;
    }

    public NMConnector getSource()
    {
        return parent;
    }
    
    public Iterator<Connector> getConnected()
    {
        List<Connector> list = new ArrayList<Connector>(children.size()+1);
        list.add(this);
        for (Connector c : children)
            list.add(c);
        return list.iterator();
    }
    
    public Iterator<Connector> getAllConnected()
    {
        return new UnmodifiableIterator<Connector>(graph.iterator());
    }

    public int getChildConnectorCount()
    {
        return children.size();
    }

    public NMConnector getChildConnector( int index )
    {
        return children.get(index);
    }

    public NMConnector[] getChildren()
    {
        return children.toArray(new NMConnector[children.size()]);
    }

    public boolean isConnectedWith( Connector c )
    {
        return graph.contains(c);
    }
    
    public String toString()
    {
        return 
        "Connector["
        +getOwner()
        +",name="+getDescriptor().getComponentName()
        +",index="+getDescriptor().getIndex()
        +",output="+getDescriptor().isOutput()
        +",children="+getChildConnectorCount()
        +",source="+(getSource()!=null)
        +"]";
    }
    
    public net.sf.nmedit.jpatch.Signal getDefaultSignal()
    {
        return Signal.bySignalID(getSignalId());
    }

    
    public net.sf.nmedit.jpatch.Signal getSignal()
    {
        if (isOutput())
            return getDefaultSignal();
        
        NMConnector out = getOutput();
        return out != null ? out.getSignal() : getDefaultSignal();
    }
    
}
