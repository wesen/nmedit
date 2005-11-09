package nomad.gui.model.component;

import java.util.ArrayList;

import nomad.gui.model.PortValueEvent;
import nomad.gui.model.PortValueListener;
import nomad.gui.model.property.ParamPortProperty;
import nomad.model.descriptive.DParameter;

/**
 * A class that links the user interface control and a parameter.  
 * 
 * @author Christian Schneider
 */
public abstract class AbstractControlPort {

	/** property for the DParameter object */
	private MyParamPortProperty paramPortProperty = null;
	/** the control this port belongs to */
	private AbstractUIControl uicontrol = null;
	/** the value listeners for the ui component */
	private ArrayList portvalueListeners = new ArrayList();
	/** the index of this port */
	private int portIndex = 0;
	
	/**
	 * Creates a new control port
	 * @param uicontrol the parent component
	 */
	public AbstractControlPort(AbstractUIControl uicontrol) {
		paramPortProperty = new MyParamPortProperty();
		this.uicontrol = uicontrol;
	}
	
	/**
	 * Sets the port index
	 * @param index the index
	 */
	void setPortIndex(int index) {
		portIndex = index;
	}
	
	/**
	 * Returns the port index
	 * @return the port index
	 */
	public int getPortIndex() {
		return portIndex;
	}
	
	/**
	 * Returns the DParameter object associated with this port
	 * @return the DParameter object associated with this port
	 */
	public abstract DParameter getParameterInfoAdapter();
	
	/**
	 * Sets the DParameter object associated with this port
	 * @param parameterInfo the new DParameter object
	 */
	public abstract void setParameterInfoAdapter(DParameter parameterInfo);
	
	/**
	 * Returns the value of the parameter ui
	 * @return the value of the parameter ui
	 */
	public abstract int getParameterValue();
	
	/**
	 * Sets the value of the parameter ui
	 * @param value the new value
	 */
	public abstract void setParameterValue(int value);

	/**
	 * @return the formatted value
	 * @see DParameter#getFormattedValue(int)
	 */
	public String getFormattedParameterValue() {
		DParameter info = getParameterInfoAdapter();
		if (info==null)
			return ""+getParameterValue();
		else
			return info.getFormattedValue(getParameterValue());
	}
	
	/**
	 * @param maxDigits upper bound for the number of characters that should be returned
	 * @return the formatted value
	 * @see DParameter#getFormattedValue(int,int)
	 */
	public String getFormattedParameterValue(int maxDigits) {
		return getParameterInfoAdapter().getFormattedValue(getParameterValue(), maxDigits);
	}
	
	/**
	 * Returns the property for the DParameter object 
	 * @return the property
	 */
	public ParamPortProperty getParamPortProperty() {
		return paramPortProperty;
	}
	
	/**
	 * notifies all listeners that the DParameter object has changed
	 */
	protected final void paramPortChanged() {
		paramPortProperty.fireChangeEvent();
	}

	/**
	 * Adds a listener that will be notified if the value of a port has changed
	 * @param listener the listener
	 */
	public void addValueListener(PortValueListener listener) {
		if (!portvalueListeners.contains(listener)) {
			portvalueListeners.add(listener);
		}
	}
	
	/**
	 * Removes the listener
	 * @param listener
	 */
	public void removeValueListener(PortValueListener listener) {
		if (portvalueListeners.contains(listener)) {
			portvalueListeners.remove(listener);
		}
	}
	
	/**
	 * Notifies all listeners that the port value has changed
	 */
	public void firePortValueUpdateEvent() {
		PortValueEvent event = new PortValueEvent(this);
		for (int i=0;i<portvalueListeners.size();i++)
			((PortValueListener)portvalueListeners.get(i)).portValueChanged(event);
	}
	
	private class MyParamPortProperty extends ParamPortProperty {
		public MyParamPortProperty() {
			super(null, AbstractControlPort.this.uicontrol);
		}

		protected Object getInternalValue() {
			return getParameterInfoAdapter();
		}

		protected void setInternalValue(Object value) {
			setParameterInfoAdapter((DParameter) value);
		}

		public String getId() {
			return "port."+portIndex;
		}
	}

}