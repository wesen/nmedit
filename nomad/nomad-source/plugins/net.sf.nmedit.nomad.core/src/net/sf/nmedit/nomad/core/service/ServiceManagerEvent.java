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

import java.awt.Event;


public class ServiceManagerEvent extends Event
{

    public static final int SERVICE_ADDED = 0;
    public static final int SERVICE_REMOVED = 1;

    public ServiceManagerEvent(ServiceManager manager, Service service, boolean added)
    {
        this(manager, service, added ? SERVICE_ADDED : SERVICE_REMOVED);
    }
    
    public ServiceManagerEvent(ServiceManager manager, Service service, int id)
    {
        super(manager, id, service);
    }
    
    public ServiceManager getServiceManager()
    {
        return (ServiceManager) target;
    }
    
    public Service getService()
    {
        return (Service) arg;
    }
    
    public int getId()
    {
        return id;
    }

    public boolean isServiceAdded()
    {
        return getId() == SERVICE_ADDED;
    }
    
    public boolean isServiceRemoved()
    {
        return getId() == SERVICE_REMOVED;
    }
    
}

