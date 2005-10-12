package nomad.xml;

public class XMLAttributeValidationException extends Exception {

	public XMLAttributeValidationException() {
		super();
	}
	
	public XMLAttributeValidationException(String message) {
		super(message);
	}
	
	public XMLAttributeValidationException(Throwable cause) {
		super(cause);
	}
	
	public XMLAttributeValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
