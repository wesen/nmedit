package org.nomad.xml.dom.substitution;

import org.nomad.util.misc.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class FilterHz2 extends Substitution {

	public FilterHz2() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 330.0 * Math.pow(2.0, (value-60)/12);

		if (aFloat<1000)
			return ""+MathRound.round(aFloat, 0)+" Hz";

		else if (aFloat<10000)
			return ""+MathRound.round(aFloat/1000, -2)+" Hz";

		else
			return ""+MathRound.round(aFloat/1000, -1)+" Hz";
	}

}
