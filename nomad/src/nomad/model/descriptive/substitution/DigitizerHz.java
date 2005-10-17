package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class DigitizerHz extends Substitution {

	public DigitizerHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 32.70 * Math.pow(2.0, value/12);

		if (aFloat<100)
			return ""+MathRound.round(aFloat, -2)+" Hz";
		
		else if (aFloat<1000)
			return ""+MathRound.round(aFloat, -1)+" Hz";
		
		else if (aFloat<10000)
			return ""+MathRound.round(aFloat/1000, -3)+" kHz";
		
		else
			return ""+MathRound.round(aFloat/1000, -2)+" kHz";
	}

}
