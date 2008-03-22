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
package net.sf.nmedit.jtheme;

import javax.swing.ImageIcon;

import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.initService.InitService;
import net.sf.nmedit.nomad.core.swing.tabs.JTabbedPane2;

public class InstallerService implements InitService
{

    public void init()
    {
        JTabbedPane2 toolPane = Nomad.sharedInstance().getToolPane();
        toolPane.addTab("Modules", new ImageIcon(getClass().getResource("/icons/eview16/members.gif")), ModulePane.getSharedInstance());
        
    }

    public void shutdown()
    {
        // TODO Auto-generated method stub

    }

    public Class<? extends Service> getServiceClass()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
