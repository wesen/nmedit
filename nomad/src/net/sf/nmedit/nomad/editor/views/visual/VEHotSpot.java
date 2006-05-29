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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class VEHotSpot extends Object implements MouseMotionListener,
		MouseListener {

	public final static Rectangle EMPTY_RECT = new Rectangle(0,0,0,0); 
	private Rectangle spot = EMPTY_RECT;
	private VEHotSpotManager manager = null;
	private Color background = Color.RED;
	private boolean flagOutline = false;
	private boolean flagDrawDashes = false;
	
	public VEHotSpot(VEHotSpotManager manager) {
		this.manager = manager;
	}
	
	public VisualEditor getEditor() {
		return manager.getEditor();
	}
	
	public Color getBackground() {
		return background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public boolean drawOutline() {
		return flagOutline;
	}
	
	public void setDrawDash(boolean dash) {
		flagOutline = dash;
	}
	
	public boolean drawDash() {
		return flagDrawDashes;
	}
	
	public void setDrawOutline(boolean dash) {
		flagDrawDashes = dash;
	}
	
	public void paint(Graphics2D g2) {
		g2.setColor(background);
		if (drawOutline()) {
			if (drawDash())
				VEPainter.paintSelection(g2, getSpot(), background);
			else
				g2.draw(getSpot());
		} else
			g2.fill(getSpot());
	}
	
	public Rectangle getSpot() {
		return new Rectangle(spot);
	}

	public void setSpot(Rectangle spot) {
		this.spot = new Rectangle(spot);
	}
	
	public void setSpotLocation(Point location) {
		setSpotLocation(location.x,location.y);
	}
	
	public void setSpotLocation(int x, int y) {
		spot.x = x;
		spot.y = y;
	}
	
	public void setSpotSize(int w, int h) {
		spot.width = w;
		spot.height = h;
	}

	private void setSpotSize(Dimension size) {
		setSpotSize(size.width, size.height);
	}

	public void setSpotSize(Component c) {
		setSpotSize(c.getSize());
	}

	public void setSpot(Component c) {
		setSpotLocation(c.getLocation());
		setSpotSize(c.getSize());
	}

	public boolean contains(MouseEvent event) {
		return getSpot().contains(event.getPoint());
	}
	
	public boolean contains(Point cursor) {
		return getSpot().contains(cursor);
	}
	
	public boolean contains(int cursorx, int cursory) {
		return getSpot().contains(cursorx, cursory);
	}
	
	public void mouseDragged(MouseEvent event) { }
	public void mouseMoved(MouseEvent event) { }
	public void mouseClicked(MouseEvent event) { }
	public void mousePressed(MouseEvent event) { }
	public void mouseReleased(MouseEvent event) { }
	public void mouseEntered(MouseEvent event) { }
	public void mouseExited(MouseEvent event) { }
	
	public void release() {
		manager.release(this);
	}

	public void aquire() {
		manager.setCurrentSpot(this);
	}
	
	public String toString() {
		return getClass().getSimpleName()+"[x="+spot.x+",y="+spot.y+",w="+spot.width+",h="+spot.height+"]";
	}
	
}
