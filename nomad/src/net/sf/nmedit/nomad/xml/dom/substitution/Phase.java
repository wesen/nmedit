package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

/**
 * @author Christian Schneider
 * @hidden
 */
public class Phase extends Substitution {

	public Phase() {
		super();
	}

	public String valueToString(int value) {
		return Integer.toString(
			(int) NomadUtilities.roundTo(value*2.8125-180.0, 0)	
		);
	}
}
