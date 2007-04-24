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
package net.sf.nmedit.jpatch.history;

import net.sf.nmedit.jpatch.AllEventsListener;
import net.sf.nmedit.jpatch.event.ConnectionEvent;
import net.sf.nmedit.jpatch.event.ModuleContainerEvent;
import net.sf.nmedit.jpatch.event.ModuleEvent;
import net.sf.nmedit.jpatch.event.ParameterEvent;

public class Synchronizer extends AllEventsListener
{
    
    private HistoryImpl history;

    public Synchronizer(HistoryImpl history)
    {
        this.history = history;
        listenConnections = true;
        listenModules = true;
        listenParameters = true;
    }
    
    public void moduleAdded(ModuleContainerEvent e)
    {
        super.moduleAdded(e);
        
        if (!history.isEnabled())
            return;
        Event event = new ModuleDeleteEvent(e.getModule());
        history.put(event);
    }

    public void moduleRemoved(ModuleContainerEvent e)
    {
        super.moduleRemoved(e);
        
        if (!history.isEnabled())
            return;
        Event event = new NewModuleEvent(e.getModule());
        history.put(event);
    }

    public void connectionAdded(ConnectionEvent e)
    {
        if (!history.isEnabled())
            return;
        Event event = new ConnectionActionEvent(e.getDestination(), e.getSource(), false);
        history.put(event);
    }

    public void connectionRemoved(ConnectionEvent e)
    {
        if (!history.isEnabled())
            return;
        Event event = new ConnectionActionEvent(e.getDestination(), e.getSource(), true);
        history.put(event);
    }

    public void parameterValueChanged(ParameterEvent e)
    {
        if (!history.isEnabled())
            return;
        history.markChanged(true);
    }

    public void moduleMoved(ModuleEvent e)
    {
        if (!history.isEnabled())
            return;
        Event event = new ModuleMoveEvent(e.getModule(), e.getOldScreenX(), e.getOldScreenY());
        history.put(event);
    }

    public void moduleRenamed(ModuleEvent e)
    {
        if (!history.isEnabled())
            return;
        Event event = new ModuleRenameEvent(e.getModule(), e.getOldName());
        history.put(event);
    }
    
}
