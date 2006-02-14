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

import java.util.Iterator;

/**
 * Iterates through an array following the given path.
 * 
 * @author Christian Schneider
 */
public class CellIterator<T> extends Path2DIterator implements Iterator<T> {

	private T[] array2d;

	public CellIterator(Array2D<T> array2d, Path2DIterator path) {
		this(array2d.getArray(), array2d.getWidth(), array2d.getHeight(), path.getPath());
	}

	public CellIterator(Array2D<T> array2d, Path2D path) {
		this(array2d.getArray(), array2d.getWidth(), array2d.getHeight(), path);
	}

	public CellIterator(T[] array2d, int xdim, int ydim, Path2D path) {
		super(xdim, ydim, path);
		this.array2d = array2d;
	}
	
	public T next() {
		nextCell();
		return current();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public void setCurrent(T value) {
		array2d[getCurrentIndex()] = value;
	}
	
	public T current() {
		return array2d[getCurrentIndex()];
	}
	
}
