package nomad.com;

/**
 * The NullComPort is an implementation of the ComPort interface that
 * does ignore any messages and actions and logs them to System.out.
 * @author Christian Schneider
 * @see nomad.com.ComPort
 */
public class NullComPort extends ComPort {

	/**
	 * True if connection is open (simulated).
	 */
	private boolean portActive = false;
	
	/**
	 * True if messages should be logged to System.out
	 */
	private boolean verbose = true;
	
	/**
	 * Creates the NullComPort implementation
	 * @param listener the ComPortListener
	 */
	public NullComPort(ComPortListener listener) {
		super(listener); 
		this.getDrivers().registerDriver(new NullMidiDriver());
	}

	public void send(MidiMessage m) {
		statusMessage("send("+m+")");
	}
	
	/**
	 * Enables verbose mode that logs events to System.out.
	 * Verbose is enabled by default.
	 * @param enabled true if events should be logged
	 */
	public void setVerbose(boolean enabled) {
		this.verbose = enabled;
	}
	
	/**
	 * Returns true if verbose is enabled.
	 * @return true if verbose is enabled.
	 */
	public boolean getVerbose() {
		return verbose;
	}
	
	/**
	 * Prints message to System.out if verbose is enabled.
	 * @param message
	 * @see #setVerbose(boolean)
	 */
	protected void statusMessage(String message) {
		if (verbose)
			System.out.println("** NullComPort:"+message);
	}

	/**
	 * The heartbeat() message implementation.
	 * @see ComPort#heartbeat()
	 */
	public void heartbeat() {
		statusMessage("heartbeat()");
	}

	/**
	 * The openPort() message implementation.
	 * @see ComPort#openPort()
	 */
	public void openPort() throws ComPortException {
		if (portActive)
			throw new ComPortException("Port is already open");

		portActive = true;
		statusMessage("openPort()");
	}

	/**
	 * The closePort() message implementation.
	 * @see ComPort#closePort()
	 */
	public void closePort() throws ComPortException {
		if (!portActive)
			throw new ComPortException("Port is not open");

		portActive = false;
		statusMessage("closePort()");
	}
	
	/**
	 * The isPortOpen() message implementation.
	 * @see ComPort#isPortOpen()
	 */
	public boolean isPortOpen() {
		return portActive;
	}
	
	private class NullMidiDriver extends MidiDriver {
		NullMidiDriver() {
			super("NullMidiDriver");
			this.registerPort(new NullMidiPort());
		}
	}

	private class NullMidiPort extends MidiPort {
		NullMidiPort() {
			super("NullPort");
		}
	}
	
}
