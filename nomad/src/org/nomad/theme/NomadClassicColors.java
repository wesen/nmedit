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
 * Created on Jan 11, 2006
 */
package org.nomad.theme;

import java.awt.Color;

public class NomadClassicColors {

	public final static Color MORPH_BLUE = Color.decode("#5A5FB3");
	public final static Color MORPH_RED = Color.decode("#CE9287");
	public final static Color MORPH_YELLOW = Color.decode("#E5DE45");
	public final static Color MORPH_GREEN = Color.decode("#9AC899");
	public final static Color MORPH_GRAY = Color.decode("#A8A8A8");
	
	public final static Color MODULE_BACKGROUND = Color.decode("#CFCFCF");
	
	public static Color alpha(Color c, int alpha) {
		
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
		
	}
	
}
