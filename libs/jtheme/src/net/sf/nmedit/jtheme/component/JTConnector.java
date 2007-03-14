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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component;

import java.awt.Container;

import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.Signal;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.plaf.JTConnectorUI;

public class JTConnector extends JTComponent
{
 
    public static final String uiClassID = "connector";
    private Connector connector;
    private ConnectorDescriptor connectorDescriptor;
    private transient JTCableManager cableManager;

    public JTConnector(JTContext context)
    {
        super(context);
        setOpaque(false);
    }
    
    public void setUI(JTConnectorUI ui)
    {
        super.setUI(ui);
    }
    
    public String getUIClassID()
    {
        return uiClassID;
    }
    
    public Signal getSignal()
    {
        return connector != null ? connector.getSignal() : null;
    }
    
    public Signal getDefaultSignal()
    {
        return connector != null ? connector.getDefaultSignal() : null;
    }
    
    public void setConnector(Connector c)
    {
        if (this.connector != c)
        {
            this.connector = c;
            
            if (c != null)
                setConnectorDescriptor(c.getDescriptor());
            else
                setConnectorDescriptor(null);
        }
    }
    
    public Connector getConnector()
    {
        return connector;
    }

    public void setConnectorDescriptor(ConnectorDescriptor descriptor)
    {
        this.connectorDescriptor = descriptor;
    }
    
    public ConnectorDescriptor getConnectorDescriptor()
    {
        return connectorDescriptor;
    }
    
    public boolean isOutput()
    {
        ConnectorDescriptor descriptor = connectorDescriptor;
        return descriptor == null ? false : descriptor.isOutput();
    }
    
    public boolean isConnected()
    {
        return connector != null ? connector.isConnected() : false;
    }
    
    public JTCableManager getCableManager()
    {
        if (cableManager == null)
            cableManager = findCableManager();
        return cableManager;
    }

    private JTCableManager findCableManager()
    {
        Container c = getParent();
        if (c != null)
        {
            c = c.getParent();
            if (c != null && c instanceof JTModuleContainer)
            {
                return ((JTModuleContainer) c).getCableManager();
            }
        }
        return null;
    }
    
}
