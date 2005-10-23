package nomad.gui;

import nomad.gui.property.ParamProperty;
import nomad.model.descriptive.DParameter;

public abstract class ControlUI extends DisplayUI {

	private ParamProperty pDefaultParam = new ParamProperty();
	
	public ControlUI() {
		super();
		getProperties().putProperty("parameter(D)", pDefaultParam);
	}

	public void setDefaultPort(DParameter param) {
		pDefaultParam.setModule(param.getParent());
		pDefaultParam.setParameter(param);
	}

	public ParamProperty getDefaultPort() {
		return pDefaultParam;
	}
	
}
