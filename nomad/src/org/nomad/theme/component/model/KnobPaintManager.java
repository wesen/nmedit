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
package org.nomad.theme.component.model;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.component.NomadControl;
import org.nomad.util.computing.CacheManager;
import org.nomad.util.computing.CachedState;
import org.nomad.util.graphics.ImageToolkit;

public class KnobPaintManager extends CacheManager {
	
	private KnobPainter painter = new KnobPainter();
	
	public void setKnobPainter(KnobPainter painter) {
		if (this.painter!=painter) {
			this.painter = painter;
			reset();
		}
	}
	
	public CachedKnobGraphics obtainGraphics(NomadComponent comp, CachedKnobGraphics graphics) {
		return (CachedKnobGraphics) obtain(comp, graphics);
	}
	
	protected int computeHash(Object state) {
		Dimension d = size(state);
		return d.width*d.height;
	}
	
	private Dimension size(Object obj) {
		if (obj instanceof CachedKnobGraphics)
			return ((CachedKnobGraphics) obj).size;
		else if (obj instanceof Component)
			return ((Component)obj).getSize();
		else
			return new Dimension(0,0);
	}

	public CachedState newCacheObject(Object state) {
		return new CachedKnobGraphics(this, size(state));
	}

	public class CachedKnobGraphics extends CachedState {
		Dimension size;
		BufferedImage image = null;
		KnobMetrics metrics;
		
		public CachedKnobGraphics(CacheManager manager, Dimension size) {
			super(manager);
			this.size = new Dimension(size);
			metrics = new KnobMetrics(this.size);
		}

		protected void compute() {
			image = ImageToolkit.createCompatibleBuffer(size, Transparency.TRANSLUCENT);
			Graphics2D g2 = image.createGraphics();
			painter.paintDecoration(g2, metrics);
			g2.dispose();
		}
		
		public KnobMetrics getMetrics() {
			return metrics;
		}
		
		public void paintDecoration(Graphics2D g2) {
			g2.drawImage(image, 0, 0, null);
		}
		
		public void paintDynamicOverlay(NomadControl control, Graphics2D g2) {
			painter.paintDynamicOverlay(control, g2, metrics);
		}
		
	}

}
