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
 * Created on Jan 12, 2006
 */
package org.nomad.editor.views.property;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import org.nomad.theme.property.Property;
import org.nomad.theme.property.editor.PropertyEditor;

class EditWindowDialog extends JDialog implements ActionListener, CellEditorListener {

	private PropertyEditor propertyEditor = null;
	private JButton modSave = new JButton("Save");
	private JButton modCancel = new JButton("Cancel");
	private Property property = null;
	
	public EditWindowDialog(JFrame frame, NomadPropertyEditor editor, Property property) {
		super(frame, "Editor:"+property.getName());
		//this.editor = editor;
		this.property = property;
		propertyEditor = property.getEditor();

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getPropertyEditor().getEditorComponent(), BorderLayout.CENTER);
		
		JPanel paneAction = new JPanel();
		paneAction.setLayout(new GridLayout(1,2));
		paneAction.add(modSave);
		paneAction.add(modCancel);
		getContentPane().add(paneAction, BorderLayout.SOUTH);
		
		modSave.addActionListener(this);
		modCancel.addActionListener(this);
		//this.editor.validate();
		
		getPropertyEditor().addCellEditorListener(this);
		

		setSize(640/2, 480/2);
		pack();

	    // center window
	    Dimension screensz  = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension framesz   = getSize();
	
	    framesz.height = Math.min(framesz.height, screensz.height);
	    framesz.width  = Math.min(framesz.width,  screensz.width);
	
	    setLocation(
	      (screensz.width-framesz.width)/2,
	      (screensz.height-framesz.height)/2
	    );
		
	}

	PropertyEditor getPropertyEditor() {
		return propertyEditor;
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==modCancel) {
			getPropertyEditor().fireEditingCanceled();
		} else {
			getPropertyEditor().fireEditingStopped();
		}
	}

	public void editingStopped(ChangeEvent event) {
		property.setValue(getPropertyEditor().getEditorValue());
		shutDown();
	}

	public void editingCanceled(ChangeEvent event) {
		shutDown();
	}
	
	public void shutDown() {
		getPropertyEditor().removeCellEditorListener(this);
		setVisible(false);
	}

}