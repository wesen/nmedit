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
 * Created on Jan 6, 2006
 */
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.nomad.image.ImageToolkit;

/**
 * @author Christian Schneider
 */
public class NomadImageView extends NomadComponent {

	private final static Image EMPTY_ICON = createEmptyIcon();
	private Image image = null;
	private Dimension imageSize = null;

	private static Image createEmptyIcon() {
		BufferedImage icon = ImageToolkit.createCompatibleBuffer(16, 16, Transparency.OPAQUE);
		Graphics2D g2 = icon.createGraphics();
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, 16, 16);
		g2.setColor(Color.BLACK);
		Font font=new Font("monospaced", Font.PLAIN, 10);
		g2.setFont(font);
		FontMetrics metrics = g2.getFontMetrics(font);
		g2.drawString("I", (16-metrics.getWidths()['I'])/2, metrics.getHeight());
		
		g2.drawRect(0, 0, 15, 15);
		
		g2.dispose();
		return icon;
	}
	
	public NomadImageView() {
		super();
		setOpaque(false);
		setImage(null);
	}

	public void setImage(Image image) {
		this.image = image==null?EMPTY_ICON:image;
		imageSize = new Dimension(this.image.getWidth(null), this.image.getHeight(null));

		//setPreferredSize(new Dimension (imageSize));
		if (image!=null)
			setOpaque(ImageToolkit.hasAlpha(image));
		else
			setOpaque(false);
	
		setOpaque(false);

		fireImageUpdateEvent();
		
		autoresize();
	}

	protected void autoresize() {
		setMinimumSize(imageSize);
		setMaximumSize(imageSize);
		setPreferredSize(imageSize);
		setSize(getPreferredSize());
	}
	
	private void fireImageUpdateEvent() {
		setOpaque(!ImageToolkit.hasAlpha(image));
		deleteOnScreenBuffer();
		repaint();
	}

	public Image getImage() {
		return image == EMPTY_ICON ? null : image;	
	}

	public void paintDecoration(Graphics2D g2) {
		g2.drawImage(image, 0, 0, imageSize.width, imageSize.height, null);
	}

}
