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

public abstract class RemoveSupport<T> implements Iterator<T> 
{
    
    private Iterator<T> iter;
    private T removable;

    public RemoveSupport(Iterator<T> iter)
    {
        this.iter = iter;
        this.removable = null;
    }

    public boolean hasNext()
    {
        return iter.hasNext();
    }

    public T next()
    {
        return removable = iter.next();
    }

    public void remove()
    {
        if (removable == null)
            throw new IllegalStateException();
        T r = removable;
        removable = null;
        remove(r);
    }
    
    protected abstract void remove(T element);
    
}
