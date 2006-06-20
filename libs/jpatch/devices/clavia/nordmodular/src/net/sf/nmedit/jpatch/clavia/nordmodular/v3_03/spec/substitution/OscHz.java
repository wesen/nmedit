package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

import net.sf.nmedit.jmisc.math.Math2;

/**
 * @author Christian Schneider
 * @hidden
 */
public class OscHz extends Substitution {

	public OscHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 440.0d * Math.pow(2.0d, (value-69)/12);

		if (aFloat<10)
			return ""+Math2.roundTo(aFloat, -2)+" Hz";
		
		else if (aFloat<100)
			return ""+Math2.roundTo(aFloat, -1)+" Hz";
				
		else if (aFloat<1000)
			return ""+Math2.roundTo(aFloat,  0)+" Hz";
					
		else if (aFloat<1000)
			return ""+Math2.roundTo(aFloat/1000.0, -2)+" kHz";
		
		else
			return ""+Math2.roundTo(aFloat/1000.0, -2)+" kHz";
	}

}
