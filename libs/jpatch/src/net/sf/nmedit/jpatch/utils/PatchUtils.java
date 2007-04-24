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
package net.sf.nmedit.jpatch.utils;

import java.util.Collection;
import java.util.LinkedList;

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleContainer;

public class PatchUtils
{

    public static int removeModules(ModuleContainer container, Collection<Module> c)
    {
        int count = c.size();
        for (Module module: c)
            container.remove(module);
        return count;
    }

    public static int removeUnusedModules(ModuleContainer container)
    {
        return removeModules(container, listUnusedModules(container));
    }

    public static Collection<Module> listUnusedModules(ModuleContainer container)
    {
        return listUnusedModules(new LinkedList<Module>(), container);
    }
    
    public static Collection<Module> listUnusedModules(Collection<Module> c, ModuleContainer container)
    {   
        if (container.getModuleCount() > 0)
        {
            for (Module m: container)
            {
                if (isUnusedModule(m))
                    c.add(m);
            }
        }
        return c;
    }
    
    public static boolean hasUnusedModules(ModuleContainer container)
    {   
        if (container.getModuleCount() > 0)
        {
            for (Module m: container)
                if (isUnusedModule(m))
                    return true;
        }
        return false;
    }

    private static boolean isUnusedModule(Module m)
    {
        ModuleContainer container = m.getParent();
        
        if (container == null)
            return true;
        
        return !container.getConnectionManager().isConnected(m, false);
    }
    
}
