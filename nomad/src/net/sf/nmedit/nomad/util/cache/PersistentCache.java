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
 * Created on May 2, 2006
 */
package net.sf.nmedit.nomad.util.cache;

import java.lang.ref.WeakReference;

public class PersistentCache<T> implements Cache<T>
{

    private T saved = null;
    private WeakReference<T> cache = null;

    public T get()
    {
        if (saved!=null)
        {
            return saved;
        }
        else if (cache != null)
        {
            // might return null
            return cache.get();
        }
        else
        {
            return null;
        }
    }

    public void put( T t )
    {
        saved = t;
        cache = null;
    }

    public void dispose()
    {
        if (saved!=null)
        {
            // keep a weak reference
            cache = new WeakReference<T>(saved);
            // remove persistent reference
            saved = null;
        }
    }

    public boolean isWeak()
    {
        return PERSISTENT;
    }

    public boolean isRecoverable()
    {
        return false;
    }

}
