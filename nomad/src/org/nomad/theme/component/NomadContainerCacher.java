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
 * Created on Jan 19, 2006
 */
package org.nomad.theme.component;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.image.BufferedImage;

import org.nomad.util.graphics.ImageBuffer;
import org.nomad.util.graphics.ImageToolkit;
import org.nomad.util.graphics.PersistenceManager;

public class NomadContainerCacher {
	
	private NomadContainerCacher() {}
	
	public static void enable() {
		//instance = new NomadContainerCacher();
	}

	public static void disable() {
		//instance = null;
	}

	public static void enableHook(NomadComponent component, PersistenceManager manager, Object sharedKey) {
		instance.listen(component, manager, sharedKey);
	}

	public static void enableHook(NomadComponent component, ImageBuffer ib) {
		instance.listen(component, ib);
	}

	public static void closeHook() {
		instance.finish();
	}

	private static NomadContainerCacher instance = new NomadContainerCacher();
	private static RenderingListener rel = new RenderingListener();
	private static BroadcastingListener brl = new BroadcastingListener();
	
	private NomadComponent component = null;
	private BufferedImage image = null;
	private Graphics2D g2 = null;
	private ImageBuffer ib = null;
	private ContainerListener cel = null;

	public void listen(NomadComponent component, ImageBuffer ib) {
		this.component=component;
		component.addContainerListener(cel = brl);
		
		this.ib = new ImageBuffer(ib);
		brl.configure(this.ib);
		component.setAlternativeBackground(ib);
	}

	public void listen(NomadComponent component, PersistenceManager manager, Object sharedKey) {
		this.component=component;
		component.addContainerListener(cel = rel);

		image = ImageToolkit.createCompatibleBuffer(component, Transparency.OPAQUE); //Transparency.TRANSLUCENT);
		ib = new ImageBuffer(manager, sharedKey, image);

		g2 = image.createGraphics();
		rel.configure(g2, ib);
		component.paintDecoration(g2);
		component.setAlternativeBackground(ib);
	}
	
	public void finish() {
		component.removeContainerListener(cel);
		cel=null;
		ib.dispose();

		if (g2!=null) {
			rel.configure(null, null);
			g2.dispose();
			image = null;
		} else {
			brl.configure(null);
		}
	}

	private static class RenderingListener extends ContainerAdapter { 

		private Graphics2D g2 = null;
		private ImageBuffer ib= null;
		
		public void configure(Graphics2D g2, ImageBuffer ib) {
			this.g2 = g2;
			this.ib = ib;
		}
		
		public void componentAdded(ContainerEvent event) {
			if (event.getChild() instanceof NomadComponent) {
				NomadComponent c = (NomadComponent) event.getChild();
				int x = c.getX(); int y = c.getY();
				g2.translate(x,y);
				g2.setClip(0,0,c.getWidth(),c.getHeight());
				c.paintDecoration(g2);
				g2.setClip(null);
				g2.translate(-x,-y);
				if (!c.hasDynamicOverlay()) {
					event.getContainer().remove(c); // remove child
				} else {
					c.setAlternativeBackground(ib.getImage(), c.getBounds());
				}
			}
		}

	}

	private static class BroadcastingListener extends ContainerAdapter { 

		private ImageBuffer ib= null;
		
		public void configure(ImageBuffer ib) {
			this.ib = ib;
		}

		public void componentAdded(ContainerEvent event) {
			if (event.getChild() instanceof NomadComponent) {
				NomadComponent c = (NomadComponent) event.getChild();
				c.setAlternativeBackground(ib.getImage(), c.getBounds());
			}
		}

	}

}
