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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.nmedit.jpatch.event.ConnectionEvent;
import net.sf.nmedit.jpatch.event.ConnectionListener;
import net.sf.nmedit.jpatch.event.ModuleContainerEvent;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.event.ModuleEvent;
import net.sf.nmedit.jpatch.event.ModuleListener;
import net.sf.nmedit.jpatch.event.ParameterEvent;
import net.sf.nmedit.jpatch.event.ParameterValueChangeListener;

public class AllEventsListener
    implements ParameterValueChangeListener,
    ModuleContainerListener, ModuleListener, ConnectionListener
{
    
    protected boolean listenParameters = false;
    protected boolean listenModules = false;
    protected boolean listenConnections = false;
    private List<ModuleContainer> containerList = new LinkedList<ModuleContainer>();
    
    public void dispose()
    {
        if (!containerList.isEmpty())
        {
            Iterator<ModuleContainer> iter = containerList.iterator();
            while (iter.hasNext())
            {
                uninstallModuleContainer(iter.next());
                iter.remove();
            }
        }
    }
    
    public boolean isListeningParameters()
    {
        return listenParameters ;
    }
    
    public boolean isListeningModules()
    {
        return listenModules;
    }
    
    public void installModuleContainer(ModuleContainer container)
    {
        if (containerList.contains(container))
            return;
        containerList.add(container);
        container.addModuleContainerListener(this);
        if (listenConnections)
        {
            ConnectionManager cm = container.getConnectionManager();
            if (cm != null)
                cm.addConnectionListener(this);
        }
        
        if (listenModules)
            for (Module m: container)
                installModule(m);
        if (listenParameters)
            for (Module m: container)
                installParameters(m);
    }
    
    public void uninstallModuleContainer(ModuleContainer container)
    {
        if (!containerList.contains(container))
            return;
        if (listenParameters)
            for (Module m: container)
                uninstallParameters(m);
        if (listenModules)
            for (Module m: container)
                uninstallModule(m);
        container.removeModuleContainerListener(this);
        if (listenConnections)
        {
            ConnectionManager cm = container.getConnectionManager();
            if (cm != null)
                cm.removeConnectionListener(this);
        }
        containerList.remove(container);
    }
    
    protected void installModule(Module module)
    {
        module.addModuleListener(this);
    }
    
    protected void uninstallModule(Module module)
    {
        module.removeModuleListener(this);
    }
    
    protected void installParameters(Module module)
    {
        ModuleDescriptor md = module.getDescriptor();
        for (int i=md.getParameterCount()-1;i>=0;i--)
            module.getParameter(md.getParameterDescriptor(i)).addParameterValueChangeListener(this);
    }
    
    protected void uninstallParameters(Module module)
    {
        ModuleDescriptor md = module.getDescriptor();
        for (int i=md.getParameterCount()-1;i>=0;i--)
            module.getParameter(md.getParameterDescriptor(i)).removeParameterValueChangeListener(this);
    }

    public void moduleAdded(ModuleContainerEvent e)
    {
        if (listenModules)
            installModule(e.getModule());
        if (listenParameters)
            installParameters(e.getModule());
    }

    public void moduleRemoved(ModuleContainerEvent e)
    {
        if (listenModules)
            uninstallModule(e.getModule());
        if (listenParameters)
            uninstallParameters(e.getModule());   
    }

    public void parameterValueChanged(ParameterEvent e)
    {
        // no op
    }

    public void moduleMoved(ModuleEvent e)
    {
        // no op
    }

    public void moduleRenamed(ModuleEvent e)
    {
        // no op
    }

    public void connectionAdded(ConnectionEvent e)
    {
        // no op
    }

    public void connectionRemoved(ConnectionEvent e)
    {
        // no op
    }

}
