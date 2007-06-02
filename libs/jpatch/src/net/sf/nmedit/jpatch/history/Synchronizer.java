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
import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PParameterEvent;

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
    
    public void moduleAdded(PModuleContainerEvent e)
    {
        super.moduleAdded(e);
        
        if (!history.isEnabled())
            return;
        Event event = new ModuleDeleteEvent(e.getModule());
        history.put(event);
    }

    public void moduleRemoved(PModuleContainerEvent e)
    {
        super.moduleRemoved(e);
        
        if (!history.isEnabled())
            return;
        Event event = new NewModuleEvent(e.getModule());
        history.put(event);
    }

    public void connectionAdded(PConnectionEvent e)
    {
        if (!history.isEnabled())
            return;
        Event event = new ConnectionActionEvent(e.getDestination(), e.getSource(), false);
        history.put(event);
    }

    public void connectionRemoved(PConnectionEvent e)
    {
        if (!history.isEnabled())
            return;
        Event event = new ConnectionActionEvent(e.getDestination(), e.getSource(), true);
        history.put(event);
    }

    public void parameterValueChanged(PParameterEvent e)
    {
        if (!history.isEnabled())
            return;
        history.markChanged(true);
    }

    public void moduleMoved(PModuleEvent e)
    {
        if (!history.isEnabled())
            return;
        Event event = new ModuleMoveEvent(e.getModule(), e.getOldScreenX(), e.getOldScreenY());
        history.put(event);
    }

    public void moduleRenamed(PModuleEvent e)
    {
        if (!history.isEnabled())
            return;
        Event event = new ModuleRenameEvent(e.getModule(), e.getOldName());
        history.put(event);
    }
    
}
