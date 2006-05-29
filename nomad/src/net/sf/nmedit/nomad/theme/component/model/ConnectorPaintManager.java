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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.component.NomadConnector;
import net.sf.nmedit.nomad.util.computing.CacheManager;
import net.sf.nmedit.nomad.util.computing.CachedState;
import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;


public class ConnectorPaintManager extends CacheManager {
	
	private ConnectorPainter painter = new ConnectorPainter(true);
	
	public void setConnectorPainter(ConnectorPainter painter) {
		if (this.painter!=painter) {
			this.painter = painter;
			reset();
		}
	}
	
	public CachedConnectorGraphics obtainGraphics(NomadComponent comp, CachedConnectorGraphics graphics) {
		return (CachedConnectorGraphics) obtain(comp, graphics);
	}
	
	protected int computeHash(Object state) {
		Dimension d = size(state);
		return d.width*d.height*color(state).getRGB();
	}
	
	private Dimension size(Object obj) {
		if (obj instanceof CachedConnectorGraphics)
			return ((CachedConnectorGraphics) obj).size;
		else if (obj instanceof Component)
			return ((Component)obj).getSize();
		else
			return new Dimension(0,0);
	}
	private Color color(Object obj) {
		if (obj instanceof CachedConnectorGraphics)
			return ((CachedConnectorGraphics) obj).background;
		else if (obj instanceof Component)
			return ((Component)obj).getBackground();
		else
			return Color.BLACK;
	}

	public CachedState newCacheObject(Object state) {
		return new CachedConnectorGraphics(this, size(state), color(state));
	}

	public class CachedConnectorGraphics extends CachedState {
		Dimension size;
		BufferedImage image = null;
		private Color background ;
		private BufferedImage cacheInUnplugged = null;
		private BufferedImage cacheInPlugged = null;
		private BufferedImage cacheOutUnplugged = null;
		private BufferedImage cacheOutPlugged = null;
		
		public CachedConnectorGraphics(CacheManager manager, Dimension size, Color background) {
			super(manager);
			this.size = new Dimension(size);
			this.background = background;
		}

		protected void compute() {
			cacheInUnplugged 	= GraphicsToolkit.createCompatibleBuffer(size, Transparency.TRANSLUCENT);
			cacheInPlugged 		= GraphicsToolkit.createCompatibleBuffer(size, Transparency.TRANSLUCENT);
			cacheOutUnplugged 	= GraphicsToolkit.createCompatibleBuffer(size, Transparency.OPAQUE);
			cacheOutPlugged 	= GraphicsToolkit.createCompatibleBuffer(size, Transparency.OPAQUE);
			render(cacheInUnplugged, true, false);
			render(cacheInPlugged, true, true);
			render(cacheOutUnplugged, false, false);
			render(cacheOutPlugged, false, true);
		}

		public void paint(NomadConnector c, Graphics2D g2) {
			if (c.isConnected()) {
				if (c.isInputConnector())
					g2.drawImage(cacheInPlugged, 0, 0, null);
				else
					g2.drawImage(cacheOutPlugged, 0, 0, null);
			} else {
				if (c.isInputConnector())
					g2.drawImage(cacheInUnplugged, 0, 0, null);
				else
					g2.drawImage(cacheOutUnplugged, 0, 0, null);
			}
		}

		protected void render(BufferedImage img, boolean renderInput, boolean renderConnected) {
			Graphics2D g2 = img.createGraphics();
			painter.paintDecoration(g2, size, background, renderInput, renderConnected);
			painter.paintDynamicOverlay(g2, size, background, renderInput, renderConnected);
			g2.dispose();
		}

	}

}
