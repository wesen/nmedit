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
 * Created on Feb 14, 2006
 */
package org.nomad.synth;

import org.nomad.patch.Patch;

public class Synth {

	private Patch[] slots = new Patch[4] ;
	private int[] pids = new int[4];
	private SynthConnection connection;
	
	public Synth(SynthConnection connection) {
		this.connection = connection;
		for (int i=0;i<slots.length;i++)
			slots[i] = new Patch();
		for (int i=0;i<pids.length;i++)
			pids[i] = 0;
	}

	public void setPId(int slot, int pid) {
		pids[slot] = pid;
	}
	
	public Patch getSlot(int slot) {
		return slots[slot];
	}
	
	public void setSlot(int slot, Patch patch) {
		slots[slot] = patch;
	}
	
	public Patch getActiveSlot() {
		return slots[0];
	}
	
}
