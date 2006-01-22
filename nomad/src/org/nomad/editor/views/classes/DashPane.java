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
 * Created on Jan 20, 2006
 */
package org.nomad.editor.views.classes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class DashPane extends JPanel {
	/**
	 * 
	 */
	private final Component item;
	public DashPane(Component item) {
		this.item = item;
		setOpaque(false);
		setBackground(Color.GRAY);
		setSize(10,10);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(this.item.getBackground());
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{ 1, 2 }, 0 ));
		g2.drawLine(0, this.item.getHeight()-1, this.item.getWidth(), this.item.getHeight()-1); // draw at the bottom
		g2.dispose();
	}
	public Dimension getPreferredSize() {
		if (this.item.getParent()!=null)
			return new Dimension(this.item.getParent().getWidth(), ComponentClassListItem.PADDING);
		else
			return new Dimension(100, ComponentClassListItem.PADDING);
	}
}