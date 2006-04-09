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
package org.nomad.editor.views.property;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.theme.property.Value;
import org.nomad.theme.property.editor.Editor;
import org.nomad.theme.property.editor.EditorEvent;
import org.nomad.theme.property.editor.EditorListener;

/**
 * @author Christian Schneider
 */
public class NomadPropertyEditor extends JPanel {

	private CPETableModel model = new CPETableModel();
	private JTable table = new JTable(model);
	private JScrollPane sc = new JScrollPane(table);
	private JFrame ownerFrame = null;
	private PropertyEditWindowAction pwea = new PropertyEditWindowAction();
	private PropertySet thePropertySet = null;
	private ArrayList<Property> properties = new ArrayList<Property>();
	private NomadComponent component;
	
	public NomadPropertyEditor(JFrame ownerFrame) {
		super();
		this.ownerFrame = ownerFrame;
		setLayout(new BorderLayout());
		add(sc, BorderLayout.CENTER);
		table.setPreferredSize(new Dimension(240, table.getRowHeight()*table.getRowCount()));
		table.setEditingColumn(1);
		table.setDefaultEditor(Object.class, new CPECellEditor());
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
				c.setFont(new Font("SansSerif",Font.PLAIN, 11));
				c.setForeground((column==0)?Color.BLACK:Color.BLUE);
				return c;
		}});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		table.addMouseListener(pwea);
		validate();
	}

	private class PropertyEditWindowAction extends MouseAdapter {
		public void mouseClicked(MouseEvent me) {
			int column = table.columnAtPoint(me.getPoint());
			int row = table.rowAtPoint(me.getPoint());
			
			if ((column==1)&&(0<=row)&&(row<table.getRowCount())) {
				Property property = properties.get(row);
                
                Editor e = property.newEditor(getComponent());
                
				if (e.isDialogModeEnabled()) { // we show the dialog window
					
					EditWindowDialog ewd = new EditWindowDialog(getOwnerFrame(), getComponent(), NomadPropertyEditor.this, property);

					//ewd.getPropertyEditor().setAutoWritebackHook(true);
					ewd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					ewd.setModal(true);
					ewd.setVisible(true);
				}
			}
		}
	}

	JFrame getOwnerFrame() {
		return ownerFrame;
	}
	
	public void updateProperties() {
		if (thePropertySet!=null) {
			PropertySet s = thePropertySet;
			setEditingPropertySet(component, null);
			setEditingPropertySet(component, s);
		}
	}
	
	public NomadComponent getComponent() {
		return component;
	}

	public void setEditingPropertySet(NomadComponent component, PropertySet thePropertySet) {
		this.component = component;
		if (this.thePropertySet!=thePropertySet) {
			properties.clear();
			this.thePropertySet=thePropertySet;
			if (thePropertySet!=null) {
				for (Property p : thePropertySet) {
					properties.add(p);
				}
			}
			tableUpdate();
		}
	}

	protected void tableUpdate() {
		table.setPreferredSize(new Dimension(240, table.getRowHeight()*table.getRowCount()));
		model.fireTableDataChanged();
	}
	
	private class CPETableModel extends AbstractTableModel {
		
	    public String getColumnName(int col) { return col==0?"Property":"Value"; }    
	    public int getColumnCount() { return 2; }
		public int getRowCount()	{ return properties.size(); }
		public boolean isCellEditable(int row, int col) {
			if (col==0) return false;
			else {
                Editor e = properties.get(row).newEditor(getComponent());
                
				return !e.isDialogModeEnabled();
			} 
		}
		public Object getValueAt(int row, int col) {
			if (thePropertySet==null) {
				return null;
			} else {
				Property property = properties.get(row);
				return col == 0 ? property.getName() : property.encode(getComponent());
			}
		}
		
        /*
		public void setValueAt(Object value, int row, int col) {
			if (thePropertySet!=null) {
				if (value instanceof String) {
					Property property = (Property) properties.get(row);
					property.setValue(getComponent(), (String)value);
				}
			}
		}
        */
        public void setValueAt(Object value, int row, int col) {
            if (value instanceof Value) {
                ((Value)value).assignTo(getComponent());
            }
        }
	}
	
	private class CPECellEditor implements TableCellEditor, EditorListener {
		
		private Editor pe = null;
        private ArrayList<CellEditorListener> listenerList = new ArrayList<CellEditorListener>();

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  {
			pe = null;
            listenerList.clear();
			if (column==1 && thePropertySet!=null) {
				Property property = (Property) properties.get(row);
                pe = property.newEditor(getComponent());
                pe.addEditorListener(this);
				if (!pe.isDialogModeEnabled()) {
					return pe.getEditorComponent();
				}
			}
			return null; // ups, this should not happen
		}

		public Object getCellEditorValue() {
			return pe==null ? null : pe.getValue();
		}

		public boolean isCellEditable(EventObject anEvent)  {	
			return true;
		}

		public boolean shouldSelectCell(EventObject anEvent)  {
			return true;
		}

		public boolean stopCellEditing() {
			if (pe==null) return false;
            pe.fireEditorEvent(EditorEvent.EventId.EDITING_STOPPED);
			// pe.fireEditingStopped(new ChangeEvent(this));
			return true;
		}

		public void cancelCellEditing() {
            if (pe!=null) pe.fireEditorEvent(EditorEvent.EventId.EDITING_CANCELED);
		}

		public void addCellEditorListener(CellEditorListener l) {
			if (pe!=null) 
                listenerList.add(l);
		}

		public void removeCellEditorListener(CellEditorListener l) {
			if (pe!=null) 
                listenerList.remove(l);
		}

        public void editorChanged( EditorEvent e )
        {
            if (!listenerList.isEmpty())
            {
                ChangeEvent event = new ChangeEvent(this);

                switch (e.getEventId())
                {
                    case EDITING_STOPPED:
                        for (int i=listenerList.size()-1;i>=0;i--)
                            listenerList.get(i).editingStopped(event);
                        break;
                    case EDITING_CANCELED:
                    for (int i=listenerList.size()-1;i>=0;i--)
                        listenerList.get(i).editingCanceled(event);
                        break;
                }
            }
        }
	}
	
}
