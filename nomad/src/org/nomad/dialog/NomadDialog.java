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
package org.nomad.dialog;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.nomad.dialog.decoration.DialogPane;

public class NomadDialog extends DialogPane implements Runnable {

	private String resultCode = null;
	private String infoTitle = "Info";
	private String infoText  = null;
	private String title = null;
	private int padding = 10;
	private boolean scrollbarsEnabled = true;

	public final static String RESULT_OK 		= "Ok";
	public final static String RESULT_CANCEL 	= "Cancel";
	
	NomadDialogWindow window;

	public NomadDialog() {
		setFont(new Font("SansSerif", Font.PLAIN, 11));
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	public boolean isOkResult() {
		return resultCode==null ? false : resultCode.equals(RESULT_OK);
	}
	
	public boolean isCancelResult() {
		return resultCode==null || resultCode.equals(RESULT_CANCEL);
	}
	
	public int getPadding() {
		return padding;
	}
	
	public void setPadding(int p) {
		this.padding = p;
	}
	
	public boolean isScrollbarEnabled() {
		return scrollbarsEnabled;
	}
	
	public void setScrollbarEnabled(boolean enabled) {
		scrollbarsEnabled = enabled;
	}
	
	public boolean setResult(String resultCode) {
		this.resultCode = resultCode;
		return true;
	}

	public String getResult() {
		return resultCode;
	}
	
	public void invoke(String[] options) {
		NomadDialogWindow.invoke(this, options);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setInfo(String infoTitle, String infoText) {
		this.infoTitle = infoTitle;
		this.infoText = infoText;
	}

	public String getInfoText() {
		return infoText;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public String getTitle() {
		return title;
	}
	
	public void close() {
		window.done();
	}

	public void run() {
		
	}
	
	public JPanel newGroup(String title) {
		JLabel lbltitle = new JLabel(title);
		lbltitle.setFont(new Font("SansSerif", Font.BOLD,11));
		return newGroup(lbltitle);
	}
	
	public JPanel newGroup(JComponent title) {
		JPanel group = new JPanel();
		group.setFont(getFont());
		group.setBorder(BorderFactory.createEtchedBorder());
		group.setBackground(getBackground());
		group.setLayout(new BoxLayout(group, BoxLayout.PAGE_AXIS));
		if (title!=null)
			addRow(group, new Component[]{title, Box.createHorizontalGlue()});
		return group;
	}
	
	public void addRow(JPanel group, Component[] components) {
		JPanel row = new JPanel();
		row.setFont(getFont());
		row.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
		row.setBackground(getBackground());
		for (int i=0;i<components.length;i++) {
			row.add(components[i]);
		}
		group.add(row);
		//group.add(Box.createVerticalStrut(10));
	}
	
	public void addRow(JPanel group, String title, Component[] components) {
		JLabel lbltitle = new JLabel(title==null?"":title);
		lbltitle.setFont(new Font("SansSerif", Font.PLAIN,10));
		Component[] c = new Component[2+components.length];
		for (int i=0;i<components.length;i++)
			c[i+2]=components[i];
		c[0] = lbltitle;
		c[1] = Box.createHorizontalGlue();
		addRow(group, c);
	}
	
	public void addRow(JPanel group, String title, Component component) {
		addRow(group, title, new Component[]{component});
	}
	
}
