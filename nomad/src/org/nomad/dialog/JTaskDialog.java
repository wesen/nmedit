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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class JTaskDialog extends JDialog {

	private TaskModel model = null;
	private int task = 0;
	private boolean flagStayOpen = true;
	private JLabel lblDescription = null;
	private JLabel lblCurrentTask = null;
	private JLabel lblCurrentTaskName = null;
	private JProgressBar pgBar = null;
	private boolean cancelRequested = false;
	private JButton btnStartStop = null;
	private JButton btnClose = null;
	private final static String TEXT_START = "Start";
	private final static String TEXT_STOP = "Stop";
	private final static String TEXT_CLOSE = "Close";
	private Thread thread = null;
	
	public JTaskDialog(JFrame owner, TaskModel model) {
		super(owner);
		this.model = model;
		setTitle(model.getDescription());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Dimension size = new Dimension(360,240);
		setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width-getWidth())/2, (screen.height-getHeight())/2);

		// create components
		createComponents((JComponent)getContentPane());
		validate();
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
		btnStartStop = new JButton(TEXT_STOP);
		btnClose = new JButton(TEXT_CLOSE);
		btnClose.setEnabled(false);
		btnStartStop.setDefaultCapable(true);

		btnStartStop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				setCanceled(!cancelRequested);
			}});
		
		btnClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}});
		
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
				
		JPanel btnPane = new JPanel();
		btnPane.setLayout( new BoxLayout(btnPane, BoxLayout.LINE_AXIS));
		btnPane.add(Box.createHorizontalGlue());
		btnPane.add(btnStartStop);
		btnPane.add(Box.createHorizontalStrut(10));
		btnPane.add(btnClose);
		btnPane.add(Box.createHorizontalStrut(10));
		btnPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnPane.setSize(Integer.MAX_VALUE,50);
		btnPane.setBorder(BorderFactory.createEtchedBorder());
		
		contentPane.add(progressPane);
		contentPane.add(Box.createVerticalStrut(10));
		contentPane.add(btnPane);
	}

	public static boolean processTasks(JFrame owner, TaskModel model) {
		return processTasks(owner, model, false);
	}

	public static boolean processTasks(JFrame owner, TaskModel model, boolean stayOpen) {
		JTaskDialog dialog = new JTaskDialog(owner, model);
		dialog.setStayOpen(stayOpen);
		dialog.setVisible(true);
		dialog.processQueue();
		dialog.setModal(true);
		return !dialog.cancelRequested;
	}

	private void setStayOpen(boolean stayOpen) {
		this.flagStayOpen = stayOpen;
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
	
	private void setCanceled(boolean cancel) {
		cancelRequested = cancel;
		if (!cancelRequested) {
			processQueue();
		}
		btnStartStop.setText(cancelRequested?TEXT_START:TEXT_STOP);
	}

	private void processQueue() {
		if (thread !=null ) {
			return;
		}

		thread = new Thread(new Runnable(){
			public void run() {

				JTaskDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				btnClose.setEnabled(false);
				try {
					while ((!cancelRequested) && (task<model.getTaskCount())) {
						status();
						repaint();
						model.run(task);
						task++;
					}
				} catch (Throwable t) {
					t.printStackTrace();
					flagStayOpen = true;
					setCanceled(true);
					status(t);
					task++;
				}
				JTaskDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				threadExited();
			}});
		thread.start();
	}
	
	private void threadExited() {
		thread = null;
		if (!((task<model.getTaskCount())||flagStayOpen)) {
			setVisible(false);
		} else {
			btnStartStop.setText(TEXT_START);
			btnClose.setEnabled(true);
		}
	}
	
}
