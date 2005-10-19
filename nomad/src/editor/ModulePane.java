package editor;

import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JSlider;

import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;

public class ModulePane extends JPanel {
	
	private DModule module ;
	private HashMap parameters = new HashMap();
	
	public ModulePane(DModule module) {
		this.module = module;
	}
	
	public DModule getModule() {
		return module;
	}

	public void setParamControl(DParameter param, JSlider slider) {
		parameters.put(param, slider);
	}
	
	public JSlider getParamControl(DParameter param) {
		return (JSlider) parameters.get(param);
	}
	
}
