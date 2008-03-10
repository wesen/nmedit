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

import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.event.PParameterListener;

/**
 * Parameter of a {@link PModule module}.
 * 
 * @author Christian Schneider
 */
/**
 * @author distrinet
 *
 */
public interface PParameter extends PComponent
{
    
    /**
     * Returns the parent module.
     * @return the parent module
     */
    PModule getParentComponent();

    /**
     * Returns the descriptor of this parameter.
     * @return the parameter descriptor
     */
    PParameterDescriptor getDescriptor();

    /**
     * Returns the value of the parameter.
     * @return the value of the parameter
     */
    int getValue();
    
    /**
     * Sets the value of the parameter.
     * The specified value will be adjusted
     * to enuser it is in the range 
     *   [getMinValue()..getMaxValue()]
     * (max(getMinValue(), min(value, getMaxValue()))).
     * 
     * @param value the new parameter value
     */
    void setValue(int value);
    
    /**
     * Returns the minimum parameter value.
     * The return value is always lesser or
     * equal than the maximum value.
     * @return the minimum parameter value
     */
    int getMinValue();

    /**
     * Returns the maximum parameter value.
     * The return value is always larger or
     * equal than the minimum value.
     * @return the maximum parameter value
     */
    int getMaxValue();
    
    /**
     * Returns the default value.
     * @return the default value
     */
    int getDefaultValue();
    
    /**
     * Returns the range of the parameter value, 
     * the number of possible parameter values.
     * The formal representation is
     * <code>range = getMaxValue()-getMinValue()+1</code>.
     * 
     * @return the range of the parameter value
     */
    int getRange();
    
    /**
     * Returns the normalized parameter value.
     * The returned value is in the range [0..1].
     * 
     * @return the normalized parameter value using the float type
     */
    float getFloatValue();
    
    /**
     * Sets the parameter value from a float.
     * The value must be normalized, thus in
     * the range [0..1]. The specified value will be
     * adjusted to ensure it is in the range [0..1] 
     * 
     * @param value the new value in the normalized state
     */
    void setFloatValue(float value);

    /**
     * Returns the normalized parameter value.
     * The returned value is in the range [0..1].
     * @return the normalized parameter value using the double type
     */
    double getDoubleValue();

    /**
     * Sets the parameter value from a double.
     * The value must be normalized, thus in
     * the range [0..1]. The specified value will be
     * adjusted to ensure it is in the range [0..1] 
     * 
     * @param value the new value in the normalized state
     */
    void setDoubleValue(double value);
    
    /**
     * Returns a string representation of the current value. 
     * @return string representation of the current value
     */
    String getDisplayValue();

    /**
     * Returns a string representation of the specified value. 
     * @return string representation of the specified value
     * @throws IllegalArgumentException if the value is less
     * then the minimum value or larger then the maximum value.
     */
    String getDisplayValue(int value);

    /**
     * Returns a string representation of the specified normalized value .
     * If the normalized value is beyond the valid bounds [0...1] it is truncated. 
     * @return a string representation of the specified value
     */
//    String getDisplayValue(double normalizedValue);

    /**
     * Adds the specified {@link PParameterListener} to the listener list.
     * @param l the listener
     */
    void addParameterListener(PParameterListener l);

    /**
     * Removes the specified {@link PParameterListener} from the listener list.
     * @param l the listener
     */
    void removeParameterListener(PParameterListener l);
    
    PParameter getExtensionParameter();
    
    void requestFocus();

    /**
     * @return the morph group this parameter belongs to. < 0 if not assigned to a group.  
     */
    int getMorphGroup();
    
    /**
     * Set the morph group of this parameter.
     * @param group index of the group. If group < 0, parameter is not part of a group 
     */
    void setMorphGroup(int group);

    
    /**
     * Make undo operations visible because parameter require special handling.
     */
	void disableUndo();

	void enableUndo();
	
    public UndoableEdit createParameterValueEdit(int oldValue, int newValue); 

}
