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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.transformation.Group;
import net.sf.nmedit.jpatch.transformation.Transformable;
import net.sf.nmedit.jpatch.transformation.TransformableConnector;
import net.sf.nmedit.jpatch.transformation.TransformableModule;
import net.sf.nmedit.jpatch.transformation.TransformableParameter;

public class TransformableModuleImpl implements TransformableModule
{

    private Group group;
    private ModuleDescriptor target;
    private List<TransformableConnector> clist;
    private List<TransformableParameter> plist;
    private transient Map<String, TransformableParameter> pmap;
    private transient Map<String, TransformableConnector> cmap;

    public TransformableModuleImpl(Group group, ModuleDescriptor target)
    {
        this.group = group;
        this.target = target;
    }
    
    public Group getGroup()
    {
        return group;
    }
    
    public void add(Transformable t)
    {
        if (t instanceof TransformableConnector)
        {
            if (clist == null)
                clist = new LinkedList<TransformableConnector>();
            clist.add((TransformableConnector)t);
        }
        else if (t instanceof TransformableParameter)
        {
            if (plist == null)
                plist = new LinkedList<TransformableParameter>();
            plist.add((TransformableParameter)t);
        }
    }
    
    public boolean remove(Transformable t)
    {
        if (t instanceof TransformableConnector)
        {
            if (clist != null)
                return clist.remove(t);
        }
        else if (t instanceof TransformableParameter)
        {
            if (plist != null)
                return plist.remove(t);
        }
        return false;
    }

    public ModuleDescriptor getTarget()
    {
        return target;
    }

    public TransformableConnector getConnector(int index)
    {
        if (clist == null)
            throw new IndexOutOfBoundsException("index:"+index);
        
        return clist.get(index);
    }

    public int getConnectorCount()
    {
        return clist == null ? 0 : clist.size();
    }

    public TransformableParameter getParameter(int index)
    {
        if (plist == null)
            throw new IndexOutOfBoundsException("index:"+index);
        
        return plist.get(index);
    }

    public int getParameterCount()
    {
        return plist == null ? 0 : plist.size();
    }

    public TransformableConnector getConnectorById(String id)
    {
        if (clist == null)
            return null;
                
        if (cmap == null)
        {
            cmap = new HashMap<String, TransformableConnector>();
            for (TransformableConnector t: clist)
                cmap.put(t.getId(), t);
        }
        return cmap.get(id);
    }

    public TransformableParameter getParameterById(String id)
    {
        if (plist == null)
            return null;
                
        if (pmap == null)
        {
            pmap = new HashMap<String, TransformableParameter>();
            for (TransformableParameter t: plist)
                pmap.put(t.getId(), t);
        }
        return pmap.get(id);
    }

}
