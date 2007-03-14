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
package net.sf.nmedit.jtheme.store;

import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.jtheme.store.helpers.ConnectorDescriptorHelper;

import org.jdom.Element;

public class ConnectorStore extends DefaultStore
{
    
    private ConnectorDescriptorHelper connectorDescriptorHelper;

    protected ConnectorStore(Element element)
    {
        super(element);
        connectorDescriptorHelper =
            ConnectorDescriptorHelper.createHelper(element.getChild("connector"));
    }

    public static ConnectorStore create(StorageContext context, Element element)
    {
        return new ConnectorStore(element);
    }
    
    @Override
    public JTComponent createComponent(JTContext context) throws JTException
    {
        JTComponent component = context.createComponent(JTContext.TYPE_CONNECTOR);
        applyLocation(component);
        applySize(component);
        return component;
    }

    protected void link(JTContext context, JTComponent component, Module module)
      throws JTException
    {
        Connector connector = connectorDescriptorHelper.lookup(module);
        if (connector != null)
        {
            JTConnector jtconnector = (JTConnector) component;
            jtconnector.setConnector(connector);
            jtconnector.setConnectorDescriptor(connector.getDescriptor());
        }
    }
    
}

