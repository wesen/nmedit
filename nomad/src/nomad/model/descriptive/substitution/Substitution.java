package nomad.model.descriptive.substitution;

public abstract class Substitution {
	
	public final static Substitution DEFAULT_SUBSTITUTION
		= new DefaultSubstitution();

	public abstract String valueToString(int value);

	public String valueToString(int value, int maxDigits) {
		return valueToString(value);
	}

}
