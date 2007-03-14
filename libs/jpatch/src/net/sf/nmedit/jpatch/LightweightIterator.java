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
 * Created on Dec 2, 2006
 */
package net.sf.nmedit.jpatch;

import java.util.Iterator;

/**
 * An iterator over a collection which might use the lightweight pattern
 * for temporary storage of the values returned by {@link Iterator#next()}
 * 
 * @see java.util.Iterator
 * @author Christian Schneider
 */
public interface LightweightIterator<E> extends Iterator<E>
{

    /**
     * Returns the next element in the iteration.
     *
     * The iterator does not guarantee that the value return by a previous
     * call to <code>next()</code> is still the same. Thus the iterator is
     * allowed to make use of the lightweight design pattern internally to 
     * avoid unecessary allocation of objects. If a lightweight is returned
     * which is always the same object but setup differently then the lightweight  
     * has to fullfill the contracts of the expected return value. For example
     * the conditions of {@link Object#equals(java.lang.Object)} and
     * {@link Object#hashCode()} have to be fullfilled.
     * 
     * @see java.util.Iterator#next()
     */
    E next();
        
}
