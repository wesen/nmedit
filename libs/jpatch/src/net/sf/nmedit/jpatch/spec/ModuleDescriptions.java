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
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch.spec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.spec.formatter.FICType;
import net.sf.nmedit.jpatch.spec.formatter.FormatterRegistry;
import net.sf.nmedit.jpatch.transformation.Transformations;
import net.sf.nmedit.nmutils.collections.ArrayMap;

public class ModuleDescriptions implements Iterable<ModuleDescriptor>
{

    private Signal signals;
    private Map<String,Type> types = new HashMap<String, Type>();
    private FormatterRegistry formatterRegistry = new FormatterRegistry();
    private ArrayMap<ModuleDescriptor> moduleMap = new ArrayMap<ModuleDescriptor>();
    private ClassLoader loader;
    protected Transformations transformations;
    
    public ModuleDescriptions()
    {
        formatterRegistry.install(new FICType(this));
    }
    
    public Transformations getTransformations()
    {
        return transformations;
    }
    
    public ClassLoader getModuleDescriptionsClassLoader()
    {
        if (loader == null)
            return getClass().getClassLoader();
        
        return loader;
    }
    
    public void setModuleDescriptionsClassLoader(ClassLoader loader)
    {
        this.loader = loader;
    }

    public Signal getDefinedSignals()
    {
        return signals;
    }
    
    public int getModuleCount()
    {
        return moduleMap.size();
    }
    
    public void add( ModuleDescriptor module )
    {
        moduleMap.put(module.getIndex(), module);
    }
    
    public ModuleDescriptor get(int index)
    {
        return moduleMap.get(index);
    }

    public void setSignals( Signal signals )
    {
        this.signals = signals;
    }

    public void addType( Type type )
    {
        types.put(type.getName(), type);
    }
    
    public FormatterRegistry getFormatterRegistry()
    {
        return formatterRegistry;
    }

    public Type getType( String name )
    {
        return types.get(name);
    }
    
    public ModuleDescriptor[] modulesByCategory(String categoryName)
    {
        List<ModuleDescriptor> modules = null;
        for (ModuleDescriptor mod : moduleMap)
        {
            if (categoryName.equals(mod.getCategory()))
            {
                if (modules == null)
                    modules = new LinkedList<ModuleDescriptor>();
                modules.add(mod);
            }
        }
        if (modules == null || modules.isEmpty())
            return new ModuleDescriptor[0];
        
        return modules.toArray(new ModuleDescriptor[modules.size()]);
    }

    public Iterator<ModuleDescriptor> iterator()
    {
        return moduleMap.iterator();
    }
    
}
