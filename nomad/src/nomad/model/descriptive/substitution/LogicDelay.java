package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class LogicDelay extends Substitution {

	public LogicDelay() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = Math.pow(2.0, value/9);

		if (aFloat<1000)
			return ""+MathRound.round(aFloat, 0)+" ms";
		
		else if (aFloat<10000)
			return ""+MathRound.round(aFloat/1000, -1)+" s";
		
		else
			return ""+MathRound.round(aFloat/1000, 0)+" s";
	}

}
