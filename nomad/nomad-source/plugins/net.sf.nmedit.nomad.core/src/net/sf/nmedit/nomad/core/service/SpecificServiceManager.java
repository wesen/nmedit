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
package net.sf.nmedit.nomad.core.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.EventListenerList;


public class SpecificServiceManager<T extends Service> implements ServiceManager<T>
{

    private EventListenerList listenerList = new EventListenerList();
    private List<T> serviceList = new ArrayList<T>();

    public void addService(T s) 
    {
        if (serviceList.contains(s))
            return;
        
        serviceList.add(s);
        
        notifyServiceListeners(s, true);
    }
    
    private void notifyServiceListeners(T s, boolean added)
    {
        ServiceManagerListener[] list = listenerList.getListeners(ServiceManagerListener.class);
        
        ServiceManagerEvent e = new ServiceManagerEvent(this, s, added);
        
        
        if (added)
        {
            for (int i=0;i<list.length;i++)
                list[i].serviceAdded(e);
        }
        else
        {
            for (int i=0;i<list.length;i++)
                list[i].serviceRemoved(e);
        }
    }

    public void removeService(T s) 
    {
        if (!serviceList.contains(s))
            return;
        
        serviceList.remove(s);
        notifyServiceListeners(s, false);
    }

    public void addServiceManagerListener(ServiceManagerListener l)
    {
        listenerList.add(ServiceManagerListener.class, l);
    }

    public void removeServiceManagerListener(ServiceManagerListener l)
    {
        listenerList.remove(ServiceManagerListener.class, l);   
    }

    public Iterator<T> iterator()
    {
        return getServices();
    }

    public Iterator<T> getServices()
    {
        return serviceList.iterator();
    }

    public boolean isEmpty()
    {
        return serviceList.isEmpty();
    }

    public int size()
    {
        return serviceList.size();
    }

}

