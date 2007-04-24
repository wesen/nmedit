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
package net.sf.nmedit.nmutils.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Subset<T> extends AbstractCollection<T>
{

    private Collection<T> superSet;
    private Filter<T> filter;
    private transient int size = 0;
    private transient int modCount;
    
    public Subset(Collection<T> superSet, Filter<T> filter)
    {
        this.superSet = superSet;
        this.filter = filter;
    }

    public boolean add(T e) 
    {
        if (!filter.contains(e))
            throw new IllegalArgumentException("filter rejects element: "+e);
        
        if (superSet.add(e))
        {
            modCount++;
            return true;
        }

        return false;
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return new FilterIterator<T>(this);
    }

    @Override
    public int size()
    {
        if (size <= 0)
            size = Counter.countIterable(this);
        return size;
    }
    
    public boolean isEmpty() 
    {
        return (size>0) ? false : !iterator().hasNext();
    }
    
    // the filter

    public static interface Filter<T>
    {
        boolean contains(T element);
    }
    
    // the filter iterator
    
    protected static class FilterIterator<T> implements Iterator<T>
    {
        
        private Subset<T> set;
        private Iterator<T> iter;
        private T element;
        private int knownMod;
        
        public FilterIterator(Subset<T> set)
        {
            this.set = set;
            this.iter = set.superSet.iterator();
            knownMod = set.modCount;
        }
        
        private T trynext()
        {
            if (element == null)
            {
                T e;
                while (iter.hasNext())
                {
                    e = iter.next();
                    if (set.filter.contains(e))
                    {
                        element = e;
                        break;
                    }
                }
            }
            return element;
        }

        public boolean hasNext()
        {
            return trynext() != null;
        }

        public T next()
        {
            T n = trynext();
            if (n == null)
                throw new NoSuchElementException();
            element = null;
            return n;
        }

        public void remove()
        {
            if (knownMod != set.modCount)
                throw new ConcurrentModificationException();
            iter.remove();
        }
        
    }
    
}
