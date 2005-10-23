package nomad.com.message;

import nomad.com.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class RequestPatchMessage extends MidiMessage {

	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
