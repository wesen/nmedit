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

import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PConnectionListener;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PModuleListener;
import net.sf.nmedit.jpatch.event.PParameterEvent;
import net.sf.nmedit.jpatch.event.PParameterListener;

public class AllEventsListener
    implements PParameterListener,
    PModuleContainerListener, PModuleListener, PConnectionListener
{
    
    protected boolean listenParameters = false;
    protected boolean listenModules = false;
    protected boolean listenConnections = false;
    private List<PModuleContainer> containerList = new LinkedList<PModuleContainer>();
    
    public void dispose()
    {
        if (!containerList.isEmpty())
        {
            Iterator<PModuleContainer> iter = containerList.iterator();
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
    
    public void installModuleContainer(PModuleContainer container)
    {
        if (containerList.contains(container))
            return;
        containerList.add(container);
        container.addModuleContainerListener(this);
        if (listenConnections)
        {
            PConnectionManager cm = container.getConnectionManager();
            if (cm != null)
                cm.addConnectionListener(this);
        }
        
        if (listenModules)
            for (PModule m: container)
                installModule(m);
        if (listenParameters)
            for (PModule m: container)
                installParameters(m);
    }
    
    public void uninstallModuleContainer(PModuleContainer container)
    {
        if (!containerList.contains(container))
            return;
        if (listenParameters)
            for (PModule m: container)
                uninstallParameters(m);
        if (listenModules)
            for (PModule m: container)
                uninstallModule(m);
        container.removeModuleContainerListener(this);
        if (listenConnections)
        {
            PConnectionManager cm = container.getConnectionManager();
            if (cm != null)
                cm.removeConnectionListener(this);
        }
        containerList.remove(container);
    }
    
    protected void installModule(PModule module)
    {
        module.addModuleListener(this);
    }
    
    protected void uninstallModule(PModule module)
    {
        module.removeModuleListener(this);
    }
    
    protected void installParameters(PModule module)
    {
        for (int i=module.getParameterCount()-1;i>=0;i--)
            module.getParameter(i).addParameterListener(this);
    }
    
    protected void uninstallParameters(PModule module)
    {
        for (int i=module.getParameterCount()-1;i>=0;i--)
            module.getParameter(i).removeParameterListener(this);
    }

    public void moduleAdded(PModuleContainerEvent e)
    {
        if (listenModules)
            installModule(e.getModule());
        if (listenParameters)
            installParameters(e.getModule());
    }

    public void moduleRemoved(PModuleContainerEvent e)
    {
        if (listenModules)
            uninstallModule(e.getModule());
        if (listenParameters)
            uninstallParameters(e.getModule());   
    }

    public void parameterValueChanged(PParameterEvent e)
    {
        // no op
    }

    public void moduleMoved(PModuleEvent e)
    {
        // no op
    }

    public void moduleRenamed(PModuleEvent e)
    {
        // no op
    }

    public void connectionAdded(PConnectionEvent e)
    {
        // no op
    }

    public void connectionRemoved(PConnectionEvent e)
    {
        // no op
    }

    public void focusRequested(PParameterEvent e)
    {
        // no op
    }

}
