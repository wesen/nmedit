package nomad.com;

/**
 * 
 * @author Christian Schneider
 * @see ComPort
 */
public interface ComPortListener {

	/**
	 * 
	 * @param message the midi message
	 */
	void comportMessageReceived(MidiMessage message);

}
