package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class FilterHz1 extends Substitution {

	public FilterHz1() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 504.0 * Math.pow(2.0, (value-64)/12);

		if (aFloat<1000)
			return ""+MathRound.round(aFloat, 0)+" Hz";

		else if (aFloat<10000)
			return ""+MathRound.round(aFloat, -2)+" Hz";

		else
			return ""+MathRound.round(aFloat, -1)+" Hz";
	}

}
