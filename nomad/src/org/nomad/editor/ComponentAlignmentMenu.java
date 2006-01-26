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
 * Created on Jan 10, 2006
 */
package org.nomad.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.nomad.theme.component.NomadComponent;

public class ComponentAlignmentMenu extends JMenu {

	private ActionActionListener actionDispatcher
		= new ActionActionListener();

	public final static int ACT_BRINGFORWARD = 0;
	public final static int ACT_SENDBACKWARD = 1;
	public final static int ACT_ALLEFT = 2;
	public final static int ACT_ALRIGHT= 3;
	public final static int ACT_ALTOP  = 4;
	public final static int ACT_ALBOTTOM= 5;
	public final static int ACT_CENTER_HRZ= 6;
	public final static int ACT_CENTER_VERT= 7;
	public final static int ACT_CENTER_HRZ_MEDIAN= 8;
	public final static int ACT_CENTER_VERT_MEDIAN= 9;
	
	private NomadComponent[] components = null;
	
	public ComponentAlignmentMenu() {
		super("Align");
		
		//addAction("to-top", "Bring Forward", ACT_BRINGFORWARD);
		//addAction("to-bottom", "Send Backward", ACT_SENDBACKWARD);
		addAction("alignment-left-16", "Align Left", ACT_SENDBACKWARD);
		addAction("alignment-right-16", "Align Right", ACT_ALRIGHT);
		addAction("alignment-top-16", "Align Top", ACT_ALTOP);
		addAction("alignment-bottom", "Align Bottom", ACT_ALBOTTOM);
		addSeparator();
		addAction("alignment-centered-horizontally", "Center Hrz. (Median)", ACT_CENTER_HRZ_MEDIAN);
		addAction("alignment-centered-vertically-16", "Center Vrt. (Median)", ACT_CENTER_VERT_MEDIAN);
		addSeparator();
		addAction("alignment-centered-horizontally", "Center Hrz.", ACT_CENTER_HRZ);
		addAction("alignment-centered-vertically-16", "Center Vrt.", ACT_CENTER_VERT);
		setEnabled(false);

	}

	private void addAction(String pngIconName, String toolTipText, int actionId) {
		String imgFile = "data/images/icons/stock_"+pngIconName+".png";
		ImageIcon icon = new ImageIcon(imgFile);
		addAction(icon, toolTipText, actionId);
	}
	
	private void addAction(ImageIcon icon, String toolTipText, int actionId) {		
		JActionButton btn = new JActionButton(icon, toolTipText, actionId);
		btn.addActionListener(actionDispatcher);
		add(btn);
	}
	
	private class JActionButton extends JMenuItem {
		public int actionId;
		public JActionButton(ImageIcon icon, String toolTipText, int actionId) {
			setIcon(icon);
			setText(toolTipText);
			this.actionId = actionId;
		}
	}

	public void setComponents(Object[] objects) {
		NomadComponent[] comps = new NomadComponent[objects.length];
		for (int i=objects.length-1;i>=0;i--)
			comps[i] = (NomadComponent) objects[i];
		setComponents(comps);
	}

	public void setComponents(NomadComponent[] components) {
		this.components = components;
		setEnabled(components.length>0);
	}
	
	public void setEnabled(boolean isEnabled) {
		if (isEnabled()!=isEnabled) {
			
			for (int i=getComponentCount()-1;i>=0;i--)
				getComponent(i).setEnabled(isEnabled);
			
			super.setEnabled(isEnabled);
		}
	}
	
	private class ActionActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			dispatchAction(((JActionButton)event.getSource()).actionId);
		}

		private void dispatchAction(int actionId) {
			switch (actionId) {
				case ACT_BRINGFORWARD: break;
				case ACT_SENDBACKWARD: break;
				case ACT_ALLEFT: alignLeft(components); break;
				case ACT_ALRIGHT: alignRight(components); break;
				case ACT_ALTOP: alignTop(components); break;
				case ACT_ALBOTTOM: alignBottom(components); break;
				case ACT_CENTER_HRZ: alignHorizontalCenter(components); break;
				case ACT_CENTER_VERT: alignVerticalCenter(components); break;
				case ACT_CENTER_HRZ_MEDIAN: alignHorizontalMedian(components); break;
				case ACT_CENTER_VERT_MEDIAN: alignVerticalMedian(components); break;
			}
		}
		
	}

	// centers all selected components vertically
	public static void alignVerticalCenter(NomadComponent[] components) {
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			Component parent = c.getParent();
			c.setLocation(c.getX(), (parent.getHeight()-c.getHeight())/2 );
		}
	}

	// centers all selected components vertically
	public static void alignHorizontalCenter(NomadComponent[] components) {
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			Component parent = c.getParent();
			c.setLocation((parent.getWidth()-c.getWidth())/2   ,c.getY());
		}
	}
	
	// align top most
	public static void alignTop(NomadComponent[] components) {
		int top = 100000;
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			top = Math.min(top,c.getY());
		}
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			c.setLocation(c.getX(), top);
		}
	}
	
	// align bottom most
	public static void alignBottom(NomadComponent[] components) {
		int bottom = 0;
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			bottom = Math.max(bottom, c.getY()+c.getHeight());
		}
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			c.setLocation(c.getX(), bottom-c.getHeight());
		}
	}
	
	// align left most
	public static void alignLeft(NomadComponent[] components) {
		int left = 0;
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			left= Math.min(left, c.getX());
		}
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			c.setLocation(left, c.getY());
		}
	}
	
	// align right most
	public static void alignRight(NomadComponent[] components) {
		int right = 0;
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			right = Math.max(right, c.getX()+c.getWidth());
		}
		for (int i=components.length-1;i>=0;i--) {
			NomadComponent c = components[i];
			c.setLocation(right-c.getWidth(), c.getY());
		}
	}
	
	public static void alignVerticalMedian(NomadComponent[] components) {
		int ymedian = 0;

		for (int i=0;i<components.length;i++) {
			Component c = (Component) components[i];
			ymedian+=c.getY()+c.getHeight()/2;
		}
		
		ymedian /=components.length;
		
		for (int i=0;i<components.length;i++) {
			Component c = (Component) components[i];
			int current = c.getY()+c.getHeight()/2;
			c.setLocation(
				c.getLocation().x,
				c.getLocation().y+(ymedian-current)
			);
		}
	}
	
	public static void alignHorizontalMedian(NomadComponent[] components) {
		int xmedian = 0;

		for (int i=0;i<components.length;i++) {
			Component c = (Component) components[i];
			xmedian+=c.getX()+c.getWidth()/2;
		}
		
		xmedian /=components.length;
		
		for (int i=0;i<components.length;i++) {
			Component c = (Component) components[i];
			int current = c.getX()+c.getWidth()/2;
			c.setLocation(
				c.getLocation().x+(xmedian-current),
				c.getLocation().y
			);
		}
	}

}
