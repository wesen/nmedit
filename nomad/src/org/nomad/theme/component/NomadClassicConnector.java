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
 * Created on Jan 11, 2006
 */
package org.nomad.theme.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.RoundGradientPaint;

public class NomadClassicConnector extends NomadConnector {

	public NomadClassicConnector() {
		super();
		setConnectorType(false);
		setConnectedState(false);
		//setBackground(NomadClassicColors.MORPH_YELLOW);
	}
	
	public void enableQualityRendering(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	private class Ellipsed {
		int r, b, pad;
		double dpad;
		double dr;
		double db;
		Ellipse2D ellipse = null;
		
		public Ellipsed(double pad) {
			this.dpad=pad;
			dr=NomadClassicConnector.this.getWidth()-dpad;
			db=NomadClassicConnector.this.getHeight()-dpad;
			ellipse = new Ellipse2D.Double(dpad,dpad,dr-dpad,db-dpad);

			this.pad=(int)Math.round(pad);
			this.r=(int)Math.round(dr);
			this.b=(int)Math.round(db);
		}	
	}

	public void paintDecoration(Graphics2D g2) {
		enableQualityRendering(g2);

		Color color = getBackground();
		
		Ellipsed ell = null;
		Ellipsed frame = null;
		g2.setColor(color);

		if (isInputConnector()) {
			ell = frame = new Ellipsed(0.6);
			g2.fill(ell.ellipse);
		} else {
			g2.fillRect(0, 0, getWidth(), getHeight());
			
			ell = new Ellipsed(1);
			g2.setPaint(new GradientPaint(
				ell.pad, ell.pad, NomadClassicColors.alpha(Color.WHITE, 0),
				ell.r,ell.b, NomadClassicColors.alpha(Color.WHITE, 160)
			));
			Stroke tmp = g2.getStroke();
			g2.setStroke(new BasicStroke(3f));
			g2.draw(ell.ellipse);
			g2.setStroke(tmp);
		}

		//g2.fill(ell.ellipse);
		ell = new Ellipsed(3); // inside black hole
		g2.setPaint(new RoundGradientPaint(ell.ellipse,
			NomadClassicColors.alpha(Color.BLACK, 150),
			NomadClassicColors.alpha(Color.BLACK, 0)
		));
		g2.fill(ell.ellipse);
		

		//g2.setColor(); // outline
		if (isInputConnector()) {
			g2.setColor(NomadClassicColors.alpha(color.brighter(), 120)); // outline
			g2.draw(frame.ellipse);
		} else {
			g2.setColor(NomadClassicColors.alpha(color.darker(), 120)); // outline
			g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
		}
	}
	
	public void paintDynamicOverlay(Graphics2D g2) {

		if (isConnected()) {
			enableQualityRendering(g2);
			
			Color color = getBackground();

			Ellipsed ell = new Ellipsed(2); // black hole
			g2.setPaint(new RoundGradientPaint(ell.ellipse,
				NomadClassicColors.alpha(color.brighter(), 200),
				NomadClassicColors.alpha(color, 60)
			));
			g2.fill(ell.ellipse);
			
			
		}
		
	}
	
}
