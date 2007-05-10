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
package net.sf.nmedit.jprotocol.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jprotocol.PRParameterDescriptor;

public class PRMessageDescriptorImpl implements
        net.sf.nmedit.jprotocol.PRMessageDescriptor
{

    private String name;
    private int id;
    private Map<String, PRParameterDescriptor> parameterMap;
    private List<PRParameterDescriptor> parameterList;
    private Map<String, Integer> parameterIndexMap;
    private transient Collection<PRParameterDescriptor> parameterCollection;
    
    public PRMessageDescriptorImpl(int id, String name)
    {
        this.id = id;
        this.name = name;
        init();
    }

    public PRParameterDescriptor getParameterAt(int index)
    {
        return parameterList.get(index);
    }
    
    public int getId()
    {
        return id;
    }
    
    public void addParameter(PRParameterDescriptor p)
    {
        parameterMap.put(p.getName(), p);

        int index = parameterList.size();
        parameterList.add(p);
        parameterIndexMap.put(p.getName(), index);
        parameterCollection = null;
    }
    
    private void init()
    {
        parameterMap = new HashMap<String, PRParameterDescriptor>();
        parameterIndexMap = new HashMap<String, Integer>();
        parameterList = new ArrayList<PRParameterDescriptor>();
    }

    public String getName()
    {
        return name;
    }
    
    public boolean containsParameter(String name)
    {
        return parameterMap.containsKey(name);
    }

    public PRParameterDescriptor getParameterByName(String name)
    {
        return parameterMap.get(name);
    }

    public int getParameterCount()
    {
        return parameterMap.size();
    }

    public Collection<PRParameterDescriptor> getParameters()
    {
        if (parameterCollection == null)
        {
            parameterCollection =
                Collections.unmodifiableCollection(parameterList);
        }
        return parameterCollection;
    }

    public int getParameterIndex(String paramName)
    {
        Integer index = parameterIndexMap.get(paramName);
        return index != null ? index.intValue() : -1;
    }

    public int getParameterIndex(PRParameterDescriptor parameter)
    {
        return getParameterIndex(parameter.getName());
    }

    public String toString()
    {
        return getClass().getName()+"[name="+name+",id="+id+",parameters="+getParameterCount()+"]";
    }
    
}
