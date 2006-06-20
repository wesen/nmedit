package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

import net.sf.nmedit.jmisc.math.Math2;

/**
 * @author Christian Schneider
 * @hidden
 */
public class AmpGain extends Substitution {
	
	public AmpGain() {
		super();
	}

	public String valueToString(int value) {
		if (value==63)
			value++;
		
		double aFloat = 0.25 * Math.pow(2.0, value/32.0d);
		return "x"+Math2.roundTo(aFloat/32.0d,-2);
	}
}
