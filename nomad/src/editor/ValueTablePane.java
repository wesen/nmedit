package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import nomad.gui.ModuleGUIComponents;
import nomad.gui.model.PortValueEvent;
import nomad.gui.model.PortValueListener;
import nomad.gui.model.component.AbstractUIControl;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;

public class ValueTablePane extends JPanel {

	private JTable table = null;
	private JScrollPane scrollpane = null;
	//private ModulePane modulePane = null;
	private OptionsTableModel dataModel = null;
	
	public ValueTablePane() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(200, 200));
		dataModel = new OptionsTableModel();
		table = new JTable(dataModel);
	    table.getColumnModel().getColumn(0).setHeaderValue("Property");
	    table.getColumnModel().getColumn(1).setHeaderValue("Value");
	    
	    scrollpane = new JScrollPane(table);
	    scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

	    this.add(scrollpane, BorderLayout.CENTER);
	}

	public void setModulePane(ModulePane modulePane) {
		//this.modulePane=modulePane;
		dataModel.setModule(modulePane);
		updateEditor();
	}
	
	private void updateEditor() {
		//
	}
	
	class OptionsTableModel extends AbstractTableModel {
		
		private DModule module=null;
		private ModulePane mpane = null;
		private ParamChangeListener[] changeListeners = new ParamChangeListener[]{};
		
		public void setModule(ModulePane modulePane) {
			if (module!=null) {
				ModuleGUIComponents comps = mpane.getModuleComponents();
				for (int i=0;i<Math.min(changeListeners.length,comps.getControlCount());i++) {
					AbstractUIControl control = comps.getControl(i);
					changeListeners[i] = new ParamChangeListener(i);
					control.getControlPort(0).removeValueListener(changeListeners[i]);
				}
				
				changeListeners = new ParamChangeListener[]{};
			}
			
			this.module= null;
			this.mpane = modulePane;
			
			if (modulePane!=null) {
				this.module=modulePane.getModule();
				
				ModuleGUIComponents comps = mpane.getModuleComponents();
				changeListeners = new ParamChangeListener[comps.getControlCount()];
				
				for (int i=0;i<comps.getControlCount();i++) {
					AbstractUIControl control = comps.getControl(i);
					changeListeners[i] = new ParamChangeListener(i);
					control.getControlPort(0).addValueListener(changeListeners[i]);
				}
			}
			this.fireTableDataChanged();
		}
		
		class ParamChangeListener implements PortValueListener {
			private int index=0;
			public ParamChangeListener(int index) {
				this.index=index;
			}
			public void portValueChanged(PortValueEvent event) {
				OptionsTableModel.this.fireTableCellUpdated(index,1);				
			}
		}

		public int getRowCount() {
			if (module!=null) {
				return module.getParameterCount();
			}
			return 0;
		}

		public Object getValueAt(int row, int column) {
			if (module!=null) {
				if (column==0) {
					if (row<module.getParameterCount()) {
						return module.getParameter(row).getName();
					}
				} else if (column==1) {
					if (row<module.getParameterCount()) {
						if (mpane.getModuleComponents().getControlCount()<=row)
							return "null";
						
						AbstractUIControl control = mpane.getModuleComponents().getControl(row);
						return control.getControlPort(0).getFormattedParameterValue();
					}
				} else
					return "0";
			}
			
			return "null";
		}

		public int getColumnCount() {
			return 2;
		}	
	}
	
}
