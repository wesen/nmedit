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
 * Created on May 29, 2006
 */
package net.sf.nmedit.nomad.main.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.nomad.theme.plugin.ThemePluginManager;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginProvider;

public class ThemePluginSelector
{
    private final  String DEFAULT_PLUGIN_NAME="Classic Theme";
    private final List<ChangeListener> pluginSelectListenerList;
    private ThemePluginProvider selectedPlugin = null;
    private Action[] selectPluginActions;

    public ThemePluginSelector()
    {
        pluginSelectListenerList = new ArrayList<ChangeListener>();

        List<Action> actions = new ArrayList<Action>();
        for (int i=0;i<ThemePluginManager.getPluginCount();i++) 
        {
            ThemePluginProvider plugin = ThemePluginManager.getPlugin(i);
            actions.add(new SelectPluginAction(plugin));
            if (plugin.getName().equals(DEFAULT_PLUGIN_NAME))
            {
                selectedPlugin = plugin;
            }
        }
        selectPluginActions = actions.toArray(new Action[actions.size()]);
    }

    public ThemePluginProvider getSelectedPlugin() {
        return selectedPlugin;
    }
    
    public void setSelectedPlugin(ThemePluginProvider plugin) 
    {
        if (this.selectedPlugin != plugin)
        {
            this.selectedPlugin = plugin;
            firePluginSelectedEvent();
        }
    }
    
    public void createMenu(JMenu mnMenu)
    {
        ButtonGroup group = new ButtonGroup();
        
        for (Action action : selectPluginActions)
        {
            JMenuItem menuItem = new PluginMenuItem(action);
            group.add(menuItem);
            mnMenu.add(menuItem);
            
            if (selectedPlugin!=null)
            {
                if (action.getValue(Action.NAME).equals(selectedPlugin.getName()))
                {
                   menuItem.setSelected(true);
                }
            }
        }
    }
    
    public void firePluginSelectedEvent()
    {
        firePluginSelectedEvent( new ChangeEvent( this ) );
    }

    public void firePluginSelectedEvent( ChangeEvent event )
    {
        for (int i = pluginSelectListenerList.size() - 1; i >= 0; i--)
        {
            pluginSelectListenerList.get( i ).stateChanged( event );
        }
    }

    public void addPluginSelectionListener( ChangeListener l )
    {
        if (!pluginSelectListenerList.contains( l ))
            pluginSelectListenerList.add( l );
    }

    public void removePluginSelectionListener( ChangeListener l )
    {
        pluginSelectListenerList.remove( l );
    }
    
    private class SelectPluginAction extends AbstractAction
    {
        private final ThemePluginProvider plugin;

        public SelectPluginAction(ThemePluginProvider plugin)
        {
            this.plugin = plugin;
            putValue(NAME, plugin.getName());
        }

        public void actionPerformed( ActionEvent e )
        {
            setSelectedPlugin(plugin);
        }
    }
    
    private class PluginMenuItem extends JRadioButtonMenuItem implements ChangeListener
    {
        private final ThemePluginProvider plugin;
        
        public PluginMenuItem(Action action)
        {
            super(action);
            plugin = ((SelectPluginAction) action).plugin;
            addPluginSelectionListener(this);
        }

        public void stateChanged( ChangeEvent e )
        {
            if (selectedPlugin == plugin)
            {
                setSelected(true);
            }
        }
    }
    
}
