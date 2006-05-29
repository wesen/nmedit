package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

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
			return ""+NomadUtilities.roundTo(aFloat, 0)+" ms";
		
		else if (aFloat<10000)
			return ""+NomadUtilities.roundTo(aFloat/1000.0d, -1)+" s";
		
		else
			return ""+NomadUtilities.roundTo(aFloat/1000.0d, 0)+" s";
	}
}
