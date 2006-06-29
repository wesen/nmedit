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
 * Created on Jun 11, 2006
 */
package net.sf.nmedit.nomad.main.sidebar.impl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import net.sf.nmedit.nomad.main.sidebar.Sidebar;
import net.sf.nmedit.nomad.main.sidebar.SidebarSection;

public class SidebarImpl
    implements Sidebar
{

    private JComponent sbComponent;
    private JToolBar toolbar;
    private List<SectionSwitcher>
        sectionList = new ArrayList<SectionSwitcher>();
    
    public SidebarImpl()
    {
        sbComponent = new JPanel();
        sbComponent.setLayout(new BorderLayout());
        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        sbComponent.add(toolbar, BorderLayout.WEST);
    }
    
    public JComponent getSidebarComponent()
    {
        return sbComponent;
    }

    public void add( SidebarSection s )
    {
        SectionSwitcher sw = new SectionSwitcher(s);
        sectionList.add(sw);
        toolbar.add(sw);
    }

    public void remove( SidebarSection s )
    {
        // TODO Auto-generated method stub
        
    }

    public int getSectionCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public SidebarSection[] getSections()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void select( SidebarSection s )
    {
        // TODO Auto-generated method stub
        
    }

    public SidebarSection getSelection()
    {
        // TODO Auto-generated method stub
        return null;
    }

    private static class SectionSwitcher extends AbstractAction
    {
        
        private SidebarSection section;

        public SectionSwitcher(SidebarSection section)
        {
            this.section = section;
            putValue(NAME, section.getTitle());
            putValue(AbstractAction.SMALL_ICON, section.getIcon());
        }

        public void actionPerformed( ActionEvent e )
        {
            section.getSidebar().select(section);
        }
        
        public boolean equals(Object o)
        {
            return (o instanceof SectionSwitcher)
                && (section.equals(((SectionSwitcher)o).section));
        }
        
    }
    
}
