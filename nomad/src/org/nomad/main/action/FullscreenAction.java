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
package org.nomad.main.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.nomad.main.Nomad;

public class FullscreenAction extends NomadAction
{

    public FullscreenAction( Nomad nomad )
    {
        super( nomad );
        

        final String description = "Full screen mode";
        putValue(NAME, "Full screen");
        // putValue(SMALL_ICON, AppIcons.IC_DOCUMENT_OPEN);
        putValue(SHORT_DESCRIPTION, description);
        putValue(LONG_DESCRIPTION,  description);
        putValue(ACCELERATOR_KEY,   KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        putValue(MNEMONIC_KEY,      new Integer(KeyEvent.VK_F) );    
        
    }

    public void actionPerformed( ActionEvent event )
    {
        getNomad().setFullScreenModeEnabled(!getNomad().isFullScreenModeEnabled());
    }

}
