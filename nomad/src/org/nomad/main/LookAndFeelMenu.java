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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class LookAndFeelMenu extends JMenu implements ActionListener {

	private ArrayList<Component> affectedComponents = new ArrayList<Component>();
	
	// private ArrayList<Component> affectedComponents = new ArrayList<Component>();
	
	public LookAndFeelMenu(String name) {
		super(name);
		ButtonGroup buttonGroup = new ButtonGroup();
		String current = UIManager.getLookAndFeel().getName();
		for (LookAndFeelInfo look : UIManager.getInstalledLookAndFeels() ) {
			JRadioButtonMenuItem menuItem = new LookAndFeelMenuItem(look);
			menuItem.addActionListener(this);
		    add(menuItem);
		    buttonGroup.add(menuItem);
		    menuItem.setSelected(look.getName().equals(current));
		}
	}
	
	class LookAndFeelMenuItem extends JRadioButtonMenuItem {
		private LookAndFeelInfo info;
		public LookAndFeelMenuItem(LookAndFeelInfo info) {
			super(info.getName());
			this.info = info;
		}
	}

	public void addAffectedComponent(Component component) {
		if (!affectedComponents.contains(component))
			affectedComponents.add(component);
	}
	
	public void removeAffectedComponent(Component component) {
		affectedComponents.remove(component);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof LookAndFeelMenuItem) {
			try {
				UIManager.setLookAndFeel( ((LookAndFeelMenuItem) event.getSource()).info.getClassName() );
				for (Component c : affectedComponents) {
		        	SwingUtilities.updateComponentTreeUI(c);
		        }
		      } catch (Exception e) {
		        e.printStackTrace();
		      }	
		}
	}
	
}
