package org.nomad.xml.dom.substitution;

import org.nomad.util.misc.MathRound;

public class TransformationSubstitution extends Substitution {
	
	private String replacement = null;
	private int comparator = -1;
	private double offset = 0;
	private double factor = 1;
	private String praefix= "";
	private String suffix = "";
	
	public TransformationSubstitution() {
	}

	public TransformationSubstitution(double offset, double factor) {
		this.offset = offset;
		this.factor = factor;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}
	
	public void setFactor(double factor) {
		this.factor = factor;
	}
	
	public void setReplacement(int comparator, String replacement) {
		this.comparator = comparator;
		this.replacement = replacement;
	}
	
	public void setDisableReplacement() {
		this.setReplacement(-1, null);
	}
	
	public void setPraefix(String praefix) {
		this.praefix = praefix!=null?praefix:"";
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix!=null?suffix:"";
	}

	public String valueToString(int value) {
		if (replacement!=null && value==comparator)
			return replacement;
		
		return praefix
			+MathRound.doubleToStr(MathRound.round( ((double)value + offset) * factor, -4))
			+suffix;
	}
// TODO handle max. digit number
/* 
	public String valueToString(int value, int maxDigits) {
		if (replacement!=null && value==comparator)
			return replacement;

		maxDigits-=praefix.length();
		maxDigits-=suffix.length();		
		
		return praefix
			+MathRound.doubleToStr(MathRound.round( ((double)value + offset) * factor,-4))
			+suffix;
	}
*/
}
