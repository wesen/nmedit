package editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import nomad.gui.model.component.AbstractUIComponent;
import nomad.gui.model.property.Property;
import nomad.model.descriptive.DParameter;

public class PropertyTableModel extends AbstractTableModel implements ChangeListener, TableCellEditor {

	private AbstractUIComponent uicomponent = null;
	
	public PropertyTableModel() {
		;
	}
	
	/**
	 * Installs listener if 'add' is set to true else remove listener.
	 * @param add true: add listener, false: remove listener
	 */
	private void setListener(boolean add) {
		if (uicomponent!=null) {
			for (int i=0;i<uicomponent.getPropertyCount();i++) 
				if (add)
					uicomponent.getProperty(i).addChangeListener(PropertyTableModel.this);
				else
					uicomponent.getProperty(i).removeChangeListener(PropertyTableModel.this);
		}
	}

	/**
	 * Sets the editable ui component
	 * @param uicomponent -
	 */
	public void setUIElement(AbstractUIComponent uicomponent) {
		install(uicomponent);
	}
	
	private void uninstall() {
		cancelCellEditing();	// stop any editing
		setListener(false); 	// remove listener		
	}
	
	public void install(AbstractUIComponent uicomponent) {
		uninstall();
		this.uicomponent = uicomponent; // store component 
		setListener(true); // add listener
		this.fireTableDataChanged(); // update
	}
	
	public int getRowCount() {
		return (uicomponent==null)?0:uicomponent.getPropertyCount();
	}

	public int getColumnCount() {
		return 2;
	}
	
	public boolean isCellEditable(int row, int col) {
		return col == 1;
	}

	public Object getValueAt(int row, int column) {
		if (uicomponent!=null) {
			Property p = uicomponent.getProperty(row);
			return column==0 ? p.getDisplayName() : p.getValue();
		}
		return null;
	}
	
	public void setValueAt(Object value, int row, int col) {
		/* is already stored by our custom cell editor */
	}

	public void stateChanged(ChangeEvent event) {
		this.fireTableDataChanged();
	}

	// cell editor
	
	private Object cellEditorValue = null;

	public Object getCellEditorValue() {
		return cellEditorValue;
	}

	public boolean isCellEditable(EventObject event) {
		return true;
	}

	public boolean shouldSelectCell(EventObject event) {
		return true;
	}

	public boolean stopCellEditing() {
		notifyCellEditorListeners(false, new ChangeEvent(this));
		return true;
	}

	public void cancelCellEditing() {
		notifyCellEditorListeners(true, new ChangeEvent(this));
	}
	
	private void notifyCellEditorListeners(boolean canceledElseStopped, ChangeEvent event) {
		if (canceledElseStopped) {
			for (int i=0;i<cellEditorListeners.size();i++) {
				((CellEditorListener) cellEditorListeners.get(i))
					.editingCanceled(event);
			}
		} else {
			for (int i=0;i<cellEditorListeners.size();i++) {
				((CellEditorListener) cellEditorListeners.get(i))
					.editingStopped(event);
			}
		}
	}

	private Vector cellEditorListeners = new Vector();

	public void addCellEditorListener(CellEditorListener listener) {
		if (!cellEditorListeners.contains(listener))
			cellEditorListeners.add(listener);
	}

	public void removeCellEditorListener(CellEditorListener listener) {
		if (cellEditorListeners.contains(listener))
			cellEditorListeners.remove(listener);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, final int row, int column) {
		cellEditorValue = value;
		Property property = uicomponent.getProperty(row);
		return property.getAllValues()!=null
			? (Component) newComboBoxCellEditor(value, property)
			: (Component) newTextFieldCellEditor(value, property);
	}

	private JTextField newTextFieldCellEditor(final Object value, final Property property) {
		final JTextField textField = new JTextField(""+value); // create component
		textField.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					//cellEditorValue = textField.getText()
					property.setValue(textField.getText()/*, PropertyTableModel.this*/);
					stopCellEditing();
				}
			}
		);
		
		return textField;
	}

	private JComboBox newComboBoxCellEditor(Object value, final Property property) {
		Object[] options = property.getAllValues();
		final JComboBox cbox = new JComboBox();
		int selectIndex = 0;
		for (int i=0;i<options.length;i++) {
			if (options[i].equals(value))
				selectIndex=i;
			cbox.addItem(options[i]);
		}
		cbox.setSelectedIndex(selectIndex);
		cbox.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					cellEditorValue = cbox.getSelectedItem();
					property.setValue(cellEditorValue/*, PropertyTableModel.this*/);
					stopCellEditing();
				}
			}
		);
		return cbox;
	}
	
}
