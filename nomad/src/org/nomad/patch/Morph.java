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


public class Morph {

	private int value = 0;
	private int keyboardAssignment = KeyboardAssignment.NONE;
	private int morphIndex;

	public final static int MORPH1 = 0;
	public final static int MORPH2 = 1;
	public final static int MORPH3 = 2;
	public final static int MORPH4 = 3;

	public static class KeyboardAssignment {
		public final static int NONE 		= 0;
		public final static int VELOCITY 	= 1;
		public final static int NOTE 		= 2;
		
		public static boolean isValid(int keyboardAssignment) {
			switch (keyboardAssignment) {
				case KeyboardAssignment.NONE :
				case KeyboardAssignment.VELOCITY:
				case KeyboardAssignment.NOTE : return true;
				default: return false;
			}
		}
	}

	public Morph(int morphIndex) {
		this.morphIndex = morphIndex;
	}

	public int getKeyboardAssignment() {
		return keyboardAssignment;
	}

	public void setKeyboardAssignment(int keyboardAssignment) {
		if (KeyboardAssignment.isValid(keyboardAssignment)) {
			this.keyboardAssignment = keyboardAssignment;
		}
	}

	public int getIndex() {
		return morphIndex;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
 