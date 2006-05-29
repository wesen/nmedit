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
package net.sf.nmedit.nomad.main.dialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class NomadTaskDialog extends NomadDialog {

	private TaskModel model = null;
	private int task = 0;
	private JLabel lblCurrentTask = null;
	private JLabel lblCurrentTaskName = null;
	private JProgressBar pgBar = null;
	private Thread thread = null;
	
	public NomadTaskDialog(TaskModel model) {
		setScrollbarEnabled(false);
		this.model = model;
		setTitle(model.getDescription());
		setPreferredSize(new Dimension(360,200));

		lblCurrentTask = new JLabel();
		lblCurrentTaskName = new JLabel();
		pgBar = new JProgressBar();
		pgBar.setMinimum(0);
		pgBar.setMaximum(model.getTaskCount()-1);
		pgBar.setValue(0);
		pgBar.setStringPainted(true);
		
		JPanel group;
		group = newGroup("Current");
		addRow(group,"Name", lblCurrentTaskName);
		addRow(group,"Task", lblCurrentTask);
		addRow(group,"Progress", pgBar);
		add(group);
	}
	
	public void invoke() {
		super.invoke(null);
	}


	private void status() {
		lblCurrentTask.setText((task+1)+"/"+model.getTaskCount());
		lblCurrentTaskName.setText(model.getTaskName(task));
		pgBar.setValue(task);
		//repaint();
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
