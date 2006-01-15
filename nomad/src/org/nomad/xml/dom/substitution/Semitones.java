package org.nomad.xml.dom.substitution;

/**
 * @author Christian Schneider
 * @hidden
 */
public class Semitones extends Substitution {

	public Semitones() {
		super();
	}

	public String valueToString(int value) {
		value -= 64;
		
		String suffix = "";
		switch ((value+72 )%12) {
			case 0: suffix = "(Oct)"; break;
			case 7: suffix = "(5th)"; break;
			case 10:suffix = "(7th)"; break;
		}
		
		return Integer.toString(value)+suffix;
	}
}
