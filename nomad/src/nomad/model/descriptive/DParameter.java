package nomad.model.descriptive;

import nomad.model.descriptive.substitution.Substitution;
/**
 * @author Christian Schneider
 * @composed 1 - 1 nomad.model.descriptive.substitution.Substitution
 */
public class DParameter {
	
	private DModule parent = null;
	
	private Substitution pmSubstitution;
	private int pmDefaultValue=0;
	private int pmId;
	private String pmName;
	private int pmMinValue=0;
	private int pmMaxValue=2;
	private int pmBitCount=-1;
	
	public DParameter(DModule parent,
		int minValue, int maxValue, int id, String name) {
		this(parent,Substitution.DEFAULT_SUBSTITUTION, minValue, maxValue, -1, id, name);
	}

	public DParameter(DModule parent,
		Substitution substitution, int minValue, int maxValue, int bitCount,
		int id, String name) {
		if (parent==null)
			throw new NullPointerException("'parent' must not be null");
		this.parent = parent;
		this.pmSubstitution = (substitution==null)?Substitution.DEFAULT_SUBSTITUTION:substitution;
		this.pmMinValue=minValue;
		this.pmBitCount=bitCount;
		this.pmMaxValue=maxValue;
		this.pmId = id;
		this.pmName = name;
	}
			
	
	public DModule getParent() {
		return parent;
	}

	public String getFormattedValue(int value) {
		return pmSubstitution.valueToString(value);
	}

	public int getBitCount() {
		return this.pmBitCount;
	}

	public String getFormattedValue(int value, int maxdigits) {
		return pmSubstitution.valueToString(value, maxdigits);
	}
	
	public int getNumStates() {
		return pmMaxValue-pmMinValue+1;
	}

	public int getDefaultValue() {
		return pmDefaultValue;
	}

	public void setDefaultValue(int pmDefaultValue) {
		this.pmDefaultValue = pmDefaultValue;
	}

	public int getId() {
		return pmId;
	}

	public String getName() {
		return pmName;
	}

	public String toString() {
		/*
		return super.toString()+"[id:"+pmId
		   	+",name:"+pmName
		   	+",unit:"+pmSubstitution
			+",states:"+getNumStates()
			+",default:"+pmDefaultValue+"]";
		*/
		return getName();
	}
}
