package editor;

import java.util.HashMap;
import java.util.Vector;
import javax.swing.JPanel;
import nomad.gui.BasicUI;
import nomad.gui.ControlUI;
import nomad.gui.DisplayUI;
import nomad.gui.knob.ParameterClass;
import nomad.gui.property.ParamProperty;
import nomad.gui.property.Property;
import nomad.gui.property.PropertyMap;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;

public class ModulePane extends JPanel {
	private DModule module ;
	private HashMap parameters = new HashMap();
	private Vector uicomponents = new Vector();
	public ModulePane(DModule module) {
		this.module = module;
	}

	public DModule getModule() {
		return module;
	}
	
	public void addUIComponent(BasicUI component) {
		if (component!=null) {
			// install info
			PropertyMap pmap = component.getProperties();
			String[] names = pmap.getPropertyNames();
			for (int i=0;i<names.length;i++) {
				Property p = pmap.getProperty(names[i]);
				if (p instanceof ParamProperty)
					((ParamProperty)p).setModule(module);
			}
		}
		uicomponents.add(component);
	}

	public int getUIComponentCount() {
		return uicomponents.size();
	}
	
	public BasicUI getUIComponent(int index) {
		return (BasicUI) uicomponents.get(index);
	}

	public void setParamControl(DParameter param, DisplayUI display) {
		parameters.put(param, display);
		addUIComponent(display);
	}
	
	public ControlUI getParamControl(DParameter param) {
		return (ControlUI) parameters.get(param);
	}
}
