package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

import net.sf.nmedit.jmisc.math.Math2;

/**
 * @author Christian Schneider
 * @hidden
 */
public class EqHz extends Substitution {

	public EqHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 471.0 * Math.pow(2.0, (value-60)/12);

		if (aFloat<1000)
			return ""+Math2.roundTo(aFloat, 0)+" Hz";

		else if (aFloat<10000)
			return ""+Math2.roundTo(aFloat/1000, -2)+" kHz";

		else
			return ""+Math2.roundTo(aFloat/1000, -1)+" kHz";
	}

}
