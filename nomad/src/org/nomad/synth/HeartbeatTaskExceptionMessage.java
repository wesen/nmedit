package org.nomad.synth;

/**
 * The message informs about a ComPortException and allows to control
 * the HeartbeatTask class that send this message.
 * @author Christian Schneider
 * @see org.nomad.port.HeartbeatErrorHandler
 * @see org.nomad.port.HeartbeatTask
 * @hidden
 */
public class HeartbeatTaskExceptionMessage {
	
	/**
	 * The task in which the exception has occured. 
	 */
	private HeartbeatTask task;
	
	/**
	 * The cause of this exception.
	 */
	private Throwable cause;
	
	/**
	 * Is false if no response was send using either ignoreCause() or emergencyStop().
	 */
	private boolean isHandled = false;

	/**
	 * The message informs about a ComPortException and allows to control
	 * the HeartbeatTask class that send this message using either 
	 * ignoreCause() or emergencyStop().
	 * @param task
	 * @param cause
	 * @see #ignoreCause()
	 * @see #emergencyStop()
	 */
	public HeartbeatTaskExceptionMessage(HeartbeatTask task, Throwable cause) {
		this.task = task;
		this.cause= cause;
	}
	
	/**
	 * Returns the cause of the exception.
	 * @return the cause.
	 */
	public Throwable getCause() {
		return cause;
	}
	
	/**
	 * Returns the HeartbeatTask object that sent this message.
	 * @return the HeartbeatTask object that sent this message.
	 */
	public HeartbeatTask getSource() {
		return task;
	}
	
	/**
	 * Asks the HeartbeatTask to ignore the exception and
	 * sets the isCausedHandle() flag to true.
	 * @see #emergencyStop()
	 */
	public void ignoreCause() {
		isHandled = true;
	}
	
	/**
	 * Returns true if either ignoreCause() or emergencyStop() was called.
	 * @return true if either ignoreCause() or emergencyStop() was called.
	 * @see #ignoreCause()
	 * @see #emergencyStop()
	 */
	public boolean isCauseHandled() {
		return isHandled;
	}
	
	/**
	 * Stops the HeartbeatTask from running and
	 * sets the isCausedHandle() flag to true.
	 * @see #ignoreCause()
	 */
	public void emergencyStop() {
		try {
			if (!isHandled)
				task.stop();
		} catch (HeartbeatTaskException e) {
			e.printStackTrace();
		}
	}
	
}
