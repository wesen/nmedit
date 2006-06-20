package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

/**
 * @author Christian Schneider
 * @hidden
 */
public class Note extends Substitution {
	
	private final static String[] NOTES = 
		new String[] {"C","C","D","D","E","F","F","G","G","A","A","B"};
	private final static String[] SHARPS = 
		new String[] {" ","#"," ","#"," "," ","#"," ","#"," ","#"," "};

	public Note() {
		super();
	}

	public String valueToString(int value) {
		int v12 = value % 12;
		return NOTES[v12]+((value/12) -1)+SHARPS[v12];
	}

}
