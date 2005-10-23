package nomad.gui.property;

import nomad.model.descriptive.DConnector;
import nomad.model.descriptive.DModule;

public class ConnectorProperty extends Property {

	private DModule info = null;
	private DConnector[] options = new DConnector[] {};
	private String[] names = new String[] {"null"};
	
	public void setModule(DModule info) {
		this.info = info;
		updateOptions();
	}
	
	private void updateOptions() {
		options = new DConnector[info.getConnectorCount()];
		names = new String[info.getConnectorCount()+1];
		for (int i=0;i<info.getConnectorCount();i++) {
			DConnector connector = info.getConnector(i);
			options[i] = connector;
			names[i] = connectorToString(connector);
		}
		names[names.length-1] = "null";
	}
	
	public Object[] getOptions() {
		return names;
	}
	
	public DConnector getSelectedConnector() {
		String value = (String) this.getValue();
		if (value!=null && !value.equals("null")) {
			for (int i=0;i<names.length;i++)
				if (names[i].equals(value))
					return options[i];
		}
		return null;
	}
	
	private String connectorToString(DConnector connector) {
		return connector.getId()+"#"+connector.getName();
	}
	
	public void setConnector(DConnector connector) {
		for (int i=0;i<options.length;i++)
			if (options[i]==connector) {
				try {
					setValue(connectorToString(connector));
				} catch (InvalidValueException e) {
					// should never occure
					e.printStackTrace();
				}
				break;
			}
	}
	
}
