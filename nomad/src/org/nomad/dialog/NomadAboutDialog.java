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
 * Created on Feb 14, 2006
 */
package org.nomad.dialog;

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JLabel;

public class NomadAboutDialog extends NomadDialog {

	public NomadAboutDialog() {
		setTitle("About");
		setScrollbarEnabled(true);
		setLayout(new GridLayout(0, 2));
		add(new JLabel("homepage:"));
		add(new JLabel("http://nmedit.sourceforge.net"));
		
		add(new JLabel("developers:"));
		add(new JLabel("Ian Hoogeboom"));
		add(Box.createGlue());
		add(new JLabel("Marcus Anderson"));
		add(Box.createGlue());
		add(new JLabel("Christian Schneider"));
		add(Box.createGlue());
		add(new JLabel("???"));
	}
	
	public void invoke() {
		super.invoke(new String[]{":Close"});
	}
	
}
