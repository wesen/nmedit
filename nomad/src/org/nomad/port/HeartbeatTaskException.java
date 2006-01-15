package org.nomad.port;

/**
 * The class HeartBeatTaskException and its subclasses are
 * thrown by the HeartbeatTask implementations.
 * 
 * @author Christian Schneider
 * @hidden
 */
public class HeartbeatTaskException extends Exception {

	/**
	 * Creates a new exception
	 * @see java.lang.Exception
	 */
	public HeartbeatTaskException() {
		super();
	}

	/**
	 * Creates a new exception
	 * @param message the detail message
	 * @see java.lang.Exception
	 */
	public HeartbeatTaskException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception
	 * @param cause the cause
	 * @see java.lang.Exception
	 */
	public HeartbeatTaskException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new exception
	 * @param message the detail message
	 * @param cause the cause
	 * @see java.lang.Exception
	 */
	public HeartbeatTaskException(String message, Throwable cause) {
		super(message, cause);
	}

}
