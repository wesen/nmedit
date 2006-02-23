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
 * Created on Jan 16, 2006
 */
package org.nomad.editor.views.visual;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import org.nomad.theme.component.NomadComponent;

public class VEComponentManager extends VEManager implements ContainerListener {

	public VEComponentManager(VisualEditor editor) {
		super(editor);
		editor.addContainerListener(this);
	}
	
	public void componentAdded(ContainerEvent event) {
		NomadComponent c = (NomadComponent) event.getChild();
		getEditor().getHotSpotManager().add(new VEComponentHotSpot(getEditor().getHotSpotManager(), c));
		c.setVisible(false);
		getEditor().repaint();
	}

	public void componentRemoved(ContainerEvent event) {
		event.getChild().setVisible(true);
		getEditor().repaint();
	}

}
