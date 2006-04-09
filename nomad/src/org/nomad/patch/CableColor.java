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
 * Created on Mar 1, 2006
 */
package org.nomad.patch;

import java.awt.Color;

import org.nomad.theme.NomadClassicColors;

public enum CableColor {
	
	// 0 ~red/audio, 1 ~ blue/control, 2 ~yellow/logic, 3 ~ gray/slave, 4 ~ green/user1, 5 ~ purple/user2, 6 ~ white/loose wire

	RED		(0,	NomadClassicColors.MORPH_RED),
	BLUE	(1,	NomadClassicColors.MORPH_BLUE),
	YELLOW	(2,	NomadClassicColors.MORPH_YELLOW),
	GRAY	(3,	NomadClassicColors.MORPH_GRAY),
	GREEN	(4,	Color.GREEN),
	PURPLE	(5,	Color.PINK),
	WHITE	(6,	Color.WHITE);

	private Color color ;
	public final int ColorID;

	private CableColor(int colorId, Color color) {
		this.ColorID = colorId;
		this.color   = color;
	}
	
	public Color getColor() {
		return color ;
	}
	
	public void setColor(Color color) {
		this.color = color ;
	}
	
	public static CableColor byColorId( int id )
	{
		switch ( id ) 
		{
			case 0: return RED;
			case 1: return BLUE;
			case 2: return YELLOW;
			case 3: return GRAY;
			case 4: return GREEN;
			case 5: return PURPLE;
			case 6: return WHITE;			
			default:return null ;
		}
	}

}
