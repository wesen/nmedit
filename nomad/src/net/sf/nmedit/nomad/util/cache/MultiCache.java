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
 * Created on May 3, 2006
 */
package net.sf.nmedit.nomad.util.cache;

import net.sf.nmedit.nomad.util.cache.op.Reusable;
import net.sf.nmedit.nomad.util.collection.List;

public class MultiCache<T> implements Cache<T>
{
    
    private Cache<List<T>> cache = null;
    private Reusable<T> op;

    public MultiCache(Reusable<T> reuseOp)
    {
        this (true, reuseOp);
    }

    public MultiCache(boolean weak, Reusable<T> reuseOp)
    {
        cache = weak ? new WeakCache<List<T>>()
                : new PersistentCache<List<T>>();
        this.op = reuseOp;
    }

    public Reusable<T> getReuseOp()
    {
        return op;
    }

    public T get()
    {
        List<T> list = cache.get();

        if (list != null && !list.isEmpty())
        {
            T element = list.removeTop();
            op.reuse(element);
            return element;
        }
        return op.recover();
    } 

    public void put( T t )
    {
        if (!op.isValid(t))
            throw new IllegalArgumentException("Invalid argument:"+t);
        
        List<T> list = cache.get();
        if (list == null)
        {
            list = new List<T>();
            cache.put(list);
        }
        list.add(t);
    }

    public void dispose()
    {
        cache.dispose();
    }

    public boolean isWeak()
    {
        return cache.isWeak();
    }

    public boolean isRecoverable()
    {
        return cache.isRecoverable();
    }

}
