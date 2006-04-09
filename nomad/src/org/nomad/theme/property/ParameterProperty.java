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
package org.nomad.theme.property;

import org.nomad.theme.component.NomadActiveLabel;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.component.NomadControl;
import org.nomad.theme.property.editor.ComboBoxEditor;
import org.nomad.theme.property.editor.Editor;
import org.nomad.xml.dom.module.DModule;

public class ParameterProperty extends Property
{
    public ParameterProperty()
    {
        this( 0 );
    }

    public ParameterProperty( int paramIndex )
    {
        super( "parameter#" + paramIndex );
    }

    @Override
    public Value decode( String value )
    {
        return new ParameterValue( this, value );
    }

    @Override
    public Value encode( NomadComponent component )
    {
        if (component instanceof NomadControl)
            return new ParameterValue( this, ( (NomadControl) component )
                    .getParameterInfo() );
        else  if (component instanceof NomadActiveLabel)
            return new ParameterValue( this, ( (NomadActiveLabel) component )
                    .getParameterInfo() );
        else
            return null;
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
            values = new Value[info.getParameterCount()];
            for (int i = 0; i < info.getParameterCount(); i++)
                values[i] = new ParameterValue( this, info.getParameter( i ) );
        }

        return new ComboBoxEditor( this, component, values );
    }

}
