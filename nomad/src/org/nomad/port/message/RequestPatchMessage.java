package org.nomad.port.message;

import org.nomad.port.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class RequestPatchMessage extends MidiMessage {

	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
