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
import java.util.NoSuchElementException;

import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;

public class PTModule extends PTTransformable<PModuleDescriptor>
    implements Iterable<PTTransformable<? extends PDescriptor>>
{

    
    private PTGroup group;
    private Map<PDescriptor, PTTransformable<? extends PDescriptor>> map;
    private Map<String, PTTransformable<? extends PDescriptor>> nameMap;
    
    public PTModule(PModuleDescriptor target)
    {
        super(null, target);
        map = new HashMap<PDescriptor, PTTransformable<? extends PDescriptor>>();
        nameMap = new HashMap<String, PTTransformable<? extends PDescriptor>>();
    }
    
    void setGroup(PTGroup g)
    {
        this.group = g;
    }
    
    public PTGroup getGroup()
    {
        return group;
    }

    public <T extends PDescriptor> void add(PTTransformable<T> t)
    {
        map.put(t.getTarget(), t);
        nameMap.put(t.getName(), t);
    }

    public PTTransformable<? extends PDescriptor> getDescriptor(String name)
    {
        return nameMap.get(name);
    }

    @SuppressWarnings("unchecked")
    public PTTransformable<PConnectorDescriptor> getConnectorByName(String name)
    {
        PTTransformable<? extends PDescriptor> t = getDescriptor(name);
        return (t != null && t.getTarget() instanceof PConnectorDescriptor) ? 
                (PTTransformable) t : null;
    }

    @SuppressWarnings("unchecked")
    public PTTransformable<PParameterDescriptor> getParameterByName(String name)
    {
        PTTransformable<? extends PDescriptor> t = getDescriptor(name);
        return (t != null && t.getTarget() instanceof PParameterDescriptor) ? 
                (PTTransformable) t : null;
    }
    
    public <T extends PDescriptor> PTTransformable<T> get(T descriptor)    
    {
        PTTransformable<? extends PDescriptor> t = map.get(descriptor);
        return (t!=null&&t.getTarget()==descriptor)?(PTTransformable<T>) t : null;
    }

    public Iterator<PTTransformable<? extends PDescriptor>> iterator()
    {
        return map.values().iterator();
    }

    public Iterator<PTTransformable<PConnectorDescriptor>> connectors()
    {
        return transformables(PConnectorDescriptor.class);
    }

    public Iterator<PTTransformable<PParameterDescriptor>> parameters()
    {
        return transformables(PParameterDescriptor.class);
    }
    
    public <T extends PDescriptor> Iterator<PTTransformable<T>> transformables(final Class<T> clazz)
    {
        return new Iterator<PTTransformable<T>>()
        {

            Iterator<PTTransformable<?extends PDescriptor>> iter = map.values().iterator();
            
            PTTransformable<T> next;
            
            @SuppressWarnings("unchecked")
            void align()
            {
                if (next == null)
                {
                    while (iter.hasNext())
                    {
                        PTTransformable<? extends PDescriptor> t = iter.next();
                        if (clazz.isInstance(t.getTarget()))
                        {
                            next = (PTTransformable) t;
                            break;
                        }
                    }
                }
            }
            
            public boolean hasNext()
            {
                align();
                return next!=null;
            }

            public PTTransformable<T> next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                PTTransformable<T> r = next;
                next = null;
                return r;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
            
        };
    }
    
}
