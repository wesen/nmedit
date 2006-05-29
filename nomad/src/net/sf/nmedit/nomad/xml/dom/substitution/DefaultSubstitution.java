package net.sf.nmedit.nomad.xml.dom.substitution;

public class DefaultSubstitution extends Substitution {

	public DefaultSubstitution() {
		super();
	}

	public String valueToString(int value) {
		return Integer.toString(value);
	}

}
