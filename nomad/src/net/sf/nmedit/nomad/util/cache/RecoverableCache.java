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

import net.sf.nmedit.nomad.util.cache.op.Recoverable;

public class RecoverableCache<T> extends EncapsulatedCache<T>
{

    private Recoverable<T> recoverOp;

    public RecoverableCache( Cache<T> cache, Recoverable<T> recoverOp )
    {
        super( cache );
        this.recoverOp = recoverOp;
    }

    public Recoverable<T> getRecoverOp()
    {
        return recoverOp;
    }

    public T get()
    {
        T cached = get();
        if (cached == null)
        {
            cached = recoverOp.recover();
            super.put(cached);
        }
        return cached;
    }

    public void put(T t)
    {
        throw new UnsupportedOperationException();
    }
    
    public boolean isRecoverable()
    {
        return true;
    }

}
