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
package net.sf.nmedit.nomad.patch.virtual;

import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import net.sf.nmedit.nomad.patch.virtual.event.ConnectorEvent;
import net.sf.nmedit.nomad.patch.virtual.event.ListenableAdapter;
import net.sf.nmedit.nomad.xml.dom.module.DConnector;

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
public class Connector extends ListenableAdapter<ConnectorEvent>
{

    /**
     * parent connector - directed towards the output connector if part of the connection
     */
    private Connector parent;

    /**
     * child connectors - against the direction to the output connector if part
     * of the connection
     */
    //private HashSet<Connector> children;
    private LinkedList<Connector> children;

    /**
     * graph/connection the connector belongs to
     */
    private Graph graph;

    /**
     * the module this connector is part of
     */
    private final Module module;

    /**
     * definition of the connector
     */
    private final DConnector definition;

    /**
     * the user interface component displaying the connector
     */
    private Component ui;

    /**
     * event message object
     */
    private static ConnectorEvent eventMessage = new ConnectorEvent();

    /**
     * Creates a new connector.
     * 
     * @param definition definition of the connector
     * @param owner module the connector is part of
     */
    public Connector( DConnector definition, Module owner )
    {
        this.module = owner;
        this.definition = definition;
        parent = null;
        children = new LinkedList<Connector>();// new HashSet<Connector>();
        graph = new Graph();
        graph.add( this );
        if (definition.isOutput())
        {
            graph.setOutput( this );
        }
    }

    /**
     * Returns the module this connector is part of.
     * @return the module this connector is part of
     */
    public Module getModule()
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
    public Connector getParent()
    {
        return parent;
    }

