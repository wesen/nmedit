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

public class EncapsulatedCache<T> implements Cache<T>
{

    protected Cache<T> cache;

    public EncapsulatedCache(Cache<T> cache)
    {
        this.cache = cache;
    }
    
    public Cache<T> getCache()
    {
        return cache;
    }

    public T get()
    {
        return cache.get();
    }

    public void put( T t )
    {
        cache.put(t);
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
