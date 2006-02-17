package org.nomad.theme.curve;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.nomad.util.misc.NomadUtilities;


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
 * Created on Feb 1, 2006
 */

public class CurvePainter {

	private final static float cableSize = 2.3f;
	private final static float innerSize = cableSize-0.6f;
	private BasicStroke stShadow = new BasicStroke(cableSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private BasicStroke stFill   = new BasicStroke(innerSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private final static double shadowAlpha = 0.3;
		
	public CurvePainter() {
		
	}
	
	public void paint(Graphics2D g2, Curve curve, int alpha) {
		paint(g2, curve, curve.getColor(), alpha);
	}

	public void paint(Graphics2D g2, Curve curve) {
		paint(g2, curve, curve.getColor());
	}

	public void paint(Graphics2D g2, Curve curve, boolean shadow) {
		if (shadow) 
			paint(g2, curve);
		else
			paint(g2, curve, curve.getColor(), null);
	}
	
	public void paint(Graphics2D g2, Curve curve, Color fillColor, int alpha) {
		Color clShadow = NomadUtilities.neighbour(fillColor, Color.BLACK, shadowAlpha);
		paint(g2, curve, NomadUtilities.alpha(fillColor,alpha), NomadUtilities.alpha(clShadow, alpha));
	}

	public void paint(Graphics2D g2, Curve curve, Color fillColor) {
		Color clShadow = NomadUtilities.neighbour(fillColor, Color.BLACK, shadowAlpha);
		paint(g2, curve, fillColor, clShadow);
	}

	public void paint(Graphics2D g2, Curve curve, Color fill, Color shadow) {
		Stroke restoreStroke = g2.getStroke(); // save current stroke

		if (shadow!=null) {
			g2.setStroke(stShadow);
			g2.setColor(shadow);
	
			g2.translate(+1, 1); // draw shadow with a small offset
			g2.draw(curve);
			g2.translate(-1,-1); // undo translation
		}
	
		g2.setStroke(stFill);
		g2.setColor(fill);

		g2.draw(curve);
		
		g2.setStroke(restoreStroke); // restore stroke
	}

	public void line(Graphics2D g2, Curve curve) {
		g2.setColor(curve.getColor());
		g2.drawLine(curve.getX1i(), curve.getY1i(), curve.getX2i(), curve.getY2i());
	}
	
}
