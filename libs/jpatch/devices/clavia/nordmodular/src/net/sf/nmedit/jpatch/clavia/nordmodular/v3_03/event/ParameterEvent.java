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
 * Created on Apr 21, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;

public class ParameterEvent extends PatchEvent
{
    
    private Parameter parameter;

    public ParameterEvent()
    {
        parameter = null;
    }

    public Parameter getParameter()
    {
        return parameter;
    }

    private void setParameter( Parameter parameter )
    {
        this.parameter = parameter;
    }
    
    public void knobAssignment( Parameter parameter )
    {
        setParameter(parameter);
        setID(KNOB_ASSIGNMENT);
    }

    public void morphAssignment( Parameter parameter )
    {
        setParameter(parameter);
        setID(MORPH_ASSIGNMENT);
    }

    public void valueChanged( Parameter parameter )
    {
        setParameter(parameter);
        setID(PARAMETER_VALUE_CHANGED);
    }

    public void morphRangeChanged( Parameter parameter )
    {
        setParameter(parameter);
        setID(PARAMETER_MORPHRANGE_CHANGED);
    }

}
