package nomad.model.descriptive.substitution;

import nomad.misc.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class DrumHz extends Substitution {

	public DrumHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 20.0 * Math.pow(2.0, value/24);

		if (aFloat<100)
			return ""+MathRound.round(aFloat, -1)+" Hz";
		
		else
			return ""+MathRound.round(aFloat, 0)+" Hz";
	}

}