    /**
     * Returns the output connector of the connection this connector is part of.
     * If no output connector is part of the connection <code>null</code> is returned.
     * 
     * @return output connector of the connection
     */
    public Connector getOutput()
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
    public Connector getRoot()
    {
        Connector c = this;
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
    public Iterator<Connector> breadthFirstSearch()
    {
        return new BreadthFirstSearchIterator( this );
    }

    /**
     * Returns an iteration over the child connectors.
     * @return iteration over the child connectors
     */
    public Iterator<Connector> childIterator()
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
     * @see #connect(Connector, Connector)
     */
    public boolean connect( Connector c )
    {
        return connect( this, c );
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
     * @see #disconnect(Connector, Connector)
     */
    public boolean disconnect( Connector c )
    {
        return disconnect( this, c );
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
    public static boolean connect( Connector a, Connector b )
    {
        if (a.graph == b.graph)
        {
            // both are part of the same graph:
            // <=> (a==b || a.graph.contains(b) || b.graph.contains(a))
            return false;
        }

        boolean aconnected = a.isConnected();
        boolean bconnected = b.isConnected();
        Connector updatePartialGraph = null;

        if (a.graph.getOutput() != null)
        {
            if (b.graph.getOutput() != null)
            {
                // can't connect two outputs
                return false;
            }
            else
            {

                // b must become root of it's graph
                b.rotate();

                for (Connector c : b.graph)
                {
                    a.graph.add( c );
                    c.graph = a.graph;
                }

                a.children.add( b );
                b.parent = a;

                // connected
            }

            updatePartialGraph = b;

        }
        else
        {
            // in -> out ==> error

            if (b.graph.getOutput() != null)
            {
                /*
                 * new connection: b / a
                 */

                // a must become root of it's graph
                a.rotate();

                // we drop graph(a). thus we don't have
                // to set the output of the graph

                for (Connector aa : a.graph)
                {
                    b.graph.add( aa );
                    aa.graph = b.graph;
                }

                b.children.add( a );
                a.parent = b;

                // connected

                updatePartialGraph = a;
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
                    Connector t = b;
                    b = a;
                    a = t;
                }

                for (Connector c : b.graph)
                {
                    a.graph.add( c );
                    c.graph = a.graph;
                }

                a.children.add( b );
                b.parent = a;

                // connected
            }
        }

        if (aconnected != a.isConnected()) a.fireConnectionStateChanged();
        if (bconnected != b.isConnected()) b.fireConnectionStateChanged();

        VoiceArea va = a.getModule().getVoiceArea();

        if (va != null)
        {
            va.connected( a, b );
            if (updatePartialGraph != null)
            {
                va.updateConnection( updatePartialGraph );
            }
        }

        return true;
    }

    /**
     * Disconnects two connectors.
     * 
     * @param a a connector
     * @param b a connector
     * @return true if the operation resulted in a modified connection
     */
    public static boolean disconnect( Connector a, Connector b )
    {
        if (( a.graph != b.graph ) || ( a == b ))
        {
            // can't seperate different graphs
            // can't disconnect from itself
            return false;
        }

        if (a.parent == b)
        {
            Connector t = b;
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
        for (Iterator<Connector> iter = new BreadthFirstSearchIterator( b ); iter
                .hasNext();)
        {
            Connector c = iter.next();
            a.graph.remove( c );
            g.add( c );
            c.graph = g;
        }

        // g has no output because b was child of a
        // and a output connector is only at the root of the tree

        if (!a.isConnected()) a.fireConnectionStateChanged();
        if (!b.isConnected()) b.fireConnectionStateChanged();

        VoiceArea va = a.getModule().getVoiceArea();

        if (va != null)
        {
            va.disconnected( a, b );
            if (a.graph.getOutput() != null)
            {
                va.updateConnection( b );
            }
            else if (b.graph.getOutput() != null)
            {
                va.updateConnection( a );
            }
        }

        return true;
    }

    /**
     * Notifies the listeners of this connector that the
     * state of the connection changed.
     */
    private void fireConnectionStateChanged()
    {
        eventMessage.connectionStateChanged( this );
        fireEvent( eventMessage );
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
        Connector n; // current child
        Connector p; // current parent of n
        Connector newn; // new parent of n

        Connector tmp;

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
            Connector[] list = children
                    .toArray( new Connector[children.size()] );
            for (Connector c : list)
            {
                disconnect( this, c );
            }
        }

        if (parent != null) disconnect( this, parent );
    }

    /**
     * Returns the definition of this connector.
     * 
     * @return the definition of this connector
     */
    public final DConnector getDefinition()
    {
        return definition;
    }

    /**
     * @see DConnector#getName()
     */
    public String getName()
    {
        return definition.getName();
    }

    /**
     * @see DConnector#getType()
     */
    public int getType()
    {
        return definition.getType();
    }

    /**
     * @see DConnector#getSignal()
     */
    public int getSignal()
    {
        return definition.getSignal();
    }

    /**
     * @see DConnector#isInput()
     */
    public boolean isInput()
    {
        return definition.isInput();
    }

    /**
     * @see DConnector#isOutput()
     */
    public boolean isOutput()
    {
        return definition.isOutput();
    }

    /**
     * @see DConnector#isSignalAudio()
     */
    public boolean isSignalAudio()
    {
        return definition.isSignalAudio();
    }

    /**
     * @see DConnector#isSignalControl()
     */
    public boolean isSignalControl()
    {
        return definition.isSignalControl();
    }

    /**
     * @see DConnector#isSignalLogic()
     */
    public boolean isSignalLogic()
    {
        return isSignalLogic();
    }

    /**
     * @see DConnector#isSignalSlave()
     */
    public boolean isSignalSlave()
    {
        return isSignalSlave();
    }

    /**
     * @see DConnector#getSignalName()
     */
    public String getSignalName()
    {
        return definition.getSignalName();
    }

    /**
     * @see DConnector#getConnectionTypeName()
     */
    public String getConnectionTypeName()
    {
        return definition.getConnectionTypeName();
    }

    /**
     * @see DConnector#getId()
     */
    public int getID()
    {
        return definition.getId();
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

    /**
     * Sets the user interface component for this connector.
     * 
     * @param ui the user interface component
     */
    public void setUI( Component ui )
    {
        this.ui = ui;
    }

    /**
     * Returns the user interface component of this connector.
     * 
     * @return the user interface component
     */
    public Component getUI()
    {
        return ui;
    }

    /**
     * Graph representing a connection.
     * 
     * @author Christian Schneider
     */
    private static final class Graph extends HashSet<Connector>
    {

        /**
         * The output connector of the graph. Can be <code>null</code>.
         */
        private Connector out;

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
            // default: no signal
            return out == null ? Signal.NONE : Signal.bySignalID( out
                    .getSignal() );
        }

        /**
         * Returns the output connector of the connection. The return value is
         * <code>null</code> when no output connector is part of a connection.
         * 
         * @return the output connector of the connection
         */
        public Connector getOutput()
        {
            return out;
        }

        /**
         * Sets the output connector property
         * 
         * @param out the output connector
         */
        public void setOutput( Connector out )
        {
            this.out = out;
        }

    }

    /**
     * Iterator performing a breadth first search starting from the connector
     * passed to the constructor.
     * 
     * @author Christian Schneider
     */
    private static final class BreadthFirstSearchIterator implements Iterator<Connector>
    {

        /**
         * Queue for the iteration. children are appended to the queue
         */
        private Queue<Connector> queue = new LinkedList<Connector>();

        /**
         * Creates a breadth first search iterator starting from the given
         * connector.
         * 
         * @param start iteration start
         */
        public BreadthFirstSearchIterator( Connector start )
        {
            queue.offer( start );
        }

        public boolean hasNext()
        {
            return !queue.isEmpty();
        }

        public Connector next()
        {
            if (queue.isEmpty())
            {
                throw new NoSuchElementException();
            }

            // remove head of the queue
            Connector n = queue.remove();

            // append children
            queue.addAll( n.children );

            return n;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

    }

}
