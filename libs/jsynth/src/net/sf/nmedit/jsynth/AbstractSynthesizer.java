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
 * Created on Jan 8, 2007
 */
package net.sf.nmedit.jsynth;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jsynth.event.ComStatusEvent;
import net.sf.nmedit.jsynth.event.ComStatusListener;
import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;

public abstract class AbstractSynthesizer implements Synthesizer
{

    private EventListenerList listenerList = new EventListenerList();
    private PropertyChangeSupport changeSupport
    = new PropertyChangeSupport(this);
    
    private Map<Object, Object> clientPropertyMap;

    private Map<Object, Object> getClientPropertyMap(boolean create)
    {
        if (create && clientPropertyMap == null)
            clientPropertyMap = new HashMap<Object, Object>();
        return clientPropertyMap;
    }
    
    public void putClientProperty(Object key, Object value)
    {
        getClientPropertyMap(true).put(key, value);
    }
    
    public Object getClientProperty(Object key)
    {
        Map<Object, Object> map = getClientPropertyMap(false);
        return map == null ? null : map.get(key);
    }

    protected void fireComStatusChanged(ComStatus status)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        ComStatusEvent e = null;
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ComStatusListener.class) {
                // Lazily create the event:
                if (e == null) e = new ComStatusEvent(this, status);
                ((ComStatusListener)listeners[i+1]).comStatusChanged(e);
            }
        }
    }

    protected void fireSynthesizerStateChanged()
    {
        SynthesizerStateListener[] list = 
            listenerList.<SynthesizerStateListener>getListeners(SynthesizerStateListener.class);
        
        if (list.length>0)
        {
            SynthesizerEvent e = new SynthesizerEvent(this);
            for (int i=0;i<list.length;i++)
            {
                list[i].synthConnectionStateChanged(e);
            }
        }
    }

    public Object getProperty(Object key)
    {
        return null;
    }
    
    public void addSynthesizerStateListener( SynthesizerStateListener l )
    {
        listenerList.add(SynthesizerStateListener.class, l);
    }

    public void removeSynthesizerStateListener( SynthesizerStateListener l )
    {
        listenerList.remove(SynthesizerStateListener.class, l);
    }
    
    public void addComStatusListener( ComStatusListener l )
    {
        listenerList.add(ComStatusListener.class, l);
    }

    public void removeComStatusListener( ComStatusListener l )
    {
        listenerList.remove(ComStatusListener.class, l);
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        changeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        changeSupport.removePropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener l)
    {
        changeSupport.addPropertyChangeListener(propertyName, l);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener l)
    {
        changeSupport.removePropertyChangeListener(propertyName, l);
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
