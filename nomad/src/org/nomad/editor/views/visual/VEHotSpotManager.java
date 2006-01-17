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

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class VEHotSpotManager extends VEManager implements MouseListener, MouseMotionListener {

	private ArrayList hotSpotList = new ArrayList();
	private VEHotSpot currentSpot = null;
	private boolean broadCastEvents = false;
	
	public VEHotSpotManager(VisualEditor editor) {
		super(editor);
		editor.addMouseListener(this);
		editor.addMouseMotionListener(this);
	}
	
	public void setBroadCastEvents(boolean enable) {
		broadCastEvents = enable;
	}
	
	public boolean getBroadCastEvents() {
		return broadCastEvents;
	}
	
	public void add(VEHotSpot spot) {
		hotSpotList.add(spot);
	}
	
	public void remove(VEHotSpot spot) {
		hotSpotList.remove(spot);
	}
	
	public int getHotSpotCount() {
		return hotSpotList.size();
	}
	
	public VEHotSpot get(int index) {
		return (VEHotSpot) hotSpotList.get(index);
	}

	public VEHotSpot getHotSpotAt(MouseEvent event) {
		return getHotSpotAt(event.getPoint());
	}

	public VEHotSpot getHotSpotAt(Point location) {
		return getHotSpotAt(location.x, location.y);
	}

	public VEHotSpot getHotSpotAt(int x, int y) {
		for (int i=getHotSpotCount()-1;i>=0;i--) {
			VEHotSpot spot = get(i);			
			if (spot.contains(x,y)) {
				return spot;
			}
		}
		return null;
	}
	
	public void setCurrentSpot(VEHotSpot spot) {
		currentSpot=spot;
	}
	
	public void setCurrentSpotAt(MouseEvent event) {
		setCurrentSpot(getHotSpotAt(event));
	}

	public void setCurrentSpotAt(Point location) {
		setCurrentSpot(getHotSpotAt(location));
	}

	public void setCurrentSpotAt(int x, int y) {
		setCurrentSpot(getHotSpotAt(x,y));
	}

	public void mouseClicked(MouseEvent event) {
		if (broadCastEvents) {
			setCurrentSpot(null);
			getEditor().getMouseEventHandler().mouseClicked(event);
			return;
		}
		if (currentSpot!=null) currentSpot.mouseClicked(event);
		else {
			VEHotSpot spot = getHotSpotAt(event);
			
			if (spot!=null) spot.mouseClicked(event);
			else getEditor().getMouseEventHandler().mouseClicked(event);
		}
	}

	public void mousePressed(MouseEvent event) {
		if (broadCastEvents) {
			setCurrentSpot(null);
			getEditor().getMouseEventHandler().mousePressed(event);
			return;
		}
		if (currentSpot!=null) currentSpot.mousePressed(event);
		else {
			VEHotSpot spot = getHotSpotAt(event);
			if (spot!=null) spot.mousePressed(event);
			else getEditor().getMouseEventHandler().mousePressed(event);
		}
	}

	public void mouseReleased(MouseEvent event) {
		if (broadCastEvents) {
			setCurrentSpot(null);
			getEditor().getMouseEventHandler().mouseReleased(event);
			return;
		}
		if (currentSpot!=null) currentSpot.mouseReleased(event);
		else {
			VEHotSpot spot = getHotSpotAt(event);
			if (spot!=null) spot.mouseReleased(event);
			else getEditor().getMouseEventHandler().mouseReleased(event);
		}
	}

	public void mouseEntered(MouseEvent event) {
		if (broadCastEvents) {
			setCurrentSpot(null);
			getEditor().getMouseEventHandler().mouseEntered(event);
			return;
		}
		if (currentSpot!=null) currentSpot.mouseEntered(event);
		else {
			VEHotSpot spot = getHotSpotAt(event);
			if (spot!=null) spot.mouseEntered(event);
			else getEditor().getMouseEventHandler().mouseEntered(event);
		}
	}

	public void mouseExited(MouseEvent event) {
		if (broadCastEvents) {
			setCurrentSpot(null);
			getEditor().getMouseEventHandler().mouseExited(event);
			return;
		}
		if (currentSpot!=null) currentSpot.mouseExited(event);
		else {
			VEHotSpot spot = getHotSpotAt(event);
			if (spot!=null) spot.mouseExited(event);
			else getEditor().getMouseEventHandler().mouseExited(event);
		}
	}

	public void mouseDragged(MouseEvent event) {
		if (broadCastEvents) {
			setCurrentSpot(null);
			getEditor().getMouseEventHandler().mouseDragged(event);
			return;
		}
		if (currentSpot!=null) currentSpot.mouseDragged(event);
		else {
			VEHotSpot spot = getHotSpotAt(event);
			if (spot!=null) spot.mouseDragged(event);
			else getEditor().getMouseEventHandler().mouseDragged(event);
		}
	}

	public void mouseMoved(MouseEvent event) {
		if (broadCastEvents) {
			setCurrentSpot(null);
			getEditor().getMouseEventHandler().mouseMoved(event);
			return;
		}
		if (currentSpot!=null) currentSpot.mouseMoved(event);
		else {
			VEHotSpot spot = getHotSpotAt(event);
			if (spot!=null) spot.mouseMoved(event);
			else getEditor().getMouseEventHandler().mouseMoved(event);
		}
	}

	public void release(VEHotSpot spot) {
		if (spot==currentSpot)
			setCurrentSpot(null);
	}

	public String toString() {
		String str = getClass().getSimpleName()+"[";
		for (int i=getHotSpotCount()-1;i>=0;i--)
			str+=get(i);
		str+="]";
		return str;
	}
	
}
