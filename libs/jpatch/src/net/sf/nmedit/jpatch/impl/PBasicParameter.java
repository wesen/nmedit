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
package net.sf.nmedit.jpatch.impl;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.event.PParameterListener;
import net.sf.nmedit.jpatch.event.ParameterEvent;

public class PBasicParameter 
    extends PBasicComponent<PParameterDescriptor> 
    implements PParameter
{

    // the parent module
    private PModule parent;
    // the parameter value
    private int value;
    private EventListenerList listenerList = new EventListenerList();

    public PBasicParameter(PParameterDescriptor descriptor,
            PModule parent, int componentIndex)
    {
        super(descriptor, componentIndex);
        this.value = getDefaultValue();
        this.parent = parent;
    }
    
    public PModule getParentComponent()
    {
        return parent;
    }

    public int getMaxValue()
    {
        return getDescriptor().getMaxValue();
    }

    public int getMinValue()
    {
        return getDescriptor().getMinValue();
    }

    public int getRange()
    {
        return getMaxValue()-getMinValue()+1;
    }

    public int getDefaultValue()
    {
        return getDescriptor().getDefaultValue();
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        int oldValue = this.value;
        if (value<getMinValue())
            value = getMinValue();
        else if (value>getMaxValue())
            value = getMaxValue();
        if (oldValue != value)
        {
            this.value = value;
            fireParameterValueChanged(oldValue, value);
        }
    }
    
    public double getDoubleValue()
    {
        int min = getMinValue();
        int max = getMaxValue();
        return (min >= max) ? 0 : ((getValue()-min)/(double)(max-min));
    }

    public float getFloatValue()
    {
        int min = getMinValue();
        int max = getMaxValue();
        return (min >= max) ? 0 : ((getValue()-min)/(float)(max-min));
    }

    public void setDoubleValue(double value)
    {
        if (value<=0d)
            setValue(getMinValue());
        else if (value>=1d)
            setValue(getMaxValue());
        else
            setValue(getMinValue() + (int)(value*(double)(getMaxValue()-getMinValue())));
    }

    public void setFloatValue(float value)
    {
        if (value<=0f)
            setValue(getMinValue());
        else if (value>=1f)
            setValue(getMaxValue());
        else
            setValue(getMinValue() + (int)(value*(float)(getMaxValue()-getMinValue())));        
    }

    public String getDisplayValue()
    {
        return getDisplayValue(value);
    }

    public String getDisplayValue(int value)
    {
        if (value<getMinValue()) value = getMinValue();
        else if (value>getMaxValue()) value = getMaxValue();
        return getDescriptor().getDisplayValue(this, value);
    }

    public void addParameterListener(PParameterListener l)
    {
        listenerList.add(PParameterListener.class, l);
    }

    public void removeParameterListener(PParameterListener l)
    {
        listenerList.remove(PParameterListener.class, l);
    }
    
    protected void fireParameterValueChanged(int oldValue, int newValue) 
    {
        ParameterEvent parameterEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PParameterListener.class) 
            {
                // Lazily create the event:
                if (parameterEvent == null)
                    parameterEvent = new ParameterEvent(this, ParameterEvent.VALUE_CHANGED);
                ((PParameterListener)listeners[i+1]).parameterValueChanged(parameterEvent);
            }
        }
    }
    
}
