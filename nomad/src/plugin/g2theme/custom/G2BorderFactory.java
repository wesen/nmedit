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
package plugin.g2theme.custom;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;


public class G2BorderFactory {

	private final static G2ModulePaneBorder
		modulePaneBorder = new G2ModulePaneBorder();

	private final static G2ButtonBorder
		buttonBorder = new G2ButtonBorder();
	
	public static Border createG2ModulePaneBorder() {
		return G2BorderFactory.modulePaneBorder;
	}

	public static Border createG2ButtonBorder() {
		return buttonBorder;
	}

	private static class G2ModulePaneBorder implements Border {

		public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
			int b = y+h-1;
			int r = x+w-1;
			g.setColor(G2ColorConstants.BORDER_LIGHT);
			g.drawLine(x, y, r, y);
			g.drawLine(x, y, x, b);
			g.setColor(G2ColorConstants.BORDER_DARK);
			g.drawLine(x, b, r, b);
			g.drawLine(r, y, r, b);
		}

		public Insets getBorderInsets(Component comp) {
			return new Insets(1,1,1,1);
		}

		public boolean isBorderOpaque() {
			return true;
		}
	}
	

	private static class G2ButtonBorder implements Border {

		public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
			int b = y+h-1;
			int r = x+w-1;
			if ((comp instanceof G2ButtonArray) && ((G2ButtonArray)comp).isMouseOver()) {
				g.setColor(G2ColorConstants.BORDER_LIGHT);
			} else {
				g.setColor(G2ColorConstants.BORDER_DARK);
			}
			g.drawLine(x, y, r, y);
			g.drawLine(x, y, x, b);
			g.setColor(G2ColorConstants.BORDER_DARK);
			g.drawLine(x, b, r, b);
			g.drawLine(r, y, r, b);
		}

		public Insets getBorderInsets(Component comp) {
			return new Insets(1,1,1,1);
		}

		public boolean isBorderOpaque() {
			return true;
		}
	}
	
	
}
