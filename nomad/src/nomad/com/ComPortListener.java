package nomad.com;

import nomad.com.message.MidiMessage;

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
