package nomad.com;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * The HeartbeatTask sends frequently heartbeat messages to the
 * synthesizer through the ComPort interface.
 * If the client specifies an HeartbeatErrorHandler the messages
 * received through that interface can be responded.
 *   
 * @author Christian Schneider
 * @see nomad.com.HeartbeatErrorHandler
 * @see nomad.com.HeartbeatTaskExceptionMessage
 */

public class HeartbeatTask
{
	/**
	 * Don't use java.util.timer since the lib' may not be threadsafe
	 * javax.swing.Timer runs the task on the event dispatching thread
	 * See also http://java.sun.com/docs/books/tutorial/uiswing/misc/timer.html
	 * and http://java.sun.com/products/jfc/tsc/articles/timer/
	 */
	private Timer timer = null;
	
	/**
	 * ComPort instance
	 */
	private ComPort comPort = null;
	
	/**
	 * The HeartbeatErrorHandler will be called if
	 * an ComPortException occures when sending the
	 * heartbeat() message.
	 * 
     * @see nomad.com.HeartbeatErrorHandler
     * @see nomad.com.HeartbeatTaskExceptionMessage
	 */
	private HeartbeatErrorHandler errorHandler = null;

	/**
	 * Default interval for sending heartbeat() message is 1 second.
	 */
	public final static int DEFAULT_HEARTBEAT_INTERVAL=1000;

	/**
	 * Creates a new HeartbeatTask instance which sends hearbeat()
	 * messages through the comPort interface. Since no HeartbeatErrorHandler
	 * is specified, the timer will be stopped if any ComPortException
	 * is thrown while invoking the heartbeat() method.
	 * @param comPort The heartbeat() messages will be send through this
	 *  interface.
     * @see nomad.com.HeartbeatErrorHandler
     * @see nomad.com.HeartbeatTaskExceptionMessage
	 */
	public HeartbeatTask(ComPort comPort) {
		this(comPort, null);
	}

	/**
	 * Creates a new HeartbeatTask instance which sends hearbeat()
	 * messages through the comPort interface. If no HeartbeatErrorHandler
	 * is specified, the timer will be stopped if any ComPortException
	 * is thrown while invoking the heartbeat() method.
	 * If the HeartbeatErrorHandler is specified, it will be notified
	 * if any exception occures. Note that if the HeartbeatErrorHandler
	 * does not call either ignoreCause() or emergencyStop() on the passed
	 * HeartbeatTaskCriticalException the timer will also be stopped.
	 * @param comPort The heartbeat() messages will be send through this interface.
	 * @param errorHandler the errorHandler
     * @see nomad.com.HeartbeatErrorHandler
     * @see nomad.com.HeartbeatTaskExceptionMessage
	 */
	public HeartbeatTask(ComPort comPort, HeartbeatErrorHandler errorHandler) {
		this.comPort = comPort;
		this.errorHandler = errorHandler;
	 }

	/**
	 * Start the timer to send frequently heartbeat() messages.
	 * The interval is DEFAULT_HEARTBEAT_INTERVAL. 
	 * @throws HeartbeatTaskException if the task is already running
	 */
	public void start() throws HeartbeatTaskException {
		start(DEFAULT_HEARTBEAT_INTERVAL);
	}

	/**
	 * Start the timer to send frequently heartbeat() messages
	 * using the specified interval time. 
	 * @param interval interval in milliseconds
	 * @throws HeartbeatTaskException thrown if the timer is already running
	 */
	public void start(int interval) throws HeartbeatTaskException {
		if (isRunning())
			throw new HeartbeatTaskException("Heartbeat Task is already running!");

		timer = new Timer(interval, new TimerEventAdapter());
		timer.setRepeats(true); // enable repeating
		timer.setCoalesce(true); // enable coalescing
		timer.start();
	}

	/**
	 * Returns true if the Timer is running.  
	 * @return true if the Timer is running.
	 */
	public boolean isRunning() {
		return timer!=null && timer.isRunning();
	}

	/**
     * Stop sending heartbeat messages.
     * @throws HeartbeatTaskException if the timer is not running.
     */
	public void stop() throws HeartbeatTaskException {
		if (!isRunning())
			throw new HeartbeatTaskException("Heartbeat Task is not running!");

		timer.stop();
		timer=null;
	}
	
	class TimerEventAdapter implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				comPort.heartbeat();
			} catch (ComPortException cause) {
				if (errorHandler==null) {
					try {
						stop();
					} catch (HeartbeatTaskException e) {
						e.printStackTrace();
					}
				} else {
					HeartbeatTaskExceptionMessage efeedback = 
						new HeartbeatTaskExceptionMessage(HeartbeatTask.this, cause);
					
					errorHandler.exceptionOccured(efeedback);
					if (efeedback.isCauseHandled())
						try {
							stop();
						} catch (HeartbeatTaskException e) {
							e.printStackTrace();
						}
				}
			}
		}
	}

}
