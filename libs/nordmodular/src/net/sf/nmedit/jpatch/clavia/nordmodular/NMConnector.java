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

import net.sf.nmedit.jpatch.AbstractConnector;
import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.ConnectorDescriptor;

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
     * Returns the output connector of the connection this connector is part of.
     * If no output connector is part of the connection <code>null</code> is returned.
     * 
     * @return output connector of the connection
     */
    public NMConnector getOutput()
    {
        return getSource();
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
        return (NMConnector) super.getRoot();
    }

    /**
     * Removes the connector from the connection it is part of. Thus the
     * transitions to it's parent and children are removed. <img
     * src="doc-files/cable-op-disconnect.png" width="528" height="223" alt="An
     * illustration of the disconnect operation." />
     * 
     * @deprecated
     */
    public void disconnectCables()
    {
        disconnect();
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
        

        // TODO signal
        return Signal.NONE;
        
        //return graph.getColor();
    }
    
    public void setConnectionColor(Signal s)
    {
        // TODO signal
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
        return (NMConnector) super.getSource();
    }
    
    public String toString()
    {
        ConnectionManager cm = getConnectionManager();
        
        return 
        "Connector["
        +getOwner()
        +",name="+getDescriptor().getComponentName()
        +",index="+getDescriptor().getIndex()
        +",output="+getDescriptor().isOutput()
        +",children="+ (cm == null ? "0" : cm.getChildCount(this))
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
