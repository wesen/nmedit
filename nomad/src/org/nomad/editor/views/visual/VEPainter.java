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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;

import org.nomad.theme.component.NomadComponent;

public class VEPainter {

	private Graphics2D g2=null;
	private final static Stroke dashStroke = new BasicStroke( 
			1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{1,2}, 0);
	
	public void setGraphics(Graphics2D g2) {
		this.g2=g2;
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	}
	
	public Graphics2D getGraphics() {
		return g2;
	}
	
	public void paintComponent(NomadComponent c) {
		Graphics gg = g2.create();
		gg.translate(c.getX(), c.getY());
		gg.setClip(0, 0, c.getWidth(), c.getHeight());
		c.paintComponent(gg);
		gg.dispose();
	}

	public void paintComponents(NomadComponent container) {
		for (int i=container.getComponentCount()-1;i>=0;i--) {
			Component c = container.getComponent(i);
			if (c instanceof NomadComponent && !c.isVisible())
				paintComponent((NomadComponent)c);
		}
	}
	
	public static void paintSelection(Graphics2D g2, Rectangle bounds, Color c) {
		Stroke stroke = g2.getStroke();
		g2.setStroke(dashStroke);
		g2.setColor(c);
		g2.draw(bounds);
		g2.setStroke(stroke);
	}
	
	public void paintSelection(Rectangle bounds, Color c) {
		paintSelection(g2, bounds, c);
	}

	public void paintSelection(NomadComponent c, Color cl) {
		paintSelection(new Rectangle(c.getX(),c.getY(),c.getWidth(),c.getHeight()), cl);
	}
	
	public void paintHotSpot(VEHotSpot spot) {
		spot.paint(g2);
	}
	
	public void paintHotSpots(VisualEditor editor) {
		VEHotSpotManager man = editor.getHotSpotManager();
		for (int i=man.getHotSpotCount()-1;i>=0;i--)
			paintHotSpot(man.get(i));
	}
	
	public void paintSelectionRect(Rectangle selection) {
		if (!selection.isEmpty()) {
			g2.setColor(new Color(0,0,255,40));
			g2.fill(selection);
			g2.setColor(Color.BLUE);
			g2.draw(selection);
		}
	}
	
}
