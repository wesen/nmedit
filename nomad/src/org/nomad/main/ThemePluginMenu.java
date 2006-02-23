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
 * Created on Jan 23, 2006
 */
package org.nomad.main;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.env.Environment;
import org.nomad.plugin.NomadPlugin;
import org.nomad.plugin.PluginManager;

public class ThemePluginMenu extends JMenu {
	
	private NomadPlugin selectedPlugin = null;
	private ArrayList<ChangeListener> pluginSelectListenerList 
		= new ArrayList<ChangeListener>();

	public ThemePluginMenu(String name) {
		super(name);
		
		Environment env = Environment.sharedInstance();
		String current = env.getProperty(key_theme);
		if (current == null)
			current = "Classic Theme";
		
		ButtonGroup themeGroup = new ButtonGroup();
		for (int i=0;i<PluginManager.getPluginCount();i++) {
			NomadPlugin plugin = PluginManager.getPlugin(i);
			if (plugin.getFactoryType()==NomadPlugin.NOMAD_FACTORY_TYPE_UI) {
				JRadioButtonMenuItem menuItem = new ThemeChanger(plugin);
				add(menuItem);
				themeGroup.add(menuItem);
				if (plugin.getName().equals(current)) {
					menuItem.setSelected(true);
					this.selectedPlugin = plugin;
				}
			}
		}	
	}

	private class ThemeChanger extends JRadioButtonMenuItem implements ActionListener {
		private NomadPlugin plugin = null;
		public ThemeChanger(NomadPlugin plugin) {
			super(plugin.getName());
			setToolTipText(plugin.getDescription());
			this.plugin = plugin;
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent event) {
			setSelectedPlugin(plugin);
		}
	}

	public NomadPlugin getSelectedPlugin() {
		return selectedPlugin;
	}
	
	private final static String key_theme="theme";
	
	public void setSelectedPlugin(NomadPlugin plugin) {
		for (int i=getMenuComponentCount()-1;i>=0;i--) {
			Component c = getMenuComponent(i);
			if (c instanceof ThemeChanger) {
				ThemeChanger mItem = (ThemeChanger) c;
				if (mItem.plugin==plugin && (!mItem.isSelected())) {
					mItem.setSelected(true);
					break;
				}
			}
		}

		Environment env = Environment.sharedInstance();
		env.setProperty(key_theme, plugin.getName());
		this.selectedPlugin = plugin;
		firePluginSelectedEvent();
	}
	
	public void reselectPlugin() {
		setSelectedPlugin(selectedPlugin);
	}
	
	public void firePluginSelectedEvent() {
		firePluginSelectedEvent(new ChangeEvent(this));
	}
	
	public void firePluginSelectedEvent(ChangeEvent event) {
		for (int i=pluginSelectListenerList.size()-1;i>=0;i--) {
			pluginSelectListenerList.get(i).stateChanged(event);
		}
	}

	public void addPluginSelectionListener(ChangeListener l) {
		if (!pluginSelectListenerList.contains(l))
			pluginSelectListenerList.add(l);
	}
	
	public void removePluginSelectionListener(ChangeListener l) {
		pluginSelectListenerList.remove(l);
	}
	
}
