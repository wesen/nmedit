package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

/**
 * @author Christian Schneider
 * @hidden
 */
public class AmpGain extends Substitution {
	
	public AmpGain() {
		super();
	}

	public String valueToString(int value) {
		if (value==63)
			value++;
		
		double aFloat = 0.25 * Math.pow(2.0, value/32.0d);
		return "x"+NomadUtilities.roundTo(aFloat/32.0d,-2);
	}
}
