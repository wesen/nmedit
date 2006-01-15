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
 * Created on Jan 4, 2006
 */
package org.nomad.theme.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.nomad.image.ImageBuffer;
import org.nomad.image.ImageToolkit;
import org.nomad.theme.UIFactory;
import org.nomad.theme.property.ComponentLocationProperty;
import org.nomad.theme.property.ComponentSizeProperty;
import org.nomad.theme.property.PropertySet;

/**
 * @author Christian Schneider
 * TODO check opacity - disable where appropriate 
 * TODO property support for im- and export
 */
public class NomadComponent extends JComponent {

	private ImageBuffer screenBuffer = new ImageBuffer(); // initially invalid, never null 
	private ImageBuffer alternativeBackground = new ImageBuffer();
	private PropertySet accessibleProperties = new PropertySet();
	private boolean flagHasDynamicOverlay = false;
	private boolean doPaintDecoration = true;
	private UIFactory uiFactory = null;

	public NomadComponent() {
		setDoubleBuffered(false); // disable default double buffer
		setOpaque(false);
		setBorder(null); // no border
		addContainerListener(new ComponentAddPolicy()); // validate child components
		addComponentListener(new ComponentUpdater());
		setPreferredSize(new Dimension(10, 10));
		setSize(getPreferredSize());
		
		addComponentListener(new ComponentAdapter(){

			public void componentResized(ComponentEvent event) {
				deleteOnScreenBuffer();
			}});
/*
		accessibleProperties.addPropertySetListener(new PropertySetListener(){
			public void propertySetEvent(PropertySetEvent event) {
				screenBuffer.dispose();
				repaint();
			}});*/
		accessibleProperties.add(new ComponentLocationProperty(this));
		accessibleProperties.add(new ComponentSizeProperty(this));
	}
	
	public void setEnvironment(UIFactory factory) {
		this.uiFactory = factory;
	}
	
	public UIFactory getEnvironment() {
		return uiFactory;
	}
	
	public void setBorder(Border border) {
		super.setBorder(null);
	}
	
	protected void finalize() throws Throwable {
		screenBuffer.dispose();
		alternativeBackground.dispose();
		super.finalize();
	}
	
	public PropertySet getAccessibleProperties() {
		return accessibleProperties;
	}

