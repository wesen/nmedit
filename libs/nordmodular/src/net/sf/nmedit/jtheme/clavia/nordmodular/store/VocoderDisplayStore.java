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
package net.sf.nmedit.jtheme.clavia.nordmodular.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.VocoderDisplay;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store2.AbstractElement;
import net.sf.nmedit.jtheme.store2.ComponentElement;

import org.jdom.Element;

public class VocoderDisplayStore extends AbstractElement implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 3348068852175373870L;

    public static final String ATT_BAND = "band";
    
    private transient String[] componentIdList = new String[16];

    public static ComponentElement createElement(StorageContext context, Element element)
    {
        VocoderDisplayStore e = new VocoderDisplayStore();
        e.initElement(context, element);
        e.initComponentIdList(element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    public VocoderDisplay createComponent(JTContext context, PModuleDescriptor descriptor, PModule module)
        throws JTException
    {
        VocoderDisplay component = (VocoderDisplay) context.createComponentInstance(VocoderDisplay.class);
        setName(component);
        setBounds(component);
        link(component, module);
        return component; 
    }

    protected void initComponentIdList(Element e)
    {
        List<?> children = e.getChildren();
        for (int i=children.size()-1;i>=0;i--)
        {
            Element p = (Element) children.get(i);
            if ("parameter".equals(p.getName()))
            {
                int band = parseInt(p.getAttributeValue(ATT_BAND), -1);
                componentIdList[band] = p.getAttributeValue(ATT_COMPONENT_ID);
            }
        }
    }

    protected void link(VocoderDisplay disp, PModule module)
    {
        for (int i=componentIdList.length-1;i>=0;i--)
        {
            PParameter p = module.getParameterByComponentId(componentIdList[i]);
            if (p != null) disp.setBandAdapter(i, new JTParameterControlAdapter(p));
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
        
        out.writeInt(componentIdList.length);
        
        int size = 0;
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
        
        componentIdList = new String[size];

        size = in.readInt();
        for (int i=0;i<size;i++)
        {
            int index = in.readInt();
            String n = (String) in.readObject();
            componentIdList[index] = n;
        }
        
    }
    
}
