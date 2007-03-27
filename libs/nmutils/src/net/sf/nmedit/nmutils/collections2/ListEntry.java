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

public class ListEntry<T>
{

    /**
     * the stored item 
     */
    public T item;
    
    /**
     * the remaining entries
     */
    public ListEntry<T> remaining;

    /**
     * Creates a new list with one element. 
     * @param item 
     */
    public ListEntry(T item)
    {
        this(item, null);
    }
    
    /**
     * Creates a new list entry for the specified item.
     * The list entry is the new first item of the list. 
     * @param item
     * @param list 
     */
    public ListEntry(T item, ListEntry<T> list)
    {
        this.item = item;
        this.remaining = list;
    }
    
}
