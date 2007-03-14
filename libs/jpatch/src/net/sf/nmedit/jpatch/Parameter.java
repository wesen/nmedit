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
 * Created on Nov 30, 2006
 */
package net.sf.nmedit.jpatch;

import net.sf.nmedit.jpatch.event.ParameterValueChangeListener;

/**
 * A parameter of a module.
 * 
 * The parameter uses integer values and maps them to it's internal representation.
 * 
 * @author Christian Schneider
 */
public interface Parameter extends Composite
{
    
    /**
     * @see Component#getDescriptor()
     */
    ParameterDescriptor getDescriptor();

    /**
     * Returns the default value of this parameter
     * @return the default value of this parameter
     */
    int getDefaultValue();

    /**
     * Returns the range covered by the parameter value.
     * The return value is always &gt;= 0 and should be &gt;0.
     * 
     * The return value is <code>getMaxValue()-getMinValue()+1</code>
     * 
     * @return the range covered by the parameter value
     */
    int getRange();

    /**
     * Returns the minimum value of this parameter.
     * The condition getMinValue()&lt;=getMaxValue() must always be true.
     * 
     * @return the minimum value of this parameter
     */
    int getMinValue();
    
    /**
     * Returns the maximum value of this parameter.
     * The condition getMinValue()&lt;=getMaxValue() must always be true.
     * 
     * @return the maximum value of this parameter
     */
    int getMaxValue();
    
    /**
     * Returns the current value.
     * @return the current value
     */ 
    int getValue();
    
    /**
     * Sets the current value. If the value is beyond the valid bounds [min...max]
     * it is truncated.
     * 
     * @param value the new value
     */
    void setValue(int value);
    
    /**
     * Returns the default value in the range [0..1].
     * @return the default value in the range [0..1]
     */
    double getNormalizedDefaultValue();
    
    /**
     * Returns the current value in the range [0..1].
     * @return the current value in the range [0..1]
     */
    double getNormalizedValue();
    
    /**
     * Sets the current value using the normalized value which is in the range [0..1].
     * If the normalized value is beyond these bounds it is truncated.
     * 
     * @param normalizedValue the normalized representation of the new value
     */
    void setNormalizedValue(double normalizedValue);

    /**
     * Returns a string representation of the current value. 
     * @return a string representation of the current value
     */
    String getFormattedValue();
    
    /**
     * Returns a string representation of the specified value.
     * If the value is beyond the valid bounds [min...max] it is truncated. 
     * @return a string representation of the specified value
     */
    String getFormattedValue(int value);

    /**
     * Returns a string representation of the specified normalized value .
     * If the normalized value is beyond the valid bounds [0...1] it is truncated. 
     * @return a string representation of the specified value
     */
    String getFormattedValue(double normalizedValue);

    /**
     * Adds a listener which is notified when the parameter value is changed.
     * @param l the listener
     */
    void addParameterValueChangeListener(ParameterValueChangeListener l);
    
    /**
     * Removes the specified listener.
     * @param l the listener
     */
    void removeParameterValueChangeListener(ParameterValueChangeListener l);
    
}
