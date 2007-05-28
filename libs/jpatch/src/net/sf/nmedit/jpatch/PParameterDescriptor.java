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
 * Describes a parameter;
 * @author Christian Schneider
 */
public interface PParameterDescriptor extends PComponentDescriptor
{

    // parameter types
    public static final int PT_VALUE = 0;
    public static final int PT_OPTION = 1;
    public static final int PT_BOOLEAN = 2;

    /**
     * Returns the type of this parameter.
     * The type can be used by an automated
     * user interface builder to decide which
     * control to create for this parameter.
     *
     * Possible values are
     * <ul><li>PT_VALUE - a knob control is preferred</li>
     * <li>PT_OPTION - radio buttons (or similar controls) are preferred </li>
     * <li>PT_BOOLEAN - a toggle button with true/false states are preferred
     *  if additionally getMaxValue()-getMinValue()+1==2 are true.
     *  The maximum value is interpreted as true, minimum value is interpreted as false</li>
     * </ul>
     * 
     * @return
     */
    int getType();

    /**
     * The maximum value of the parameter.
     * @return maximum value of the parameter
     */
    int getMaxValue();

    /**
     * The minimum value of the parameter.
     * @return minimum value of the parameter
     */
    int getMinValue();

    /**
     * The default value of the parameter.
     * @return default value of the parameter
     */
    int getDefaultValue();

    /**
     * Returns a string representation of the specified value. 
     * @return string representation of the specified value
     * @throws IllegalArgumentException if the value is less
     * then the minimum value or larger then the maximum value.
     */
    String getDisplayValue(PParameter parameter, int value);

    /**
     * Returns the parent descriptor.
     */
    PModuleDescriptor getParentDescriptor();

}
