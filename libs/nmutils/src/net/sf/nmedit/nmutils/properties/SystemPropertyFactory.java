/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.nmutils.properties;

import java.util.Properties;

public class SystemPropertyFactory
{
    
    private static SystemPropertyFactory instance = new SystemPropertyFactory();
    
    private SystemPropertyFactory currentFactory = null;

    public static SystemProperties getProperties(Class<?> forClass)
    {
        return instance.getPropertiesForClass(forClass);
    }

    public SystemProperties getPropertiesForClass(Class<?> forClass)
    {
        if (currentFactory != null)
            return currentFactory.getPropertiesForClass(forClass);
        else
            return new RootSystemProperties(new Properties());
    }
    
    public static SystemPropertyFactory sharedInstance()
    {
        return instance;
    }
    
    public SystemPropertyFactory getFactory()
    {
        return currentFactory;
    }
    
    public void setFactory(SystemPropertyFactory factory)
    {
        this.currentFactory = factory;
    }
    
}
