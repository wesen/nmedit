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

import java.util.Collection;

/**
 * Describes a message.
 */
public interface PRMessageDescriptor
{

    /**
     * Returns the name of the described message.
     * @return the name of the described message.
     */
    String getName();
    
    /**
     * Returns the id of the described message.
     * @return the id of the described message.
     */
    int getId();
    
    /**
     * Returns an immutable collection of the parameters of this message.
     * @return the parameters
     */
    Collection<PRParameterDescriptor> getParameters();
    
    /**
     * Returns the parameter at the specified index.
     * @param index the parameter index
     * @return the parameter
     */
    PRParameterDescriptor getParameterAt(int index);
    
    /**
     * Returns the number of parameters.
     * @return the number of parameters
     */
    int getParameterCount();
    
    /**
     * Returns true if the message contains a parameter with the specified name.
     * @param name name of the parameter to lookup
     * @return true if the parameter exists, false otherwise
     */
    boolean containsParameter(String name);
    
    /**
     * Returns the parameter with the specified name.
     * @param name name of the parameter to lookup
     * @return the parameter
     */
    PRParameterDescriptor getParameterByName(String name);
    
    /**
     * Returns the index of the parameter with the specified name.
     * If the parameter does not exist -1 is returned.
     * @param paramName
     * @return index of the parameter
     */
    int getParameterIndex(String paramName);
    

    /**
     * Returns the index of the parameter.
     * If the parameter does not exist -1 is returned.
     * @param paramete the parameter
     * @return index of the parameter
     */
    int getParameterIndex(PRParameterDescriptor parameter);

    
}
