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
 * Created on Apr 19, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteringIterator<T> implements Iterator<T>
{

    private T[] array;
    private Filter<T> filter;
    private int next;
    protected T element;

    public FilteringIterator( T[] array, Filter<T> filter )
    {
        this.array = array;
        this.filter = filter;
        this.element = null;
        this.next = -1;
        align();
    }

    private void align()
    {
        while (( ++next < array.length ) && ( !filter.include( array[next] ) ))
            ;
    }

    public boolean hasNext()
    {
        return next < array.length;
    }

    public T next()
    {
        if (next >= array.length)
        {
            throw new NoSuchElementException();
        }
        element = array[next];
        align();
        return element;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
