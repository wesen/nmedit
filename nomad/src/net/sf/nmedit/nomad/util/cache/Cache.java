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

public interface Cache<T>
{

    public final static boolean WEAK = true;
    public final static boolean PERSISTENT = false;
    
    /**
     * Returns the cached object.
     * If the cache is recoverable and the cached object was lost,
     * it will be recovered and returned.
     * 
     * @return the cached object
     */
    T get();
    
    /**
     * Puts the object in the cache.
     * 
     * @param t
     * @throws UnsupportedOperationException if the operation is not supported
     */
    void put(T t);
    
    /**
     * If {@link #isWeak()} is <code>false</code> it
     * removes the saved object but remains a weak
     * reference to it. After calling this the
     * {@link #get()} method might still return
     * the object. To completely remove the object
     * use <code>set(null)</code>.
     */
    void dispose();
    
    /**
     * Returns true when the underlying implementation
     * uses only weak references.
     * 
     * @return true when the underlying implementation
     * uses only weak references
     */
    boolean isWeak();
    
    /**
     * Returns true when the lost object is recoverable.
     * @return true when the lost object is recoverable
     */
    boolean isRecoverable();
    
}
