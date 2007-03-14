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
package net.sf.nmedit.jpatch;

import java.awt.Point;
import java.util.Iterator;

import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.event.ModuleListener;

public class DefaultModule implements Module
{
    
    private ModuleDescriptor moduleDescriptor;

    private Parameter[] parameterList;

    public DefaultModule(ModuleDescriptor moduleDescriptor)
    {
        this.moduleDescriptor = moduleDescriptor;
        
        parameterList = new Parameter[moduleDescriptor.getParameterCount()];
        
        for (int i=0;i<moduleDescriptor.getParameterCount();i++)
        {
            ParameterDescriptor pd =
                moduleDescriptor.getParameter(i);
            parameterList[i] = new PImpl(this,pd);
        }
    }
    
    private static class PImpl extends DefaultParameter
    {

        private ParameterDescriptor pd;
        private Module mod;

        public PImpl(Module mod, ParameterDescriptor pd)
        {
            this.mod = mod;
            this.pd = pd;
        }
        
        public ParameterDescriptor getDescriptor()
        {
            return pd;
        }

        public Module getOwner()
        {
            return mod;
        }
        
    }

    public void addModuleListener(ModuleListener l)
    {
        
    }

    public Connector getConnector(ConnectorDescriptor descriptor)
            throws InvalidDescriptorException
    {
        throw new UnsupportedOperationException();
    }

    public int getConnectorCount()
    {
        return 0;
    }

    public ModuleDescriptor getDescriptor()
    {
        return moduleDescriptor;
    }

    public Parameter getParameter(ParameterDescriptor descriptor)
            throws InvalidDescriptorException
    {
        int index = descriptor.getIndex();
        
        if (index>=0 && index<parameterList.length)
        {
            Parameter p = parameterList[index];
            if (p.getDescriptor().equals(descriptor))
                return p;
        }
        throw new InvalidDescriptorException();
    }

    public int getParameterCount()
    {
        return getDescriptor()
        .getParameterCount();
    }

    public ModuleContainer getParent()
    {
        return null;
    }

    public Point getScreenLocation()
    {
        return new Point(getScreenX(), getScreenY());
    }

    public int getScreenX()
    {
        return 0;
    }

    public int getScreenY()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean hasConnections()
    {
        return false;
    }

    public boolean hasOutgoingConnections()
    {
        return false;
    }

    public void removeConnections()
    {
        throw new UnsupportedOperationException();
    }

    public void removeModuleListener(ModuleListener l)
    {
        // TODO Auto-generated method stub

    }

    public void setScreenLocation(int x, int y)
    {
        // TODO Auto-generated method stub

    }

    public void setScreenLocation(Point location)
    {
        // TODO Auto-generated method stub

    }

    public String getName()
    {
        return null;
    }
    public Patch getPatch()
    {
        return null;
    }

    public void add(Module module)
    {
        throw new UnsupportedOperationException();
    }

    public void addModuleContainerListener(ModuleContainerListener l)
    {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Module module)
    {
        return false;
    }

    public Module createModule(ModuleDescriptor descriptor)
            throws InvalidDescriptorException
    {
        throw new UnsupportedOperationException();
    }

    public ConnectionManager getConnectionManager()
    {
        ModuleContainer parent = getParent();
        return parent == null ? null : parent.getConnectionManager();
    }

    public int getModuleCount()
    {
        return 0;
    }

    public void remove(Module module)
    {
        throw new UnsupportedOperationException();
    }

    public void removeModuleContainerListener(ModuleContainerListener l)
    {
        throw new UnsupportedOperationException();
    }

    public Iterator<Module> iterator()
    {
        throw new UnsupportedOperationException();
    }

}

