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
package net.sf.nmedit.jprotocol;

import net.sf.nmedit.jpdl.Packet;

/**
 * A message.
 */
public interface PRMessage
{

    /**
     * Return a descriptor of this message.
     * @return descriptor of this message
     */
    PRMessageDescriptor getDescriptor();

    /**
     * Returns the name of this message.
     * @return name of this message.
     */
    String getName();

    /**
     * Returns the id of this message.
     * @return the id of this message
     */
    int getId();
        
    /**
     * Returns the value of the parameter at the specified index.
     * @param index index of the parameter
     * @return returns the parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     */
    int getValueAt(int index);
    
    /**
     * Sets the value of the parameter at the specified index.
     * @param index index of the parameter
     * @param value new value
     * @throws IllegalArgumentException if the parameter does not exist
     */
    void setValueAt(int index, int value);

    /**
     * Returns the value of the parameter with the specified name.
     * @param name name of the parameter
     * @return returns the parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     */
    int getValue(String paramName);
    
    /**
     * Sets the value of the parameter with the specified name.
     * @param name index of the parameter
     * @param value new value
     * @throws IllegalArgumentException if the parameter does not exist
     */
    void setValue(String paramName, int value);
    
    /**
     * Returns the value of the specified parameter. 
     * @param parameter descriptor used to lookup the parameter
     * @return returns the parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     */
    int getValue(PRParameterDescriptor parameter);

    /**
     * Sets the value of the specified parameter. 
     * @param parameter descriptor used to lookup the parameter
     * @return returns the parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     */
    void setValue(PRParameterDescriptor parameter, int value);
    
    /**
     * Iterates through all parameters of this message and
     * sets their values to the values obtained from the specified 
     * packet. The values in the packet are looked up using 
     * {@link PRParameterDescriptor#getBinding()} as path.
     * 
     * @param packet
     */
    void setValues(Packet packet);
    
}
