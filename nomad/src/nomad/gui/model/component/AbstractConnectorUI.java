package nomad.gui.model.component;

import nomad.gui.model.UIFactory;
import nomad.gui.model.property.ConnectorProperty;
import nomad.model.descriptive.DConnector;

/**
 * The connector user interface class.
 * 
 * @author Christian Schneider
 */
public abstract class AbstractConnectorUI extends AbstractUIComponent {

	/** Property for the DConnector object */
	private ConnectorProperty cp = new MyConnectorProperty();
	
	public AbstractConnectorUI(UIFactory factory) {
		super(factory);
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

	/**
	 * Returns the DConnector object associated to this component
	 * @return the DConnector object associated to this component
	 */
	public abstract DConnector getConnectorInfoAdapter() ;
	/**
	 * Sets the DConnector object associated to this component
	 * @param connector the connector object
	 */
	public abstract void setConnectorInfoAdapter(DConnector connector);
	
	/**
	 * Returns the connector property to access the DConnector object
	 * @return the connector property to access the DConnector object
	 */
	public ConnectorProperty getConnectorProperty() {
		return cp;
	}
	
	/**
	 * Notify listeners that a different connector is used
	 */
	protected final void connectorChanged() {
		cp.fireChangeEvent();
	}

	/**
	 * Returns the string 'Connector'
	 */
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
