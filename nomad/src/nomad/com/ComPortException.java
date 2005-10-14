package nomad.com;

/**
 * The class ComPortException and its subclasses are
 * thrown by the ComPort implementations.
 * 
 * @author Christian Schneider
 */
public class ComPortException extends Exception {

	/**
	 * Creates a new exception
	 * @see java.lang.Exception
	 */
	public ComPortException() {
		super();
	}

	/**
	 * Creates a new exception
	 * @param message the detail message
	 * @see java.lang.Exception
	 */
	public ComPortException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception
	 * @param cause the cause
	 * @see java.lang.Exception
	 */
	public ComPortException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new exception
	 * @param message the detail message
	 * @param cause the cause
	 * @see java.lang.Exception
	 */
	public ComPortException(String message, Throwable cause) {
		super(message, cause);
	}

}
