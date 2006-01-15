package org.nomad.patch;

class Controller {

//1 section index: 0 = common section, 1 = poly section, 2 = morph section (the morph has module index 1, section index 2)
//4 CC number: 0..31, 33..120, controller 32 is reserved for bank selection

	private int section, module, parameter, ccNumber;

	Controller (int newSection, int newModule, int newParameter, int newCCNumber) {
		section = newSection;
		module = newModule;
		parameter = newParameter;
		ccNumber = newCCNumber;
	}	

// Getters

	public int getSection() {
		return section;
	}
	
	public int getModule() {
		return module;
	}
	
	public int getParameter() {
		return parameter;
	}
	
	public int getCCNumber() {
		return ccNumber;
	}
}	
