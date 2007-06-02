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

import net.sf.nmedit.jpatch.PLight;
import net.sf.nmedit.jpatch.PLightDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.event.PLightEvent;
import net.sf.nmedit.jpatch.event.PLightListener;

public class PBasicLight extends PBasicComponent<PLightDescriptor> implements PLight
{

    private PModule parent;
    private int value;
    private EventListenerList listenerList = new EventListenerList();
    private transient PLightEvent lightEvent;

    public PBasicLight(PLightDescriptor descriptor, PModule parent, int componentIndex)
    {
        super(descriptor, componentIndex);
        this.parent = parent;
        this.value = descriptor.getDefaultValue();
    }
    
    public int getType()
    {
        return getDescriptor().getType();
    }
    
    public void addLightListener(PLightListener l)
    {
        listenerList.add(PLightListener.class, l);
    }
    
    public void removeLightListener(PLightListener l)
    {
        listenerList.remove(PLightListener.class, l);
    }

    protected void fireLightChanged()
    {
        Object[] list = listenerList.getListenerList();
        
        if (list.length>0)
        {
            if (lightEvent == null)
                lightEvent = new PLightEvent(this);

            for (int i=list.length-2;i>=0;i-=2)
                if (list[i]==PLightListener.class)
                    ((PLightListener)list[i+1]).lightChanged(lightEvent);
        }
    }

    public int getDefaultValue()
    {
        return getDescriptor().getDefaultValue();
    }

    public int getMaxValue()
    {
        return getDescriptor().getMaxValue();
    }

    public int getMinValue()
    {
        return getDescriptor().getMinValue();
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        int oldValue = this.value;
        value = Math.max(getMinValue(), Math.min(value, getMaxValue()));
        if (value != oldValue)   
        {
            this.value = value;
            fireLightChanged();
        }
    }

    public PModule getParentComponent()
    {
        return parent;
    }

}
