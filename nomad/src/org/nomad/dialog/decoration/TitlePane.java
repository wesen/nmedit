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
 * Created on Feb 14, 2006
 */
package org.nomad.dialog.decoration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.nomad.util.misc.NomadUtilities;

public class TitlePane extends DialogPane {

	private String title = "Title";
	
	public TitlePane() {
		super();
		
		setFont(new Font("SansSerif", Font.PLAIN, 18));
		setBackground(Color.WHITE);
		
		setPreferredSize(new Dimension(10, 40));
	}

	protected void paintComponent(Graphics g) {
		paintTitle((Graphics2D) g);
	}
	
	protected void paintTitle(Graphics2D g2) {
		g2.setColor(Colors.WHITE_SMOKE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(NomadUtilities.alpha(Color.DARK_GRAY, 80));
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		final int roundShadow = 4;
		

		g2.setColor(NomadUtilities.alpha(Color.DARK_GRAY, 180));
		g2.fillRect(0, 0, getWidth(), getHeight()-roundShadow);
		
		g2.setFont(getFont());
		final int fx = 10;
		final int fy =( (getHeight() + g2.getFontMetrics().getHeight()/2)/2) - roundShadow/2;

		g2.setColor(Color.WHITE);
		g2.drawString(title, fx, fy);
	}

	public void setTitle(String title) {
		this.title = title; 
		repaint();
	}
	
}
