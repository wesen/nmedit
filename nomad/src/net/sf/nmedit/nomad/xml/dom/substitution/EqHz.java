package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

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
			return ""+NomadUtilities.roundTo(aFloat, 0)+" Hz";

		else if (aFloat<10000)
			return ""+NomadUtilities.roundTo(aFloat/1000, -2)+" kHz";

		else
			return ""+NomadUtilities.roundTo(aFloat/1000, -1)+" kHz";
	}

}
