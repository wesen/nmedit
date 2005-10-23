package nomad.com.message;

import nomad.com.ComPortListener;
import nomad.patch.Patch;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class PatchMessage extends MidiMessage {
	public abstract Patch getPatch(); 
	public abstract int getPid();
	
	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
