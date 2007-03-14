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
 * Created on Sep 2, 2006
 */
package net.sf.nmedit.jmisc.collections;

/**
 * An indexed object.
 * 
 * @author Christian Schneider
 */
public interface Indexed
{
    
    /**
     * Returns the index of this object.
     * 
     * <p> Whenever it is invoked on the same object more than once during 
     * an execution of a Java application, the <tt>getIndex</tt> method 
     * must consistently return the same integer. </p>
     * 
     * @return the index of the object
     */
    int getIndex();

}
