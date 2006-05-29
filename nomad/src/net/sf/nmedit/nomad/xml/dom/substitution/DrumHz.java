package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;


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
			return ""+NomadUtilities.roundTo(aFloat, -1)+" Hz";
		
		else
			return ""+NomadUtilities.roundTo(aFloat, 0)+" Hz";
	}

}
