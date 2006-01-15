package org.nomad.port.message;

import org.nomad.port.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class PatchListMessage extends MidiMessage {
	public abstract int getSection();
	public abstract int getPosition();
	public abstract StringList getNames(); 
	public abstract boolean endOfList();
	public abstract boolean endOfSection(String name);
	public abstract boolean emptyPosition(String name);
	
	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
