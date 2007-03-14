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
package net.sf.nmedit.jtheme.store.helpers;

import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import org.jdom.Attribute;
import org.jdom.Element;

public final class ConnectorDescriptorHelper
{
    
    private static final ConnectorDescriptorHelper INVALID
        = new ConnectorDescriptorHelper(-1, true);

    private int connIndex;
    private boolean connIsOutput;
    
    private transient ModuleDescriptor cachedModuleDescriptor;
    private transient ConnectorDescriptor cachedConnectorDescriptor;

    public ConnectorDescriptorHelper(int connIndex, boolean connIsOutput)
    {
        this.connIndex = connIndex;
        this.connIsOutput = connIsOutput;
    }
    
    public int getConnectorIndex()
    {
        return connIndex;
    }
    
    public boolean isOutput()
    {
        return connIsOutput;
    }
    
    public boolean isValid()
    {
        return connIndex >= 0;
    }
    
    public ConnectorDescriptor lookup(ModuleDescriptor moduleDescriptor)
    {
        if (cachedModuleDescriptor != moduleDescriptor)
        {
            if (!isValid())
                return null;
            
            ConnectorDescriptor connectorDescriptor =
                moduleDescriptor.getConnector(connIndex, connIsOutput);
            cachedModuleDescriptor = moduleDescriptor;
            cachedConnectorDescriptor = connectorDescriptor;
        }
        return cachedConnectorDescriptor;
    }
    
    public Connector lookup(Module module)
    {
        ConnectorDescriptor descriptor = lookup(module.getDescriptor());
        if (descriptor == null)
            return null;

        try
        {
            return module.getConnector(descriptor);
        }
        catch (InvalidDescriptorException e)
        {
            return null;
        }
    }
    
    public static ConnectorDescriptorHelper invalidHelper()
    {
        return ConnectorDescriptorHelper.INVALID;
    }

    public static ConnectorDescriptorHelper createHelper(Element connectorElement)
    {
        if (connectorElement == null)
            return invalidHelper();
        
        // read the index
        Attribute indexAtt = connectorElement.getAttribute("index");
        
        if (indexAtt == null)
            return invalidHelper();
        
        int index = str2int(indexAtt.getValue());
        if (index<0)
            return invalidHelper();
            
        // read the output type
        Attribute connTypeAtt = connectorElement.getAttribute("type");
        if (connTypeAtt == null)
            return invalidHelper();
        String connType = connTypeAtt.getValue();
        
        boolean isOutput = connType.equals("output");
        if (!(isOutput || connType.equals("input")))
            return invalidHelper();
        
        return new ConnectorDescriptorHelper(index, isOutput);
    }

    private static int str2int(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (NullPointerException e)
        {
            return -1;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }
    
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof ConnectorDescriptorHelper))
            return false;
        
        ConnectorDescriptorHelper _o = (ConnectorDescriptorHelper) o;
        
        return (getConnectorIndex() == _o.getConnectorIndex())
                && (isOutput() == _o.isOutput());
    }
    
    public int hashCode()
    {
        return isOutput() ? (connIndex) : (connIndex+1000);
    }
    
}
