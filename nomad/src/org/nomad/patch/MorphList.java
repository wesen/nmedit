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

public class MorphList {

	private Morph[] morphList ;
	
	public MorphList() {
		morphList =
			new Morph[] {
			new Morph(Morph.MORPH1),
			new Morph(Morph.MORPH2),
			new Morph(Morph.MORPH3),
			new Morph(Morph.MORPH4)
		};
	}

	public Morph get(int morphIndex) {
		return morphList[morphIndex];
	}

	public Morph getMorph1() { return get(Morph.MORPH1); }
	public Morph getMorph2() { return get(Morph.MORPH2); }
	public Morph getMorph3() { return get(Morph.MORPH3); }
	public Morph getMorph4() { return get(Morph.MORPH4); }


	public void setMorphValues(int morph1, int morph2, int morph3, int morph4) {
		getMorph1().setValue(morph1);
		getMorph2().setValue(morph2);
		getMorph3().setValue(morph3);
		getMorph4().setValue(morph4);
	}
	
	public void setKeyboardAssignments(int morph1, int morph2, int morph3, int morph4) {
		getMorph1().setKeyboardAssignment(morph1);
		getMorph2().setKeyboardAssignment(morph2);
		getMorph3().setKeyboardAssignment(morph3);
		getMorph4().setKeyboardAssignment(morph4);
	}
	
	public int[] getMorphValues() {
		int[] values = new int[4];
		for (int i=0;i<4;i++)
			values[i] = get(i).getValue();
		
		return values;
	}
	
	public int[] getKeyboardAssignments() {
		int[] keyboardAssignments = new int[4];
		for (int i=0;i<4;i++)
			keyboardAssignments[i] = get(i).getKeyboardAssignment();
		
		return keyboardAssignments;
	}
	
}
 