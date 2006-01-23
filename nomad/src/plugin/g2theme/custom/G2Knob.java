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
package plugin.g2theme.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Arc2D;

import org.nomad.theme.component.NomadClassicKnob;
import org.nomad.theme.component.NomadControl;
import org.nomad.theme.component.model.KnobMetrics;
import org.nomad.theme.component.model.KnobPainter;

public class G2Knob extends NomadClassicKnob {

	private final static G2KnobPainter knobPainter = new G2KnobPainter();
	
	public G2Knob() {
		setBackground(Color.WHITE);
		setKnobPainter(knobPainter); // bad, 
	}
	
	
	private static class G2KnobPainter extends KnobPainter {

		public void paintDecoration(Graphics2D g2, KnobMetrics m) {
			configureGraphics(g2);

			g2.setColor(Color.BLACK);
			// mid - left bottom corner
			g2.drawLine(Math.round((float)m.cx), Math.round((float)m.cy), m.lBar.x, m.lBar.y);
			// mid - right bottom corner
			g2.drawLine(Math.round((float)m.cx), Math.round((float)m.cy), m.rBar.x, m.rBar.y);

			// fill
			g2.setPaint(new GradientPaint(
				m.pointTL1, Color.WHITE,
				m.pointBR1, Color.LIGHT_GRAY, true
			));
			
			
			/*
			g2.setPaint(new RoundGradientPaint(
					m.ellipse, Color.WHITE, Color.LIGHT_GRAY
			));
			*/
			g2.fill(m.ellipse);
/*
			// inner outline
			g2.setPaint(new GradientPaint(
				m.pointTL1, Color.WHITE,
				m.pointBR1, Color.BLACK, true
			));
			g2.setStroke(new BasicStroke(1.5f));
			g2.draw(m.ellipseSmall);
*/
			// outer outline
			/*g2.setPaint(new GradientPaint(
				m.pointTL1, Color.LIGHT_GRAY,
				m.pointBR1, Color.BLACK, true
			));*/
			outline(m, g2);
		}
		
		public void outline(KnobMetrics m, Graphics2D g2) {
			g2.setPaint(null);
			g2.setColor(G2Outline);
			g2.setStroke(new BasicStroke(1.0f));
			g2.draw(m.ellipse);
		}
		
		private final Color G2Outline = Color.decode("#454545");
		
		public void paintDynamicOverlay(NomadControl control, Graphics2D g2, KnobMetrics m) {
			configureGraphics(g2);

			if (control.hasFocus()) {
				// we highlight the button
				g2.setColor(clHighlight);
				// outer outline
				g2.setPaint(new GradientPaint(
						m.pointTL1, Color.LIGHT_GRAY,
						m.pointBR1, Color.WHITE, true
				));
				g2.fill(m.ellipse);

				outline(m, g2);
			}
			
			Arc2D morph = m.getMorphArc(control);
			if (morph!=null) {
				// draw morph color overlay
				g2.setPaint(control.getMorphBackground());
				g2.fill(m.ellipse);

				// draw morph arc overlay
				g2.setPaint(control.getMorphForeground());
				g2.fill(morph);	
			}

			// draw marker
			Point p = m.calcPosition(control.getValuePercentage());
			g2.setColor(G2Outline);
			g2.drawLine(Math.round((float)m.cx), Math.round((float)m.cy), p.x, p.y);
		}

	}
	
	
}
