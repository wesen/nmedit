package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

import net.sf.nmedit.jmisc.math.Math2;

/**
 * @author Christian Schneider
 * @hidden
 */
public class DrumHz extends Substitution {

	public DrumHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 20.0d * Math.pow(2.0, value/24.0d);

		if (aFloat<100)
			return ""+Math2.roundTo(aFloat, -1)+" Hz";
		
		else
			return ""+Math2.roundTo(aFloat, 0)+" Hz";
	}

}
