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
package net.sf.nmedit.nomad.core.jpf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.ServiceException;
import net.sf.nmedit.nomad.core.service.ServiceRegistry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;

public class JPFServiceInstallerTool
{
    
    private static transient Log _logger ;
    
    private static Log getLogger()
    {
        if (_logger == null)
            _logger = LogFactory.getLog(JPFServiceInstallerTool.class);
        
        return _logger;
    }

    public static final String SERVICE_IMPLEMENTATION_CLASS_KEY = "class";
    public static final String SERVICE_CLASS_KEY = "service";
    public static final String SERVICE_DESCRIPTION_KEY = "description";

    public static void activateAllServices(Plugin mainPlugin)
    {
        Log log = getLogger();

        if (log.isInfoEnabled())
        {
            log.info("Activating services...");
        }
        
        Map<String, Class<Service>> serviceClassCache = 
            new HashMap<String, Class<Service>>();

        ExtensionPoint serviceExtensionPoint = 
            mainPlugin.getDescriptor().getExtensionPoint("Service");
        
        Collection<Extension> connectedExtensions =
            serviceExtensionPoint.getConnectedExtensions();
        
        PluginManager manager = 
            mainPlugin.getManager();

        if (log.isInfoEnabled())
        {
            log.info("Connected extensions: "+connectedExtensions.size());
        }
        
        for (Extension extension : connectedExtensions)
        {
            ClassLoader pluginClassLoader =
                manager.getPluginClassLoader(extension.getDeclaringPluginDescriptor());
            
            activateService(log, serviceClassCache, extension, pluginClassLoader);
        }
        
    }

    private static void activateService(Log log, Map<String, Class<Service>> serviceClassCache, 
            Extension extension, ClassLoader pluginClassLoader)
    {
        String serviceClassName =
            extension.getParameter(SERVICE_CLASS_KEY).valueAsString();
        
        String implementationClassName =
            extension.getParameter(SERVICE_IMPLEMENTATION_CLASS_KEY).valueAsString();
        
        if (log.isInfoEnabled())
        {
            String description =
                extension.getParameter(SERVICE_DESCRIPTION_KEY).valueAsString();
            
            log.info("Service implementation / Service: "+implementationClassName
                    +" (description="+description+") / "+serviceClassName);
        }
        
        Class<Service> serviceClass;
        try
        {
            serviceClass =
                lookupServiceClass(serviceClassCache, serviceClassName, pluginClassLoader);
        }
        catch (ClassNotFoundException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn("Error loading service class: "+serviceClassName, e);
            }
            
            return ;
        }
        
        Class<Service> serviceImplementationClass;
        try
        {
            serviceImplementationClass =
                lookupServiceImplementationClass(serviceClass, implementationClassName, pluginClassLoader);
        }
        catch (ClassNotFoundException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn("Error loading service implementation class: "+implementationClassName, e);
            }
            return ;
        }
        
        Service serviceInstance;
        try
        {
            serviceInstance = serviceImplementationClass.newInstance();
        }
        catch (InstantiationException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn("Error instantiating service: "+serviceImplementationClass, e);
            }
            
            return;
        }
        catch (IllegalAccessException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn("Error instantiating service: "+serviceImplementationClass, e);
            }
            
            return;
        }
        
        try
        {
            ServiceRegistry.addService(serviceClass, serviceInstance);
        }
        catch (ServiceException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn("Error installing service: "+ serviceInstance, e);
            }
            
            return;
        }
    }
    
    @SuppressWarnings("unchecked")
    private static Class<Service> lookupServiceImplementationClass
        (Class<? extends Service> serviceClass, String serviceImplementationName, 
                ClassLoader loader) throws ClassNotFoundException
    {
        Class<?> _class = 
            loader.loadClass(serviceImplementationName);

        if (!serviceClass.isAssignableFrom(_class))
            throw new ClassCastException("Service class is not subclass of "+serviceClass+": "+_class);

        Class<Service> serviceImplementationClass 
            = (Class<Service>) _class;
        
        return serviceImplementationClass;
    }

    @SuppressWarnings("unchecked")
    private static Class<Service> lookupServiceClass(Map<String, 
            Class<Service>> serviceClassCache, String serviceClassName, 
            ClassLoader loader) throws ClassNotFoundException
    {
        Class<Service> serviceClass
            = serviceClassCache.get(serviceClassName);
        
        if (serviceClass != null)
            return serviceClass;
        
        Class<?> _class =
            Class.forName(serviceClassName);
        
        if (!Service.class.isAssignableFrom(_class))
            throw new ClassCastException("Service class is not subclass of "+Service.class+": "+_class);
        
        serviceClass = (Class<Service>) _class;
        
        serviceClassCache.put(serviceClassName, serviceClass);
        
        return serviceClass;
    }
    
}

