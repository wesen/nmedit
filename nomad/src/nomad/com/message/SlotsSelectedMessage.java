package nomad.com.message;

import nomad.com.ComPortListener;

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
