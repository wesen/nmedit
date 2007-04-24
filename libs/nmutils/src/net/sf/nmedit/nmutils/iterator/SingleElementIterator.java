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
package net.sf.nmedit.nmutils.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleElementIterator<T> implements Iterator<T>
{
    
    protected T element;
    protected T next;
    protected T removable;

    public SingleElementIterator(T element)
    {
        this.next = this.element = element;
    }

    public boolean hasNext()
    {
        return next != null;
    }

    public T next()
    {
        if (!hasNext())
            throw new NoSuchElementException();
        
        removable = next;
        next = null;
        return removable;
    }

    public void remove()
    {
        if (removable == null)
            throw new IllegalStateException();
        remove(removable);
    }

    protected void remove(T removable)
    {
        throw new UnsupportedOperationException();
    }
    
}
