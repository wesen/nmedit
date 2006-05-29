package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

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
			return ""+NomadUtilities.roundTo(aFloat, -2)+" Hz";
		
		else if (aFloat<100)
			return ""+NomadUtilities.roundTo(aFloat, -1)+" Hz";
				
		else if (aFloat<1000)
			return ""+NomadUtilities.roundTo(aFloat,  0)+" Hz";
					
		else if (aFloat<1000)
			return ""+NomadUtilities.roundTo(aFloat/1000.0, -2)+" kHz";
		
		else
			return ""+NomadUtilities.roundTo(aFloat/1000.0, -2)+" kHz";
	}

}
