package nomad.model.descriptive.substitution;

/**
 * A object used by the DParameter objects to get a formatted
 * string representation of their value
 * 
 * @author Christian Schneider
 * @see nomad.model.descriptive.DParameter
 */
public abstract class Substitution {
	
	/**
	 * The default substitution used of no custom substitution is given.
	 * @see DefaultSubstitution
	 */
	public final static Substitution 
		DEFAULT_SUBSTITUTION = new DefaultSubstitution();

	/**
	 * Returns a string repressentation containing the formatted value.
	 * @param value the value that should be formatted
	 * @return the formatted value
	 * @see nomad.model.descriptive.DParameter#getFormattedValue(int)
	 */
	public abstract String valueToString(int value);

	/**
	 * Returns a string repressentation containing the formatted value.
	 * @param value the value that should be formatted
	 * @param maxDigits maximum number of digits
	 * @return the formatted value
	 * @see nomad.model.descriptive.DParameter#getFormattedValue(int, int)
	 */
	public String valueToString(int value, int maxDigits) {
		return valueToString(value);
	}

}
