package net.sf.nmedit.nomad.xml.dom.substitution;

/**
 * @author Christian Schneider
 * @hidden
 */
public class BPM extends Substitution {

	public BPM() {
		super();
	}

	public String valueToString(int value) {
		if (value<=32)
			value = 2 * value +24;
		
		else if (value<=96)
			value = value +56;
		
		else
			value = 2*value -40;
	    
		return Integer.toString(value)+" bpm";
	}
}
