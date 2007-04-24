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
 */package net.sf.nmedit.nmutils.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterator<T>
{
    
    protected T[] array;
    
    private int start;
    private int removeIndex;

    private int end;
    

    public ArrayIterator(T[] array)
    {
        this(array, 0, array.length);
    }

    public ArrayIterator(T[] array, int start, int end)
    {
        this.start = start;
        this.end = end;
        this.array = array;
        this.removeIndex = -1;
    }

    public boolean hasNext()
    {
        return start<end;
    }
    
    public T next()
    {
        if (!hasNext())
            throw new NoSuchElementException();

        removeIndex = start++;
        return array[removeIndex];
    }

    public void remove()
    {
        if (removeIndex < 0)
            throw new IllegalStateException();
        
        int tmpIndex = removeIndex;
        removeIndex = -1;
        remove(tmpIndex);
    }

    protected void remove(int index)
    {
        throw new UnsupportedOperationException();
    }
    
}
