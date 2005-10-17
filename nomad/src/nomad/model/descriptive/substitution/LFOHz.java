package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class LFOHz extends Substitution {

	public LFOHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 440.0 * Math.pow(2.0, (value-177)/12);

		if (aFloat<0.1)
			return ""+MathRound.round(1/aFloat, -1)+" s";

		else if (aFloat<10)
			return ""+MathRound.round(aFloat, -2)+" Hz";
				
		else if (aFloat<100)
			return ""+MathRound.round(aFloat, -1)+" Hz";

		else
			return ""+MathRound.round(aFloat, 0)+" Hz";
	}

}
