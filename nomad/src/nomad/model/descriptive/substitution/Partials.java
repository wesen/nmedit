package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class Partials extends Substitution {
	
	private final static String[] FRACTIONS =
		new String[] {"1:32", "1:16", "1:8", 
			"1:4", "1:2", "1:1", "2:1", 
			"4:1", "8:1", "16:1", "32:1"};

	public Partials() {
		super();
	}

	public String valueToString(int value) {
		if ((value+8)%12==0)
			return FRACTIONS[value / 12];
		else
			return "x"+MathRound.round(Math.pow(2.0, (value-64)/12), -3);
	}
}
