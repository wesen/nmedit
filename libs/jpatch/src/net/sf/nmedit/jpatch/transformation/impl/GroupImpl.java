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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.transformation.Group;
import net.sf.nmedit.jpatch.transformation.TransformableModule;

public class GroupImpl implements Group
{
    
    private List<TransformableModule> modules;
    
    public GroupImpl()
    {
        super();
        modules = new LinkedList<TransformableModule>();
    }
    
    public void add(TransformableModule t)
    {
        modules.add(t);
    }
    
    public boolean remove(TransformableModule t)
    {
        return modules.remove(t);
    }

    public TransformableModule getModule(int index)
    {
        return modules.get(index);
    }

    public int size()
    {
        return modules.size();
    }

    public Iterator<TransformableModule> iterator()
    {
        return modules.iterator();
    }

    public boolean contains(TransformableModule m)
    {
        return modules.contains(m);
    }

    public boolean containsTarget(ModuleDescriptor md)
    {
        return getTransformable(md) != null;
    }

    public TransformableModule getTransformable(ModuleDescriptor md)
    {
        for (TransformableModule t: this)
        {
            if (md == t.getTarget() || md.equals(t.getTarget()))
                return t;
        }
        return null;
    }

}
