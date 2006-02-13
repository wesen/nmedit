package org.nomad.theme.curve;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;


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
	
	private final static int cap = BasicStroke.CAP_ROUND;
	private final static int join= BasicStroke.JOIN_ROUND;
	private BasicStroke stShadow = new BasicStroke(cableSize, cap, join);
	private BasicStroke stFill   = new BasicStroke(innerSize, cap, join);
	private final static int cableSize = 2;
	private final static int innerSize = cableSize-1;
	private final static double shadowAlpha = 0.2;
		
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
		Color clShadow = neighbour(fillColor, Color.BLACK, shadowAlpha);
		paint(g2, curve, alpha(fillColor,alpha), alpha(clShadow, alpha));
	}

	public void paint(Graphics2D g2, Curve curve, Color fillColor) {
		Color clShadow = neighbour(fillColor, Color.BLACK, shadowAlpha);
		paint(g2, curve, fillColor, clShadow);
	}

	public void paint(Graphics2D g2, Curve curve, Color fill, Color shadow) {
		Stroke restoreStroke = g2.getStroke(); // save current stroke

		if (shadow!=null) {
			g2.setStroke(stShadow);
			g2.setColor(shadow);
	
			g2.translate(+1,1); // draw shadow with a small offset
			g2.draw(curve);
			g2.translate(-1, -1); // undo translation
		}
	
		g2.setStroke(stFill);
		g2.setColor(fill);

		g2.draw(curve);
		
		g2.setStroke(restoreStroke); // restore stroke
	}

	private static Color alpha(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	private static Color neighbour(Color c1, Color c2, double f) {
		return new Color(
			c1.getRed() 	+ (int)((c2.getRed()	-c1.getRed()	)*f),
			c1.getGreen() 	+ (int)((c2.getGreen()	-c1.getGreen()	)*f),
			c1.getBlue() 	+ (int)((c2.getBlue()	-c1.getBlue()	)*f),
			c1.getAlpha() 	+ (int)((c2.getAlpha()	-c1.getAlpha()	)*f)
		);
	}

	public void line(Graphics2D g2, Curve curve) {
		g2.setColor(curve.getColor());
		g2.drawLine(curve.getX1i(), curve.getY1i(), curve.getX2i(), curve.getY2i());
	}
	
}
