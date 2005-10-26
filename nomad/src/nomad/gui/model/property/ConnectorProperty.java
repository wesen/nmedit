package nomad.gui.model.property;

import nomad.gui.model.component.AbstractUIComponent;
import nomad.model.descriptive.DConnector;
import nomad.model.descriptive.DModule;

public abstract class ConnectorProperty extends AbstractModuleProperty {

	public ConnectorProperty(AbstractUIComponent uicomponent) {
		super("", uicomponent);
	}

	public Object[] getAllValues() {
		if (getConnector()==null)
			return null;
		
		DModule module = getModule();
		DConnector[] connectorList = new DConnector[module.getConnectorCount()]; 
		for (int i=0;i<module.getConnectorCount();i++) {
			connectorList[i] = module.getConnector(i);
		}
		return connectorList;
	}
	
	public void setConnector(DConnector connector) {
		super.setValue(connector);
	}

	public DConnector getConnector() {
		return (DConnector) getValue();
	}
	
	public DModule getModule() {
		return getConnector().getParent();
	}

	public String getDisplayName() {
		DConnector connector = getConnector();
		return connector == null ? "connector.invalid" : "connector."+connector.getId(); 
	}

}
