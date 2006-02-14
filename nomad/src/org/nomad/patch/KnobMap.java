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
 * Created on Feb 13, 2006
 */
package org.nomad.patch;


public class KnobMap extends Assignable {

	public final static int KNOB1 		=	 0;
	public final static int KNOB2 		=	 1;
	public final static int KNOB3 		=	 2;
	public final static int KNOB4 		=	 3;
	public final static int KNOB5 		=	 4;
	public final static int KNOB6 		=	 5;
	public final static int KNOB7 		=	 6;
	public final static int KNOB8 		=	 7;
	public final static int KNOB9 		=	 8;
	public final static int KNOB10 		=	 9;
	public final static int KNOB11 		=	10;
	public final static int KNOB12 		=	11;
	public final static int KNOB13 		=	12;
	public final static int KNOB14 		=	13;
	public final static int KNOB15 		=	14;
	public final static int KNOB16 		=	15;
	public final static int KNOB17 		=	16;
	public final static int KNOB18 		=	17;
	public final static int PEDAL 		=	19;
	public final static int AFTERTOUCH 	=	20;
	public final static int SWITCH 		=	22;
	
	private int knobIndex;

	public KnobMap(int knobIndex) {
		this.knobIndex = knobIndex;
	}
	
	public int getIndex() {
		return knobIndex;
	}

}
