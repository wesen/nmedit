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
 * Created on Jan 18, 2006
 */
package org.nomad.theme.component.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.nomad.image.ImageToolkit;
import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.RoundGradientPaint;
import org.nomad.theme.component.NomadClassicConnector;
import org.nomad.theme.component.NomadConnector;

public final class NomadClassicConnectorGraphics {

	private final static HashMap cacheMap = new HashMap();
	
	public static ConnectorCachedGraphics obtainGraphicsCache(NomadConnector connector, ConnectorCachedGraphics currentGraphics) {
		if ((currentGraphics==null)||(!currentGraphics.matches(connector.getBackground(), connector.getSize()))) {
			if (currentGraphics!=null) unregister(currentGraphics);			
			currentGraphics = searchCache(connector.getBackground(), connector.getSize());
			if (!currentGraphics.isValid) {
				currentGraphics.render();
			}
			register(currentGraphics);
		}
		return currentGraphics;
	}

	private static ConnectorCachedGraphics searchCache(Color background, Dimension size) {
		ConnectorCachedGraphics ccg = new ConnectorCachedGraphics(background, size);
		ConnectorCachedGraphics mappedCCg = (ConnectorCachedGraphics) cacheMap.get(ccg);
		return (mappedCCg==null) ? ccg : mappedCCg;
	}
	
	private static void register(ConnectorCachedGraphics ccg) {
		if (0==(ccg.referenceCount++)) {
			cacheMap.put(ccg,ccg);
		}
	}
	
	private static void unregister(ConnectorCachedGraphics ccg) {
		if ((--ccg.referenceCount)==0) {
			cacheMap.remove(ccg);
		}
	}

	public static class ConnectorCachedGraphics {
		private BufferedImage cacheInUnplugged = null;
		private BufferedImage cacheInPlugged = null;
		private BufferedImage cacheOutUnplugged = null;
		private BufferedImage cacheOutPlugged = null;
		private Color background = null;
		private Dimension size = null;
		private int hashCode = 0;
		private boolean isValid = false;
		private int referenceCount = 0; // 0 also means not cached
		
		protected ConnectorCachedGraphics(Color background, Dimension size) {
			this.background = background;
			this.size = new Dimension(size);
			// calculate hash code
			hashCode = background.getRGB()+(size.width*size.height);
		}

		public int hashCode() {
			return hashCode;			
		}
		
		protected boolean matches(Color background, Dimension size) {
			return this.background.equals(background) && this.size.equals(size);
		}
		
		public boolean equals(Object obj) {
			if (!(obj instanceof ConnectorCachedGraphics)) return false;
			ConnectorCachedGraphics c = (ConnectorCachedGraphics) obj;
			return matches(c.background, c.size);
		}
		
		public void paint(NomadClassicConnector c, Graphics2D g2) {
			if (c.isConnected()) {
				if (c.isInputConnector())
					g2.drawImage(cacheInPlugged, 0, 0, c);
				else
					g2.drawImage(cacheOutPlugged, 0, 0, c);
			} else {
				if (c.isInputConnector())
					g2.drawImage(cacheInUnplugged, 0, 0, c);
				else
					g2.drawImage(cacheOutUnplugged, 0, 0, c);
			}
		}
		
		protected void render() {
			cacheInUnplugged 	= ImageToolkit.createCompatibleBuffer(size, Transparency.TRANSLUCENT);
			cacheInPlugged 		= ImageToolkit.createCompatibleBuffer(size, Transparency.TRANSLUCENT);
			cacheOutUnplugged 	= ImageToolkit.createCompatibleBuffer(size, Transparency.OPAQUE);
			cacheOutPlugged 	= ImageToolkit.createCompatibleBuffer(size, Transparency.OPAQUE);
			render(cacheInUnplugged, true, false);
			render(cacheInPlugged, true, true);
			render(cacheOutUnplugged, false, false);
			render(cacheOutPlugged, false, true);
			
			isValid = true;
		}
		
		protected void render(BufferedImage img, boolean renderInput, boolean renderConnected) {
			Graphics2D g2 = img.createGraphics();
			paintDecoration(g2, renderInput, renderConnected);
			paintDynamicOverlay(g2, renderInput, renderConnected);
			g2.dispose();
		}

		protected void enableQualityRendering(Graphics2D g2) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		public void paintDecoration(Graphics2D g2, 
				boolean renderInput,
				boolean renderConnected) {

			enableQualityRendering(g2);

			Color color = background;
			
			Ellipsed ell = null;
			Ellipsed frame = null;
			g2.setColor(color);

			if (renderInput) {
				ell = frame = new Ellipsed(0.6,size);
				g2.fill(ell.ellipse);
			} else {
				g2.fillRect(0, 0,size.width, size.height);
				
				ell = new Ellipsed(1,size);
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
			ell = new Ellipsed(3,size); // inside black hole
			g2.setPaint(new RoundGradientPaint(ell.ellipse,
				NomadClassicColors.alpha(Color.BLACK, 150),
				NomadClassicColors.alpha(Color.BLACK, 0)
			));
			g2.fill(ell.ellipse);
			

			//g2.setColor(); // outline
			if (renderInput) {
				g2.setColor(NomadClassicColors.alpha(color.brighter(), 120)); // outline
				g2.draw(frame.ellipse);
			} else {
				g2.setColor(NomadClassicColors.alpha(color.darker(), 120)); // outline
				g2.drawRect(0, 0, size.width-1, size.height-1);
			}
		}
		
		public void paintDynamicOverlay(Graphics2D g2,
				boolean renderInput,
				boolean renderConnected) {

			if (renderConnected) {
				enableQualityRendering(g2);
				
				Color color = background;

				Ellipsed ell = new Ellipsed(2,size); // black hole
				g2.setPaint(new RoundGradientPaint(ell.ellipse,
					NomadClassicColors.alpha(color.brighter(), 200),
					NomadClassicColors.alpha(color, 60)
				));
				g2.fill(ell.ellipse);	
			}
		}
		
		public String toString() {
			return "ConnectorGraphicsCache[background="+background.getRGB()
			+",size="+size.width+","+size.height
			+",valid="+isValid
			+",references="+referenceCount
			+",hash="+hashCode()+"]";
		}

		public void dispose() {
			unregister(this);
		}
		
	}
	
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
	
}
