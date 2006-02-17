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
 * Created on Feb 14, 2006
 */
package org.nomad.dialog.decoration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import org.nomad.dialog.NomadDialog;

public class ButtonPane extends DialogPane implements ActionListener {

	private NomadDialog dialog;

	public ButtonPane(NomadDialog dialog) {
		this.dialog = dialog;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		final int i = 10;
		setBorder(BorderFactory.createEmptyBorder(i,i,i,i));
		
		setBackground(Colors.WHITE_SMOKE);
		add(Box.createHorizontalGlue());
		//setMinimumSize(new Dimension(10, 50));
		//setPreferredSize(getMinimumSize());
	}
	
	public void addButton(String option) {
		add(new OptionButton(option));
	}
	
	private class OptionButton extends JButton {
		public OptionButton(String option) {
			setAlignmentY(0);
			addActionListener(ButtonPane.this);
			
			if (option!=null && option.startsWith(":")) {
				setDefaultCapable(true);
				option=option.substring(1);
			} else {
				setDefaultCapable(false);
			}
			setText(option);
		}
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof OptionButton) {
			OptionButton ob = (OptionButton) event.getSource();
			
			if (dialog.setResult(ob.getText())) {
				dialog.close();
			}
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, 0, getWidth(), 0);
	}

	public void setOptions(String[] options) {
		for (int i=0;i<options.length;i++) {
			add(Box.createHorizontalStrut(5));
			addButton(options[i]);
		}
	}
	
}
