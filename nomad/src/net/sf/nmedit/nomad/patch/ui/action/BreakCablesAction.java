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
 * Created on Feb 25, 2006
 */
package net.sf.nmedit.nomad.patch.ui.action;

import java.awt.event.ActionEvent;

import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.patch.virtual.Connector;

public class BreakCablesAction extends ModuleUIAction
{

    private Connector connector;

    public BreakCablesAction( ModuleUI m, Connector c )
    {
        super( m );
        this.connector = c;
        putValue( NAME, "Break" );

        setEnabled( c.canBreak() );
    }

    public Connector getConnector()
    {
        return connector;
    }

    public void actionPerformed( ActionEvent event )
    {
        getConnector().breakCable();
    }

}
