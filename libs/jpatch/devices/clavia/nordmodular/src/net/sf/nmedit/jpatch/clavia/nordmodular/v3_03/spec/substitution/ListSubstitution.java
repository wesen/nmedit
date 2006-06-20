package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution;

public class ListSubstitution extends Substitution {
	private String[] set;

	public ListSubstitution(String[] set) {
		this.set=set;
	}

	public String valueToString(int value) {
		if (value<0||value>=set.length)
			return null;
		return set[value];
	}

	public String toString() {
		String text="";
		for (int i=0;i<set.length;i++)
			text+=set[i]+((i<set.length-1)?",":"");
		
		return super.toString()+"[{"+text+"}]";
	}
}
