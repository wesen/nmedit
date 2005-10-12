package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

public class DrumPartials extends Substitution {
	
	private final static String[] FRACTIONS =
		new String[] {"1:1", "2:1", "4:1"};

	public DrumPartials() {
		super();
	}

	public String valueToString(int value) {
		if (value%48==0)
			return FRACTIONS[value / 48];
		else
			return "x"+MathRound.round(Math.pow(2.0, value/48), -2);
	}
}
