package editor;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;

import nomad.gui.model.ModuleGUIBuilder;
import nomad.gui.model.UIFactory;
import nomad.model.descriptive.DModule;
import nomad.patch.ModuleSection.ModulePixDimension;

public class ModuleUIBuilder {

	public static ModulePane buildModuleUI(UIFactory factory, DModule moduleInfo) {
		ModulePane modulePane = new ModulePane(factory, moduleInfo);
		
		modulePane.setForeground(Color.GRAY);
		modulePane.setSize(new Dimension(ModulePixDimension.PIXWIDTH, 
				moduleInfo.getHeight()*ModulePixDimension.PIXHEIGHT));
		modulePane.setPreferredSize(new Dimension(ModulePixDimension.PIXWIDTH, 
				moduleInfo.getHeight()*ModulePixDimension.PIXHEIGHT));
		modulePane.setBorder(BorderFactory.createRaisedBevelBorder());
		

		ModuleGUIBuilder.createGUIComponents(modulePane, null, moduleInfo);
		modulePane.initHack();
		
		//addUIComponents(factory, modulePane, moduleInfo);
		
		return modulePane;
	}

	/*
	private static void addUIComponents(UIFactory factory, ModulePane modulePane, DModule moduleInfo) {

		int pad=5;
		int line=0;
		int lineHeight=12;
		//modulePane.setLayout(new BoxLayout(modulePane, BoxLayout.Y_AXIS));
		modulePane.setLayout(null);
		JLabel label;
		label=new JLabel(new ImageIcon(moduleInfo.getIcon()));
		label.setLocation(pad,pad);
		label.setSize(16,16);
		modulePane.add(label);
		label=new JLabel(moduleInfo.getName());
		label.setFont(new Font("Dialog", Font.BOLD, 10));
		label.setSize(10*moduleInfo.getName().length(),16);
		label.setLocation(pad+16+10,pad);
		modulePane.add(label);

		line++;
		
		for (int i=0;i<moduleInfo.getParameterCount();i++) {
			DParameter info = moduleInfo.getParameter(i);
			//new ParamView(modulePane,info,pad,pad+line*lineHeight);
			
			int offset= 20*(i%4);

			AbstractUIControl control = info.getNumStates()<6 // option
				? factory.newDefaultOptionControlInstance()
			    : factory.newDefaultControlInstance();
			
			control.getControlPort(0).setParameterInfoAdapter(info);
			control.getComponent().setLocation(pad+offset,pad+line*lineHeight);
			new Draggable(control.getComponent());
			
			//slider.setSize(100, 16);
			modulePane.add(control.getComponent());
			modulePane.setParamControl(info, control);
			
			if (i%4==3)
				line++;
		}
	//	if ((moduleInfo.getParameterCount()-1)%4!=3)
	//		line++;*
		
		if (moduleInfo.getConnectorCount()>0) {
			ConnectorRow.addRow(factory, moduleInfo, modulePane, pad, line, lineHeight);
		}
		
		modulePane.validate();
		modulePane.updateUI();
	}
*/	
}
/*
class ConnectorRow {
	public static void addRow(UIFactory theUIFactory, DModule module, ModulePane target, int pad, int line, int lineHeight)  {
		for (int i=0;i<module.getConnectorCount();i++) {
			AbstractConnectorUI cui = theUIFactory.newDefaultConnectorInstance();
			cui.setConnectorInfoAdapter(module.getConnector(i));
			cui.getComponent().setLocation(pad+i*18,pad+line*lineHeight);
			new Draggable(cui.getComponent());
			target.add(cui.getComponent());
			target.addUIComponent(cui);
		}
	}
}
*/