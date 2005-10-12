package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

public class EqHz extends Substitution {

	public EqHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 471.0 * Math.pow(2.0, (value-60)/12);

		if (aFloat<1000)
			return ""+MathRound.round(aFloat, 0)+" Hz";

		else if (aFloat<10000)
			return ""+MathRound.round(aFloat/1000, -2)+" kHz";

		else
			return ""+MathRound.round(aFloat/1000, -1)+" kHz";
	}

}
