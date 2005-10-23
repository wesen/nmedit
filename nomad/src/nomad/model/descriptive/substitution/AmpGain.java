package nomad.model.descriptive.substitution;

import nomad.misc.MathRound;

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
		
		double aFloat = 0.25 * Math.pow(2.0, value/32);
		return "x"+MathRound.round(aFloat/32,-2);
	}
}
