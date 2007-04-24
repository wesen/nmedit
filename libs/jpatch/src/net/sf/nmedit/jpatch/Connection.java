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
 * Created on Nov 30, 2006
 */
package net.sf.nmedit.jpatch;

/**
 * Represents a connection between two connectors.
 * 
 * @see net.sf.nmedit.jpatch.ConnectionManager
 * @author Christian Schneider
 */
public interface Connection
{

    /**
     * The source connector. This is either an input or an output connector
     * 
     * @return the source connector
     */
    Connector getSource();
    
    /**
     * The destination connector. This is always an input connector
     * 
     * @return the destination connector
     */
    Connector getDestination();
    
    /**
     * Return the source connector's owner
     * @return the source connector's owner
     */
    Module getSourceModule();
    
    /**
     * Return the destination connector's owner
     * @return the destination connector's owner
     */
    Module getDestinationModule();
    
    /**
     * Returns <code>true</code> if the specified connector is the source or destination connector of this connection
     * @param c
     * @return
     */
    boolean contains(Connector c);

    boolean contains(Module m);
    
}
