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

import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultSingleSelectionModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SingleSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;

public class JTabBar<T> extends JComponent
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5578461150725889787L;

    public static String uiClassID = "TabBarUI";
    
    private Vector<Tab> tabs = new Vector<Tab>();
    
    private boolean tipRegistered;
    
    // The default selection model
    protected SingleSelectionModel model;
    protected ChangeListener changeListener;
    
    // source is always this
    protected transient ChangeEvent changeEvent = null;
    private boolean closeActionEnabled = true;
    
    private int recentSelectionCounter = 0;
    
    public JTabBar()
    {
        setOpaque(true);
        setUI(FFTabBarUI.createUI(this));
        setModel(new DefaultSingleSelectionModel());
    }

    public void setCloseActionEnabled(boolean enabled)
    {
        if (this.closeActionEnabled != enabled)
        {
            this.closeActionEnabled = enabled;
            revalidate();
            repaint();
        }
    } 
 
    public boolean isCloseActionEnabled()
    {
        return closeActionEnabled;
    }

    public String getUIClassID() 
    {
        return uiClassID;
    }
    
    public SingleSelectionModel getModel() 
    {
        return model;
    }

    public void setModel(SingleSelectionModel model) {
        SingleSelectionModel oldModel = getModel();

        if (oldModel != null) {
            oldModel.removeChangeListener(changeListener);
            changeListener = null;
        }

        this.model = model;

        if (model != null) {
            changeListener = createChangeListener();
            model.addChangeListener(changeListener);
        }

        firePropertyChange("model", oldModel, model);
        repaint();
    }

    protected ChangeListener createChangeListener() 
    {
        return new ModelListener();
    }
    
    protected class ModelListener implements ChangeListener, Serializable 
    {
        /**
         * 
         */
        private static final long serialVersionUID = 6525131641051659632L;

        public void stateChanged(ChangeEvent e) 
        {
            fireStateChanged();
            updateRecentSelectionIndex(getSelectedIndex());
        }       
    }
    
    protected void updateRecentSelectionIndex(int tabIndex)
    {
        if (tabIndex<0 || tabIndex>=getTabCount())
            return;
        
        Tab tab = tabs.get(tabIndex);
        tab.recentlySelected = recentSelectionCounter++;
        
        if (recentSelectionCounter == Integer.MAX_VALUE)
        {
            List<Tab> order = new ArrayList<Tab>(tabs.size());
            order.addAll(tabs);
            Collections.<Tab>sort(order);
            
            recentSelectionCounter = 0;
            for (Tab t: order)
                t.recentlySelected = recentSelectionCounter++;
        }
    }
    
    protected int getPreviousSelection(int tabIndex)
    {
        if (tabIndex<0 || tabIndex>=getTabCount())
            return -1;
        
        Tab tab = tabs.get(tabIndex);
        
        int recentIndex = -1;
        int recentValue = -1;
        
        // find tab t with the max(t.recentValue) and
        // t.recentValue < tab.recentValue
        for (int i=tabs.size()-1;i>=0;i--)
        {
            if (i != tabIndex)
            {
                Tab t = tabs.get(i);
                if (t.recentlySelected<tab.recentlySelected 
                    && recentValue < t.recentlySelected)
                {
                    recentIndex = i;
                    recentValue = t.recentlySelected;
                }
            }
        }
        
        return recentIndex;
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }
    
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }
        
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[])listenerList.getListeners(
                ChangeListener.class);
    }

    public int getSelectedIndex() 
    {
        return model.getSelectedIndex();
    }
    
    public void setSelectedIndex(int index) 
    {
        if (index != -1) checkIndex(index);
        setSelectedIndexImpl(index);
    }

    private void setSelectedIndexImpl(int index) 
    {
        model.setSelectedIndex(index);
    }

    public int indexOfTab(String title)
    {
        for(int i = 0; i < getTabCount(); i++) 
        {
            String t = getTitleAt(i);
            if (t == title || (t!=null && t.equals(title == null? "" : title))) { 
                return i;
            }
        }
        return -1; 
    }

    public int indexOfTab(Icon icon) {
        for(int i = 0; i < getTabCount(); i++) {
            Icon tabIcon = getIconAt(i);
            if ((tabIcon != null && tabIcon.equals(icon)) ||
                (tabIcon == null && tabIcon == icon)) { 
                return i;
            }
        }
        return -1; 
    }
    
    public int indexAtLocation(int x, int y)
    {
        if (ui != null)
        {
            return ((TabBarUI)ui).tabForCoordinate(this, x, y);
        }
        return -1;
    }
    
    private void checkIndex(int index) {
        if (index < 0 || index >= tabs.size()) {
            throw new IndexOutOfBoundsException("Index: "+index+", Tab count: "+tabs.size());
        }
    }
    
    protected void fireStateChanged() 
    {
        repaint();
        
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==ChangeListener.class) 
            {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }          
        }
    }   
    
    protected void registerTipComponent(boolean condition)
    {
        if ((!tipRegistered) && condition) 
        {
            ToolTipManager.sharedInstance().registerComponent(this);
            tipRegistered = true;
        }
    }

    public void setUI(TabBarUI ui)
    {
        super.setUI(ui);
        // disabled icons are generated by LF so they should be unset here
        for (int i = 0; i < getTabCount(); i++) 
        {
            Icon icon = tabs.get(i).disabledIcon;
            if (icon instanceof UIResource) 
            {
                setDisabledIconAt(i, null);
            }
        }
    }

    public TabBarUI getUI()
    {
        return (TabBarUI) ui;
    }
    
    public int getTabCount()
    {
        return tabs.size();
    }

    public String getToolTipText(MouseEvent event) {
        if (ui != null) {
            
            String tip = ((TabBarUI)ui).getToolTipTextAt(this, event.getX(), event.getY());
            if (tip != null)
                return tip;
        }
        return super.getToolTipText(event);
    }
    
    // Setters for the Tabs

     public void setTitleAt(int index, String title) {
         Tab tab = tabs.get(index);
         String oldTitle = tab.title;
         tab.title = title;
         
         if (oldTitle != title) {
             firePropertyChange("indexForTitle", -1, index);
         }
         tab.updateDisplayedMnemonicIndex();
         if (title == null || oldTitle == null ||
             !title.equals(oldTitle)) {
             revalidate();
             repaint();
         }
     }

     public void setIconAt(int index, Icon icon) {
         Tab tab = tabs.get(index);
         Icon oldIcon = tab.icon;        
         if (icon != oldIcon) {
             tab.icon = icon;
     
             /* If the default icon has really changed and we had
              * generated the disabled icon for this page, then
              * clear the disabledIcon field of the page.
              */
             if (tab.disabledIcon instanceof UIResource) {
                 tab.disabledIcon = null;
             }
     
             revalidate();
             repaint();
         }
     }

     public void setDisabledIconAt(int index, Icon disabledIcon) {
         Tab tab = tabs.get(index);
         
         Icon oldIcon = tab.disabledIcon;
         tab.disabledIcon = disabledIcon;
         if (disabledIcon != oldIcon && !isEnabledAt(index)) {
             revalidate();
             repaint();
         }
     }

     public void setToolTipTextAt(int index, String toolTipText) {
         Tab tab = tabs.get(index);
         //String oldToolTipText = tab.tip;
         tab.tip = toolTipText;
         registerTipComponent(toolTipText != null);
     }

     public void setEnabledAt(int index, boolean enabled) {
         Tab tab = tabs.get(index);
         boolean oldEnabled = tab.enabled;
         tab.enabled = enabled;
         if (enabled != oldEnabled) {
             revalidate();
             repaint();
         }
     }

     public void setItemAt(int index, T item) {
         Tab tab = tabs.get(index);
         T oldItem = tab.item;
         tab.item = item;
         if (item != oldItem) {
             revalidate();
             repaint();
         }
     }

     public void setDisplayedMnemonicIndexAt(int index, int mnemonicIndex) 
     {
         Tab tab = tabs.get(index);
         int oldMnemonicIndex = tab.mnemonicIndex;
         tab.mnemonicIndex = mnemonicIndex;
         if (mnemonicIndex != oldMnemonicIndex) {
             repaint();
         }
     }

     public void setMnemonicAt(int index, int mnemonic) 
     {
         Tab tab = tabs.get(index);
         int oldMnemonic = tab.mnemonic;
         tab.mnemonic = mnemonic;
         if (mnemonic != oldMnemonic) {
             repaint();
         }
     }

