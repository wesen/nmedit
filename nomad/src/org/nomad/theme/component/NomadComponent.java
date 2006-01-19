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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.nomad.patch.Module;
import org.nomad.theme.ModuleGUI;
import org.nomad.theme.UIFactory;
import org.nomad.theme.property.ComponentLocationProperty;
import org.nomad.theme.property.ComponentSizeProperty;
import org.nomad.theme.property.PropertySet;
import org.nomad.util.graphics.ImageBuffer;
import org.nomad.util.graphics.ImageToolkit;

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
	private Module module = null;

	public NomadComponent() {
		setDoubleBuffered(false); // disable default double buffer
		setOpaque(false);
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
		// super.setBorder(null);
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
		//repaint();
	}
	
	public NomadComponent getNomadComponent(int i) {
		return (NomadComponent) getComponent(i);
	}

	public void setAlternativeBackground(ImageBuffer background) {
		alternativeBackground.dispose();
		alternativeBackground = new ImageBuffer(background);
		deleteOnScreenBuffer();
	}

	public void setAlternativeBackground(Image image, Rectangle bounds) {
		alternativeBackground.dispose();
		alternativeBackground = new ImageBuffer(image);
		alternativeBackground.setRegion(bounds);
		deleteOnScreenBuffer();
	}
	
	public void paintComponent(Graphics g) {
		if (getWidth()<=0 || getHeight() <=0)
			return;

		if (!screenBuffer.isValid() || 
				screenBuffer.getImage().getWidth(null)!=getWidth() || screenBuffer.getImage().getHeight(null)!=getHeight()) {
			//VolatileImage img = null;
			BufferedImage img = null;
			
			if (doPaintDecoration) {
				
				if (alternativeBackground.isValid()) { 
					  img = ImageToolkit.createCompatibleBuffer(this,
						ImageToolkit.hasAlpha(alternativeBackground.getImage())
						? Transparency.TRANSLUCENT
						: Transparency.OPAQUE );
	//img=createVolatileImage(getWidth(),getHeight());
					Graphics2D g2 = img.createGraphics();
					alternativeBackground.paint(g2);
					g2.dispose();
				} else {
					img = ImageToolkit.createCompatibleBuffer(this, 
							isOpaque() ? Transparency.OPAQUE : Transparency.TRANSLUCENT );
	//img=createVolatileImage(getWidth(),getHeight());
					Graphics2D g2 = img.createGraphics();
					paintDecoration(g2);
					g2.dispose();
				}
				
			} else {//img=createVolatileImage(getWidth(),getHeight());
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
	
	/*
	private class ComponentAddPolicy implements ContainerListener {
		private boolean isIncompatible(Component c) {
			return (!(c instanceof NomadComponent))
			||     (getParent() instanceof NomadComponent);
		}

		public void componentAdded(ContainerEvent event) {
			if (isIncompatible(event.getChild())) {
				remove(event.getChild()); // undo component add
				throw new IllegalStateException("Incompatible component added: "+event.getChild());
			}
		}

		public void componentRemoved(ContainerEvent event) { }
	}*/

	private String nameAlias = getClass().getName();
	
	public String getNameAlias() {
		return nameAlias;
	}

	public void setNameAlias(String alias) {
		nameAlias = alias;
	}

	public Module getModule() {
		if ((module == null) && (getParent() instanceof ModuleGUI)) {
			module = ((ModuleGUI)getParent()).getModule();
		}

		return module;
	}
	
	public void link() {
		//
	}
	
}
