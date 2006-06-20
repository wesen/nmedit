package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

import net.sf.nmedit.jmisc.math.Math2;

/**
 * @author Christian Schneider
 * @hidden
 */
public class SmoothTime extends Substitution {

	public SmoothTime() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = Math.pow(2.0, value/9.0d);

		if (aFloat<1000)
			return ""+Math2.roundTo(aFloat, 0)+" ms";
		
		else if (aFloat<10000)
			return ""+Math2.roundTo(aFloat/1000.0d, -1)+" s";
		
		else
			return ""+Math2.roundTo(aFloat/1000.0d, 0)+" s";
	}
}
