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
 * Created on Jan 9, 2006
 */
package org.nomad.theme.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * @author Christian Schneider
 */
public class NomadBorderFactory {

	public static Border createNordEditor311Border() {
		return createNordEditor311LoweredBorder();
	}

	public static Border createNordEditor311RaisedBorder() {
		return new NordEditor311Border(true,1);
	}

	public static Border createNordEditor311LoweredBorder() {
		return new NordEditor311Border(false,1);
	}

	public static Border createNordEditor311RaisedBorder(int size) {
		return new NordEditor311Border(true,size);
	}

	public static Border createNordEditor311LoweredBorder(int size) {
		return new NordEditor311Border(false,size);
	}

	public static Border createNordEditor311Border(boolean raised, int size) {
		return new NordEditor311Border(raised,size);
	}
	
	private static class NordEditor311Border implements Border {

		public final static Color clLight = Color.decode("#EDEEEF");
		public final static Color clShadow = Color.decode("#929292");
		public Color clupper;
		public Color cllower;
		public int size;
		
		public NordEditor311Border(boolean raised, int size) {
			this.size=Math.max(1,size);
			if (raised) {
				clupper = clLight;
				cllower = clShadow;
			} else {
				clupper = clShadow;
				cllower = clLight;
			}
		}
		
		public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
			w--; h--;
			Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(x,y);
			g2.setStroke(new BasicStroke(size));
			
			g2.setColor(clupper);
			g2.drawLine(size,0,w-size-1,0);
			g2.drawLine(0,x+size,0,h-size-1);
			
			g2.setColor(cllower);
			g2.drawLine(w,h-size,w,size);
			g2.drawLine(w-size,h,size,h);
			
			g2.dispose();
		}

		public Insets getBorderInsets(Component component) {
			return new Insets(size,size,size,size);
		}

		public boolean isBorderOpaque() {
			return false;
		}
		
	}
	
}
