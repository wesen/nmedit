package nomad.com.message;

import nomad.com.BitInputStream;

/**
 * Parent class for all midi messages
 * @author Christian Schneider
 * @composed 1 - 1 nomad.com.BitInputStream
 */
public abstract class MidiMessage {

	/**
	 * Creates a new MidiMessage 
	 */
	protected MidiMessage() {
		super();
	}
	protected MidiMessage(BitInputStream data) {
		super();
	}
	
	protected abstract BitInputStream getRawData();

}
