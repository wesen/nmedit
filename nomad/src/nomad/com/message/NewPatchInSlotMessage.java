package nomad.com.message;

import nomad.com.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class NewPatchInSlotMessage extends MidiMessage { 
	public abstract int getPid();
	
	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
