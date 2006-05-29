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
package net.sf.nmedit.nomad.editor.views.visual;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class VEMouseEventHandler extends VEManager implements MouseListener, MouseMotionListener {


	public VEMouseEventHandler(VisualEditor editor) {
		super(editor);
	}

	public void mouseClicked(MouseEvent event) {
		getEditor().setSelection(null);
		getEditor().setSelectionRect(null);
	}

	private Point selStart = new Point(0,0);
	
	public void mousePressed(MouseEvent event) {
		selStart=event.getPoint();
		acquireSource();
	}

	public void mouseReleased(MouseEvent event) {
		getEditor().setSelectionRect(null);
		releaseSource();
	}

	public void mouseEntered(MouseEvent event) {
		
	}

	public void mouseExited(MouseEvent event) {
		
	}

	public void mouseDragged(MouseEvent event) {
		Point selEnd = event.getPoint();
		
		Rectangle sel = new Rectangle(
			Math.min(selStart.x, selEnd.x),
			Math.min(selStart.y, selEnd.y),
			Math.abs(selStart.x-selEnd.x),
			Math.abs(selStart.y-selEnd.y)
		);
		
		getEditor().setSelectionRect(sel);
		getEditor().rebuildSelection();
	}

	public void mouseMoved(MouseEvent event) {
		
	}

	public void acquireSource() {
		getEditor().getHotSpotManager().setBroadCastEvents(true);
	}
	
	public void releaseSource() {
		getEditor().getHotSpotManager().setBroadCastEvents(false);
	}
	
}
