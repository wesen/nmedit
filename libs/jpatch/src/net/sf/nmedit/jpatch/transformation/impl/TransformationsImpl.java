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
package net.sf.nmedit.jpatch.transformation.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.transformation.Group;
import net.sf.nmedit.jpatch.transformation.TransformTool;
import net.sf.nmedit.jpatch.transformation.Transformations;

public class TransformationsImpl implements Transformations
{
    
    private List<Group> groups;
    private transient Map<ModuleDescriptor, Collection<Group>> targetMap;
    
    public TransformationsImpl()
    {
        groups = new LinkedList<Group>();
    }
    
    public void add(Group g)
    {
        groups.add(g);
    }
    
    public boolean remove(Group g)
    {
        return groups.remove(g);
    }

    public Group getGroup(int index)
    {
        return groups.get(index);
    }

    public int getGroupCount()
    {
        return groups.size();
    }

    public Iterator<Group> iterator()
    {
        return groups.iterator();
    }

    public Collection<Group> getTargets(ModuleDescriptor source)
    {
        if (targetMap == null)
            targetMap = new HashMap<ModuleDescriptor, Collection<Group>>();
        
        Collection<Group> c = targetMap.get(source);
        if (c == null)
        {
            List<Group> list = new LinkedList<Group>();
            buildTargets(list, source);
            c = Collections.<Group>unmodifiableList(list);
            targetMap.put(source, c);
        }
        
        return c;
    }

    private void buildTargets(Collection<Group> c, ModuleDescriptor source)
    {
        for (Group group: this)
        {
            if (group.containsTarget(source))
            {
                c.add(group);
            }
        }
    }

    public TransformTool createTransformation(ModuleDescriptor source)
    {
        return new DefaultTransformationToolImpl(this, source);
    }

}
