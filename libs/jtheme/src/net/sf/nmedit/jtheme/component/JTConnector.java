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
import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PSignal;
import net.sf.nmedit.jpatch.event.PConnectorListener;
import net.sf.nmedit.jpatch.event.PConnectorStateEvent;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.plaf.JTConnectorUI;

public class JTConnector extends JTComponent implements PConnectorListener
{
 
    /**
     * 
     */
    private static final long serialVersionUID = -2055932385325208113L;
    public static final String uiClassID = "connector";
    private PConnector connector;
    private PConnectorDescriptor descriptor;
    private transient JTCableManager cableManager;

    public JTConnector(JTContext context)
    {
        super(context);
        setOpaque(true);
    }
    
    public void setUI(JTConnectorUI ui)
    {
        super.setUI(ui);
    }
    
    public String getUIClassID()
    {
        return uiClassID;
    }
    
    public PSignal getSignal()
    {
        return descriptor != null ? descriptor.getDefaultSignalType() : null;
    }
    
    public PSignal getDefaultSignal()
    {
        return descriptor != null ? descriptor.getDefaultSignalType() : null;
    }
    
    public void setConnector(PConnector c)
    {
        if (this.connector != c)
        {
            if (this.connector != null)
            {
                this.connector.removeConnectorListener(this);
            }
            
            this.connector = c;
            
            if (c != null)
            {
                c.addConnectorListener(this);
                setConnectorDescriptor(c.getDescriptor());
                setToolTipText(c.getName());
            }
            else
            {
                setConnectorDescriptor(null);
                setToolTipText(null);
            }
            repaint();
        }
    }
    
    public PConnector getConnector()
    {
        return connector;
    }

    public void setConnectorDescriptor(PConnectorDescriptor descriptor)
    {
        if (this.descriptor != descriptor)
        {
            this.descriptor = descriptor;
            if (descriptor != null)
            {
                setOpaque(descriptor.isOutput());
            }
            else
            {
                setOpaque(true);
            }
        }
    }
    
    public PConnectorDescriptor getConnectorDescriptor()
    {
        return descriptor;
    }
    
    public boolean isOutput()
    {
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

    public void connectorStateChanged(PConnectorStateEvent e)
    {
        repaint();
    }
    
    public static final Cable[] NO_CABLES = new Cable[0];
    
    public Cable[] getConnectedCables()
    {
        JTCableManager cableManager = getCableManager();
        List<Cable> cableList = new ArrayList<Cable>();
        for (Cable cable : cableManager)
        {
            if (cable.getSourceComponent() == this || cable.getDestinationComponent()==this)
                cableList.add(cable);
        }
        if (cableList.isEmpty())
            return NO_CABLES;
        
        return cableList.toArray(new Cable[cableList.size()]);
    }


    
}
