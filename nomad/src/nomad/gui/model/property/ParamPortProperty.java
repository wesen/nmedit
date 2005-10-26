package nomad.gui.model.property;

import nomad.gui.model.component.AbstractUIComponent;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;

/**
 * Internal transfer type is DParameter
 * @author Christian Schneider
 */
public abstract class ParamPortProperty extends AbstractModuleProperty {

	public ParamPortProperty(String portId, AbstractUIComponent uicomponent) {
		super("port."+portId, uicomponent);
	}

	public Object[] getAllValues() {
		if (getParameter()==null)
			return null;

		DModule module = getModule();
		DParameter[] paramList = new DParameter[module.getParameterCount()]; 
		for (int i=0;i<module.getParameterCount();i++)
			paramList[i] = module.getParameter(i);
		return paramList;
	}
	
	public void setParameter(DParameter param) {
		super.setValue(param);
	}

	public DParameter getParameter() {
		return (DParameter) getValue();
	}
	
	public DModule getModule() {
		return getParameter().getParent();
	}

}