	public void paintDecoration(Graphics2D g2) {
		if (isOpaque() && getBackground()!=null) {
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(),getHeight());
		}
	}

	public void paintDynamicOverlay(Graphics2D g2) {
		// no dynamic painting here
	}
	
	public boolean hasDynamicOverlay() {
		return flagHasDynamicOverlay; // default
	}
	
	public void setDynamicOverlay(boolean enable) {
		this.flagHasDynamicOverlay = enable;
		//screenBuffer.dispose();
		//repaint();
	}

	public void deleteAlternativeBackground() {
		alternativeBackground.dispose();
		repaint();
	}
	
	public NomadComponent getNomadComponent(int i) {
		return (NomadComponent) getComponent(i);
	}

	public BufferedImage renderBackground() {
		BufferedImage img = ImageToolkit.createCompatibleBuffer(this, Transparency.OPAQUE);
		{	// for this component
			Graphics2D g2 = img.createGraphics();
			paintDecoration(g2);
			g2.dispose();
		}

		for (int i=getComponentCount()-1;i>=0;i--) {
			NomadComponent c = getNomadComponent(i);
			
			Graphics2D g2 = img.createGraphics();
			g2.translate(c.getX(), c.getY());
			g2.setClip(0,0,c.getWidth(),c.getHeight());
			c.paintDecoration(g2);
			g2.dispose();
		}

		return img;
	}
	
	public void removeDecoration() {
		for (int i=getComponentCount()-1;i>=0;i--) {
			if (!getNomadComponent(i).hasDynamicOverlay())
				remove(i);
		}
		deleteOnScreenBuffer();
		//repaint();
	}

	public void broadcastBackground(ImageBuffer background) {
		boolean setOpacity = !ImageToolkit.hasAlpha(background.getImage());
		setAlternativeBackground(background, true);
		setOpaque(setOpacity);
		
/*
		for (int i=getComponentCount()-1;i>=0;i--) {
			NomadComponent c = getNomadComponent(i);
			//c.setAlternativeBackground(background, c.getLocation(), c.getSize(), false);
			c.setOpaque(false);
			c.doPaintDecoration = false;
//			c.repaint();
		}
		*/
		
		for (int i=getComponentCount()-1;i>=0;i--) {
			NomadComponent c = getNomadComponent(i);
			c.setAlternativeBackground(background, c.getLocation(), c.getSize(), false);
			c.setOpaque(setOpacity);
//			c.repaint();
		}

		//deleteOnScreenBuffer();
		//repaint();
	}

	public void setAlternativeBackground(ImageBuffer background, boolean shared) {
		alternativeBackground.dispose();
		if (shared)
			alternativeBackground = new ImageBuffer(background);
		else {
			alternativeBackground = new ImageBuffer(background.getImage());
			alternativeBackground.setRegion(background.getRegion());
		}
		deleteOnScreenBuffer();
	}
	
	public void setAlternativeBackground(ImageBuffer background, int x, int y, int w, int h, boolean shared) {
		setAlternativeBackground(background, shared);
		alternativeBackground.setRegion(new Rectangle(x, y, w, h));
	}
	
	public void setAlternativeBackground(ImageBuffer background, Point offset, Dimension size, boolean shared) {
		setAlternativeBackground(background, offset.x, offset.y, size.width, size.height, shared);
	}

	public void paintComponent(Graphics g) {
		if (!screenBuffer.isValid() || 
				screenBuffer.getImage().getWidth(null)!=getWidth() || screenBuffer.getImage().getHeight(null)!=getHeight()) {
			BufferedImage img = null;
			
			if (doPaintDecoration) {
				
				if (alternativeBackground.isValid()) { 
					img = ImageToolkit.createCompatibleBuffer(this,
						ImageToolkit.hasAlpha(alternativeBackground.getImage())
						? Transparency.TRANSLUCENT
						: Transparency.OPAQUE );
	
					Graphics2D g2 = img.createGraphics();
					alternativeBackground.paint(g2);
					g2.dispose();
				} else {
					img = ImageToolkit.createCompatibleBuffer(this, 
							isOpaque() ? Transparency.OPAQUE : Transparency.TRANSLUCENT );
	
					Graphics2D g2 = img.createGraphics();
					paintDecoration(g2);
					g2.dispose();
				}
				
			} else {
				img = ImageToolkit.createCompatibleBuffer(this, Transparency.TRANSLUCENT );
			}

			if (hasDynamicOverlay()) {
				Graphics2D g2 = img.createGraphics();
				paintDynamicOverlay(g2);
				g2.dispose();	
			}

			screenBuffer = new ImageBuffer(img); // create new buffer
		} // end invalid buffer

		// flip buffer to screen
		screenBuffer.paint(g);
	}
	
	public void deleteOnScreenBuffer() {
		screenBuffer.dispose();
	}
	
	public Iterator getExportableNomadComponents() {
		return new Iterator() {
			
			int index = 0;

			public boolean hasNext() {
				return index<getComponentCount();
			}

			public Object next() {
				if (!hasNext()) throw new NoSuchElementException();
				return getComponent(index++);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}};
	}
	
	/*public void revalidate() {
		deleteOnScreenBuffer();
		setSize(getPreferredSize());
		super.revalidate();
	}*/
	
	private class ComponentAddPolicy implements ContainerListener {
		private boolean isIncompatible(Component c) {
			return (!(c instanceof NomadComponent))||(getParent() instanceof NomadComponent);
		}

		public void componentAdded(ContainerEvent event) {
			if (isIncompatible(event.getChild())) {
				remove(event.getChild()); // undo component add
				throw new IllegalStateException("Incompatible component added: "+event.getChild());
			}
		}

		public void componentRemoved(ContainerEvent event) { }
	}
	
	private class ComponentUpdater implements ComponentListener {

		public void componentResized(ComponentEvent event) { 
			deleteOnScreenBuffer(); // update buffer
		}
		public void componentHidden(ComponentEvent event) {
			deleteOnScreenBuffer(); // free unused memory
		}
		public void componentMoved(ComponentEvent event) { }
		public void componentShown(ComponentEvent event) { }
	}

	private String nameAlias = getClass().getName();
	
	public String getNameAlias() {
		return nameAlias;
	}
	
	public void setNameAlias(String alias) {
		nameAlias = alias;
	}
	
}
