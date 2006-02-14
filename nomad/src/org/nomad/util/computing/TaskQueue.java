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
 * Created on Feb 9, 2006
 */
package org.nomad.util.computing;

import java.util.LinkedList;

import javax.swing.SwingUtilities;

public class TaskQueue<T> extends LinkedList<T> implements Runnable {

	private int counter = 0;
	private Callback<T> callback;

	public TaskQueue(Callback<T> callback) {
		this.callback = callback;
	}
	
	public boolean offer(T element) {
		if (contains(element)) {
			return false;
		}
		
		if (super.offer(element)) {			
			if (counter==0) invokeLater();
			return true;
		} else
			return false;
	}

	protected void invokeLater() {
		counter ++;
		SwingUtilities.invokeLater(this);
	}

	public void run() {
		T t = poll();
		if (t!=null) callback.call(t);
		counter = Math.max(0, counter-1);
		if (counter>0) invokeLater();
	}

}
