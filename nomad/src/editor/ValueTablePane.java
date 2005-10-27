package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import nomad.gui.ModuleGUIComponents;
import nomad.gui.model.PortValueEvent;
import nomad.gui.model.PortValueListener;
import nomad.gui.model.component.AbstractControlPort;
import nomad.gui.model.component.AbstractUIControl;
import nomad.model.descriptive.DModule;

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
		private ArrayList changeListeners = new ArrayList();
		
		public void setModule(ModulePane modulePane) {
			if (module!=null) {
				ModuleGUIComponents comps = mpane.getModuleComponents();
				for (int i=0;i<comps.getControlCount();i++) {
					AbstractUIControl control = comps.getControl(i);
					for (int j=0;j<control.getControlPortCount();j++) {
						AbstractControlPort port = control.getControlPort(j);
						for (int k=0;k<changeListeners.size();k++)
							port.removeValueListener((ParamChangeListener)changeListeners.get(k));
					}
				}
				
				changeListeners = new ArrayList();
			}
			
			this.module= null;
			this.mpane = modulePane;
			
			if (modulePane!=null) {
				this.module=modulePane.getModule();
				
				ModuleGUIComponents comps = mpane.getModuleComponents();
				for (int i=0;i<comps.getControlCount();i++) {
					AbstractUIControl control = comps.getControl(i);
					for (int j=0;j<control.getControlPortCount();j++) {
						AbstractControlPort port = control.getControlPort(j);
						ParamChangeListener listener = new ParamChangeListener();
						changeListeners.add(listener);
						port.addValueListener(listener);
					}
				}
			}
			this.fireTableDataChanged();
		}
		
		class ParamChangeListener implements PortValueListener {
			public void portValueChanged(PortValueEvent event) {
				OptionsTableModel.this.fireTableDataChanged();				
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
						ModuleGUIComponents comps = mpane.getModuleComponents();
						for (int i=0;i<comps.getControlCount();i++) {
							AbstractUIControl control = comps.getControl(i);
							for (int j=0;j<control.getControlPortCount();j++) {
								AbstractControlPort port = control.getControlPort(j);
								if (port.getParameterInfoAdapter()==module.getParameter(row)) {
									return control.getControlPort(j).getFormattedParameterValue();
								}
							}
						}
						
						return "?";
					}
				} else
					return "";
			}
			
			return "null";
		}

		public int getColumnCount() {
			return 2;
		}	
	}
	
}
