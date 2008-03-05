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

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Element;

public abstract class ControlElement extends AbstractElement implements Serializable
{

    protected String componentId;

    @Override
    protected void initElement(StorageContext context, Element e)
    {
        super.initElement(context, e);
        initLinks(e);
    }

    protected void initLinks(Element e)
    {
        componentId = lookupChildComponentId(e, "parameter");
    }
    
    protected void setParameter(JTControl control, PModuleDescriptor descriptor, PModule module)
    {
        if (componentId != null && module != null)
        {
            PParameterDescriptor parameterDescriptor = descriptor.getParameterByComponentId(componentId);
            PParameter parameter = null;
            try
            {
                parameter = module.getParameter(parameterDescriptor);
                control.setAdapter(new JTParameterControlAdapter(parameter));
                PParameter extension = parameter.getExtensionParameter();
                if (extension != null)
                    control.setExtensionAdapter(new JTParameterControlAdapter(extension));
            }
            catch (NullPointerException e)
            {
                if (parameterDescriptor != null && parameter != null)
                    throw e; // unexpected
                else
                {
                    // ignore (for now)
                }
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
        
}
