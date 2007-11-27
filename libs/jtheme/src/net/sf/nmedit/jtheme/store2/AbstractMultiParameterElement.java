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
package net.sf.nmedit.jtheme.store2;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class AbstractMultiParameterElement extends AbstractElement
    implements Serializable
{

    protected transient String[] parameterElementNames;
    protected transient String[] componentIdList;
    protected BindParameterInfo bindings = null;
    
    public AbstractMultiParameterElement(String[] parameterElementNames)
    {
        this.parameterElementNames = parameterElementNames;
        componentIdList = new String[parameterElementNames.length];
    }
    
    public AbstractMultiParameterElement(Class<? extends JTComponent> jtclass)
    {
        BindParameterInfo info = BindParameterInfo.forClass(jtclass);
        this.bindings = info;
        parameterElementNames = new String[info.getAdapterCount()];
        componentIdList = new String[info.getAdapterCount()];
        
        Iterator<String> iter = info.parameters();
        int i=0;
        while (iter.hasNext())
            parameterElementNames[i++] = iter.next();
    }
    
    @Override
    protected void initElement(StorageContext context, Element e)
    {
        super.initElement(context, e);
        initComponentIdList(e);
    }

    protected void initComponentIdList(Element e)
    {
        List<?> children = e.getChildren();
        for (int i=children.size()-1;i>=0;i--)
        {
            Element p = (Element) children.get(i);
            for (int index=parameterElementNames.length-1;index>=0;index--)
            {
                String n = parameterElementNames[index];
                if (n.equals(p.getName()))
                {
                    Attribute a = p.getAttribute(ATT_COMPONENT_ID);
                    if (a != null)
                    {
                        componentIdList[index] = a.getValue();   
                    }
                    break;
                }
            }
        }
    }
    

    protected void link(JTComponent component, PModule module)
    {
        if (bindings == null)
            throw new UnsupportedOperationException();
        
        for (int i=0;i<parameterElementNames.length;i++)
        {
            String name = parameterElementNames[i];
            PParameter param = module.getParameterByComponentId(componentIdList[i]);
            if (param != null)
            {
                Method setter = bindings.getSetter(name);
                try
                {
                    setter.invoke(component, new Object[]{new JTParameterControlAdapter(param)});
                }
                catch (Exception e)
                {
                    // TODO log exception instead of stack trace
                    e.printStackTrace();
                }
            }
            else
            {
                // log: parameter not found
            }
        }
    }
    
    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
        
        out.writeInt(parameterElementNames.length);
        
        int size = 0;
        for (int i=0;i<parameterElementNames.length;i++)
        {
            String n = parameterElementNames[i];
            if (n!=null)
                size++;
        }
        
        out.writeInt(size);
        for (int i=0;i<parameterElementNames.length;i++)
        {
            String n = parameterElementNames[i];
            if (n != null)
            {
                out.writeInt(i);
                out.writeObject(n);
            }
        }
        
        size = 0;
        for (int i=0;i<componentIdList.length;i++)
        {
            String n = componentIdList[i];
            if (n!=null)
                size++;
        }

        out.writeInt(size);
        for (int i=0;i<componentIdList.length;i++)
        {
            String n = componentIdList[i];
            if (n != null)
            {
                out.writeInt(i);
                out.writeObject(n);
            }
        }
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        int size = in.readInt();
        
        parameterElementNames = new String[size];
        componentIdList = new String[size];

        size = in.readInt();
        for (int i=0;i<size;i++)
        {
            int index = in.readInt();
            String n = (String) in.readObject();
            parameterElementNames[index] = n;
        }
        
        size = in.readInt();
        for (int i=0;i<size;i++)
        {
            int index = in.readInt();
            String n = (String) in.readObject();
            componentIdList[index] = n;
        }
        
    }
}
