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

import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.Value;
import org.nomad.theme.property.editor.Editor;
import org.nomad.theme.property.editor.EditorEvent;
import org.nomad.theme.property.editor.EditorListener;

class EditWindowDialog extends JDialog implements ActionListener, CellEditorListener, EditorListener {

	private Editor propertyEditor = null;
	private JButton modSave = new JButton("Save");
	private JButton modCancel = new JButton("Cancel");
	private Property property = null;
	private NomadComponent component;
	
	public EditWindowDialog(JFrame frame, NomadComponent component, NomadPropertyEditor editor, Property property) {
		super(frame, "Editor:"+property.getName());
		this.component = component;
		//this.editor = editor;
		this.property = property;
		propertyEditor = property.newEditor(component);

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
		
		getPropertyEditor().addEditorListener(this);
		

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

	Editor getPropertyEditor() {
		return propertyEditor;
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==modCancel) {
			getPropertyEditor().fireEditorEvent(EditorEvent.EventId.EDITING_CANCELED);
		} else {
            getPropertyEditor().fireEditorEvent(EditorEvent.EventId.EDITING_STOPPED);
		}
	}

	public void editingStopped(ChangeEvent event) {
        Value v = getPropertyEditor().getValue();
        v.assignTo(getComponent());
		shutDown();
	}

	private NomadComponent getComponent() {
		return component;
	}

	public void editingCanceled(ChangeEvent event) {
		shutDown();
	}
	
	public void shutDown() {
		getPropertyEditor().removeEditorListener(this);
		setVisible(false);
	}

    public void editorChanged( EditorEvent e )
    {
        shutDown();
    }

}