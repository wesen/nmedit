package org.nomad.port.message;

import org.nomad.port.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class VoiceCountMessage extends MidiMessage {
	public abstract int getVoiceCount(int slot);

	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
