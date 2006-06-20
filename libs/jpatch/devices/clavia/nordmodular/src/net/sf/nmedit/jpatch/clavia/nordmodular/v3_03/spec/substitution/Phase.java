package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

import net.sf.nmedit.jmisc.math.Math2;

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
			(int) Math2.roundTo(value*2.8125-180.0, 0)	
		);
	}
}
