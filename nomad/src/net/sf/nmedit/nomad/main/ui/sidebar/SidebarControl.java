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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class SidebarControl extends JToolBar
{
    
    private Map<Sidebar, JToggleButton> sidebars = new HashMap<Sidebar, JToggleButton>();
    private Sidebar current = null;
    private JComponent currentComponent = null;
    private Sidebar[] sidebarsBuffer = null;
    private List<SidebarListener> sidebarListenerList = new ArrayList<SidebarListener>();
    private SidebarEvent message = new SidebarEvent();
    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    public SidebarControl()
    {
        super(VERTICAL);
        setMinimumSize(new Dimension(16,16));
        setFloatable(false);
        message.setSidebarControl(this);
        splitPane.setResizeWeight(1);
        splitPane.setEnabled(false);
        setBorder(null);
    }
    
    public JSplitPane getSplitPane()
    {
        return splitPane;
    }

    public void addSidebar(Sidebar bar)
    {
        if (sidebars.keySet().contains(bar))
            throw new IllegalStateException("Sidebar already added");
        
        JToggleButton button = createSidebarActivationButton(bar);
        
        sidebars.put(bar, button);
        add(button);
        sidebarsBuffer = null;
    }
    
    public void removeSidebar(Sidebar bar)
    {
        if (bar==current) closeCurrentSidebar();
        
        
        JToggleButton button = sidebars.remove(bar);
        if (button == null)
            throw new IllegalStateException("Sidebar cannot be removed: not found");
        else
            remove(button);

        sidebarsBuffer = null;
    }

    public void setCurrentSidebar(Sidebar bar)
    {
        JToggleButton toggle = sidebars.get(bar);
        if (toggle!=null)
            toggle.doClick();
    }
    
    private void setCurrentSidebarInternal(Sidebar bar)
    {
        if (current != bar)
        {
            closeCurrentSidebar();
            
            if (bar!=null) activateSidebar(bar);
        }
    }
    
    private void activateSidebar(Sidebar bar)
    {
        JComponent c = bar.createView();

        current = bar;
        currentComponent = c;

        sidebarActivated(bar, c);
    }

    public boolean isCurrentSidebarSet()
    {
        return current!=null;
    }
    
    public Sidebar[] getSidebars()
    {
        if (sidebarsBuffer == null)
        {
            Set<Sidebar> sset = sidebars.keySet();
            sidebarsBuffer = sset.toArray(new Sidebar[sset.size()]);
        }
        return sidebarsBuffer;
    }
    
    public void closeCurrentSidebar()
    {
        if (!isCurrentSidebarSet()) return;
            
        JToggleButton button = sidebars.get(current);
        button.setSelected(false); // we don't use a ButtonGroup because we allow that all buttons can be not selected 
        
        current.disposeView();

        Sidebar tbar = current;
        JComponent tc = currentComponent;
        
        currentComponent = null;
        current = null;
        
        sidebarDeactivated(tbar, tc);
    }
    
    public void addSidebarListener(SidebarListener l)
    {
        if (!sidebarListenerList.contains(l))
            sidebarListenerList.add(l);
    }
    
    public void removeSidebarListener(SidebarListener l)
    {
        sidebarListenerList.remove(l);
    }
    
    private void sidebarActivated( Sidebar bar, JComponent c )
    {
        splitPane.setEnabled(true);

        splitPane.setRightComponent(c);
        Dimension d = c.getPreferredSize();
        if (d == null) d = c.getSize();
        splitPane.setDividerLocation(splitPane.getWidth()-Math.max(200, d.width));
        
        message.setID(SidebarEvent.ACTIVATED);
        message.setSidebar(bar);
        message.setView(c);
        
        for (int i=sidebarListenerList.size()-1;i>=0;i--)
            sidebarListenerList.get(i).sidebarActivated(message);
    }

    private void sidebarDeactivated( Sidebar bar, JComponent c )
    {
        splitPane.setEnabled(false);
        
        splitPane.remove(c);
        splitPane.setDividerLocation(1.0);
        
        message.setID(SidebarEvent.DEACTIVATED);
        message.setSidebar(bar);
        message.setView(c);
        
        for (int i=sidebarListenerList.size()-1;i>=0;i--)
            sidebarListenerList.get(i).sidebarDeactivated(message);
    }

    public Sidebar getCurrentSidebar()
    {
        return current;
    }
    
    public JComponent getCurrentSidebarComponent()
    {
        return currentComponent;
    }
    
    private JToggleButton createSidebarActivationButton(Sidebar bar)
    {
        JToggleButton button = new JToggleButton(new SidebarActivationAction(bar));
        
        button.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        button.setBorderPainted(false);
        
        return button;
    }
    
    private class SidebarActivationAction extends AbstractAction
    {

        private final Sidebar bar;

        public SidebarActivationAction(Sidebar bar)
        {
            this.bar = bar;
            putValue(SMALL_ICON, bar.getIcon());
            putValue(SHORT_DESCRIPTION, bar.getDescription());
        }
        
        public void actionPerformed( ActionEvent e )
        {
            if (getCurrentSidebar()==bar) closeCurrentSidebar();
            else setCurrentSidebarInternal(bar);
        }
    }

}
