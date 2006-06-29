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
 * Created on Jun 28, 2006
 */
package net.sf.nmedit.nomad.main.ui.sidebar;

import javax.swing.JComponent;

public class SidebarEvent
{

    public final static int ACTIVATED = 1;
    public final static int DEACTIVATED = 0;
    
    private Sidebar bar = null;
    private JComponent view = null;
    private SidebarControl control = null;
    private int ID = -1;
    
    public SidebarEvent()
    { }

    public void setSidebar(Sidebar b)
    {
        this.bar = b;
    }
    
    public Sidebar getSidebar()
    {
        return bar;
    }
    
    public JComponent getView()
    {
        return view;
    }
    
    public void setView(JComponent view)
    {
        this.view = view;
    }
    
    public int getID()
    {
        return ID;
    }
    
    public void setID(int id)
    {
        this.ID = id;
    }

    public void setSidebarControl( SidebarControl control )
    {
        this.control  = control;
    }
    
    public SidebarControl getSidebarControl()
    {
        return control;
    }
    
}
