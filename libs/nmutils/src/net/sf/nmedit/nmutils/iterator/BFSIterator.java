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
 * Created on Sep 5, 2006
 */
package net.sf.nmedit.nmutils.iterator;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * An iterator that implements a breadth-first search on a tree.
 * 
 * @author Christian Schneider
 */
public abstract class BFSIterator<E> implements Iterator<E>
{
    
    // bfs node buffer
    private Queue<E> queue;
    // the element returned by the latest next() call
    private E current = null;
    
    /**
     * Creates a breadth-first search iterator starting from the specified element
     * and using a {@link LinkedList} as queue.
     * If the start element is <code>null</code> the iteration contains no elements.
     * 
     * @param start <code>null</code> or the first element of the iteration
     */
    public BFSIterator(E start)
    {
        this(new LinkedList<E>(), start);
    }

    /**
     * Creates a breadth-first search iterator starting from the specified element
     * using the provided {@link Queue} for buffering nodes.
     * If the start element is <code>null</code> the iteration contains no elements.
     * 
     * @param start <code>null</code> or the first element of the iteration
     * 
     * @throws NullPointerException when the queue is <code>null</code>
     * @throws IllegalArgumentException when the queue is not empty
     */
    public BFSIterator(Queue<E> queue, E start)
    {
        this.queue = queue;
        if (!queue.isEmpty())
            throw new IllegalArgumentException("queue <"+queue+"> not empty");
        if (start!=null) 
            queue.offer(start);
    }

    /**
     * @see Iterator#hasNext()
     */
    public final boolean hasNext()
    {
        // when the queue is empty the iteration is complete
        return !queue.isEmpty();
    }

    /**
     * @see Iterator#next()
     */
    public final E next()
    {
        // check for modifications according to a custom policy
        checkMod();
        // remove the head of the queue which is the next element in the iteration
        // we do not check hasNext() since {@link Queue#remove()} throws the NoSuchElementException for us.
        // the element is stored in the 'current' variable which can be used for removing it using Iterator#remove()
        current = queue.remove();
        // for BFS search we now have to enqueue the nodes children 
        enqueueChildren(queue, current);
        // return next element in the iteration
        return current;
    }
    
    /**
     * Returns the current element of the iteration.
     * 
     * @return the current element of the iteration (never returns null)
     * @see #remove()
     * @throws IllegalStateException when {@link #next()} was not called or the element has already been removed
     */
    protected final E getRemoveElement()
    {
        checkMod();
        E e = current;
        if (e==null)
            throw new IllegalStateException();
        current = null;
        return e;
    }

    /**
     * throws an {@link UnsupportedOperationException}. An implementing of this operation
     * can use {@link #getRemoveElement()} to get the latest element in the iteration
     * that will be removed.
     * 
     * @see Iterator#remove()
     * @throws UnsupportedOperationException
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Custom implementation of the 'check for modification' policy.
     * This operation is called by {@link #next()} and {@link #remove()}
     * (if implemented) to check for modifications on the underlying collection that
     * interfere with the iteration. When such modifications are detected
     * it throws a {@link ConcurrentModificationException}. 
     * 
     * @throws ConcurrentModificationException modifications are detected that interfere with the iteration
     */
    protected void checkMod() throws ConcurrentModificationException
    { }

    /**
     * Enqueues the children of the specified element in the specified queue if it has some. 
     * 
     * @param queue where the children are enqueued
     * @param parent parent of the enqueued children
     * @throws NullPointerException when queue or parent are <code>null</code>
     */
    protected abstract void enqueueChildren(Queue<E> queue, E parent);

}
