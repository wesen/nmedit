package org.nomad.port.message;

import org.nomad.port.ComPortListener;

/**
 * Parent interface for all midi messages
 * @author Christian Schneider
 * //@composed 1 - 1 nomad.com.BitInputStream
 */
public abstract class MidiMessage {
	public abstract boolean expectsReply();
	public abstract boolean isReply();
	public abstract void setSlot(int slot);
	public abstract int getSlot();
	public abstract void notifyListener(ComPortListener listener);
}
