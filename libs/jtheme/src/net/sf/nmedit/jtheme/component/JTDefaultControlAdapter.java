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
package net.sf.nmedit.jtheme.component;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jpatch.PParameter;

public class JTDefaultControlAdapter implements JTControlAdapter
{

    private ChangeListener listener;
    private int minValue;
    private int maxValue;
    private int defaultValue;
    private int value;
    private JTComponent component;
    
    public JTDefaultControlAdapter(int minValue, int maxValue, int defaultValue)
    {
        if (minValue>maxValue)
            throw new IllegalArgumentException("minimum value must be smaller or equal than the maximum value");
        
        if (defaultValue<minValue || maxValue<defaultValue)
            throw new IllegalArgumentException("default value out of range");
        
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }
    
    public void setChangeListener(ChangeListener l)
    {
        this.listener = l;
    }
    
    public ChangeListener getChangeListener()
    {
        return listener;
    }
    
    protected void notifyChangeListener()
    {
        if (listener != null)
            listener.stateChanged(new ChangeEvent(this));
    }
    
    public int getMaxValue()
    {
        return maxValue;
    }

    public int getMinValue()
    {
        return minValue;
    }

    public double getNormalizedValue()
    {
        return (minValue<maxValue) ? (value/(double)(maxValue-minValue)) : 0;
    }

    public int getValue()
    {
        return value;
    }

    public void setMaxValue(int maxValue)
    {
        if (this.maxValue != maxValue)
        {
            this.maxValue = maxValue;
            notifyChangeListener();
        }
    }

    public void setMinValue(int minValue)
    {
        if (this.minValue != minValue)
        {
            this.minValue = minValue;
            notifyChangeListener();
        }
    }

    public void setNormalizedValue(double value)
    {
        int intValue = (int) (value*(maxValue-minValue));
        setValue(intValue);
    }

    public void setValue(int value)
    {
        value = Math.max(minValue, Math.min(value, maxValue));
        
        if (this.value != value)
        {
            this.value = value;
            notifyChangeListener();
        }
    }

    public int getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public PParameter getParameter()
    {
        return null;
    }

    public JTComponent getComponent()
    {
        return this.component;
    }

    public void setComponent(JTComponent c)
    {
        this.component = c;
    }

}

