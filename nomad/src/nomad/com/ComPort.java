package nomad.com;

import nomad.com.message.MidiMessage;

/**
 * ComPort is the interface to the underlying Nord Modular
 * protocol implementation.
 * @composed 1 - 1 nomad.com.MidiDriverList
 * @stereotype Interface
 *    
 * @author Christian Schneider
 */
public abstract class ComPort {
	
	/**
	 * listening object 
	 */
	private ComPortListener listener = null;

	/**
	 * List of supported drivers.
	 */
	private MidiDriverList driverList = new MidiDriverList();
	
	/**
	 * The used midi driver.
	 */
	private MidiDriver driver = null;

	ComPort(ComPortListener listener) {
		if (listener==null)
			throw new NullPointerException(
				"'MidiEventListener' must not be null"
			);
		this.listener = listener;
	}
	
	/**
	 * Returns a list containg the available midi drivers.
	 * @return a list containg the available midi drivers.
	 */
	public MidiDriverList getDrivers() {
		return driverList; 
	}
	
	/**
	 * Connects to the synth through the underlying implementation. 
	 * @throws ComPortException While connecting to the synth an
	 * exception occured.
	 */
	public abstract void openPort() throws ComPortException;
	
	/**
	 * Closes the connection to the synth through the underlying
	 * implementation. 
	 * @throws ComPortException While disconnecting from the synth an
	 * exception occured.
	 */
	public abstract void closePort() throws ComPortException;
	
	/**
	 * Returns true of there is an active connection to the synth,
	 * which allowes sending and receiving messages. 
	 * @return true if port is open.
	 */
	public abstract boolean isPortOpen();
	
	/**
	 * Sends a message to the synth.
	 * @param message the message
	 * @throws ComPortException an exception occured while trying
	 * to send the message.
	 */
	public abstract void send(MidiMessage message)  throws ComPortException;
	
	/**
	 * Sends the heartbeat() message to the synth.
	 * @throws ComPortException an exception occured while trying
	 * to send the message.
	 */
	public abstract void heartbeat() throws ComPortException;

	/**
	 * Sets the driver that should be used by the underlying architecture.
	 * @param driver
	 */
	public void setDriver(MidiDriver driver) {
		this.driver = driver;
	}
	
	/**
	 * Returns the current driver.
	 * @return the current driver.
	 */
	public MidiDriver getDriver() {
		return driver;
	}
	
	/**
	 * Broadcasts the received MidiMessage message to the listener.
	 * @param message the received MidiMessage message
	 */
	protected void received(MidiMessage message) {
		listener.comportMessageReceived(message);
	}

	/**
	 * Creates a instance of the ComPort implementation.
	 * @param listener The object that wants to listen the ComPort instance.
	 * @return instance of the ComPort implementation. 
	 */
	public static ComPort getDefaultComPortInstance(ComPortListener listener) { 
		return new NullComPort(listener);
	}
	
}
