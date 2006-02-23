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

import javax.swing.JComponent;

import org.nomad.env.Environment;
import org.nomad.patch.Module;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.theme.property.ComponentLocationProperty;
import org.nomad.theme.property.ComponentSizeProperty;
import org.nomad.theme.property.PropertySet;
import org.nomad.util.graphics.ImageBuffer;
import org.nomad.util.graphics.ImageToolkit;
import org.nomad.util.iterate.ComponentIterator;

/**
 * @author Christian Schneider
 * TODO check opacity - disable where appropriate 
 * TODO property support for im- and export
 */
public class NomadComponent extends JComponent {

	private ImageBuffer alternativeBackground = new ImageBuffer();
	private boolean flagHasDynamicOverlay = false;
	private Environment env;
	private Module module = null;

	public NomadComponent() {
		env=Environment.sharedInstance();
		setOpaque(false);
		setDoubleBuffered(false);
	}

	public void setEnvironment(Environment env) {
		this.env = env;
	}
	
	public Environment getEnvironment() {
		return env;
	}

	protected void finalize() throws Throwable {
		alternativeBackground.dispose();
		super.finalize();
	}

    public void paintBorder(Graphics g) {
    	// do not paint border here
    }

    public void paintNomadBorder(Graphics2D g2) {
    	// but we still want to be able to paint the border
		super.paintBorder(g2);
    }
    
	public void registerProperties(PropertySet set) {
		set.add(new ComponentLocationProperty());
		set.add(new ComponentSizeProperty());
	}
	
	public void paintDecoration(Graphics2D g2) {
		if (isOpaque() && getBackground()!=null) {
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(),getHeight());
			paintNomadBorder(g2);
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
	}

	public void deleteAlternativeBackground() {
		alternativeBackground.dispose();
	}
	
	public NomadComponent getNomadComponent(int i) {
		return (NomadComponent) getComponent(i);
	}

	public void setAlternativeBackground(ImageBuffer background) {
		alternativeBackground.dispose();
		alternativeBackground = new ImageBuffer(background);
		setOpaque(true);
	}

	public void setAlternativeBackground(Image image, Rectangle bounds) {
		alternativeBackground.dispose();
		alternativeBackground = new ImageBuffer(image);
		alternativeBackground.setRegion(bounds);
		setOpaque(true);
	}
	
	private BufferedImage screen = null;
	private int sw = 0;
	private int sh = 0;
	
	private boolean screenInvalid = true;

	public void fullRepaint() {
		screenInvalid = true;
		repaint();
	}

	private Graphics2D getOffscreenBufferGraphics() {
		int w = getWidth(); 
		int h = getHeight();
		
		boolean resized = (sw!=w)||(sh!=h);
		
		if (screenInvalid||resized) {
			sw=w; sh=h;
			
			if (resized||screen==null) {
				screen = getGraphicsConfiguration().createCompatibleImage(sw,sh,
						alternativeBackground.isValid() ? Transparency.OPAQUE : Transparency.TRANSLUCENT);
				return screen.createGraphics();
			} else {
				// clear image
				Graphics2D g2 = screen.createGraphics();
				if (!isOpaque()) {
					ImageToolkit.clearRegion(g2, 0, 0, sw, sh);;
				}
				return g2;
			}
		} else {
			return null;
		}
	}

	private void paintContents(Graphics2D g2) {
		if (alternativeBackground.isValid()) {
			alternativeBackground.paint(g2);
		} else {
			paintDecoration(g2);
		}

		if (hasDynamicOverlay()) {
			paintDynamicOverlay(g2);
		}
	}
	
	public void paintComponent(Graphics g) {
		if (getWidth()<=0 || getHeight() <=0) return;
		
		Graphics2D g2;
		if ((g2=getOffscreenBufferGraphics())!=null) {
			paintContents(g2);
			g2.dispose();
			screenInvalid = false;
		}
		g.drawImage(screen, 0, 0, this);
	}
	
	public Iterator<NomadComponent> getExportableNomadComponents() {
		return new ComponentIterator<NomadComponent>(NomadComponent.class, this);
	}

	private String nameAlias = getClass().getName();
	
	public String getNameAlias() {
		return nameAlias;
	}

	public void setNameAlias(String alias) {
		nameAlias = alias;
	}

	public Module getModule() {
		if ((module == null) && (getParent() instanceof ModuleUI)) {
			module = ((ModuleUI)getParent()).getModule();
		}

		return module;
	}

	public void link(Module module) {
		//
	}
	
	public void unlink() {
		//
	}
	
}
