package net.sf.nmedit.nomad.xml.dom.substitution;

import net.sf.nmedit.nomad.util.NomadUtilities;

/**
 * @author Christian Schneider
 * @hidden
 */
public class PartialRange extends Substitution {

	public PartialRange() {
		super();
	}

	public String valueToString(int value) {		
		switch(value) {
			case 0:   return "0";
			case 127: return "?64 *";
		}

		String suffix = (value>64)? " *":"";
		return "?"+NomadUtilities.roundTo(value/2.0, -1)+suffix;
	}
}
