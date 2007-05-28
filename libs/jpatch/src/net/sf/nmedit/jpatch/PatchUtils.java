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
package net.sf.nmedit.jpatch;

import java.util.Collection;
import java.util.LinkedList;


public class PatchUtils
{

    private static final int[] EMPTY_INTS = new int[0];

    public static int[] getParameterValues(PModule module)
    {
        int pcount = module.getParameterCount();
        if (pcount <= 0) return EMPTY_INTS;
        
        int[] values = new int[pcount];
        for (int i=pcount-1;i>=0;i--)
            values[i] = module.getParameter(i).getValue();
        return values;
    }
 
    public static int setParameterValues(PModule module, int[] values)
    {
        int count = Math.min(module.getParameterCount(), values.length);
        for (int i=count-1;i>=0;i--)
            module.getParameter(i).setValue(values[i]);
        return count;
    }
    
    public static int removeModules(PModuleContainer container, Collection<PModule> c)
    {
        int count = c.size();
        for (PModule PModule: c)
            container.remove(PModule);
        return count;
    }

    public static int removeUnusedModules(PModuleContainer container)
    {
        return removeModules(container, listUnusedModules(container));
    }

    public static Collection<PModule> listUnusedModules(PModuleContainer container)
    {
        return listUnusedModules(new LinkedList<PModule>(), container);
    }
    
    public static Collection<PModule> listUnusedModules(Collection<PModule> c, PModuleContainer container)
    {   
        if (container.getModuleCount() > 0)
        {
            for (PModule m: container)
            {
                if (isUnusedModule(m))
                    c.add(m);
            }
        }
        return c;
    }
    
    public static boolean hasUnusedModules(PModuleContainer container)
    {   
        if (container.getModuleCount() > 0)
        {
            for (PModule m: container)
                if (isUnusedModule(m))
                    return true;
        }
        return false;
    }

    private static boolean isUnusedModule(PModule m)
    {
        PModuleContainer container = m.getParentComponent();
        return container == null || (!container.getConnectionManager().isConnected(m));
    }
    
}
