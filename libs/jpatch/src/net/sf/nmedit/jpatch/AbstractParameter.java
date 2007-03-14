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
 * Created on Dec 2, 2006
 */
package net.sf.nmedit.jpatch;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.event.ParameterEvent;
import net.sf.nmedit.jpatch.event.ParameterValueChangeListener;

public abstract class AbstractParameter extends AbstractComponent implements Parameter
{
    // event listeners
    protected EventListenerList eventListenerList = null;
    // temporary listener list - this is faster than searching for the right listeners 
    // field is declared transient because it can be rebuild from the eventListenerList
    private transient ParameterValueChangeListener[] pvcListeners;
    // cached event message
    // field is declared transient because it is recreated when needed
    private transient ParameterEvent cachedValueChangeEvent;

    public abstract int getValue();
    public abstract void setValue( int value );
    
    public int getDefaultValue()
    {
        return getDescriptor().getDefaultValue();
    }

    public int getRange()
    {
        return getDescriptor().getRange();
    }

    public int getMinValue()
    {
        return getDescriptor().getMinValue();
    }

    public int getMaxValue()
    {
        return getDescriptor().getMaxValue();
    }

    public static final double normalizeValue(int value, int minValue, int maxValue)
    {
        if (value<=minValue)
            return 0d;
        else if (value>=maxValue)
            return 1d;
        
        int divisor = maxValue-minValue;
        if (divisor>0)
            return value/(double) divisor;
        else
            return 0d;
    }
    
    public static final int deNormalizeValue(double normalizedValue, int minValue, int maxValue)
    {
        int value = (int) (normalizedValue * (maxValue-minValue));
        if (value <= minValue)
            return minValue;
        else if (value >= maxValue)
            return maxValue;
        else
            return value;
    }
    
    public double getNormalizedDefaultValue()
    {
        return normalizeValue(getDefaultValue(), getMinValue(), getMaxValue());
    }

    public double getNormalizedValue()
    {
        return normalizeValue(getValue(), getMinValue(), getMaxValue());
    }

    public void setNormalizedValue( double normalizedValue )
    {
        setValue(deNormalizeValue(normalizedValue, getMinValue(), getMaxValue()));
    }

    public String getFormattedValue()
    {
        return getFormattedValue(getValue());
    }

    public String getFormattedValue( int value )
    {   
        return getDescriptor().getFormattedValue(this, value);
    }

    public String getFormattedValue( double normalizedValue )
    {
        return getFormattedValue(deNormalizeValue(normalizedValue, getMinValue(), getMaxValue()));
    }

    public String getName()
    {
        return getDescriptor().getName();
    }

    protected void fireParameterValueChanged()
    {
        if (eventListenerList != null)
        {
            if (pvcListeners == null)
            {
                // note: getListeners(Class) swaps order of the listeners
                pvcListeners = eventListenerList.getListeners(ParameterValueChangeListener.class);
            }
            
            final int len = pvcListeners.length;
            if (len>0)
            {
                if (cachedValueChangeEvent == null)
                {
                    cachedValueChangeEvent = new ParameterEvent(this, ParameterEvent.VALUE_CHANGED);
                }
                
                // i=0 to len-1 because of opposite order
                for (int i=0;i<len;i++)
                {
                    pvcListeners[i].parameterValueChanged(cachedValueChangeEvent);
                }
            }
        }
    }
    
    public void addParameterValueChangeListener(ParameterValueChangeListener l)
    {
        if (l == null)
            return ;

        if (eventListenerList==null)
            eventListenerList = new EventListenerList();
        
        eventListenerList.add(ParameterValueChangeListener.class, l);
        pvcListeners = null;
    }

    public void removeParameterValueChangeListener(ParameterValueChangeListener l)
    {
        if (l != null && eventListenerList != null)
        {
            eventListenerList.remove(ParameterValueChangeListener.class, l);
            if (eventListenerList.getListenerCount()==0)
                eventListenerList = null;
            pvcListeners = null;
        }
    }

}
