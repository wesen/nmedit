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

import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.component.NomadConnector;
import net.sf.nmedit.nomad.theme.property.editor.ComboBoxEditor;
import net.sf.nmedit.nomad.theme.property.editor.Editor;
import net.sf.nmedit.nomad.xml.dom.module.DModule;


public class ConnectorProperty extends Property
{
    public ConnectorProperty()
    {
        this( 0 );
    }

    public ConnectorProperty( int paramIndex )
    {
        super( "connector#" + paramIndex );
    }

    @Override
    public Value decode( String value )
    {
        return new ConnectorValue( this, value );
    }

    @Override
    public Value encode( NomadComponent component )
    {
        if (component instanceof NomadConnector)
            return new ConnectorValue( this, ( (NomadConnector) component )
                    .getConnectorInfo() );
        else return null;
    }

    @Override
    public Editor newEditor( NomadComponent component )
    {
        Value[] values;

        DModule info = findModuleInfo( component );
        if (info == null)
            values = new Value[0];
        else
        {
            values = new Value[info.getConnectorCount()];
            for (int i = 0; i < info.getConnectorCount(); i++)
                values[i] = new ConnectorValue( this, info.getConnector( i ) );
        }

        return new ComboBoxEditor( this, component, values );
    }

}
