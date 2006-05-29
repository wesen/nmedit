package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;


/**
 * @author Christian Schneider
 * @hidden
 */
public class DrumPartials extends Substitution {
	
	private final static String[] FRACTIONS =
		new String[] {"1:1", "2:1", "4:1"};

	public DrumPartials() {
		super();
	}

	public String valueToString(int value) {
		if (value%48==0)
			return FRACTIONS[value / 48];
		else
			return "x"+NomadUtilities.roundTo(Math.pow(2.0d, value/48.0d), -2);
	}
}
