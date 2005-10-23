package editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.CellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nomad.gui.BasicUI;
import nomad.gui.LabelUI;
import nomad.gui.property.InvalidValueException;
import nomad.gui.property.Property;
import nomad.gui.property.PropertyMap;

public class PropertyTablePane extends JPanel {

	private JTable table = null;
	private JScrollPane scrollpane = null;
	private ModulePane modulePane = null;
	private OptionsTableModel dataModel = null;
	private JComboBox cbUIelements = null;
	
	public PropertyTablePane() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(200, 400));
		dataModel = new OptionsTableModel();
		table = new JTable(dataModel);
	    table.getColumnModel().getColumn(0).setHeaderValue("Property");
	    table.getColumnModel().getColumn(1).setHeaderValue("Value");
	    
	    scrollpane = new JScrollPane(table);
	    scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

	    this.add(scrollpane, BorderLayout.CENTER);

	    
	    TableColumn col = table.getColumnModel().getColumn(1);
	    //col.setCellEditor(dataModel);
	    col.setCellEditor(dataModel);
	    
	    // If the cell should appear like a combobox in its
	    // non-editing state, also set the combobox renderer
	    //col.setCellRenderer(dataModel);
	    
	    cbUIelements = new JComboBox();
	    cbUIelements.addActionListener(new SelectUIElementListener());
	    reloadComboBox();
	    this.add(cbUIelements, BorderLayout.NORTH);
	}
	
	private void reloadComboBox() {
		cbUIelements.removeAllItems();
		if (modulePane!=null)
			for (int i=0;i<modulePane.getUIComponentCount();i++)
				cbUIelements.addItem(modulePane.getUIComponent(i));
	}

	public void setModulePane(ModulePane modulePane) {
		this.modulePane=modulePane;
		reloadComboBox();
		updateEditor();
	}
	
	private class SelectUIElementListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			dataModel.setUIElement((BasicUI)cbUIelements.getSelectedItem());
		}
	}
	
	private void updateEditor() {
		//
	}
	
	class OptionsTableModel extends AbstractTableModel implements ChangeListener, TableCellEditor {
		
		private BasicUI uicomponent = null;
		private Vector properties = null;
		
		private void setListener(boolean remove) {
			if (properties!=null)
				if (remove)
					for (int i=0;i<properties.size();i++)
						((Property)properties.get(i)).removeChangeListener(this);
				else
					for (int i=0;i<properties.size();i++)
						((Property)properties.get(i)).addChangeListener(this);
		}
		
		public void setUIElement(BasicUI uicomponent) {
			cancelCellEditing();
			setListener(true); // remove listener
			this.uicomponent = uicomponent;
			properties = new Vector();
			if (uicomponent!=null) {
				PropertyMap map = uicomponent.getProperties();
				String[] names = map.getPropertyNames();
				for (int i=0;i<names.length;i++)
					properties.add(map.getProperty(names[i]));
			}

			setListener(false); // add listener
			this.fireTableDataChanged();
		}
		
		public int getRowCount() {
			return (uicomponent==null)?0:uicomponent.getProperties().getPropertyNames().length;
		}

		public Object getValueAt(int row, int column) {
			if (uicomponent==null)
				return null;
			else {
				if (column==0) {
					PropertyMap map = uicomponent.getProperties();
					String name = map.getPropertyNames()[row];
					return name;
				} else {
					return ((Property) properties.get(row)).getStringRepresentation();
				}
			}
		}

		public int getColumnCount() {
			return 2;
		}	
		
		public boolean isCellEditable(int row, int col) {
			switch (col) {
				case 0: //Name
					return false;
			    case 1: //value
			    	return true;
			    default:
			    	return false;
			}
		}
		
		public void setValueAt(Object value, int row, int col) {
			switch (col) {
			    case 0: //Name
			     break;
			    case 1: //value
			    	try {
						((Property)properties.get(row)).setValue(value);
					} catch (InvalidValueException e) {
						e.printStackTrace();
					}
			     break;
			}
		}

		public void stateChanged(ChangeEvent arg0) {
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
			notifyCellEditorListeners(false, new ChangeEvent(table));
			return true;
		}

		public void cancelCellEditing() {
			notifyCellEditorListeners(true, new ChangeEvent(table));
		}
		
		private void notifyCellEditorListeners(boolean canceledElseStopped, ChangeEvent event) {
			for (int i=0;i<cellEditorListeners.size();i++) {
				CellEditorListener listener = (CellEditorListener)
					cellEditorListeners.get(i);
				if (canceledElseStopped)
					listener.editingCanceled(event);
				else
					listener.editingStopped(event);
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
			Property p = (Property) properties.get(row);
			if (!p.hasOptions()) {
				JTextField textField = new JTextField(""+value);
				textField.addActionListener(
						new ActionListener() {
							private final int rrow = row;
							public void actionPerformed(ActionEvent event) {
								cellEditorValue = ((JTextField)event.getSource()).getText();
								try {
									((Property)properties.get(rrow)).setValue(cellEditorValue);
									stopCellEditing();
								} catch (InvalidValueException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				);
				
				return textField;
			} else {
				Object[] options = p.getOptions();
				JComboBox cbox = new JComboBox();
				int selectIndex = 0;
				for (int i=0;i<options.length;i++) {
					if (options[i].equals(value))
						selectIndex=i;
					cbox.addItem(options[i]);
				}
				cbox.setSelectedIndex(selectIndex);
				cbox.addActionListener(
						new ActionListener() {
							private final int rrow = row;
							
							public void actionPerformed(ActionEvent event) {
								cellEditorValue = ((JComboBox)event.getSource()).getSelectedItem();
								try {
									((Property)properties.get(rrow)).setValue(cellEditorValue);
									stopCellEditing();
								} catch (InvalidValueException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				);
				return cbox;
			}
		}

	}
	
}
