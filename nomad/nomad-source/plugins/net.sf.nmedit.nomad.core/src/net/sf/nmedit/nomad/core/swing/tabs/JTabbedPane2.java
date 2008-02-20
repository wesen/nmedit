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
package net.sf.nmedit.nomad.core.swing.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.nmutils.Platform;

public class JTabbedPane2 extends JComponent
{

    /**
     * 
     */
    private static final long serialVersionUID = -2177154687789871416L;
    private JTabBar<Component> tabBar;
    
    public JTabbedPane2()
    {
        tabBar = new JTabBar<Component>();
        super.setLayout(new TabbedPane2Layout(this));
        add(tabBar, BorderLayout.NORTH);
        TabListener tabListener = new TabListener(this);
        tabListener.install();
        setOpaque(true);
    }
    
    public JTabBar<Component> getTabBar()
    {
        return tabBar;
    }

    public void addAskRemoveListener(PropertyChangeListener l)
    {
        tabBar.addPropertyChangeListener("ask-remove", l);
    }

    public void removeAskRemoveListener(PropertyChangeListener l)
    {
        tabBar.removePropertyChangeListener("ask-remove", l);
    }
    
    private static class TabbedPane2Layout implements LayoutManager2
    {
        
        private JTabbedPane2 tabbedPane;
        
        public TabbedPane2Layout(JTabbedPane2 tabbedPane)
        {
            this.tabbedPane = tabbedPane;
        }

