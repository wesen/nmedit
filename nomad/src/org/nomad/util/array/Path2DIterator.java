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
package org.nomad.util.array;

import java.util.NoSuchElementException;

public class Path2DIterator {

	private int xdim;
	private int ydim;
	private int xydim;
	private int current;
	private int successor;
	private Path2D path;

	public Path2DIterator(int xdim, int ydim, Path2D path) {
		this.path = path;
		this.xdim = Math.max(0,xdim);
		this.ydim = Math.max(0,ydim);
		this.xydim= this.xdim*this.ydim;
		reset();
	}
	
	public void reset() {
		this.current  = -1;
		this.successor = xydim<=0 ? -1 : path.getStartIndex(xdim, ydim, xydim);
	}
	
	public boolean hasNext() {
		return successor>=0;
	}
	
	public Path2D getPath() {
		return path;
	}
	
	public void nextCell() {
		if (!hasNext())
			throw new NoSuchElementException();
		
		current = successor;
		successor = path.getSuccessor(current, xdim, ydim, xydim);
	}

	public boolean hasCurrent() {
		return current>=0;
	}
	
	public int getCurrentIndex() {
		return current;
	}
	
	public int getCurrentX() {
		return Array2D.getX(current, xdim);
	}
	
	public int getCurrentY() {
		return Array2D.getY(current, xdim);
	}
	
}
