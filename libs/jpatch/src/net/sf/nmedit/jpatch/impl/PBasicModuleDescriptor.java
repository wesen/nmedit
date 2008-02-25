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
package net.sf.nmedit.jpatch.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PLightDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;

/**
 * The reference implementation of interface {@link PModuleDescriptor}.
 * @author Christian Schneider
 */
public class PBasicModuleDescriptor extends PBasicDescriptor
implements PModuleDescriptor//, Serializable
{

    private static final long serialVersionUID = 5166147655672576230L;
    private List<PParameterDescriptor> parameters;
    private List<PConnectorDescriptor> connectors;
    private List<PLightDescriptor> lights;
    private transient Map<Object, PDescriptor> componentIdMap;
    private transient ModuleDescriptions modules;

    public PBasicModuleDescriptor(ModuleDescriptions modules, String name, Object componentId)
    {
        this(modules, name, componentId, false);
    }

    public void setModuleDescriptions(ModuleDescriptions modules)
    {
        this.modules = modules;
    }
    
    public PBasicModuleDescriptor(ModuleDescriptions modules, String name, Object componentId, boolean setComponents)
    {
        super(name, componentId);
        this.modules = modules;
        if (!setComponents)
        {
            parameters = new ArrayList<PParameterDescriptor>(1);
            connectors = new ArrayList<PConnectorDescriptor>(1);
            lights = new ArrayList<PLightDescriptor>(0);
        }
    }

    public void setParameters(Collection<PParameterDescriptor> c)
    {
        parameters = new ArrayList<PParameterDescriptor>(c);
        for (int i=parameters.size()-1;i>=0;i--)
            parameters.get(i).setDescriptorIndex(i);
    }

    public void setConnectors(Collection<PConnectorDescriptor> c)
    {
        connectors = new ArrayList<PConnectorDescriptor>(c);
        for (int i=connectors.size()-1;i>=0;i--)
            connectors.get(i).setDescriptorIndex(i);
    }

    public void setLights(Collection<PLightDescriptor> c)
    {
        lights = new ArrayList<PLightDescriptor>(c);
        for (int i=lights.size()-1;i>=0;i--)
            lights.get(i).setDescriptorIndex(i);
    }
    
    public ModuleDescriptions getModules()
    {
        return modules;
    }

    public void addLightDescriptor(PLightDescriptor descriptor)
    {
        int index = lights.size();
        lights.add(index, descriptor);
        descriptor.setDescriptorIndex(index);
    }

    public void addParameterDescriptor(PParameterDescriptor descriptor)
    {
        int index = parameters.size();
        parameters.add(index, descriptor);
        descriptor.setDescriptorIndex(index);
    }

    public void addConnectorDescriptor(PConnectorDescriptor descriptor)
    {
        int index = connectors.size();
        connectors.add(index, descriptor);
        descriptor.setDescriptorIndex(index);
    }

    public PLightDescriptor getLightDescriptor(int index)
    {
        return lights.get(index);
    }

    public int getLightDescriptorCount()
    {
        return lights.size();
    }
    
    public PConnectorDescriptor getConnectorDescriptor(int index)
    {
        return connectors.get(index);
    }

    public int getConnectorDescriptorCount()
    {
        return connectors.size();
    }

    public PParameterDescriptor getParameterDescriptor(int index)
    {
        return parameters.get(index);
    }

    public int getParameterDescriptorCount()
    {
        return parameters.size();
    }

    public int getLimit()
    {
        return getIntAttribute("limit", -1);
    }

    public PDescriptor getComponentByComponentId(Object id)
    {
        if (componentIdMap == null)
        {
            int dc = getParameterDescriptorCount()+getConnectorDescriptorCount()+getLightDescriptorCount();
            componentIdMap = new HashMap<Object, PDescriptor>(dc*2);
            for (PDescriptor d: parameters) putComponentInMap(d);
            for (PDescriptor d: connectors) putComponentInMap(d);
            for (PDescriptor d: lights) putComponentInMap(d);
        }
        return componentIdMap.get(id);
    }

    private void putComponentInMap(PDescriptor c)
    {
        componentIdMap.put(c.getComponentId(), c);
    }

    public String getCategory()
    {
        return getStringAttribute("category");
    }

    public boolean isInstanciable()
    {
        return getBooleanAttribute("instantiable", true);
    }
/*
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        
        Map<Object, Object> extensions = null;
        for (PParameterDescriptor parameter: parameters)
        {
            if (parameter instanceof PBasicParameterDescriptor)
            {
                PParameterDescriptor extension = parameter.getExtensionDescriptor();
                if (extension != null)
                {
                    if (extensions == null)
                        extensions = new HashMap<Object, Object>();
                    extensions.put(parameter.getComponentId(), extension.getComponentId());
                }
            }
        }
        if (extensions == null)
            out.writeInt(0); // size
        else
        {
            out.writeInt(extensions.size());
            for (Entry<Object, Object> e: extensions.entrySet())
            {
                out.writeObject(e.getKey());
                out.writeObject(e.getValue());
            }
        }
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        
        int extSize = in.readInt(); // extensions
        
        for (int i=0;i<extSize;i++)
        {
            Object src = in.readObject();
            Object dst = in.readObject();

            PParameterDescriptor psrc = getParameterByComponentId(src);
            PParameterDescriptor pdst = getParameterByComponentId(dst);
            ((PBasicParameterDescriptor)psrc).setExtensionDescriptor(pdst);
        }

        for (PLightDescriptor d: lights)
        {
            if (d instanceof PBasicLightDescriptor)
                ((PBasicLightDescriptor)d).setParent(this);
        }
        for (PParameterDescriptor d: parameters)
        {
            if (d instanceof PBasicParameterDescriptor)
                ((PBasicParameterDescriptor)d).setParent(this);
        }
        for (PConnectorDescriptor d: connectors)
        {
            if (d instanceof PBasicConnectorDescriptor)
                ((PBasicConnectorDescriptor)d).setParent(this);
        }
    }
*/
}