        public Dimension maximumLayoutSize(Container target) {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        
        public void layoutContainer(Container parent)
        {
            Insets insets = parent.getInsets();
            int w = tabbedPane.getWidth()-insets.left-insets.right;
            int h = tabbedPane.getHeight()-insets.top-insets.bottom;
            
            JTabBar<?> t = tabbedPane.tabBar;
            Dimension d = t.getPreferredSize();
            
            if (t.getTabCount() == 0)
            {
                t.setBounds(insets.left, insets.top, 0, 0);
                d.setSize(0, 0);
            }
            else
            {
                t.setBounds(insets.left, insets.top, w, d.height);
            }
            Component c = tabbedPane.getSelectedComponent();
            if (c != null)
                c.setBounds(insets.left, insets.top+d.height, w, Math.max(0, h-d.height));
        }

        public Dimension minimumLayoutSize(Container parent)
        {
            synchronized (parent.getTreeLock()) {
                Dimension dim = new Dimension(0, 0);
                
                Component c = tabbedPane.getSelectedComponent();
                if (c != null)
                {
                    Dimension d = c.getMinimumSize();
                    dim.width=Math.max(dim.width, d.width);
                    dim.height+=d.height;
                }
                
                c = tabbedPane.tabBar;
                if (c != null)
                {
                    Dimension d = c.getMinimumSize();
                    dim.width=Math.max(dim.width, d.width);
                    dim.height+=d.height;
                }

                Insets insets = parent.getInsets();
                dim.width += insets.left + insets.right;
                dim.height += insets.top + insets.bottom;
                
                return dim;
            }
        }

        public Dimension preferredLayoutSize(Container parent)
        {
            synchronized (parent.getTreeLock()) {
                Dimension dim = new Dimension(0, 0);
                
                Component c = tabbedPane.getSelectedComponent();
                if (c != null)
                {
                    Dimension d = c.getPreferredSize();
                    dim.width=Math.max(dim.width, d.width);
                    dim.height+=d.height;
                }
                
                c = tabbedPane.tabBar;
                if (c != null)
                {
                    Dimension d = c.getPreferredSize();
                    dim.width=Math.max(dim.width, d.width);
                    dim.height+=d.height;
                }

                Insets insets = parent.getInsets();
                dim.width += insets.left + insets.right;
                dim.height += insets.top + insets.bottom;
                
                return dim;
            }
        }

        public void addLayoutComponent(String name, Component comp)
        {
            // no op
        }

        public void removeLayoutComponent(Component comp)
        {
            // no op
        }

        public void addLayoutComponent(Component comp, Object constraints)
        {
            // TODO Auto-generated method stub
            
        }

        public float getLayoutAlignmentX(Container target)
        {
            // TODO Auto-generated method stub
            return 0;
        }

        public float getLayoutAlignmentY(Container target)
        {
            // TODO Auto-generated method stub
            return 0;
        }

        public void invalidateLayout(Container target)
        {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    protected void paintComponent(Graphics g)
    {
        if (getTabCount()>0)
        {
            Component selected = getSelectedComponent();
            if (selected != null && selected.isOpaque())
                return;
        }

        // TODO use UIDefaults
        if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
        	g.setColor(Color.DARK_GRAY);
        } else {
        	g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void setLayout(LayoutManager mgr) 
    {
        throw new UnsupportedOperationException("custom layout not supported");
    }

    protected static class TabListener implements ChangeListener
    {
        
        protected JTabbedPane2 tp;
        protected Component currentComponent;

        public TabListener(JTabbedPane2 tp)
        {
            this.tp = tp;
        }

        public void install()
        {
            tp.addTabBarChangeListener(this);
        }

        public void stateChanged(ChangeEvent e)
        {
            Component c = tp.getSelectedComponent();
            
            if (currentComponent == c)
                return;
            
            synchronized (tp.getTreeLock())
            {
                if (currentComponent != null)
                {
                    tp.remove(currentComponent);
                }
                
                currentComponent = c;
    
                if (currentComponent != null)
                {
                    tp.add(currentComponent, BorderLayout.CENTER);
                }
            }

            tp.validate();
            tp.revalidate();
            tp.repaint();
        }
        
    }

    public void addTabBarChangeListener(ChangeListener l) 
    {
        tabBar.addChangeListener(l);
    }
    
    public void removeTabBarChangeListener(ChangeListener l) 
    {
        tabBar.removeChangeListener(l);
    }
        
    public ChangeListener[] getTabBarChangeListeners() 
    {
        return tabBar.getChangeListeners();
    }

    public SingleSelectionModel getModel() 
    {
        return tabBar.getModel();
    }

    public void setModel(SingleSelectionModel model) 
    {
        tabBar.setModel(model);
    }

    public int getSelectedIndex() 
    {
        return tabBar.getSelectedIndex();
    }

    public void setSelectedIndex(int index) 
    {
        tabBar.setSelectedIndex(index);
    }

    public Component getSelectedComponent() 
    {
        return tabBar.getSelectedItem();
    }

    public void setSelectedComponent(Component c) 
    {
        tabBar.setSelectedItem(c);
    }

    public void insertTab(String title, Icon icon, Component component, String tip, int index) 
    {
        tabBar.insertTab(title, icon, component, tip, index);
    }

    public void addTab(String title, Icon icon, Component component, String tip) 
    {
        insertTab(title, icon, component, tip, getTabCount()); 
    }

    public void addTab(String title, Icon icon, Component component) 
    {
        insertTab(title, icon, component, null, getTabCount());
    }

    public void addTab(String title, Component component) 
    {
        insertTab(title, null, component, null, getTabCount());
    }

    public void removeTabAt(int index) 
    {
        tabBar.removeTabAt(index);
    }

    public void removeAllTabs() 
    {
        tabBar.setSelectedIndex(-1);

        int tabCount = getTabCount();
        // We invoke removeTabAt for each tab, otherwise we may end up
        // removing Components added by the UI.
        while (tabCount-- > 0) {
            removeTabAt(tabCount);
        }
    }

    public int getTabCount() 
    {
        return tabBar.getTabCount();
    }


// Getters for the Pages

    public String getTitleAt(int index) 
    {
        return tabBar.getTitleAt(index);
    }

    public Icon getIconAt(int index) 
    {
        return tabBar.getIconAt(index);
    }

    public Icon getDisabledIconAt(int index) 
    {
        return tabBar.getDisabledIconAt(index);
    }

    public String getToolTipTextAt(int index)
    {
        return tabBar.getToolTipTextAt(index);
    }

    public boolean isEnabledAt(int index)
    {
        return tabBar.isEnabledAt(index);
    }

    public Component getComponentAt(int index)
    {
        return tabBar.getItemAt(index);
    }

    public int getMnemonicAt(int index)
    {
        return tabBar.getMnemonicAt(index);
    }

    public int getDisplayedMnemonicIndexAt(int index)
    {
        return tabBar.getDisplayedMnemonicIndexAt(index);
    }

    public void setTitleAt(int index, String title) 
    {
        tabBar.setTitleAt(index, title);
    }

    public void setIconAt(int index, Icon icon) 
    {
        tabBar.setIconAt(index, icon);
    }

    public void setDisabledIconAt(int index, Icon disabledIcon) 
    {
        tabBar.setDisabledIconAt(index, disabledIcon);
    }

    public void setToolTipTextAt(int index, String toolTipText)
    {
        tabBar.setToolTipTextAt(index, toolTipText);
    }

    public void setEnabledAt(int index, boolean enabled)
    {
        tabBar.setEnabledAt(index, enabled);
    }

    public void setComponentAt(int index, Component component) 
    {
        tabBar.setItemAt(index, component);
    }

    public void setDisplayedMnemonicIndexAt(int index, int mnemonicIndex) 
    {
        tabBar.setDisplayedMnemonicIndexAt(index, mnemonicIndex);
    }

    public void setMnemonicAt(int index, int mnemonic)
    {
        tabBar.setMnemonicAt(index, mnemonic);
    }

// end of Page setters

    public int indexOfTab(String title) 
    {
        return tabBar.indexOfTab(title);
    }

    public int indexOfTab(Icon icon) 
    {
        return tabBar.indexOfTab(icon);
    }

    public boolean containsComponent(Component component)
    {
        return tabBar.indexOfItem(component)>=0;
    }
    
    public int indexOfComponent(Component component) 
    {
        return tabBar.indexOfItem(component); 
    }

    public int indexAtLocation(int x, int y) {
        return tabBar.indexAtLocation(x, y);
    }

    public void setCloseActionEnabled(boolean enabled)
    {
        tabBar.setCloseActionEnabled(enabled);
    } 
 
    public boolean isCloseActionEnabled()
    {
        return tabBar.isCloseActionEnabled();
    }

    
/*
    private JTabComponent tabBar;
    
    public JTabbedPane2()
    {
        this.tabBar = new JTabComponent();
        
        JTabbedPane
    }

    public int getSelectedIndex() {
        return model.getSelectedIndex();
    }
    public void setSelectedIndex(int index) {
        
    }
    public Component getSelectedComponent() { 
        
    }
    public void setSelectedComponent(Component c) {
        
    }
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        
    }
    public void addTab(String title, Icon icon, Component component, String tip) {
        
    }
    public void addTab(String title, Icon icon, Component component) {
        
    }
    public void addTab(String title, Component component) {
        
    }
    public Component add(Component component) {
        
    }
    public Component add(String title, Component component) {
        
    }
    

    public Component add(Component component, int index) {
        
    }
    public void removeTabAt(int index) {    
        
    }

    public void remove(Component component) {
        
    }

    public void remove(int index) {
        removeTabAt(index);
        
    }

        public void removeAll() {
        }
        public int getTabCount() {}
        public String getTitleAt(int index) {
            
        }
        public Icon getIconAt(int index) {

            return ((Page)pages.elementAt(index)).icon;
        }
        public Icon getDisabledIconAt(int index) {
            
        }
        public String getToolTipTextAt(int index) {
            
        }
        public boolean isEnabledAt(int index) {
            
        }
        public Component getComponentAt(int index) {
            
        }
        public int getMnemonicAt(int tabIndex) {
            
        }
        public int getDisplayedMnemonicIndexAt(int tabIndex) {
            
        }
        public void setTitleAt(int index, String title) {
            
        }
        public void setIconAt(int index, Icon icon) {
            
        }
        public void setDisabledIconAt(int index, Icon disabledIcon) {
            
        }
        public void setToolTipTextAt(int index, String toolTipText) {
            
        }
        public void setEnabledAt(int index, boolean enabled) {
            
        }
        public void setComponentAt(int index, Component component) {
            
        }
        public void setDisplayedMnemonicIndexAt(int tabIndex, int mnemonicIndex) {
            
        }
        public void setMnemonicAt(int tabIndex, int mnemonic) {
            
        }
        public int indexOfTab(String title) {
            
        }
        public int indexOfTab(Icon icon) {
            
        }
        public int indexOfComponent(Component component) {
            
        }
        public String getToolTipText(MouseEvent event) {
            if (ui != null) {
                int index = ((TabbedPaneUI)ui).tabForCoordinate(this, event.getX(), event.getY());

                if (index != -1) {
                    return ((Page)pages.elementAt(index)).tip;
                }
            }
            return super.getToolTipText(event);
        }
        */
}
