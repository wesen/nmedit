package org.nomad.port.message;

import org.nomad.port.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class SlotsSelectedMessage extends MidiMessage {
	public abstract boolean isSelected(int slot);
	public abstract void setSelected(int slot, boolean state);

	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
