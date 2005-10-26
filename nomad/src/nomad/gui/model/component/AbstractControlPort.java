package nomad.gui.model.component;

import java.util.ArrayList;

import nomad.gui.model.PortValueEvent;
import nomad.gui.model.PortValueListener;
import nomad.gui.model.property.ParamPortProperty;
import nomad.model.descriptive.DParameter;

public abstract class AbstractControlPort {

	private ParamPortProperty paramPortProperty = null;
	private AbstractUIControl uicontrol = null;
	private ArrayList portvalueListeners = new ArrayList();
	
	public AbstractControlPort(AbstractUIControl uicontrol) {
		paramPortProperty = new MyParamPortProperty();
		this.uicontrol = uicontrol;
	}
	
	public abstract DParameter getParameterInfoAdapter();
	public abstract void setParameterInfoAdapter(DParameter parameterInfo);
	public abstract int getParameterValue();
	public abstract void setParameterValue(int value);

	public String getFormattedParameterValue() {
		DParameter info = getParameterInfoAdapter();
		if (info==null)
			return ""+getParameterValue();
		else
			return info.getFormattedValue(getParameterValue());
	}
	
	public String getFormattedParameterValue(int maxDigits) {
		return getParameterInfoAdapter().getFormattedValue(getParameterValue(), maxDigits);
	}
	
	public ParamPortProperty getParamPortProperty() {
		return paramPortProperty;
	}
	
	protected final void paramPortChanged() {
		paramPortProperty.fireChangeEvent();
	}

	public void addValueListener(PortValueListener listener) {
		if (!portvalueListeners.contains(listener)) {
			portvalueListeners.add(listener);
		}
	}
	
	public void removeValueListener(PortValueListener listener) {
		if (portvalueListeners.contains(listener)) {
			portvalueListeners.remove(listener);
		}
	}
	
	public void firePortValueUpdateEvent() {
		PortValueEvent event = new PortValueEvent(this);
		for (int i=0;i<portvalueListeners.size();i++)
			((PortValueListener)portvalueListeners.get(i)).portValueChanged(event);
	}
	
	private class MyParamPortProperty extends ParamPortProperty {
		public MyParamPortProperty() {
			super("0", AbstractControlPort.this.uicontrol);
		}

		protected Object getInternalValue() {
			return getParameterInfoAdapter();
		}

		protected void setInternalValue(Object value) {
			setParameterInfoAdapter((DParameter) value);
		}

		public String getId() {
			return "port.0";
		}
	}

}