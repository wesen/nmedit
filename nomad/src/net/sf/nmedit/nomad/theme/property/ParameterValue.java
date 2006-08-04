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

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.theme.component.NomadComponent;


public class ParameterValue extends Value
{
    private DParameter parameter;

    protected ParameterValue( Property property, DParameter parameter,
            String representation )
    {
        super( property, representation );
        this.parameter = parameter;
    }

    public ParameterValue( Property property, DParameter parameter )
    {
        this( property, parameter, PropertyUtilities
                .encodeParameter( parameter ) );
    }

    public ParameterValue( Property property, String representation )
    {
        this( property, decodeSavely( representation ), representation );
    }
    
    private static DParameter decodeSavely(String r)
    {
        try
        {
            return PropertyUtilities.decodeParameter(r);
        } catch (DecoderException e)
        {
            // e.printStackTrace();
            return null;
        }
    }

    public DParameter getParameter()
    {
        return parameter;
    }

    public final void assignTo( NomadComponent component )
    {
        component.setParameterInfo(getProperty().getName(), getParameter());
    }

}
