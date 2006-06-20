package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

import net.sf.nmedit.jmisc.math.Math2;

/**
 * @author Christian Schneider
 * @hidden
 */
public class DigitizerHz extends Substitution {

	public DigitizerHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 32.70 * Math.pow(2.0, value/12.0d);

		if (aFloat<100)
			return ""+Math2.roundTo(aFloat, -2)+" Hz";
		
		else if (aFloat<1000)
			return ""+Math2.roundTo(aFloat, -1)+" Hz";
		
		else if (aFloat<10000)
			return ""+Math2.roundTo(aFloat/1000.0d, -3)+" kHz";
		
		else
			return ""+Math2.roundTo(aFloat/1000.0d, -2)+" kHz";
	}

}
