package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

/**
 * @author Christian Schneider
 * @hidden
 */
public class FilterHz1 extends Substitution {

	public FilterHz1() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 504.0 * Math.pow(2.0, (value-64)/12.0d);

		if (aFloat<1000)
			return ""+NomadUtilities.roundTo(aFloat, 0)+" Hz";

		else if (aFloat<10000)
			return ""+NomadUtilities.roundTo(aFloat, -2)+" Hz";

		else
			return ""+NomadUtilities.roundTo(aFloat, -1)+" Hz";
	}

}
