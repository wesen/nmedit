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
package org.nomad.util.iterate;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DelegateIterator<T, S extends T> implements Iterator<S>, Iterable<S> {

	private int index = -1;
	private T[] items;
	private Class<S> clazz;
	protected S current = null;

	public DelegateIterator(Class<S> clazz, T[] items) {
		this.clazz = clazz;
		this.items = items;
		align();
	}

	private void align() {
		index++;					
								  
		while (	index<items.length && !clazz.isAssignableFrom(items[index].getClass()) )	
		{
			index++;
		}
	}
	
	public boolean hasNext() {
		return index<items.length;
	}

	public S next() {
		if (!hasNext())
			throw new NoSuchElementException();
		current = clazz.cast(items[index]);
		align();
		return current;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<S> iterator() {
		return this;
	}

}
