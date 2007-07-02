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

import java.util.HashMap;
import java.util.Map;

public class GlobalRegistry
{

    private static GlobalRegistry instance = new GlobalRegistry();
    
    private GlobalRegistry()
    {
        
    }

    public static GlobalRegistry getInstance()
    {
        return instance;
    }

    private Map<Class<?>, Registry<?>>
        registryMap = new HashMap<Class<?>, Registry<?>>();
    
    @SuppressWarnings("unchecked")
    public <T> Registry<T> getRegistry(Class<T> clazz)
    {
        return (Registry<T>) registryMap.get(clazz);
    }
    
    public <T> void put(Class<T> clazz, Registry<T> registry)
    {
        registryMap.put(clazz, registry);
    }
    
}
