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

import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModuleContainer;

public class ConnectionActionEvent implements Event
{
    private int moduleIndexA ;
    private int moduleIndexB ;
    private int connectorIndexA ;
    private int connectorIndexB ;
    private PModuleContainer container;
    private boolean create;

    public ConnectionActionEvent(PConnector a, PConnector b, boolean create)
    {
        moduleIndexA = a.getParentComponent().getComponentIndex();
        moduleIndexB = b.getParentComponent().getComponentIndex();
        connectorIndexA = a.getComponentIndex();
        connectorIndexB = b.getComponentIndex();
        container = a.getParentComponent().getParentComponent();
        this.create = create;
    }

    public void performCreate(PModuleContainer container)
    {
        PConnector ca = (PConnector)container.getModule(moduleIndexA).getComponent(connectorIndexA);
        PConnector cb = (PConnector)container.getModule(moduleIndexB).getComponent(connectorIndexB);
        if(!ca.connect(cb))
            throw new IllegalStateException("could not connect: "+ca+", "+cb);
    }

    public void performRemove(PModuleContainer container)
    {
        PConnector ca = (PConnector)container.getModule(moduleIndexA).getComponent(connectorIndexA);
        PConnector cb = (PConnector)container.getModule(moduleIndexB).getComponent(connectorIndexB);
        if (!ca.disconnect(cb))
            throw new IllegalStateException("could not disconnect: "+ca+", "+cb);
    }

    public String getTitle()
    {
        return null;
    }

    public void perform()
    {
        if (create) performCreate(container);
        else performRemove(container);
    }
    
}
