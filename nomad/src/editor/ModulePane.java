package editor;

import java.util.Iterator;

import javax.swing.JLabel;

import nomad.gui.AbstractModuleGUI;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.misc.FontInfo;
import nomad.model.descriptive.DModule;

public class ModulePane extends AbstractModuleGUI {
	private DModule module ;
	public ModulePane(DModule module) {
		this.setLayout(null);
		this.module = module;
		JLabel title = new JLabel(module.getName());
		
		title.setSize(FontInfo.getTextRect(title.getText(), title.getFont(), title));
		title.setLocation(0,0);
		this.add(title);
	}

	public DModule getModule() {
		return module;
	}
	
	public void initHack() {
		Iterator iter = getModuleComponents().getAllComponents();
		while (iter.hasNext()) {
			addExistingUIComponent((AbstractUIComponent)iter.next());
		}
	}


	private void addExistingUIComponent(AbstractUIComponent component) {
		new Draggable(component.getComponent());
	}
	
	public void addUIComponent(AbstractUIComponent component) {
		getModuleComponents().addComponent(component);
		addExistingUIComponent(component);
	}

}
