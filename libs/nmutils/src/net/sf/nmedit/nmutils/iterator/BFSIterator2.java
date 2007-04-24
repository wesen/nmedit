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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public abstract class BFSIterator2<E> implements Iterator<E>
{

    private LinkedList<E> queue;
    private LinkedList<E> tempQueue;
    private E removable;
    private E start;
    
    public BFSIterator2(E start)
    {
        this.start = start;
        queue = new LinkedList<E>();
        tempQueue = new LinkedList<E>();
        queue.offer(start);
    }
    
    protected E start()
    {
        return start;
    }
    
    public boolean hasNext()
    {
        return !(queue.isEmpty() || tempQueue.isEmpty());
    }
    
    protected void checkModification() throws ConcurrentModificationException
    {
        // custom implementation
    }

    public E next()
    {
        checkModification();
        if (!hasNext())
            throw new NoSuchElementException();

        if (queue.isEmpty() && (!tempQueue.isEmpty()))
        {
            LinkedList<E> swap = queue;
            queue = tempQueue;
            tempQueue = swap;
        }
        
        E next = queue.remove();
        removable = next;
        
        enqueueChildren(tempQueue, next);
        
        return next;
    }

    protected abstract void enqueueChildren(Queue<E> queue, E parent);

    public void remove()
    {
        checkModification();
        if (removable == null)
            throw new IllegalStateException();
        remove(removable);
        tempQueue.clear();
    }

    protected void remove(E removable)
    {
        throw new UnsupportedOperationException();
    }

}
