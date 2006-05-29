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
 * Created on Feb 23, 2006
 */
package net.sf.nmedit.nomad.main.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.resources.AppIcons;


public class AppExitAction extends NomadAction {

	public AppExitAction(Nomad nomad) {
		super(nomad);
		
		final String description = "Exit application";

		putValue(NAME, "Exit");
		putValue(SMALL_ICON, AppIcons.IC_APP_EXIT);
	    putValue(SHORT_DESCRIPTION, description);
	    putValue(LONG_DESCRIPTION, 	description);
	    //putValue(ACCELERATOR_KEY, 	KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	    putValue(MNEMONIC_KEY, 		new Integer(KeyEvent.VK_X) );    
		
	}

	public void actionPerformed(ActionEvent event) {
		getNomad().exitNomad();
	}

}
