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

/**
 * Describes a parameter of a message.
 */
public interface PRParameterDescriptor
{

    /**
     * Returns the name of this parameter.
     * @return the name of this parameter
     */
    String getName();
    
    /**
     * Returns the path to the PDL variable associated with this parameter.
     * @return the path to the PDL variable
     */
    String getBinding();

    /**
     * Returns the default value or -1 if no default value was defined.
     * @return the default value
     */
    int getDefaultValue();
    
    /**
     * Returns true if the default value property of this parameter is defined.
     * @return true if the default value property of this parameter is defined
     */
    boolean hasDefaultValue();
    
}
