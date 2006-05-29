package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

/**
 * @author Christian Schneider
 * @hidden
 */
public class LFOHz extends Substitution {

	public LFOHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 440.0 * Math.pow(2.0, (value-177)/12.0d);

		if (aFloat<0.1)
			return ""+NomadUtilities.roundTo(1/aFloat, -1)+" s";

		else if (aFloat<10)
			return ""+NomadUtilities.roundTo(aFloat, -2)+" Hz";
				
		else if (aFloat<100)
			return ""+NomadUtilities.roundTo(aFloat, -1)+" Hz";

		else
			return ""+NomadUtilities.roundTo(aFloat, 0)+" Hz";
	}

}
