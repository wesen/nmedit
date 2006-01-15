package org.nomad.port.message;

import org.nomad.port.ComPortListener;

public abstract class GetPatchMessage extends MidiMessage {
	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
