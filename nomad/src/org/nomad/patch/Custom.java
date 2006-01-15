package org.nomad.patch;

class Custom {
//	int moduleIndex;
//	int customCount;
	private int value;
	private String name;

	Custom(int newValue, String newName) {
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

	public void setName(String newName) {
		name = newName;
	}
}
