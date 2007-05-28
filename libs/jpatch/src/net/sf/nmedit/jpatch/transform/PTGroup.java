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
package net.sf.nmedit.jpatch.transform;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.nmedit.jpatch.PModuleDescriptor;

public class PTGroup implements Iterable<PTModule>
{
    
    private Map<PModuleDescriptor, PTModule> modules = 
        new HashMap<PModuleDescriptor, PTModule>();

    public void add(PTModule module)
    {
        modules.put(module.getTarget(), module);
        module.setGroup(this);
    }

    public void remove (PTModule module)
    {
        modules.remove(module.getTarget());
        module.setGroup(null);
    }

    public PTModule getTransformable(PModuleDescriptor md)
    {
        for (PTModule t: this)
        {
            if (md == t.getTarget() || md.equals(t.getTarget()))
                return t;
        }
        return null;
    }

    public void remove (PModuleDescriptor module)
    {
        PTModule m = modules.remove(module);
        if (m != null) m.setGroup(null);
    }
    
    public PTModule get(PModuleDescriptor d)
    {
        return modules.get(d);
    }
    
    public int size()
    {
        return modules.size();
    }

    public Iterator<PTModule> iterator()
    {
        return modules.values().iterator();
    }

    public boolean contains(PModuleDescriptor module)
    {
        return modules.containsKey(module);
    }

    public boolean contains(PTModule module)
    {
        return contains(module.getTarget());
    }

    public boolean containsTarget(PModuleDescriptor m)
    {
        for (PTTransformable<?> t: this)
            if (m.equals(t.getTarget()))
                return true;
        return false;
    }
    
}
