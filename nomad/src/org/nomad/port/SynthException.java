package org.nomad.port;

/**
 * The class ComPortException and its subclasses are
 * thrown by the class Synth.
 * 
 * @author Christian Schneider
 * @see org.nomad.port.Synth
 * @hidden
 */
public class SynthException extends Exception {

	/**
	 * Creates a new exception
	 * @see java.lang.Exception
	 */
	public SynthException() {
		super();
	}

	/**
	 * Creates a new exception
	 * @param message the detail message
	 * @see java.lang.Exception
	 */
	public SynthException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception
	 * @param cause the cause
	 * @see java.lang.Exception
	 */
	public SynthException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new exception
	 * @param message the detail message
	 * @param cause the cause
	 * @see java.lang.Exception
	 */
	public SynthException(String message, Throwable cause) {
		super(message, cause);
	}

}
