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
 * Created on Feb 27, 2006
 */
package org.nomad.theme;

import java.awt.Graphics2D;

import org.nomad.theme.component.NomadComponent;

public class ModuleBuilderUtilities {

	public static void renderChildBackgroundTo(Graphics2D g2, NomadComponent c) {
		int x = c.getX(); int y = c.getY();
		g2.translate(x,y);
		g2.setClip(0,0,c.getWidth(),c.getHeight());
		c.paintDecoration(g2);
		g2.setClip(null);
		g2.translate(-x,-y);
	}
	
	/*
	public static void setChildBackground(NomadComponent c, Image image) {
		c.setAlternativeBackground(image, c.getBounds());
	}*/
}
