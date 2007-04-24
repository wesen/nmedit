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

import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.ModuleContainer;

public class ConnectionActionEvent implements Event
{

    private int moduleIdA ;
    private int moduleIdB ;
    private ConnectorDescriptor da;
    private ConnectorDescriptor db;
    private ModuleContainer container;
    private boolean create;

    public ConnectionActionEvent(Connector a, Connector b, boolean create)
    {
        moduleIdA = a.getOwner().getUniqueId();
        moduleIdB = b.getOwner().getUniqueId();
        container = a.getOwner().getParent();
        da = a.getDescriptor();
        db = b.getDescriptor();
        this.create = create;
    }

    public void performCreate(ModuleContainer container)
    {
        Connector ca = Utils.getModuleById(container, moduleIdA).getConnector(da);
        Connector cb = Utils.getModuleById(container, moduleIdB).getConnector(db);
        if (ca.connectWith(cb) == null)
            throw new IllegalStateException("could not connect connectors: "+ca+", "+cb);
    }

    public void performRemove(ModuleContainer container)
    {
        Connector ca = Utils.getModuleById(container, moduleIdA).getConnector(da);
        Connector cb = Utils.getModuleById(container, moduleIdB).getConnector(db);
        if (!ca.disconnectFrom(cb))
            throw new IllegalStateException("connectors were not connected: "+ca+", "+cb);
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
