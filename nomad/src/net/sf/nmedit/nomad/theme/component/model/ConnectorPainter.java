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
 * Created on Jan 22, 2006
 */
package net.sf.nmedit.nomad.theme.component.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

import net.sf.nmedit.nomad.util.NomadUtilities;
import net.sf.nmedit.nomad.util.graphics.RoundGradientPaint;


public class ConnectorPainter {

	private static class Ellipsed {
		int r, b, pad;
		double dpad;
		double dr;
		double db;
		Ellipse2D ellipse = null;
		
		public Ellipsed(double pad, Dimension size) {
			this.dpad=pad;
			dr=size.width-dpad;
			db=size.height-dpad;
			ellipse = new Ellipse2D.Double(dpad,dpad,dr-dpad,db-dpad);

			this.pad=(int)Math.round(pad);
			this.r=(int)Math.round(dr);
			this.b=(int)Math.round(db);
		}	
	}
	
	private boolean gradients;
	
	public ConnectorPainter(boolean gradients) {
		this.gradients = gradients;
	}

	protected void enableQualityRendering(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void paintDecoration(Graphics2D g2,
			Dimension size,
			Color color,
			boolean renderInput,
			boolean renderConnected) {

		enableQualityRendering(g2);

		Ellipsed ell = null;
		Ellipsed frame = null;
		g2.setColor(color);

		if (renderInput) {
			ell = frame = new Ellipsed(0.6,size);
			g2.fill(ell.ellipse);
		} else {
			g2.fillRect(0, 0,size.width, size.height);
			
			if (gradients) {
				ell = new Ellipsed(1,size);
				g2.setPaint(new GradientPaint(
						ell.pad, ell.pad, NomadUtilities.alpha(Color.WHITE, 0),
						ell.r,ell.b, NomadUtilities.alpha(Color.WHITE, 160)
				));
				Stroke tmp = g2.getStroke();
				g2.setStroke(new BasicStroke(3f));
				g2.draw(ell.ellipse);
				g2.setStroke(tmp);
			}
		}

		//g2.fill(ell.ellipse);
		ell = new Ellipsed(3.5,size); // inside black hole
		if (gradients) {
			g2.setPaint(new RoundGradientPaint(ell.ellipse,
				NomadUtilities.alpha(Color.BLACK, 150),
				NomadUtilities.alpha(Color.BLACK, 0)
			));
		} else {
			//g2.setColor(Color.decode("#470F0F"));
			g2.setColor(NomadUtilities.alpha(Color.BLACK, 200));
		}
		g2.fill(ell.ellipse);
		

		//g2.setColor(); // outline
		/*if (renderInput) {
			g2.setColor(NomadUtilities.alpha(color.brighter(), 120)); // outline
			g2.draw(frame.ellipse);
		} else {*/
			g2.setColor(NomadUtilities.alpha(Color.DARK_GRAY, gradients?120:200)); // outline
			
		if (renderInput)
			g2.draw(frame.ellipse);
		else
			g2.drawRect(0, 0, size.width-1, size.height-1);
		//}
	}
	
	public void paintDynamicOverlay(Graphics2D g2,
			Dimension size,
			Color color,
			boolean renderInput,
			boolean renderConnected) {

		if (renderConnected) {
			enableQualityRendering(g2);

			Ellipsed ell = new Ellipsed(2,size); // black hole
			g2.setPaint(new RoundGradientPaint(ell.ellipse,
				NomadUtilities.alpha(color.brighter(), 200),
				NomadUtilities.alpha(color, 60)
			));
			g2.fill(ell.ellipse);	
		}
	}
	
}
