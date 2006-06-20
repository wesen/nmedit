package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

import net.sf.nmedit.jmisc.math.Math2;

/**
 * @author Christian Schneider
 * @hidden
 */
public class PartialRange extends Substitution {

	public PartialRange() {
		super();
	}

	public String valueToString(int value) {		
		switch(value) {
			case 0:   return "0";
			case 127: return "?64 *";
		}

		String suffix = (value>64)? " *":"";
		return "?"+Math2.roundTo(value/2.0, -1)+suffix;
	}
}
