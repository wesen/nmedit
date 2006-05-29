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
package net.sf.nmedit.nomad.patch.virtual.event;

/**
 * Adapter class implementing the {@link net.sf.nmedit.nomad.patch.virtual.event.Listenable} interface. 
 * 
 * @author Christian Schneider
 */
public class ListenableAdapter<T> implements Listenable<T>
{

    /**
     * the listener list
     */
    private EventChain<T> listenerList ;
    
    public ListenableAdapter()
    {
        // the listener list is empty
        listenerList = null;
    }

    public void addListener(EventListener<T> l)
    {
        listenerList = new EventChain<T>(l, listenerList);
    }

    public void removeListener(EventListener<T> l)
    {
        if (listenerList!=null)
            listenerList = listenerList.remove(l);
    }
    
    /**
     * fires an event
     * @param e the event message
     */
    protected void fireEvent(T e)
    {
        if (listenerList!=null)
            listenerList.fireEvent(e);
    }
    
    /**
     * Returns <code>true</code> when the listener list contains the listener.
     * @param l the listener
     * @return <code>true</code> when the listener list contains the listener.
     */
    public boolean containsListener(EventListener<T> l)
    {
        return (listenerList!=null) && listenerList.containsListener(l);
    }
    
}
