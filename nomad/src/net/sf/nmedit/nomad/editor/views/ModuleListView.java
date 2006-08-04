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
 * Created on Jul 28, 2006
 */
package net.sf.nmedit.nomad.editor.views;

import java.awt.Component;
import java.awt.Image;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.ModuleDescriptions;

public class ModuleListView extends JScrollPane
{
    
    private JList listView;

    public ModuleListView()
    {
        super( new JList() );
        listView = (JList) getViewport().getView();
        DModule[] modules = ModuleDescriptions.sharedInstance().getModuleList();
        Arrays.<DModule>sort(modules, new Comparator<DModule>(){

            public int compare( DModule a, DModule b )
            {
                return a.getName().compareTo(b.getName());
            }
            
        });
        listView.setListData(modules);
        listView.setCellRenderer(new ModuleLCR());
    }
    
    public DModule getSelection(ListSelectionEvent e)
    {
        if (e.getSource()==listView)
        {
            Object val = listView.getSelectedValue();
            if (val instanceof DModule)
                return (DModule) val;
        }
        return null;
    }
    
    private static class ModuleLCR extends DefaultListCellRenderer
    {

        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            DModule dmod = (DModule)value;
            super.getListCellRendererComponent(list, dmod.getName(), index, isSelected, cellHasFocus);
            Image ic = dmod.getIcon();
            setIcon(ic == null ? null : new ImageIcon(dmod.getIcon()));
            return this;
        }
        
    }
    
    public void addListSelectionListener(ListSelectionListener l)
    {
        listView.addListSelectionListener(l);
    }
    
    public void removeListSelectionListener(ListSelectionListener l)
    {
        listView.removeListSelectionListener(l);
    }
    
}
