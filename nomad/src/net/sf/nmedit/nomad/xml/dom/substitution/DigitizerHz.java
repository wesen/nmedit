package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

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
			return ""+NomadUtilities.roundTo(aFloat, -2)+" Hz";
		
		else if (aFloat<1000)
			return ""+NomadUtilities.roundTo(aFloat, -1)+" Hz";
		
		else if (aFloat<10000)
			return ""+NomadUtilities.roundTo(aFloat/1000.0d, -3)+" kHz";
		
		else
			return ""+NomadUtilities.roundTo(aFloat/1000.0d, -2)+" kHz";
	}

}
