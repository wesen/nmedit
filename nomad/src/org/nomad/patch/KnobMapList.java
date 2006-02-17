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

import java.util.Arrays;
import java.util.Iterator;

import org.nomad.util.iterate.ArrayIterator;

public class KnobMapList implements Iterable<KnobMap> {
	
	private KnobMap[] knobMapList ;

	public KnobMapList() {
		knobMapList = new KnobMap[23];
		Arrays.fill(knobMapList, null);
		for (int i=KnobMap.KNOB1;i<=KnobMap.KNOB18;i++)
			knobMapList[i] = new KnobMap(i);

		KnobMap m;
		
		m = new KnobMap(KnobMap.PEDAL);
		knobMapList[m.getIndex()] = m;
		
		m = new KnobMap(KnobMap.AFTERTOUCH);
		knobMapList[m.getIndex()] = m;
		
		m = new KnobMap(KnobMap.SWITCH);
		knobMapList[m.getIndex()] = m;
	}

	protected boolean inBounds(int knobIndex) {
		return (knobIndex>=0) && (knobIndex<knobMapList.length);
	}
	
	public KnobMap getKnobMap(int knobIndex) {
		return inBounds(knobIndex) ? knobMapList[knobIndex] : null;
	}

	public Iterator<KnobMap> iterator() {
		return new ArrayIterator<KnobMap>(knobMapList);
	}
	
}
