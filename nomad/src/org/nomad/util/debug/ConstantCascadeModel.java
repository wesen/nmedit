package org.nomad.util.debug;

public class ConstantCascadeModel implements CascadeModel {

	private String prefix = "";

	public ConstantCascadeModel(String prefix) {
		setPrefix(prefix);
	}

	public String prefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix==null?"":prefix;
	}
	
}
