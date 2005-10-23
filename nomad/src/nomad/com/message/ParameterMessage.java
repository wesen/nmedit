package nomad.com.message;

import nomad.com.ComPortListener;

/**
 * @author Christian Schneider
 * @hidden
 */
public abstract class ParameterMessage extends MidiMessage {
	public abstract int getSection();
	public abstract int getModule(); 
	public abstract int getParameter();
	public abstract int getValue();
	public abstract int getPid() ;
	public abstract void setParameter(int param);
	public abstract void setValue(int value);

	public void notifyListener(ComPortListener listener) {
		listener.messageReceived(this);
	}
}
