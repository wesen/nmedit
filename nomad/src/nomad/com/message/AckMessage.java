package nomad.com.message;

import nomad.com.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class AckMessage extends MidiMessage {
	public abstract void setPid1(int pid);
	public abstract void setPid2(int pid);
	public abstract int getPid1();
	public abstract int getPid2();

	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
