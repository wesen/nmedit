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
 * Created on Apr 21, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;

/**
 * Event sent by a {@link net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector}.
 * 
 * @author Christian Schneider
 */
public class ConnectorEvent extends PatchEvent
{

    /**
     * The connector.
     */
    private Connector c = null;

    /**
     * Returns the connector
     * @return the connector
     */
    public Connector getConnector()
    {
        return c;
    }

    /**
     * Initialize the event object.
     * @param connector
     */
    public void connectionStateChanged( Connector connector )
    {
        setID(CONNECTOR_CONNECTED_STATE_CHANGED);
        this.c = connector;
    }
    
}