//   tabs

     public T getSelectedItem() 
     { 
         int index = getSelectedIndex();
         return index == -1 ? null : tabs.get(index).item;
     }
     
     public int indexOfItem(T item)
     {
         for(int i = 0; i < getTabCount(); i++) 
         {
             T jtem = getItemAt(i);
             if ((jtem != null && jtem.equals(item)) ||
                 (jtem == null && jtem == item)) { 
                 return i;
             }
         }
         return -1; 
     }

     public void setSelectedItem(T item) 
     {
         if (item == null)
         {
             if (getTabCount()==0)
                 setSelectedIndexImpl(-1);
             return;
         }
         
         int index = indexOfItem(item);
         if (index != -1) 
         {
             setSelectedIndex(index);
         } 
         else 
         {
             throw new IllegalArgumentException("item not found in tab bar");
         }
     }
     
     public void moveTab(int target, int source)
     {
         checkIndex(target);
         checkIndex(source);
         
         boolean selected = getSelectedIndex() == source;
         
         if (target == source)
             return;
         
         Tab sourceTab = tabs.remove(source);
         tabs.add(target, sourceTab);

         if (selected)
             setSelectedIndex(target);
         revalidate();
         repaint();
     }

     public void insertTab(String title, Icon icon, T item, String tip, int index) 
     {
         int newIndex = index;

         // If component already exists, remove corresponding
         // tab so that new tab gets added correctly
         // Note: we are allowing component=null because of compatibility,
         // but we really should throw an exception because much of the
         // rest of the JTabbedPane implementation isn't designed to deal
         // with null components for tabs.
         int removeIndex = indexOfItem(item);
         if (item != null && removeIndex != -1) 
         {
             removeTabAt(removeIndex);
             if (newIndex > removeIndex) 
             {
                 newIndex--;
             }
         }

         int selectedIndex = getSelectedIndex();

         tabs.insertElementAt(new Tab(this, title != null? title : "", icon, null,
                                        item, tip), newIndex);

         if (tabs.size() == 1) 
         {
             setSelectedIndex(0);
         }

         if (selectedIndex >= newIndex) 
         {
             setSelectedIndexImpl(selectedIndex + 1);
         }

         registerTipComponent(tip!=null);

         revalidate();
         repaint();
     }

     public void addTab(String title, Icon icon, T item, String tip) 
     {
         insertTab(title, icon, item, tip, tabs.size()); 
     }

     public void addTab(String title, Icon icon, T item) 
     {
         insertTab(title, icon, item, null, tabs.size()); 
     }

     public void addTab(String title, T item) 
     {
         insertTab(title, null, item, null, tabs.size()); 
     }

     public void removeTabAt(int index) 
     {    
         checkIndex(index);

         int selected = getSelectedIndex();
         int previouslySelected = getPreviousSelection(selected);
     
         tabs.remove(index);

         if (previouslySelected>selected && previouslySelected>0)
         {
             // !!! important if previouslySelected-1 == index
             // then the selection model does not change and
             // then it does not fire an event
             setSelectedIndexImpl(-1); // set to value(-1)!= previouslySelected-1
             setSelectedIndexImpl(previouslySelected-1);
         }
         else if (previouslySelected>= 0 && previouslySelected<selected)
         {
             setSelectedIndexImpl(previouslySelected);
         }
         /* if the selected tab is after the removal */
         else if (selected > index) {
             setSelectedIndexImpl(selected - 1);

         /* if the selected tab is the last tab */
         } else if (selected >= getTabCount()) {
             setSelectedIndexImpl(selected - 1);
         /* selected index hasn't changed, but the associated tab has */
         } else if (index == selected) {
             fireStateChanged();
         }
         
         if (tabs.isEmpty())
             recentSelectionCounter = 0;

         revalidate();
         repaint();
     }

