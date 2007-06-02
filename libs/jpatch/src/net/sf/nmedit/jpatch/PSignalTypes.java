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
package net.sf.nmedit.jpatch;

/**
 * Contains all defined signals.
 * 
 * @author Christian Schneider
 */
public interface PSignalTypes extends Iterable<PSignal>
{

    /**
     * Returns the number of defined signals.
     * @return the number of defined signals
     */
    int getSignalTypeCount();
    
    /**
     * Returns the signal at the specified index.
     * @param index the signal index
     * @return the signal at the specified index
     */
    PSignal getSignalType(int index);
    
    /**
     * Returns the signal with the specified id.
     * @param id the signal id
     * @return the signal with the specified id
     */
    PSignal getSignalTypeById(int id);
    
    /**
     * Returns the signal with the specified name.
     * @param name name of the signal
     * @return the signal with the specified name
     */
    PSignal getSignalTypeByName(String name);
    
    /**
     * Returns the signal definition for the no-signal situation.
     * @return the no-signal
     */
    PSignal noSignal();
    
    /**
     * Returns true if the specified signal is defined,
     * false otherwise.
     * @param signal the signal
     * @return true if the specified signal is defined
     */
    boolean contains(PSignal signal);

}
