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
 * Created on Apr 10, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DCustom;


/**
 * Implementation of the custom parameter.
 * 
 * @author Christian Schneider
 */
public class Custom 
{

    /**
     * the current value
     */
    private int value;
    
    /**
     * definition of this custom parameter
     */
    private final DCustom definition;
    
    /**
     * creates a new custom parameter 
     * @param definition 
     */
    public Custom(DCustom definition)
    {
        this.definition = definition;
    }
    
    public final DCustom getDefinition()
    {
        return definition;
    }

    public int getValue()
    {
        return value;
    }
    
    public void setValue(int v)
    {
        if (this.value != v && definition.getMinValue()<=v && v<=definition.getMaxValue())
        {
            this.value = v;
            // FIXME: listener not notified
            //fireParameterValueChanged();
        }
    }
/*
    private EventChain<ParameterListener,Event> parameterListenerList = null;

    public void addParameterListener(ParameterListener l)
    {
        parameterListenerList = new EventChain<ParameterListener,Event>(l, parameterListenerList);
    }

    public void removeParameterListener(ParameterListener l)
    {
        if (parameterListenerList!=null)
            parameterListenerList = parameterListenerList.remove(l);
    }

    void fireParameterValueChanged()
    {
        if (parameterListenerList!=null)
            parameterListenerList.fireEvent(ParameterListener.ValueChangeSenderInstance, EventBuilder.parameterValueChanged(this));
    }*/
    
}
