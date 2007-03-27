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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component;

import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.plaf.JTControlUI;

public abstract class JTControl extends JTComponent implements ChangeListener
{

    private JTControlAdapter adapter;

    public JTControl(JTContext context)
    {
        super(context);
    }

    public void setUI(JTControlUI controlUI)
    {
        super.setUI(controlUI);
    }
    
    public int getOrientation()
    {
        return SwingConstants.HORIZONTAL;
    }
    
    public void addChangeListener(ChangeListener l)
    {
        listenerList.add(ChangeListener.class, l);
    }
    
    public void removeChangeListener(ChangeListener l)
    {
        listenerList.remove(ChangeListener.class, l);
    }
    
    protected transient ChangeEvent changeEvent; // this is source
    
    protected void fireStateChanged()
    {
        repaint();
        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeEvent.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }
    
    public void stateChanged(ChangeEvent e)
    {
        fireStateChanged();
    }
    
    public JTControlAdapter getControlAdapter()
    {
        return adapter;
    }

    public void setAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = adapter;
        this.adapter = adapter;
        
        if (oldAdapter != null)
            oldAdapter.setChangeListener(null);
        if (adapter != null)
            adapter.setChangeListener(this);
    }

    public int getValue()
    {
        return adapter != null ? adapter.getValue() : 0; 
    }
    
    public void setValue(int value)
    {
        if (adapter != null)
            adapter.setValue(value);
        
        repaint();
    }
    
    public int getMinValue()
    {
        return adapter != null ? adapter.getMinValue() : 0;
    }
    
    public void setMinValue(int minValue)
    {
        if (adapter != null)
            adapter.setMinValue(minValue); 
    }
    
    public int getMaxValue()
    {
        return adapter != null ? adapter.getMaxValue() : 0;
    }
    
    public void setMaxValue(int maxValue)
    {
        if (adapter != null)
            adapter.setMaxValue(maxValue); 
    }
    
    public double getNormalizedValue()
    {
        return adapter != null ? adapter.getNormalizedValue() : 0;
    }
    
    public void setNormalizedValue(double value)
    {
        if (adapter != null)
            adapter.setNormalizedValue(value);
        
        repaint();
    }

    public int getDefaultValue()
    {
        return adapter != null ? adapter.getDefaultValue() : 0;
    }
  
    public void setDefaultValue(int defaultValue)
    {
        if (adapter!=null)
            adapter.setDefaultValue(defaultValue);
    }
    
}
