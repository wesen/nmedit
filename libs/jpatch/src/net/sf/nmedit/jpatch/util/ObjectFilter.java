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
package net.sf.nmedit.jpatch.util;

public interface ObjectFilter<T>
{
    
    /**
     * Returns an object which identifies this filter and it's configuration.
     * This allows caching the filtered results.
     * 
     * If the filter does not depend on parameters than the filter class
     * should be returned.
     */
    Object getIdentifier();

    /**
     * Returns true if the specified object is accepted or false otherwise.
     */
    boolean accepts(T o);

}
