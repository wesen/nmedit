package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import nomad.gui.BasicUI;

public class PropertyTablePane extends JPanel {

	private JTable table = null;
	private JScrollPane scrollpane = null;
	private ModulePane modulePane = null;
	private PropertyTableModel dataModel = null;
	private JComboBox cbUIelements = null;
	
	public PropertyTablePane() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(200, 400));
		dataModel = new PropertyTableModel();
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
	
}
