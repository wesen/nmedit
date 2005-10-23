package nomad.gui.property;


public class BoolProperty extends Property {

	private final static Boolean bool_true = new Boolean(true);
	private final static Boolean bool_false = new Boolean(false);
	private final static Boolean[] bool_options = new Boolean[] {bool_true, bool_false};
	private boolean defaultValue = false;

	/**
	 * Creates the BoolProperty object with false as default value.
	 */
	public BoolProperty() {
		this(false);
	}
	
	/**
	 * Creates the BoolProperty object with 'defaultValue' as default value.
	 * @param defaultValue the default value
	 */
	public BoolProperty(boolean defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}
	
	public Object[] getOptions() {
		return bool_options;
	}
	
	/**
	 * Checks value if it is either of type Boolean or one of the strings "true" or "false". 
	 */
	protected Object checkAndNormalizeValue(Object value) throws InvalidValueException {
		if (value==null)
			throw new InvalidValueException("Value must not be 'null'.");

		if (value instanceof String) {
			if (value.equals("true")) 
				return bool_true;
			else if (value.equals("false")) 
				return bool_false;
			else
				throw new InvalidValueException("Unrecognized string '"+value+"'.");
		}
		else if (value instanceof Boolean)
			return value;

		throw new InvalidValueException("Unrecognized value '"+value+"'.");
	}

	public Object getDefaultValue() {
		return new Boolean(defaultValue);
	}
	
	public boolean getBooleanValue() {
		return ((Boolean)getValue()).booleanValue();
	}
	
	public void setValue(boolean value, Object sender) {
		try {
			super.setValue(new Boolean(value), sender);
		} catch (InvalidValueException e) {
			// should never occure
			e.printStackTrace();
		}
	}

}
