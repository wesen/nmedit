package net.sf.nmedit.nomad.xml.dom.module;

import net.sf.nmedit.nomad.xml.dom.substitution.Substitution;

public class DCustom extends DParameter {

	public DCustom(DModule parent,
		int minValue, int maxValue, int id, String name) {
		super(parent, minValue, maxValue, id, name);
	}

	public DCustom(DModule parent,
		Substitution substitution, int minValue, int maxValue, int bitCount,
		int id, String name) {
		super(parent, substitution, minValue, maxValue, bitCount, id, name);
	}
}
