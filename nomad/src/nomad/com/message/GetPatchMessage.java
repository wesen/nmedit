package nomad.com.message;

import nomad.com.ComPortListener;

public abstract class GetPatchMessage extends MidiMessage {
	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
