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
 * Created on Mar 15, 2006
 */
package net.sf.nmedit.nomad.theme.property;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DConnector;
import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.component.NomadConnector;


public class ConnectorValue extends Value
{
    private DConnector connector;

    protected ConnectorValue( Property property, DConnector connector, String representation )
    {
        super(property, representation);
        this.connector = connector;
    }
    
    public ConnectorValue( Property property, DConnector connector )
    {
        this(property, connector, PropertyUtilities.encodeConnector(connector));
    }

    public ConnectorValue( Property property, String representation )
    {
        this( property, PropertyUtilities.decodeConnector(representation), representation );
    }
    
    public DConnector getConnector() {
        return connector;
    }

    public void assignTo( NomadComponent component )
    {
        if (component instanceof NomadConnector)
            ((NomadConnector) component).setConnectorInfo(getConnector());
    }
    
}
