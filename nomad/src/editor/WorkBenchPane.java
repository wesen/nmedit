package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import nomad.gui.model.component.AbstractUIComponent;
import nomad.gui.model.component.AbstractUIControl;
import nomad.model.descriptive.DModule;
import nomad.patch.ModuleSection.ModulePixDimension;

public class WorkBenchPane extends JPanel implements CreateUIElementListener {

	DModule module = null;
	private ModulePane modulePane = null;
	private ValueTablePane valueTable=null;
	private PropertyTablePane propertyTable=null;
	
	public WorkBenchPane(ValueTablePane optionsTable, PropertyTablePane propertyTable) {
		this.setBackground(Color.LIGHT_GRAY);//.DARK_GRAY);
		this.setBorder(BorderFactory.createEmptyBorder(100,50,100,50));
		this.setPreferredSize(new Dimension(100+ModulePixDimension.PIXWIDTH,300));
		this.valueTable = optionsTable;
		this.propertyTable = propertyTable;
	}
	
	public void setModule(ModulePane modulePane) {
		this.module=null;
		if (this.modulePane!=null) 
			this.remove(this.modulePane);
		this.modulePane = modulePane;
		if (modulePane!=null) {
			this.module = modulePane.getModule();
			this.add(BorderLayout.CENTER, modulePane);
			this.updateUI();
		}
		
		valueTable.setModulePane(modulePane);
		propertyTable.setModulePane(modulePane);
	}

	public void newUIElement(AbstractUIComponent element) {
		if (modulePane!=null) {
			
			if (element instanceof AbstractUIControl) {
				AbstractUIControl control = ((AbstractUIControl)element);
				for (int i=0;i<control.getControlPortCount();i++) {
					if (modulePane.getModule().getParameterCount()>0)
						control.getControlPort(i).setParameterInfoAdapter(modulePane.getModule().getParameter(0));
				}
			}

			modulePane.addUIComponent(element);
			modulePane.add(element.getComponent());
			modulePane.validate();
			modulePane.updateUI();

			// update
			valueTable.setModulePane(this.modulePane);
			propertyTable.setModulePane(this.modulePane);			
		}
	}
}
