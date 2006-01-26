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

import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;

import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.PropertySetEvent;
import org.nomad.theme.property.PropertySetListener;

public class VEPropertySetChangeHandler extends ContainerAdapter implements PropertySetListener {

	private VisualEditor editor = null;
	
	public VEPropertySetChangeHandler(VisualEditor editor) {
		this.editor = editor;
	}

	public VisualEditor getEditor() {
		return editor;
	}
	
	public void propertySetEvent(PropertySetEvent event) {
		editor.repaint();
	}

	public void componentAdded(ComponentEvent event) {
		if (event.getSource() instanceof NomadComponent) {
			((NomadComponent)event.getSource()).createAccessibleProperties(true);
		}
	}

	public void componentRemoved(ComponentEvent event) {
		// editor.repaint();
	}

}
