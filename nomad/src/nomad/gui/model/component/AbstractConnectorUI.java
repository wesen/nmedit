package nomad.gui.model.component;

import nomad.gui.model.property.ConnectorProperty;
import nomad.model.descriptive.DConnector;

public abstract class AbstractConnectorUI extends AbstractUIComponent {

	private ConnectorProperty cp = new MyConnectorProperty();
	
	public AbstractConnectorUI() {
		
	}

	protected void installProperties(boolean install) {
		super.installProperties(install);
		installConnectorProperty(install);
	}
	
	protected void installConnectorProperty(boolean install) {
		if (install)
			registerProperty(cp);
		else
			unregisterProperty(cp);
	}

	
	public abstract DConnector getConnectorInfoAdapter() ;
	public abstract void setConnectorInfoAdapter(DConnector connector);
	
	public ConnectorProperty getConnectorProperty() {
		return cp;
	}
	
	protected final void connectorChanged() {
		cp.fireChangeEvent();
	}

	public String getName() {
		return "Connector";
	}

	private class MyConnectorProperty extends ConnectorProperty {

		public MyConnectorProperty() {
			super(AbstractConnectorUI.this);
		}
		
		protected Object getInternalValue() {
			return getConnectorInfoAdapter();
		}

		protected void setInternalValue(Object value) {
			setConnectorInfoAdapter((DConnector)value);
		}

		public String getId() {
			return "connector";
		}
		
	}

}
