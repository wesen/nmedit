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
 * Created on Feb 12, 2006
 */
package net.sf.nmedit.nomad.editor.views;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class ComponentZOrderMenuItem extends JMenuItem {

	private Component component;

	public ComponentZOrderMenuItem() {
		super();
	}

	public ComponentZOrderMenuItem(String text) {
		super(text);
	}
	
	public void setComponent(Component c) {
		this.component = c;
		setEnabled(component!=null);
	}
	
	public Component getComponent() {
		return component;
	}

	public final static class SendToBackAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() instanceof ComponentZOrderMenuItem) {
				Component c = ((ComponentZOrderMenuItem) event.getSource()).getComponent();
				if (c!= null) SendToBackAction.sentToBack(c);
			}
		}

		public static void sentToBack(Component c) {
			Container p = c.getParent();
			if (p!=null) {
				Component[] list = p.getComponents();
				for (int i=0;i<list.length;i++) {
					p.remove(list[i]);
				}
				p.add(c);
				for (int i=0;i<list.length;i++) {
					if (list[i]!=c) p.add(list[i]);
				}
			}
		}
	}


	public final static class BringToFrontAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() instanceof ComponentZOrderMenuItem) {
				Component c = ((ComponentZOrderMenuItem) event.getSource()).getComponent();
				if (c!= null) BringToFrontAction.bringToFront(c);
			}
		}

		public static void bringToFront(Component c) {
			Container p = c.getParent();
			if (p!=null) {
				Component[] list = p.getComponents();
				for (int i=0;i<list.length;i++) {
					p.remove(list[i]);
				}
				for (int i=0;i<list.length;i++) {
					if (list[i]!=c) p.add(list[i]);
				}
				p.add(c);
			}
		}
	}
	
}
