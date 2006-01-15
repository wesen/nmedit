package org.nomad.port.message;

import org.nomad.patch.Patch;
import org.nomad.port.ComPortListener;


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
