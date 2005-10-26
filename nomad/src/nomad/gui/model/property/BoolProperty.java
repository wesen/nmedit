package nomad.gui.model.property;

import nomad.gui.model.component.AbstractUIComponent;

/**
 * The abstract internal operations must use java.lang.Boolean as transfer type for value
 * @author Christian Schneider
 */
public abstract class BoolProperty extends Property {

	private boolean defaultValue = false;
	
	public BoolProperty(String displayName, AbstractUIComponent uicomponent) {
		super(displayName, uicomponent);
	}

	public Boolean getBooleanObjectValue() {
		return (Boolean) getValue();
	}

	public boolean getBooleanValue() {
		return getBooleanObjectValue().booleanValue();
	}

	public void setValue(boolean value) {
		setValue(new Boolean(value));
	}

	public Object parseString(String representation) {
		if (representation==null)
			return null;
		return Boolean.valueOf(representation);
	} 

	public Object[] getAllValues() {
		return new Boolean[] {new Boolean(true), new Boolean(false)};
	}
	
	public Object getDefaultValue() {
		return new Boolean(getDefaultBooleanValue());
	}
	
	public boolean getDefaultBooleanValue() {
		return defaultValue;
	}
	
	public void setDefaultBooleanValue(boolean defaultValue){
		this.defaultValue = defaultValue;
	}
	
}
