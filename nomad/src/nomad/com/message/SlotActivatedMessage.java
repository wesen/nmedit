package nomad.com.message;

import nomad.com.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class SlotActivatedMessage extends MidiMessage {
	public abstract int getActiveSlot();
	public abstract void setActiveSlot(int slot);

	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
