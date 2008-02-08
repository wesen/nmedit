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
package net.sf.nmedit.jsynth;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;

public abstract class AbstractSlot implements Slot
{
    
    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    protected EventListenerList eventListenerList = new EventListenerList();

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        changeSupport.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener l)
    {
        changeSupport.addPropertyChangeListener(propertyName, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        changeSupport.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String propertyName,
            PropertyChangeListener l)
    {
        changeSupport.removePropertyChangeListener(propertyName, l);
    }

    public void addSlotListener(SlotListener l)
    {
        eventListenerList.add(SlotListener.class, l);
    }

    public void removeSlotListener(SlotListener l)
    {
        eventListenerList.remove(SlotListener.class, l);
    }

    protected void fireNewPatchInSlotEvent(Object oldPatch, Object newPatch)
    {
        if (eventListenerList == null)
            return;
        
        SlotListener[] list = eventListenerList.getListeners(SlotListener.class);
 
        if (list.length == 0)
            return;
        
        SlotEvent e = new SlotEvent(SlotEvent.SYNTH_SLOT_NEWPATCH, this);
        e.setOldPatch(oldPatch);
        e.setNewPatch(newPatch);
        
        for (int i=0;i<list.length;i++)
        {
            list[i].newPatchInSlot(e);
        }
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(String propertyName, int oldValue, int newValue)
    {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue)
    {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
}
