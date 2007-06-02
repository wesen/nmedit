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

import java.awt.Color;

/**
 * Identifies a specific signal type for example 
 * an audio signal, a logic signal or a control signal. 
 */
public interface PSignal 
{

    /**
     * Returns the name of this signal. The return value
     * must not be <code>null</code>. The value might be
     * displayed to the user (used as fallback), 
     * thus it should be named carefully.
     * @return the name of this signal
     */
    String getName();
    
    /**
     * Returns an identifier of this signal.
     * Each {@link #getDefinedSignals() signal} is unique.
     * @return the identifier of this signal
     */
    int getId();
    
    /**
     * Returns the defined signal types.
     * @return the defined signal types
     */
    PSignalTypes getDefinedSignals();
    
    /**
     * Returns the preferred color for this signal.
     * The value must not be null.
     * @return the preferred color for this signal
     */
    Color getColor();
    
}
