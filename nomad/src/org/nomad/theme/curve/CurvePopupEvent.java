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
 * Created on Feb 12, 2006
 */
package org.nomad.theme.curve;

import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import org.nomad.patch.Connector;
import org.nomad.patch.Cables;

public class CurvePopupEvent {

	private Cables transitionTable;
	private Connector connector;
	private CurvePanel component;
	private MouseEvent mouseEvent;

	public CurvePopupEvent(MouseEvent mouseEvent, CurvePanel component, Connector c)
	{
		this.mouseEvent = mouseEvent;
		this.component = component;
		this.transitionTable = component.getTransitions();
		this.connector = c;
	}

	public Cables getTransitionTable() {
		return transitionTable;
	}

	public Connector getConnector() {
		return connector;
	}

	public CurvePanel getComponent() {
		return component;
	}
	
	public MouseEvent getMouseEvent() {
		return mouseEvent;
	}
		
	public void show(JPopupMenu popup) {
		popup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	}
	
}
