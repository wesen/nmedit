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

/**
 * Describes the properties of a parameter.
 * 
 * @author Christian Schneider
 */
public interface ParameterDescriptor extends CompositeDescriptor
{

    /**
     * Returns the parameter's default value
     * @return the parameter's default value
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
     * @see Descriptor#getSourceDescriptor()
     */
    ParameterDescriptor getSourceDescriptor();

    /**
     * Returns the parameter-class  
     * @return
     */
    String getParameterClass();
    
    int getIndex();

    String getFormattedValue(Parameter parameter, int value);
}
