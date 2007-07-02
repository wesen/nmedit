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

import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Element;

public class ConnectorElement extends AbstractElement implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 4299487398364650900L;
    protected String componentId;
    protected transient PConnectorDescriptor cachedConnectorDescriptor;

    public static AbstractElement createElement(StorageContext context, Element element)
    {
        ConnectorElement e = new ConnectorElement();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }
    
    @Override
    public JTComponent createComponent(JTContext context, PModuleDescriptor descriptor, PModule module)
        throws JTException
    {
        JTConnector jtConnector = 
            (JTConnector) context.createComponent(JTContext.TYPE_CONNECTOR);
        setBounds(jtConnector);

        PConnectorDescriptor connectorDescriptor = null;
        if (cachedConnectorDescriptor != null)
            connectorDescriptor = cachedConnectorDescriptor;
        else if (componentId != null)
            connectorDescriptor = cachedConnectorDescriptor
                = descriptor.getConnectorByComponentId(componentId);
        if (connectorDescriptor != null)
        {
            if (module != null)
            {
                PConnector connector = module.getConnector(connectorDescriptor);
                if (connector != null)
                {
                    jtConnector.setConnector(connector);
                    return jtConnector;
                }
            }
            jtConnector.setConnectorDescriptor(connectorDescriptor);
        }
        
        return jtConnector;
    }

    @Override
    protected void initElement(StorageContext context, Element e)
    {
        super.initElement(context, e);
        initLinks(e);
    }

    protected void initLinks(Element e)
    {
        componentId = lookupChildComponentId(e, "connector");
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
