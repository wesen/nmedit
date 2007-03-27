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
 * Created on May 9, 2006
 */
package net.sf.nmedit.nmutils.collections2;

import java.util.NoSuchElementException;

/**
 * A lightweight stack implementation.
 * 
 * The basic stack operations {@link #add(T)},
 * {@link #getTop()}, {@link #removeTop()}, {@link #isEmpty()},
 * {@link #clear()} are performed in constant time <code>O(1)</code>.
 * 
 * @author Christian Schneider
 */
public class List<T> 
{

    // the linked list
    private ListEntry<T> list = null;
    // predecessor of current selection
    private ListEntry<T> predecessor = null;
    // current selection
    private ListEntry<T> selection = null;

    /**
     * Returns the internal list.
     * @return the internal list
     */
    public ListEntry<T> getInternalList()
    {
        return list;
    }
    
    /**
     * Returns the size of the list.
     * The operation requires time <code>O(n)</code>
     * @return the size of the list.
     */
    public int size()
    {
        if (list == null)
            return 0;
        ListEntry pos = list;
        
        int size = 0;

        while (pos!=null)
        {
            size++;
            pos=pos.remaining;
        }
        
        return size;
    }
    
    /**
     * Adds the specified item to the front of the list.
     * The operation is performed in constant time <code>O(1)</code>.
     * @param item
     */
    public void add(T item)
    {
        list = new ListEntry<T>(item, list);
    }
    
    /**
     * Returns true when the list contains the specified item.
     * The operation is performed in time <code>O(n)</code>.
     * Repeated calls with the same argument are performed
     * in constant time <code>O(1)</code>
     * 
     * @param item the item
     * @return true when the list contains the specified item
     */
    public boolean contains(T item)
    {
        return select(item);
    }
    
    /**
     * Returns the top element of the list.
     * @return the top element of the list
     */
    public T getTop()
    {
        if (isEmpty())
            throw new NoSuchElementException();
        return list.item;
    }
    
    /**
     * Removes the top element of the list.
     * 
     * @return the top element of the list.
     * @throws NoSuchElementException if the list was empty
     */
    public T removeTop()
    {
        if (isEmpty())
            throw new NoSuchElementException();
        
        if (list==selection||list==predecessor)
            deselect();
        
        ListEntry<T> entry = list;
        list = list.remaining;
        entry.remaining = null;
        return entry.item;
    }
    
    /**
     * Removes the specified element from the list.
     * The operation is performed in time <code>O(n)</code>.
     * If the {@link #contains(T)} opertion was called before
     * with the same argument, and the specified item is in the list,
     * the operation is performed in constant time <code>O(1)</code>.
     * 
     * @param item
     * @return <code>true</code> when the element was found and removed. 
     */
    public boolean remove(T item)
    {
        if (select(item))
        {
            if (list == selection)
            {
                list = selection.remaining;
            }
            else
            {
                predecessor.remaining = selection.remaining;
            }
            selection.remaining = null;
            deselect();
            return true;
        }
        else return false;
    }

    /**
     * Returns the predecessor of the {@link ListEntry} that was
     * selected by {@link #select(T)}.
     * @return the predecessor of the current selection
     */
    protected ListEntry<T> getPredecessor()
    {
        return predecessor;
    }
    
    /**
     * Returns the {@link ListEntry} that was
     * selected by {@link #select(T)}.
     * @return the current selection
     */
    protected ListEntry<T> getSelection()
    {
        return selection;
    }
    
    /**
     * Selects the specified item.
     * 
     * @param item
     * @return returns true, when the specified item is in the list
     */
    protected boolean select(T item)
    {
        if (list==null)
        {
            deselect();
            return false; // nothing to select
        }
        else if (selection!=null && selection.item==item)
        {
            return true; // already selected
        }
        else if (list.item==item)
        {
            predecessor = null;
            selection = list; // select
            return true; 
        }
        else
        {
            // search in list
            predecessor = list;
            selection = list.remaining;
            
            while (selection!=null)
            {
                if (selection.item==item)
                {
                    // found
                    return true;
                }
                predecessor = selection;
                selection = selection.remaining;
            }
         
            // item not in list
            predecessor = null;
            return false;
        }
    }
    
    /**
     * deselects the current selection
     */
    protected void deselect()
    {
        predecessor = null;
        selection = null;
    }
    
    /**
     * Returns <code>true</code> when the list is empty
     * @return <code>true</code> when the list is empty
     */
    public boolean isEmpty()
    {
        return list==null;
    }
    
    /**
     * Removes each element from the list.
     */
    public void clear()
    {
        list = null;
    }
    
}
