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

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class InfoPane extends DialogPane {

	private JLabel lblTitle;
	private JLabel lbltext ;
	private String strText = "";
	
	public InfoPane() {
		setBackground(Colors.WHITE_SMOKE);
		
		setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		
		lblTitle = new JLabel("Info");
		lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		lbltext=new JLabel();
		lbltext.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lbltext.setAlignmentY(0);

		setText("Line One\nLine Two");
		
		add(lblTitle);
		add(Box.createVerticalStrut(20));
		add(lbltext);
		
		updateSize();
	}
	
	protected void updateSize() {
		setMinimumSize(new Dimension(
				(int)Math.max(100,Math.max(lbltext.getPreferredSize().getWidth(), lblTitle.getPreferredSize().getWidth())+10)
				, 10));
		setPreferredSize(getMinimumSize());
		setSize(getPreferredSize());
	}
	
	public void setTitle(String title) {
		lblTitle.setText(title);
		updateSize();
	}
	
	public String getTitle() {
		return lblTitle.getText();
	}
	
	public void setText(String text) {
		strText = text;
		lbltext.setText("<html>"+text.replaceAll("\\n","<br>")+"</html>");
		updateSize();
	}
	
	public String getText() {
		return strText;
	}
}
