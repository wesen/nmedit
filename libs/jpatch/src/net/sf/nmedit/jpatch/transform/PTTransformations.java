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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.PModuleDescriptor;

public class PTTransformations
    implements Iterable<PTGroup>
{
    
    private List<PTGroup> groups = 
        new ArrayList<PTGroup>();
    private transient Map<PModuleDescriptor, Collection<PTGroup>> targetMap;

    public void add(PTGroup group)
    {
        groups.add(group);
    }
    
    public int size()
    {
        return groups.size();
    }
    
    public PTGroup remove(int index)
    {
        return groups.remove(index);
    }
    
    public PTGroup get(int index)
    {
        return groups.get(index);
    }

    public Iterator<PTGroup> iterator()
    {
        return groups.iterator();
    }

    public Collection<PTGroup> getTargets(PModuleDescriptor source)
    {
        if (targetMap == null)
            targetMap = new HashMap<PModuleDescriptor, Collection<PTGroup>>();
        
        Collection<PTGroup> c = targetMap.get(source);
        if (c == null)
        {
            List<PTGroup> list = new LinkedList<PTGroup>();
            buildTargets(list, source);
            c = Collections.<PTGroup>unmodifiableList(list);
            targetMap.put(source, c);
        }
        
        return c;
    }

    private void buildTargets(Collection<PTGroup> c, PModuleDescriptor source)
    {
        for (PTGroup group: this)
        {
            if (group.contains(source))
            {
                c.add(group);
            }
        }
    }

    public TransformTool createTransformation(PModuleDescriptor source)
    {
        return new TransformTool(this, source);
    }

}
