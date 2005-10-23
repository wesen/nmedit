package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nomad.gui.BasicUI;
import nomad.gui.ConnectorUI;
import nomad.gui.ControlUI;
import nomad.gui.UIFactory;
import nomad.model.descriptive.DConnector;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;
import nomad.patch.ModuleSection.ModulePixDimension;

public class WorkBenchPane extends JPanel implements CreateUIElementListener {

	DModule module = null;
	private ModulePane modulePane = null;
	private ValueTablePane valueTable=null;
	private PropertyTablePane propertyTable=null;
	private UIFactory theUIFactory = null;
	
	public WorkBenchPane(UIFactory theUIFactory, ValueTablePane optionsTable, PropertyTablePane propertyTable) {
		this.theUIFactory = theUIFactory;
		this.setBackground(Color.LIGHT_GRAY);//.DARK_GRAY);
		this.setBorder(BorderFactory.createEmptyBorder(100,50,100,50));
		this.setPreferredSize(new Dimension(100+ModulePixDimension.PIXWIDTH,300));
		this.valueTable = optionsTable;
		this.propertyTable = propertyTable;
	}
	
	public void setModule(DModule module) {
		this.module=module;

		if (modulePane!=null) {
			this.remove(modulePane);
		}
		
		modulePane = new ModulePane(module);
		modulePane.setForeground(Color.GRAY);
		modulePane.setSize(new Dimension(ModulePixDimension.PIXWIDTH, 
				module.getHeight()*ModulePixDimension.PIXHEIGHT));
		modulePane.setPreferredSize(new Dimension(ModulePixDimension.PIXWIDTH, 
				module.getHeight()*ModulePixDimension.PIXHEIGHT));
		modulePane.setBorder(BorderFactory.createRaisedBevelBorder());
		
		addUIComponents();
		
		this.add(BorderLayout.CENTER, modulePane);
		this.updateUI();
		
		valueTable.setModulePane(modulePane);
		propertyTable.setModulePane(modulePane);
	}

	void addUIComponents() {
		int pad=5;
		int line=0;
		int lineHeight=12;
		//modulePane.setLayout(new BoxLayout(modulePane, BoxLayout.Y_AXIS));
		modulePane.setLayout(null);
		JLabel label;
		label=new JLabel(new ImageIcon(module.getIcon()));
		label.setLocation(pad,pad);
		label.setSize(16,16);
		modulePane.add(label);
		label=new JLabel(module.getName());
		label.setFont(new Font("Dialog", Font.BOLD, 10));
		label.setSize(10*module.getName().length(),16);
		label.setLocation(pad+16+10,pad);
		modulePane.add(label);

		line++;
		
		for (int i=0;i<module.getParameterCount();i++) {
			DParameter info = module.getParameter(i);
			//new ParamView(modulePane,info,pad,pad+line*lineHeight);
			
			int offset= 20*(i%4);

			ControlUI control =  theUIFactory.newDefaultControlInstance();
			control.setDefaultPort(info);
			control.getComponent().setLocation(pad+offset,pad+line*lineHeight);
			new Draggable(control.getComponent());
			
			//slider.setSize(100, 16);
			modulePane.add(control.getComponent());
			modulePane.setParamControl(info, control);
			
			if (i%4==3)
				line++;
		}
		if ((module.getParameterCount()-1)%4!=3)
			line++;
		
		if (module.getConnectorCount()>0) {
			ConnectorRow.addRow(theUIFactory, module, modulePane, pad, line, lineHeight);
		}
		
		modulePane.validate();
		modulePane.updateUI();
	}

	public void newUIElement(BasicUI element) {
		if (modulePane!=null) {
			modulePane.addUIComponent(element);
			modulePane.add(element.getComponent());
			modulePane.validate();
			modulePane.updateUI();
			// install dragging support
			new Draggable(element.getComponent());

			// update
			valueTable.setModulePane(this.modulePane);
			propertyTable.setModulePane(this.modulePane);
		}
	}
	
}

class ConnectorRow {
	public static void addRow(UIFactory theUIFactory, DModule module, ModulePane target, int pad, int line, int lineHeight)  {
		for (int i=0;i<module.getConnectorCount();i++) {
			ConnectorUI cui = theUIFactory.newDefaultConnectorInstance();
			cui.getConnectorProperty().setModule(module);
			cui.getConnectorProperty().setConnector(module.getConnector(i));
			cui.getComponent().setLocation(pad+i*18,pad+line*lineHeight);
			new Draggable(cui.getComponent());
			target.add(cui.getComponent());
			target.addUIComponent(cui);
		}
	}
}
