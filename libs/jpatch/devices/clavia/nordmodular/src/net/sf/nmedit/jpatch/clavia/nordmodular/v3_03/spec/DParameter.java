package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec;


import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution.Substitution;

/**
 * A object descriping the properties of a parameter
 * @author Christian Schneider
 * @composed 1 - 1 nomad.model.descriptive.substitution.Substitution
 */
public class DParameter extends DModulePart {
	
	/** The substitutin object that is used to get a string representation of the parameters raw value */
	private Substitution pmSubstitution;
	/** The default value */
	private int pmDefaultValue=0;
	/** The id of this parameter */
	private int pmId;
	/** The name of this parameter */
	private String pmName;
	/** The smallest possible value */
	private int pmMinValue=0;
	/** The largest possible value */
	private int pmMaxValue=2;
	/** The number of bit's this parameter's value has */
	private int pmBitCount=-1;
	
	/**
	 * Creates a new parameter object that uses the default substitution and has no bit count information (bitcount=-1)
	 * @param parent the parent module this parameter belongs to
	 * @param minValue the smallest possible value
	 * @param maxValue the largest possible value
	 * @param id the id of this parameter
	 * @param name the name of this parameter
	 * @see Substitution#DEFAULT_SUBSTITUTION
	 */
	public DParameter(DModule parent,
		int minValue, int maxValue, int id, String name) {
		this(parent,Substitution.DEFAULT_SUBSTITUTION, minValue, maxValue, -1, id, name);
	}

	/**
	 * Creates a new parameter object using a custom substitution.
	 * @param parent the parent module this parameter belongs to
	 * @param minValue the smallest possible value
	 * @param maxValue the largest possible value
	 * @param id the id of this parameter
	 * @param name the name of this parameter
	 * @param substitution the substitution used to get a string representation of it's raw numeric value
	 * @param bitCount number of bits the value has
	 */	
	public DParameter(DModule parent,
		Substitution substitution, int minValue, int maxValue, int bitCount,
		int id, String name) {
		super(parent);
		this.pmSubstitution = (substitution==null)?Substitution.DEFAULT_SUBSTITUTION:substitution;
		this.pmMinValue=minValue;
		this.pmBitCount=bitCount;
		this.pmMaxValue=maxValue;
		this.pmId = id;
		this.pmName = name;
	}

	/**
	 * Returns the number of bits used by this parameter or -1 if this information is not available
	 * @return the number of bits used by this parameter or -1 if this information is not available
	 */
	public int getBitCount() {
		return this.pmBitCount;
	}

	/**
	 * Returns a String representation of value using the substitution used by this parameter 
	 * @param value the value
	 * @return the formatted value
	 * @see Substitution
	 */
	public String getFormattedValue(int value) {
		return pmSubstitution.valueToString(value);
	}
	/**
	 * Returns a String representation of value using the substitution used by this parameter.
	 * The return value is shortened so that it does not return more that maxdigits characters if
	 * possible. 
	 *  
	 * @param value the value
	 * @param maxdigits upper bound for number of characters returned
	 * @return the formatted value
	 * @see Substitution
	 */
	public String getFormattedValue(int value, int maxdigits) {
		return pmSubstitution.valueToString(value, maxdigits);
	}
	
	/**
	 * Returns number of states this parameter has
	 * @return number of states this parameter has
	 */
	public int getNumStates() {
		return pmMaxValue-pmMinValue+1;
	}

	/**
	 * Returns the smallest possible value
	 * @return the smallest possible value
	 */
	public int getMinValue() {
		return pmMinValue;
	}

	/**
	 * Returns the largest possible value
	 * @return the largest possible value
	 */
	public int getMaxValue() {
		return pmMaxValue;
	}
	
	/**
	 * Returns the default value of this parameter
	 * @return the default value of this parameter
	 */
	public int getDefaultValue() {
		return pmDefaultValue;
	}

	/**
	 * Sets the default value
	 * @param pmDefaultValue the new default value
	 */
	public void setDefaultValue(int pmDefaultValue) {
		this.pmDefaultValue = pmDefaultValue;
	}

	/**
	 * Returns the id of this parameter
	 * @return the id of this parameter
	 */
	public int getId() {
		return pmId;
	}

	/**
	 * Returns the name of this parameter
	 * @return the name of this parameter
	 */
	public String getName() {
		return pmName;
	}

	/**
	 * Returns the name of this parameter
	 * @return the name of this parameter
	 */
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