//   Getters for the Tabs

      public String getTitleAt(int index) {
          return tabs.get(index).title;
      }

      public Icon getIconAt(int index) {
          return tabs.get(index).icon;
      }

      public Icon getDisabledIconAt(int index) {
          Tab tab = tabs.get(index);
          if (tab.disabledIcon == null) {
              tab.disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, tab.icon);
          }
          return tab.disabledIcon;
      }

      /**
       * Returns the tab tooltip text at <code>index</code>.
       *
       * @param index  the index of the item being queried
       * @return a string containing the tool tip text at <code>index</code>
       * @exception IndexOutOfBoundsException if index is out of range 
       *            (index < 0 || index >= tab count)
       *
       * @see #setToolTipTextAt
       * @since 1.3
       */
      public String getToolTipTextAt(int index) {
          return tabs.get(index).tip;
      }

      /**
       * Returns whether or not the tab at <code>index</code> is
       * currently enabled.
       *
       * @param index  the index of the item being queried
       * @return true if the tab at <code>index</code> is enabled;
       *          false otherwise
       * @exception IndexOutOfBoundsException if index is out of range 
       *            (index < 0 || index >= tab count)
       *
       * @see #setEnabledAt
       */
      public boolean isEnabledAt(int index) {
          return tabs.get(index).enabled;
      }

      public T getItemAt(int index) {
          return tabs.get(index).item;
      }

      public int getMnemonicAt(int tabIndex) {
          return tabs.get(tabIndex).getMnemonic();
      }

      public int getDisplayedMnemonicIndexAt(int tabIndex) {
          return tabs.get(tabIndex).getDisplayedMnemonicIndex();
      }

    static int findDisplayedMnemonicIndex(String text, int mnemonic) {
        if (text == null || mnemonic == '\0') {
            return -1;
        }

        char uc = Character.toUpperCase((char)mnemonic);
        char lc = Character.toLowerCase((char)mnemonic);

        int uci = text.indexOf(uc);
        int lci = text.indexOf(lc);

        if (uci == -1) {
            return lci;
        } else if(lci == -1) {
            return uci;
        } else {
            return (lci < uci) ? lci : uci;
        }
    }

    private class Tab implements Comparable<Tab>
    {
        String title;
        Icon icon;
        Icon disabledIcon;
        JTabBar parent;
        T item;
        String tip;
        boolean enabled = true;
        boolean needsUIUpdate;
        int mnemonic = -1;
        int mnemonicIndex = -1;
        int recentlySelected = 0;

        Tab(JTabBar parent, 
             String title, Icon icon, Icon disabledIcon, T item, String tip) {
            this.title = title;
            this.icon = icon;
            this.disabledIcon = disabledIcon;
            this.parent = parent;
            this.item = item;
            this.tip = tip;
        }

        void setMnemonic(int mnemonic) 
        {
            this.mnemonic = mnemonic;
            updateDisplayedMnemonicIndex();
        }

        int getMnemonic() 
        {
            return mnemonic;
        }

        /*
         * Sets the page displayed mnemonic index
         */
        void setDisplayedMnemonicIndex(int mnemonicIndex) {
            if (this.mnemonicIndex != mnemonicIndex) {
                if (mnemonicIndex != -1 && (title == null ||
                        mnemonicIndex < 0 ||
                        mnemonicIndex >= title.length())) {
                    throw new IllegalArgumentException(
                                "Invalid mnemonic index: " + mnemonicIndex);
                }
                this.mnemonicIndex = mnemonicIndex;
                JTabBar.this.firePropertyChange("displayedMnemonicIndexAt",
                                                    null, null);
            }
        }
 
        /*
         * Returns the page displayed mnemonic index
         */
        int getDisplayedMnemonicIndex() {
            return this.mnemonicIndex;
        }
  
        void updateDisplayedMnemonicIndex() {
            setDisplayedMnemonicIndex(findDisplayedMnemonicIndex(title, mnemonic));
        }

        public int compareTo(Tab o)
        {
            if (recentlySelected<o.recentlySelected)
                return -1;
            else if (recentlySelected==o.recentlySelected)
                return 0;
            else
                return 1;
        }

    }

    public void askRemove(int index)
    {
        firePropertyChange("ask-remove", -1, index);
    }

    public void showContextMenuForTab(MouseEvent e, int tabIndex)
    {
        firePropertyChange("show-context-menu", e, tabIndex);
    }

}
