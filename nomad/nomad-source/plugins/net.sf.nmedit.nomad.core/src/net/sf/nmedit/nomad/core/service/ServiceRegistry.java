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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.nmutils.collections.EmptyIterator;

public class ServiceRegistry
{
    
    private static ServiceRegistry instance = new ServiceRegistry();
    
    public static ServiceRegistry getInstance()
    {
        return instance;
    }
    
    private Map<Class<? extends Service>, ServiceManager> managers
     = new HashMap<Class<? extends Service>, ServiceManager>();
    
    private ServiceManagerEventMulticaster multicaster = new
        ServiceManagerEventMulticaster();
    
    @SuppressWarnings("unchecked")
    private <T extends Service> ServiceManager<T> getManager(Class<T> serviceClass)
    {
        return managers.get(serviceClass);
    }
    
    public static <T extends Service> Iterator<T> getServices(Class<T> serviceClass)
    {
        return getInstance()._getServices(serviceClass);
    }

    private <T extends Service> Iterator<T> _getServices(Class<T> serviceClass)
    {
        ServiceManager<T> manager = getManager(serviceClass);
        return (manager == null) ? EmptyIterator.<T>getInstance(): manager.iterator();
    }

    public static <T extends Service> void addService(Class<T> serviceClass, T service) throws ServiceException
    {
        getInstance()._addService(serviceClass, service);
    }

    private <T extends Service> void _addService(Class<T> serviceClass, T service) throws ServiceException
    {
        ServiceManager<T> manager = getManager(serviceClass);
        
        if (manager == null)
        {
            manager = new SpecificServiceManager<T>();
            managers.put(serviceClass, manager);
            manager.addServiceManagerListener(multicaster);
        }
        
        manager.addService(service);
    }

    public static <T extends Service> void removeService(Class<T> serviceClass, T service) 
    {
        getInstance()._removeService(serviceClass, service);
    }

    private <T extends Service> void _removeService(Class<T> serviceClass, T service)
    {
        ServiceManager<T> manager = getManager(serviceClass);
        
        if (manager == null) return;
        
        manager.removeService(service);
        
        if (manager.isEmpty())
        {
            manager.removeServiceManagerListener(multicaster);
            managers.remove(serviceClass);
        }
    }
    
    public static void addServiceManagerEventListener(ServiceManagerListener l)
    {
        getInstance().multicaster.addServiceManagerEventListener(l);
    }

    public static void removeServiceManagerEventListener(ServiceManagerListener l)
    {
        getInstance().multicaster.removeServiceManagerEventListener(l);
    }
    
    private static class ServiceManagerEventMulticaster implements ServiceManagerListener
    {

        private EventListenerList eventListenerList = new EventListenerList();

        public ServiceManagerEventMulticaster()
        {
        }
        
        public void serviceAdded(ServiceManagerEvent e)
        {
            ServiceManagerListener[] list = eventListenerList.getListeners(ServiceManagerListener.class);
            
            for (int i=0;i<list.length;i++)
                list[i].serviceAdded(e);
        }

        public void serviceRemoved(ServiceManagerEvent e)
        {
            ServiceManagerListener[] list = eventListenerList.getListeners(ServiceManagerListener.class);
            
            for (int i=0;i<list.length;i++)
                list[i].serviceRemoved(e);
        }

        public void addServiceManagerEventListener(ServiceManagerListener l)
        {
            eventListenerList.add(ServiceManagerListener.class, l);
        }

        public void removeServiceManagerEventListener(ServiceManagerListener l)
        {
            eventListenerList.add(ServiceManagerListener.class, l);
        }

    }

    
}

