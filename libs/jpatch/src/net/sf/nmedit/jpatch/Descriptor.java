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

/**
 * A descriptor is a data set the contains all information
 * necessary to create an instance of the described object.
 * The descriptor can become very complex depending on the data strucuture. 
 * 
 * Depending on the implementation the descriptor can be used to share
 * data in common with several instances of the same class. 
 * 
 * @author Christian Schneider
 */
public interface Descriptor
{

    /**
     * Returns the name of this descriptor.
     * @return the name of this descriptor
     */
    String getName();
    
    /**
     * Returns the descriptor from which this one originates.
     * 
     * In the case when copies of copies of ... (or similar operations) are made
     * subsequent decriptors might only be a variation of the descriptor from which each of these
     * descriptors originate. To avoid a cascade effect and the implied depencies each of
     * these descriptors should only reflect the differences from the momentaneous values
     * and the originating descriptor.
     *
     * @return the source descriptor. If the return value is <code>null</code> than there is
     * either no descriptor from which this one originates or such a reference is not stored. 
     */
    Descriptor getSourceDescriptor();
    
}
