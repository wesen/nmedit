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

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import org.nomad.theme.component.NomadComponent;

public class VEComponentHotSpot extends VEHotSpot implements ContainerListener {

	private NomadComponent component = null;
	
	public VEComponentHotSpot(VEHotSpotManager manager, NomadComponent component) {
		super(manager);
		this.component = component;
		getEditor().addContainerListener(this);
		setSpot(component);
		component.setEnabled(true);
		setDrawOutline(true);
		setDrawDash(true);
	}

	public NomadComponent getComponent() {
		return component;
	}

	/* not working with invisible components
	public void componentResized(ComponentEvent event) { setSpot(getComponent()); }
	public void componentMoved(ComponentEvent event) { setSpot(getComponent()); } */

	public Rectangle getSpot() {
		return getComponent().getBounds();
	}
	
	public void componentShown(ComponentEvent event) { }
	public void componentHidden(ComponentEvent event) { }
	public void componentAdded(ContainerEvent event) { }

	public void componentRemoved(ContainerEvent event) {
		if (event.getChild()==getComponent()) {
			getEditor().removeContainerListener(this);
			getEditor().getHotSpotManager().remove(this);
		}
	}
	
	public void paint(Graphics2D g2) {
		if (getEditor().isSelected(getComponent()))
			super.paint(g2);
	}

	public void mouseClicked(MouseEvent event) {
		getEditor().setSelection(component);
	}
	

	private Point dragStart = null;
	private Point dragOffset = null;
	private Cursor lastCursor= null;

	public void mouseDragged(MouseEvent event) {
		ArrayList selection = getEditor().getSelection();
		
		if (dragOffset==null) {
			
			if (!selection.contains(getComponent()) && selection.size()>0) {
				getEditor().setSelectionRect(null);
				getEditor().rebuildSelection();
			}
				
			
			dragStart = event.getPoint();
			// first time drag
			aquire();
			Point loc = getComponent().getLocation();
			dragOffset = new Point(loc.x-dragStart.x, loc.y-dragStart.y);
			if (lastCursor==null) {
				lastCursor = getEditor().getCursor();
			}
			getEditor().setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		getComponent().setLocation(event.getX()+dragOffset.x, event.getY()+dragOffset.y);
		
		if (!selection.contains(getComponent()))
			getEditor().setSelection(getComponent());
		
		// drag complete selection
		for (int i=selection.size()-1;i>=0;i--) {
			NomadComponent c = (NomadComponent) selection.get(i);
			if (c!=getComponent()) {
				Point off = new Point(c.getX()-dragStart.x, c.getY()-dragStart.y);
				c.setLocation(event.getX()+off.x, event.getY()+off.y);
			}
		}
		dragStart = event.getPoint();
		getEditor().getPropertyEditor().updateProperties();
	}
	
	public void mouseReleased(MouseEvent event) {
		dragOffset = null;
		release();
		if (lastCursor!=null) {
			getEditor().setCursor(lastCursor);
			lastCursor = null;
		}
		//getEditor().repaint();
	}
	
	public void mouseMoved(MouseEvent event) {
		
	}
	
}
