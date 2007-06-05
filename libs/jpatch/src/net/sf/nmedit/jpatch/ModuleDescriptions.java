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
package net.sf.nmedit.jpatch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.formatter.FICType;
import net.sf.nmedit.jpatch.formatter.FormatterRegistry;
import net.sf.nmedit.jpatch.transform.PTTransformations;

public class ModuleDescriptions implements Iterable<PModuleDescriptor>
{

    private PSignalTypes signalTypes;
    private Map<String,PTypes<PType>> types = new HashMap<String, PTypes<PType>>();
    private FormatterRegistry formatterRegistry = new FormatterRegistry();
    private Map<Object, PModuleDescriptor> moduleMap = new HashMap<Object, PModuleDescriptor>(127);
    private ClassLoader loader;
    protected PTTransformations transformations;
    protected PModuleMetrics metrics; 
    
    public ModuleDescriptions()
    {
        formatterRegistry.install(new FICType(this));
    }
    
    public PModuleMetrics getMetrics()
    {
        return metrics;
    }
    
    public PTTransformations getTransformations()
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

    public PSignalTypes getDefinedSignals()
    {
        return signalTypes;
    }
    
    public int getModuleCount()
    {
        return moduleMap.size();
    }
    
    public void add( PModuleDescriptor module )
    {
        moduleMap.put(module.getComponentId(), module);
    }
    
    public PModuleDescriptor getModuleById(Object id)
    {
        return moduleMap.get(id);
    }
    
    public void setSignals( PSignalTypes signalTypes )
    {
        this.signalTypes = signalTypes;
    }

    public void addType( PTypes<PType> type )
    {
        types.put(type.getName(), type);
    }
    
    public FormatterRegistry getFormatterRegistry()
    {
        return formatterRegistry;
    }

    public PTypes<PType> getType( String name )
    {
        return types.get(name);
    }
    
    public PModuleDescriptor[] modulesByCategory(String categoryName)
    {
        List<PModuleDescriptor> modules = null;
        for (PModuleDescriptor mod : this)
        {
            if (categoryName.equals(mod.getCategory()))
            {
                if (modules == null)
                    modules = new LinkedList<PModuleDescriptor>();
                modules.add(mod);
            }
        }
        if (modules == null || modules.isEmpty())
            return new PModuleDescriptor[0];
        
        return modules.toArray(new PModuleDescriptor[modules.size()]);
    }

    public Iterator<PModuleDescriptor> iterator()
    {
        return moduleMap.values().iterator();
    }


    public void setTransformations(PTTransformations t)
    {
        this.transformations = t;
    }
    
}
