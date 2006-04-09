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
 * Created on Feb 25, 2006
 */
package org.nomad.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.nomad.patch.CableColor;
import org.nomad.patch.ui.CableDisplay;
import org.nomad.patch.ui.ModuleSectionUI;
import org.nomad.patch.ui.PatchUI;

public class CableColorSelectorFactory {

	public static JPanel newSelector() {
		JColorPanel panel = new JColorPanel();
		panel.setLayout(new GridLayout(1,7));
		
		for (CableColor color : EnumSet.allOf(CableColor.class)) {
			panel.add(new JColorButton(color));
		}

		panel.add(new JAllColorButton());
		panel.setPreferredSize(new Dimension(8*16,16));
		return panel;
	}

	private static class JColorButton extends JToggleButton  {

		private CableColor color;

		public JColorButton(String t) {
			super(t);
			color = null;
		}

		public JColorButton(CableColor color) {
			this.color = color ;
			setBackground(color.getColor());
			setForeground(color.getColor());
		}
		/*
		protected void paintComponent(Graphics g) {
			g.setColor(getBackground());
			g.fillRect(0,0,getWidth(),getHeight());
		}*/
		
	}
	
	private static class JAllColorButton extends JColorButton {
		public JAllColorButton() {
			super("A");
			setFont(new Font("SansSerif", Font.PLAIN, 9));
		}
	}

	private static class JColorPanel extends JPanel implements ActionListener {
		
		PatchUI patchui = null;
		
		public void add(JColorButton btn) {
			super.add(btn);
			btn.addActionListener(this);
		}

		public void actionPerformed(ActionEvent event) {
			if (patchui == null)
				return; 
			
			if (event.getSource() instanceof JAllColorButton) {

				changeAll(patchui.getCommonSection());
				changeAll(patchui.getPolySection());
				
			} else if (event.getSource() instanceof JColorButton) 
			{
				JColorButton btn = ((JColorButton) event.getSource()); 
				
				change(patchui.getCommonSection().getCableDisplay(), btn.color);
				change(patchui.getPolySection().getCableDisplay(), btn.color);
			}
		}
		
		void change(CableDisplay d, CableColor color) {
			d.setCablesVisible(color, !d.areCablesVisible(color));
		}
		
		void changeAll(ModuleSectionUI d) {
			d.setCablesVisible(!d.areCablesVisible());
		}
		
	}

	public static void setPatchUI(JPanel colorSelector, PatchUI patchui) {
		if (colorSelector instanceof JColorPanel) {
			((JColorPanel)colorSelector).patchui = patchui;
		} else {
			System.err.println("Not a color selector");
		}
	}
	
}
