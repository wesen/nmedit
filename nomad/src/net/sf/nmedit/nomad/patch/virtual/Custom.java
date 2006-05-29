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
package net.sf.nmedit.nomad.patch.virtual;

import net.sf.nmedit.nomad.patch.virtual.event.CustomEvent;
import net.sf.nmedit.nomad.patch.virtual.event.ListenableAdapter;
import net.sf.nmedit.nomad.xml.dom.module.DCustom;


/**
 * Implementation of the custom parameter.
 * 
 * @author Christian Schneider
 */
public class Custom extends ListenableAdapter<CustomEvent>
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
     * event message object
     */
    private static CustomEvent eventMessage = new CustomEvent();

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
            eventMessage.valueChanged(this);
            fireEvent(eventMessage);
        }
    }
    
}
