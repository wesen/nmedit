package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class PartialRange extends Substitution {

	public PartialRange() {
		super();
	}

	public String valueToString(int value) {		
		switch(value) {
			case 0:   return "0";
			case 127: return "±64 *";
		}

		String suffix = (value>64)? " *":"";
		return "±"+MathRound.round(value/2.0, -1)+suffix;
	}
}
