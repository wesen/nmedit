package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import nomad.gui.model.component.AbstractUIComponent;

public class PropertyTablePane extends JPanel {

	private JTable table = null;
	private JScrollPane scrollpane = null;
	private ModulePane modulePane = null;
	private PropertyTableModel dataModel = null;
	private JComboBox cbUIelements = null;
	
	private JButton btnRemove = new JButton("Remove");
	private WorkBenchPane wbp = null;
	private JPanel actionPane = new JPanel();
	
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

	    btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				AbstractUIComponent component = (AbstractUIComponent)cbUIelements.getSelectedItem();
				doRemove(component);
			}});
	    
	    TableColumn col = table.getColumnModel().getColumn(1);
	    //col.setCellEditor(dataModel);
	    col.setCellEditor(dataModel);
	    
	    // If the cell should appear like a combobox in its
	    // non-editing state, also set the combobox renderer
	    //col.setCellRenderer(dataModel);
	    
	    cbUIelements = new JComboBox();
	    cbUIelements.addActionListener(new SelectUIElementListener());
	    reloadComboBox();
	    actionPane.setLayout( new BorderLayout());
	    actionPane.add(btnRemove, BorderLayout.NORTH);
	    actionPane.add(cbUIelements, BorderLayout.CENTER);
	    this.add(actionPane, BorderLayout.NORTH);
	}
	
	private void doRemove(AbstractUIComponent uiComponent) {
		//
		if (modulePane!=null && uiComponent !=null)
		{
			modulePane.remove(uiComponent.getComponent());
			Iterator iter = modulePane.getModuleComponents().getAllComponents();
			while (iter.hasNext()) {
				if (iter.next()==uiComponent) {
					iter.remove();
					//System.out.println("removed");
					break;
				}
			}

			ModulePane tmp = modulePane;
			
			// update
			wbp.setModule(null); // remove all listeners
			wbp.setModule(tmp);
		}
	}
	
	private void reloadComboBox() {
		cbUIelements.removeAllItems();
		if (modulePane!=null) {
			Iterator iter = modulePane.getModuleComponents().getAllComponents();
			while (iter.hasNext()) 
				cbUIelements.addItem(iter.next());
		}
	}

	public void setModulePane(ModulePane modulePane) {
		this.modulePane=modulePane;
		reloadComboBox();
		updateEditor();
	}
	
	private class SelectUIElementListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			dataModel.setUIElement((AbstractUIComponent)cbUIelements.getSelectedItem());
		}
	}
	
	private void updateEditor() {
		//
	}

	public void setWorkBench(WorkBenchPane workBench) {
		this.wbp = workBench;
	}
	
}
