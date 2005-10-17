package nomad.com;

import nomad.com.message.MidiMessage;

/**
 * The Synth manages the connection to the Nord Modular through the
 * ComPort interface.
 * 
 * @author Christian Schneider
 * @see nomad.com.ComPort
 * @composed 1 - 1 nomad.com.ComPort
 * @composed 0 - 1 nomad.com.HeartbeatTask
 * @has 1 - n nomad.com.SynthListenerSubscriberList
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

/** ------------------------------------------------------------------------ */	
/*
	  public Patch getPatch(int arg0) 
	  public void setPatch(int arg0, Patch arg1) 
	  public void load(int arg0, int arg1) 
	  public void store(int arg0, int arg1)
	  public int getActiveSlot()
	  public void setActiveSlot(int arg0)
	  public boolean isSlotSelected(int arg0)
	  public void setSlotSelected(int arg0, boolean arg1) 
	  public int getSlotVoices(int arg0)
	  public void addListener(SynthListener arg0)
	  public void removeListener(SynthListener arg0)
	  public void notifyListeners(int slot, Patch patch)
	  public void notifyListeners()
	  public void notifyListeners(int slot)
	  public void messageReceived(IAmMessage message)
	  public void messageReceived(LightMessage message) 
	  public void messageReceived(PatchMessage message)
	  public void messageReceived(AckMessage message)
	  public void messageReceived(PatchListMessage message)
	  public void messageReceived(NewPatchInSlotMessage message)
	  public void messageReceived(VoiceCountMessage message)
	  public void messageReceived(SlotsSelectedMessage message)
	  public void messageReceived(SlotActivatedMessage message)
	  public void messageReceived(ParameterMessage message)
*/
	
}
