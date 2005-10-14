package nomad.com;

/**
 * The Synth manages the connection to the Nord Modular through the
 * ComPort interface.
 * 
 * @author Christian Schneider
 * @see nomad.com.ComPort
 */
public class Synth implements ComPortListener, HeartbeatErrorHandler {

	/**
	 * The used ComPort implementation.
	 */
	private ComPort comPort = ComPort.getDefaultComPortInstance(this);
	
	/**
	 * Timer for sending frequentyl heartbeat() messages. 
	 */
	private HeartbeatTask heart = null;
	
	/**
	 * Listeners that want to be notified if the connection state changed.
	 */
	private SynthListenerSubscriberList connectionStateListeners =
		new SynthListenerSubscriberList();
	
	/**
	 * Creates a new Synth object.
	 */
	public Synth() {
		super();
	}

	/**
	 * Returns the ComPort object
	 * @return the ComPort
	 */
	public ComPort getCompPort() {
		return comPort;
	}
	
	/**
	 * Registers a listener for connect/disconnect events.
	 * @param listener the listener
	 */
	public void addSynthConnectionStateListener(SynthConnectionStateListener listener) {
		connectionStateListeners.subscribeListener(listener);
	}
	
	/**
	 * Removes listener from the list of subscribers.
	 * @param listener the listener
	 */
	public void removeSynthConnectionStateListener(SynthConnectionStateListener listener) {
		connectionStateListeners.unsubscribeListener(listener);
	}
	
	/**
	 * Notifies all SynthConnectionStateListeners.
	 *
	 */
	private void notifySynthConnectionStateListener() {
		for (int i=0;i<connectionStateListeners.getSubscriberCount();i++)
			((SynthConnectionStateListener)connectionStateListeners.getSubscriber(i))
			.synthConnectionStateChanged(this);
	}

	/**
	 * Invoked by ComPort if an message is received
	 * @see ComPort
	 */
	public void comportMessageReceived(MidiMessage message) {
		//
	}

	/**
	 * Connects to the synth using the current ComPort implementation.
	 * @throws SynthException if either an connection is active or
	 * an exception occured while opening the connection throught ComPort. 
	 */
	public void connect() throws SynthException {
		if (comPort.isPortOpen())
			throw new SynthException("Already connected.");

		try {
			comPort.openPort();
		} catch (ComPortException e) {
			throw new SynthException(e);
		}
		
		heart = new HeartbeatTask(comPort, this);
		try {
			heart.start();
		} catch (HeartbeatTaskException cause) {
			heart = null;
			throw new SynthException(cause);
		}
		
		notifySynthConnectionStateListener();
	}

	/**
	 * Closes the current connection to the synth.
	 * @throws SynthException if no connection is active.
	 */
	public void disconnect() throws SynthException {
		if (!comPort.isPortOpen())
			throw new SynthException("Not connected.");
		
		if (heart!=null && heart.isRunning())
			try {
				heart.stop();
			} catch (HeartbeatTaskException e) {
				e.printStackTrace();
			}
		
		try {
			comPort.closePort();
		} catch (ComPortException e) {
			throw new SynthException(e);
		}
		
		notifySynthConnectionStateListener();
	}

	/**
	 * Returns true if there is an active connection to the synth.
	 * @return true if there is an active connection to the synth.
	 */
	public boolean isConnected() {
		return comPort.isPortOpen();
	}

	public void exceptionOccured(HeartbeatTaskExceptionMessage e) {
		System.out.println("** Exception while sending heartbeat(): forced disconnect");
		e.getCause().printStackTrace();
		e.emergencyStop();
		heart = null;
		try {
			disconnect();
		} catch (SynthException e1) {
			e1.printStackTrace();
		}
	}

}
