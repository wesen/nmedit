package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

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
			(int) MathRound.round(value*2.8125-180.0, 0)	
		);
	}
}
