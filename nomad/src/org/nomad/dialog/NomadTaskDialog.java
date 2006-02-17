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
 * Created on Jan 18, 2006
 */
package org.nomad.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class NomadTaskDialog extends NomadDialog {

	private TaskModel model = null;
	private int task = 0;
	private JLabel lblDescription = null;
	private JLabel lblCurrentTask = null;
	private JLabel lblCurrentTaskName = null;
	private JProgressBar pgBar = null;
	private Thread thread = null;
	
	public NomadTaskDialog(TaskModel model) {
		this.model = model;
		setTitle(model.getDescription());
		
		Dimension size = new Dimension(360,240);
		setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width-getWidth())/2, (screen.height-getHeight())/2);

		// create components
		createComponents(this);
		validate();
	}
	
	public void invoke() {
		super.invoke(null);
	}

	private void createComponents(JComponent contentPane) {
		contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		lblDescription = new JLabel(model.getDescription());
		lblCurrentTask = new JLabel();
		lblCurrentTaskName = new JLabel();
		pgBar = new JProgressBar();
		pgBar.setMinimum(0);
		pgBar.setMaximum(model.getTaskCount()-1);
		pgBar.setValue(0);
		pgBar.setStringPainted(true);
		
		lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblCurrentTask.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblCurrentTaskName.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblDescription.setLocation(0,0);
		lblCurrentTask.setLocation(0,0);
		lblCurrentTaskName.setLocation(0,0);
		pgBar.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel textPane = new JPanel();
		textPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		textPane.setBackground(null);
		textPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx=1;c.weighty=1;
		//c.insets=new Insets(10,10,10,10);
		//c.anchor = GridBagConstraints.LINE_START;
		c.anchor=GridBagConstraints.LINE_START;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridwidth=4; c.gridheight=1; c.gridx=1; c.gridy=1; c.gridwidth=GridBagConstraints.REMAINDER;
		textPane.add(setupFont(lblDescription,true),c);
		c.fill=GridBagConstraints.NONE;
		c.gridwidth=1; c.gridheight=1; c.gridx=1; c.gridy=2;
		textPane.add(setupFont(new JLabel("Task:"),true),c);
		c.gridwidth=3; c.gridheight=1; c.gridx=2; c.gridy=2; c.gridwidth=GridBagConstraints.REMAINDER;
		textPane.add(setupFont(lblCurrentTaskName,false),c);
		c.gridwidth=1; c.gridheight=1; c.gridx=1; c.gridy=3;
		textPane.add(setupFont(new JLabel("Progress:"),true),c);
		c.gridwidth=3; c.gridheight=1; c.gridx=2; c.gridy=3; c.gridwidth=GridBagConstraints.REMAINDER;
		textPane.add(setupFont(lblCurrentTask,false),c);
		c.gridwidth=4; c.gridheight=1; c.gridx=1; c.gridy=4; c.gridwidth=GridBagConstraints.REMAINDER;
		c.fill=GridBagConstraints.HORIZONTAL;
		textPane.add(pgBar,c);
		
		//pgBar.setSize(100,20);
		JPanel progressPane = new JPanel();
		progressPane.setLayout(new BoxLayout(progressPane, BoxLayout.PAGE_AXIS));		

		progressPane.add(textPane);
		progressPane.setBorder(BorderFactory.createEtchedBorder());
		progressPane.setBackground(Color.WHITE);
				
		contentPane.add(progressPane);
		contentPane.add(Box.createVerticalStrut(10));
	}

	private void status() {
		lblCurrentTask.setText((task+1)+"/"+model.getTaskCount());
		lblCurrentTaskName.setText(model.getTaskName(task));
		pgBar.setValue(task);
	}
	
	private JComponent setupFont(JComponent c, boolean isLabel) {
		Font f = new Font("SansSerif", isLabel?Font.BOLD:Font.PLAIN, 11);
		c.setFont(f);
		return c;
	}
	
	private void status(Throwable t) {
		lblCurrentTaskName.setText(lblCurrentTaskName.getText()+" ("+t.getClass().getSimpleName()+" :"+t.getMessage()+")");
		lblCurrentTaskName.setForeground(Color.RED);
	}

	public void run() {
		processQueue();
	}

	private void processQueue() {
		if (thread !=null ) {
			return;
		}
		
		thread = new Thread(
				new Runnable(){
			public void run() {

				NomadTaskDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				while (task<model.getTaskCount()) {
					try {
						status();
						repaint();
						model.run(task);
						task++;
					} catch (Throwable t) {
						task++;
						error(t);
						return;
					}
				}
				NomadTaskDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				close();
			}});
		thread.start();
	}

	private void error(Throwable t) {
		status(t);
		ExceptionNotificationDialog dialog = new ExceptionNotificationDialog(t);

		if (task>=model.getTaskCount()-1) {
			dialog.invoke();
			NomadTaskDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			close();
		} else {
			String ignore="Ignore";
			
			dialog.invoke(new String[]{ignore,":Abort"});
			if (ignore.equals(dialog.getResult())) {
				
				thread = null;
				
				processQueue();
			} else {
				NomadTaskDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				close();
			}
		}
	}
	
}
