package nomad.model.descriptive.substitution;

import nomad.misc.MathRound;

/**
 * @author Christian Schneider
 * @hidden
 */
public class NoteScale extends Substitution {

	public NoteScale() {
		super();
	}

	public String valueToString(int value) {		
		switch(value) {
			case 0:   return "0";
			case 127: return "±64";
		}

		String suffix = "";
		switch ((value/2)%12 + value%2) {
			case 0: suffix = "(Oct)"; break;
			case 7: suffix = "(5th)"; break;
			case 10:suffix = "(7th)"; break;
		}
		return "±"+MathRound.round(value/2.0, -1)+suffix;
	}
}
