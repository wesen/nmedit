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
 * Created on Nov 11, 2006
 */
package net.sf.nmedit.nomad.core.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import net.sf.nmedit.nomad.core.swing.JTabComponentUI;

public class JTabComponent extends JComponent
{
    
    private List<JTab> tabs = new ArrayList<JTab>();
    private int selectedTabIndex = -1;

    public JTabComponent()
    {
        setFocusable(true);
        setOpaque(true);
        setUI(JTabComponentUI.createUI(this));
    }
/*
    public void revalidate()
    {
        
    }

    public void invalidate()
    {
        
    }*/
    
    public void add(JTab tab)
    {
        add(tab, getTabCount());   
    }
    
    public void add(JTab tab, int index)
    {
        if (index<0 || index>getTabCount())
            throw new IllegalArgumentException("invalid index: "+index);
        
        if (index==getTabCount())
        {
            tabs.add(tab);
        }
        else
        {
            tabs.add(index, tab);
        }
        
        if (getTabCount() == 1)
            setSelectedTabIndex(0);
        else
            repaint();
    }

    public void remove(JTab tab)
    {
        int index = tabs.indexOf(tab);
        if (index>=0)
            closeTabAt(index);
    }
    
    public int getTabCount()
    {
        return tabs.size();
    }
    
    public JTab getTab(int index)
    {
        return tabs.get(index);
    }
    
    public JTab getTabSelectedTab()
    {
        return getTabOrNull(selectedTabIndex);
    }
    
    private JTab getTabOrNull(int index)
    {
        if (index>=0&&index<getTabCount())
            return getTab(index);
        else
            return null;
    }
 
    public int getSelectedTabIndex()
    {
        return selectedTabIndex;
    }
    
    public void setSelectedTab(JTab t)
    {
        setSelectedTabIndex(tabs.indexOf(t));
    }

    public void setSelectedTabIndex( int newSelectionIndex )
    {
        if (newSelectionIndex>=0 && newSelectionIndex<getTabCount())
        {
            setSelectedTabIndexInternal(newSelectionIndex);
        }
    }
    
    private void setSelectedTabIndexInternal( int newSelectionIndex )
    {
        if (this.selectedTabIndex!=newSelectionIndex)
        {
            int oldIndex = selectedTabIndex;
            selectedTabIndex = newSelectionIndex;
            firePropertyChange("selection", oldIndex, newSelectionIndex);
            repaint();
        }
    }

    public void closeTabAt( int index )
    {
        if (index>=0 && index<getTabCount())
        {
            JTab removed ;
            if (index==selectedTabIndex)
            {
            int newSelectionIndex = selectedTabIndex;
            if (newSelectionIndex>=getTabCount()-2)
                newSelectionIndex--;
            removed = tabs.remove(index);
            setSelectedTabIndexInternal(newSelectionIndex);
            }
            else
            {
                removed = tabs.remove(index);
            }
            firePropertyChange("tab.removed", removed, null);
            repaint();
        }
    }
    
}
