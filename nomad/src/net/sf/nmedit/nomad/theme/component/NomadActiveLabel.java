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
 * Created on Jan 9, 2006
 */
package net.sf.nmedit.nomad.theme.component;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.theme.NomadClassicColors;
import net.sf.nmedit.nomad.theme.property.ParameterProperty;
import net.sf.nmedit.nomad.theme.property.PropertySet;


/**
 * @author Christian Schneider
 */
public class NomadActiveLabel extends NomadLabel implements EventListener<ParameterEvent>
{

    public final static int     DEFAULT_PADDING = 2;

    private final static Border defaultBorder   =
        BorderFactory.createCompoundBorder(
        NomadBorderFactory.createNordEditor311Border(),
        BorderFactory.createEmptyBorder(2,2,2,2));

    public NomadActiveLabel()
    {
        super();
        setSizePropertyEnabled( true );
        setIconSupportEnabled( false );
        setOpaque(true);
        setBackground( NomadClassicColors.TEXT_DISPLAY_BACKGROUND );
        setForeground( Color.WHITE );
        //setAutoResize( false );
        setBorder( defaultBorder );
    }
    
    protected void autoResize()
    {
        // ignore
    }

    public void registerProperties( PropertySet set )
    {
        super.registerProperties( set );
        set.add( new ParameterProperty());
    }

    public void link( Module module )
    {
        DParameter p = getParameterInfo("parameter#0");
        if (p != null) // TODO remove this
            parameter = module.getParameter( p.getContextId() );
        if (parameter != null)
        {
            parameter.addListener( this );
            updateParamText();
        }
    }

    public void unlink()
    {
        if (parameter != null)
        {
            parameter.removeListener( this );
            parameter = null;
        }
    }

    private Parameter               parameter     = null;

    private void updateParamText()
    {
        if (parameter != null)
        {
            setText( parameter.getDefinition().getFormattedValue(
                    parameter.getValue() ) );
        }
    }

    public void event( ParameterEvent event )
    {
        updateParamText();
    }

}
