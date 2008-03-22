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
 * Descriptor for a {@link PParameter parameter}.
 * 
 * @author Christian Schneider
 */
public interface PParameterDescriptor extends PDescriptor
{

    /**
     * The default parameter control for this component is preferred.
     */
    public static final int PT_VALUE = 0;
    
    /**
     * A radio button group is preferred.  
     */
    public static final int PT_OPTION = 1;
    
    /**
     * A knob is preferred.  
     */
    public static final int PT_KNOB = 2;

    /**
     * A slider is preferred.
     */
    public static final int PT_SLIDER = 3;
    
    /**
     * A two-state switch is preferred.  
     */
    public static final int PT_BOOLEAN = 4;

    /**
     * Returns the type of this parameter. The type can be used
     * by an automated user interface builder to decide which
     * control to create for this parameter.
     *
     * Possible values are
     * <ul><li>PT_VALUE - The default parameter control for this component is preferred</li>
     * <li>PT_KNOB - A knob (or similar control) is preferred</li>
     * <li>PT_SLIDER - A slider (or similar control) is preferred</li>
     * <li>PT_OPTION - A radio button group (or similar control) is preferred</li>
     * <li>PT_BOOLEAN - A two-state switch (or similar control) is preferred
     *  if additionally getMaxValue()-getMinValue()+1==2 is true (it must have exactly two states).
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
    String getDisplayValue(int value);

    /**
     * Returns the parent module descriptor.
     */
    PModuleDescriptor getParentDescriptor();

    PParameterDescriptor getExtensionDescriptor();
    
}
