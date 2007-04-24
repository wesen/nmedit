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

/*
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch.spec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.ImageSource;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.ParameterDescriptor;
import net.sf.nmedit.nmutils.collections.EmptyIterator;

public class DefaultModuleDescriptor extends BasedDescriptor implements ModuleDescriptor
{
    private static final ConnectorDescriptor[] NO_CONNECTORS = new ConnectorDescriptor[0];
    private static final ParameterDescriptor[] NO_PARAMETERS = new ParameterDescriptor[0];

    private ConnectorDescriptor[] connectors = NO_CONNECTORS;
    private ParameterDescriptor[] parameters = NO_PARAMETERS;
    private String name; 
    private String displayName;
    private int index;
    private Map<String,ImageSource> images = null;
    private ModuleDescriptions md;

    public DefaultModuleDescriptor(ModuleDescriptions md, String name, int index)
    {
        this.md = md;
        this.name = name;
        this.displayName = name;
        this.index = index;
    }

    public ModuleDescriptions getModuleDescriptions()
    {
        return md;
    }
    
    void setConnectors(ConnectorDescriptor[] connectors)
    {
        this.connectors = connectors;
    }

    void setParameters(ParameterDescriptor[] parameters)
    {
        this.parameters = parameters;
    }

    public int getConnectorCount()
    {
        return connectors.length;
    }

    public ConnectorDescriptor getConnectorDescriptor( int index )
    {
        return connectors[index];
    }

    public int getParameterCount()
    {
        return parameters.length;
    }

    public ParameterDescriptor getParameterDescriptor( int index )
    {
        return parameters[index];
    }

    public ModuleDescriptor getSourceDescriptor()
    {
        return null;
    }

    public String getComponentName()
    {
        return name;
    }

    public String getName()
    {
        return getClass().getName();
    }
    
    public String toString()
    {
        return getClass().getName()+"[name="+name+",index="+index+",connectors="+getConnectorCount()+",parameters="+getParameterCount()+"]";
    }

    private Map<String, ParameterDescriptor[]> paramByClass = null;
    private String category;
    private String moduleClass;
    
    public ParameterDescriptor[] getParameterDescriptorList( String parameterClass )
    {
        ParameterDescriptor[] result = null;
        
        if (paramByClass != null)
        {
            result = paramByClass.get(parameterClass);
            if (result != null)
            {
                return result != NO_PARAMETERS ? result.clone() : NO_PARAMETERS;
            }
        }
        else
        {
            paramByClass = new HashMap<String, ParameterDescriptor[]>();
        }
        
        List<ParameterDescriptor> paramList = null;
        for (int i=0;i<parameters.length;i++)
        {
            Object n = parameters[i].getParameterClass();
            if (parameterClass.equals(n))
            {
                if (paramList == null)
                    paramList = new ArrayList<ParameterDescriptor>(parameters.length);
                paramList.add(parameters[i]);
            }
        }
        
        result = (paramList == null) ? NO_PARAMETERS : paramList.toArray(new ParameterDescriptor[paramList.size()]);
        paramByClass.put(parameterClass, result);
        return result;
    }

    public ConnectorDescriptor[] getConnectorDescriptorList( String connectorClass )
    {
        List<ConnectorDescriptor> list = null;
        for (int i=0;i<connectors.length;i++)
        {
            Object n = connectors[i].getConnectorClass();
            if (n == connectorClass || (n instanceof String && n.equals(connectorClass)))
            {
                if (list == null)
                    list = new ArrayList<ConnectorDescriptor>(connectors.length);
                list.add(connectors[i]);
            }
        }

        if (list == null)
            return NO_CONNECTORS;
        else
        {
            return list.toArray(new ConnectorDescriptor[list.size()]);
        }
    }

    public int getIntegerAttribute( String aname, int defaultValue )
    {
        Object att = getAttribute(aname);
        if (att instanceof Integer)
            return ((Integer) att).intValue();
        return defaultValue;
    }

    public float getFloatAttribute( String aname, float defaultValue )
    {
        Object att = getAttribute(aname);
        if (att instanceof Float)
            return ((Float) att).floatValue();
        return defaultValue;
    }

    public double getDoubleAttribute( String aname, double defaultValue )
    {
        Object att = getAttribute(aname);
        if (att instanceof Double)
            return ((Double) att).doubleValue();
        return defaultValue;
    }

    public boolean getBooleanAttribute( String aname, boolean defaultValue )
    {
        Object att = getAttribute(aname);
        if (att instanceof Boolean)
            return ((Boolean) att).booleanValue();
        return defaultValue;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    public String getDisplayName()
    {
        return displayName;
    }
    
    public int getIndex()
    {
        return index;
    }

    public void putImage(String key, ImageSource image)
    {
        if (images == null)
            images = new HashMap<String,ImageSource>();
        images.put(key, image);
    }
    
    public ImageSource get16x16Icon()
    {
        return getImage("icon16x16");
    }
    
    public ImageSource get32x32Icon()
    {
        return getImage("icon32x32");
    }
    
    public ImageSource getImage( String key )
    {
        return images != null ? images.get(key) : null;
    }

    public Iterator<ImageSource> getImages()
    {
        return images != null ? images.values().iterator() : new EmptyIterator<ImageSource>();
    }

    public void setCategory( String cat )
    {
        this.category = cat;
    }
    
    public String getCategory()
    {
        return category;
    }

    public void setModuleClass( String mclass )
    {
        this.moduleClass = mclass;
    }
    
    public String getModuleClass()
    {
        return moduleClass;
    }
    
    public ParameterDescriptor getParameter(int index)
    {
        for (int i=parameters.length-1;i>=0;i--)
        {
            ParameterDescriptor p = parameters[i];
            if (p.getIndex() == index)
                return p;
        }
        return null;
    }
    
    public ParameterDescriptor getParameter(int index, String pclass)
    {
        for (int i=parameters.length-1;i>=0;i--)
        {
            ParameterDescriptor p = parameters[i];
            if (p.getIndex()==index && pclass.equals(p.getParameterClass()))
                return p;
        }
        return null;
    }
    
    public ConnectorDescriptor getConnector(int index)
    {
        for (int i=connectors.length-1;i>=0;i--)
        {
            ConnectorDescriptor c = connectors[i];
            if (c.getIndex() == index)
                return c;
        }
        return null;
    }
    
    public ConnectorDescriptor getConnector(int index, String cclass)
    {
        for (int i=connectors.length-1;i>=0;i--)
        {
            ConnectorDescriptor c = connectors[i];
            if (c.getIndex() == index && cclass.equals(c.getConnectorClass()))
                return c;
        }
        return null;
    }

    public ConnectorDescriptor getConnector( int index, boolean output )
    {
        for (int i=connectors.length-1;i>=0;i--)
        {
            ConnectorDescriptor c = connectors[i];
            if ((c.isOutput()==output)&& (c.getIndex() == index))
                return c;
        }
        return null;
    }
    
}
