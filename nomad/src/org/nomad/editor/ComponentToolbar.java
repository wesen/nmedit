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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class ComponentToolbar extends JToolBar {

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
	
	public ComponentToolbar() {
		super();

		addAction("to-top", "Bring Forward", ACT_BRINGFORWARD);
		addAction("to-bottom", "Send Backward", ACT_SENDBACKWARD);
		addAction("alignment-left-16", "Align Left", ACT_SENDBACKWARD);
		addAction("alignment-right-16", "Align Right", ACT_ALRIGHT);
		addAction("alignment-top-16", "Align Top", ACT_ALTOP);
		addAction("alignment-bottom", "Align Bottom", ACT_ALBOTTOM);

		addAction("alignment-centered-horizontally", "Align Bottom", ACT_CENTER_HRZ);
		addAction("alignment-centered-vertically-16", "Align Bottom", ACT_CENTER_VERT);
	}

	private void addAction(String pngIconName, String toolTipText, int actionId) {
		String imgFile = "org/icons/stock_"+pngIconName+".png";
		ImageIcon icon = new ImageIcon(imgFile);
		addAction(icon, toolTipText, actionId);
	}
	
	private void addAction(ImageIcon icon, String toolTipText, int actionId) {
		JActionButton btn = new JActionButton(icon, toolTipText, actionId);
		btn.addActionListener(actionDispatcher);
		add(btn);
	}
	
	private class ActionActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			dispatchAction(((JActionButton)event.getSource()).actionId);
		}

		private void dispatchAction(int actionId) {
			System.out.println(actionId);
		}
		
	}
	
	private class JActionButton extends JButton {
		
		public int actionId;
		
		public JActionButton(ImageIcon icon, String toolTipText, int actionId) {
			
			setIcon(icon);
			setToolTipText(toolTipText);
			this.actionId = actionId;
			/*
			Dimension d = new Dimension(icon.getIconWidth(), icon.getIconHeight());
			
			setSize(d);
			setPreferredSize(d);
			*/
		}
		
	}
	
}
