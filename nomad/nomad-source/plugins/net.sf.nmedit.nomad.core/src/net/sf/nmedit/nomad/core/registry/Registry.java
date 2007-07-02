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
package net.sf.nmedit.nomad.core.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.nmutils.collections.UnmodifiableIterator;

public class Registry<T> implements Iterable<T>
{

    private List<T> items = new ArrayList<T>();
    private EventListenerList listenerList = new EventListenerList();

    public void add(T item)
    {
        if (items.contains(item))
            return ;
        if (items.add(item))
            fireItemEvent(item, RegistryEvent.ITEM_REGISTERED);
    }

    public void remove(T item)
    {
        if (items.remove(item))
        {
            fireItemEvent(item, RegistryEvent.ITEM_UNREGISTERED);
        }
    }
    
    public int size()
    {
        return items.size();
    }

    public void addRegistryListener(RegistryListener l)
    {
        listenerList.add(RegistryListener.class, l);
    }

    public void removeRegistryListener(RegistryListener l)
    {
        listenerList.remove(RegistryListener.class, l);
    }
    
    private void fireItemEvent(T item, int id)
    {
        RegistryEvent event = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==RegistryListener.class) {
                // Lazily create the event:
                if (event == null)
                    event = new RegistryEvent(this, id, item);
                RegistryListener l = ((RegistryListener)listeners[i+1]);
                if (id == RegistryEvent.ITEM_REGISTERED)
                    l.itemRegistered(event);
                else
                    l.itemUnregistered(event);
            }
        }
    }

    public Iterator<T> iterator()
    {
        return new UnmodifiableIterator<T>(items.iterator());
    }
    
    public <S extends T> Iterator<S> iterator(final Class<S> clazz)
    {
        return new Iterator<S>()
        {

            Iterator<T> iter = items.iterator();
            S next;

            public boolean hasNext()
            {
                while (next == null && iter.hasNext())
                {
                    T n = iter.next();
                    if (clazz.isInstance(n))
                        next = clazz.cast(n);
                }
                return next != null;
            }

            public S next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                
                S result = next;
                next = null;
                return result;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
    
}
