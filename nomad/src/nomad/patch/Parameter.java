package nomad.patch;

import javax.swing.JComponent;


class Parameter extends JComponent{
	private int value;
	private String name;

	Parameter(int newValue, String newName) {
		value = newValue;
		name = newName;
	}

// Getters

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
// Setters

    public void setValue(int newValue) {
        value = newValue;
    }   

    public void setValueWithoutFireStarter(int newValue) {
        value = newValue;
    }

	public void setName(String newName) {
		name = newName;
	}	
}
