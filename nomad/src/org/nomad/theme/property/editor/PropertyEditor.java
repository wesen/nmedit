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
 * Created on Jan 8, 2006
 */
package org.nomad.theme.property.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import org.nomad.theme.property.Property;

/**
 * @author Christian Schneider
 */
public abstract class PropertyEditor {

	private Property property = null;
	private ArrayList cellEditorListenerList = new ArrayList();
	private boolean flagAutoWritebackHook = false;

	public PropertyEditor(Property p) {
		this.property = p;
		addCellEditorListener(new CellEditorListener(){
			public void editingStopped(ChangeEvent event) {
				if (flagAutoWritebackHook) {
					// enabled
					property.setValue(getEditorValue());
				}
			}

			public void editingCanceled(ChangeEvent event) {
				// nothing to do here
			}});
	}
	
	public Property getProperty() {
		return property;
	}

	public void setAutoWritebackHook(boolean enable) {
		flagAutoWritebackHook = enable;
	}
	
	public boolean getAutoWritebackHook() {
		return flagAutoWritebackHook;
	}
	
	public abstract Object getEditorValue();
	
	public void addCellEditorListener(CellEditorListener l) {
		if (!cellEditorListenerList.contains(l))
			cellEditorListenerList.add(l);
	}
	
	public void removeCellEditorListener(CellEditorListener l) {
		cellEditorListenerList.remove(l);
	}

	public void fireEditingCanceled() {
		fireEditingCanceled(new ChangeEvent(this));
	}

	public void fireEditingStopped() {
		fireEditingStopped(new ChangeEvent(this));
	}
	
	public void fireEditingCanceled(ChangeEvent event) {
		for (int i=cellEditorListenerList.size()-1;i>=0;i--)
			((CellEditorListener)cellEditorListenerList.get(i)).editingCanceled(event);
	}
	
	public void	fireEditingStopped(ChangeEvent event) {
		for (int i=cellEditorListenerList.size()-1;i>=0;i--)
			((CellEditorListener)cellEditorListenerList.get(i)).editingStopped(event);
 	}
	
	public abstract JComponent getEditorComponent();
	
	public static class TextEditor extends PropertyEditor {

		private JTextField textField = new JTextField();

		public TextEditor(Property p) {
			super(p);

			textField.setFont(new Font("SansSerif",Font.PLAIN, 11));
			textField.setForeground(Color.BLUE);
			textField.setBorder(null);
			
			textField.addKeyListener(new KeyAdapter(){

				public void keyTyped(KeyEvent event) {
					switch (event.getKeyCode()) {
						case KeyEvent.VK_ENTER: {
							textField.removeKeyListener(this);
							fireEditingStopped(); break;
						}
						case KeyEvent.VK_ESCAPE: {
							textField.removeKeyListener(this);
							fireEditingCanceled(); break;
						}
					}
				}
			});
			
			textField.setText(p.getValueString());
			textField.setSelectionStart(0);
			textField.setSelectionEnd(textField.getText().length()-1);
		}
		public Object getEditorValue() { return textField.getText(); }		
		public JComponent getEditorComponent() { return textField; }	
	}
	
	public static class TextAreaEditor extends PropertyEditor {

		private JTextArea textArea = new JTextArea();

		public TextAreaEditor(Property p, String text) {
			this(p);
			textArea.setText(text);
		}

		public TextAreaEditor(Property p) {
			super(p);

			textArea.setFont(new Font("SansSerif",Font.PLAIN, 11));
			textArea.setForeground(Color.BLUE);
			textArea.setBorder(null);
			
			textArea.addKeyListener(new KeyAdapter(){

				public void keyTyped(KeyEvent event) {
					switch (event.getKeyCode()) {
						/*case KeyEvent.VK_ENTER: {
							textArea.removeKeyListener(this);
							fireEditingStopped(); break;
						}*/
						case KeyEvent.VK_ESCAPE: {
							textArea.removeKeyListener(this);
							fireEditingCanceled(); break;
						}
					}
				}
			});
			
			textArea.setText(p.getValueString());
			textArea.setSelectionStart(0);
			textArea.setSelectionEnd(textArea.getText().length()-1);
		}
		public Object getEditorValue() { return textArea.getText(); }		
		public JComponent getEditorComponent() { return textArea; }	
	}
	
	public static class ComboBoxEditor extends PropertyEditor {
		private JComboBox comboBox = null;
		private Object[] items = null;

		public ComboBoxEditor(Property p, Object[] items, Object selected) {
			this(p,items);
			comboBox.setSelectedItem(selected);
		}
		
		public ComboBoxEditor(Property p, Object[] items) {
			super(p);
			this.items = items;
			comboBox = new JComboBox(items);
			comboBox.setFont(new Font("SansSerif",Font.PLAIN, 11));
			comboBox.setForeground(Color.BLUE);
			comboBox.setSelectedItem(p.getValue());
			comboBox.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent event) {
					fireEditingStopped();
				}});
			comboBox.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent event) {
					switch (event.getKeyCode()) {
						case KeyEvent.VK_ENTER: {
							fireEditingStopped(); break;
						}
						case KeyEvent.VK_ESCAPE: {
							fireEditingCanceled(); break;
						}
					}
				}
			});

		}
		public Object getEditorValue() { return items[comboBox.getSelectedIndex()]; }		
		public JComponent getEditorComponent() { return comboBox; }	
	}
	
	public static class CheckBoxEditor extends PropertyEditor {
		private JCheckBox checkBox = null;
		public CheckBoxEditor(Property p, boolean initiallyChecked) {
			super(p);
			checkBox = new JCheckBox(p.getName());
			checkBox.setFont(new Font("SansSerif",Font.PLAIN, 11));
			checkBox.setForeground(Color.BLUE);
			checkBox.setSelected(initiallyChecked);
			checkBox.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					fireEditingStopped();
				}});
			checkBox.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent event) {
					switch (event.getKeyCode()) {
						case KeyEvent.VK_ENTER: {
							fireEditingStopped(); break;
						}
						case KeyEvent.VK_ESCAPE: {
							fireEditingCanceled(); break;
						}
					}
				}
			});

		}
		public Object getEditorValue() { return new Boolean(checkBox.isSelected()); }		
		public JComponent getEditorComponent() { return checkBox; }	
	}
	
}
