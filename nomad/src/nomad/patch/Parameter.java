package nomad.patch;

import nomad.model.descriptive.DParameter;

class Parameter {
	private int value;
	private DParameter dParameter = null;

	Parameter(DParameter dParameter) {
		this.dParameter = dParameter;
		value = dParameter.getDefaultValue();
	}

	public int getValue() {
		return value;
	}

    public void setValue(int newValue) {
        value = newValue;
    }   
}
