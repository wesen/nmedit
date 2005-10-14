package nomad.com;

import java.util.Vector;

/**
 * Interface for the midi driver
 * @author Christian Schneider
 */
public abstract class MidiDriver {
	
	/**
	 * Name of the driver
	 */
	private String name = null;
	
	/**
	 * List containing the ports that can be used
	 */
	private Vector midiPortList = new Vector();
	
	/**
	 * index of the selected port
	 */
	private int defaultPortIndex = -1;

	/**
	 * Creates a new MidiDriver object.
	 * @param name
	 */
	MidiDriver(String name) {
		this.name = name;
	}

	/**
	 * Adds port to the list of supported ports
	 * @param port the port
	 * @see MidiPort
	 */
	void registerPort(MidiPort port) {
		midiPortList.add(port);
		if (midiPortList.size()==1)
			setDefaultPort(0);
	}

	/**
	 * Returns the number of available ports.
	 * @return the number of available ports.
	 * @see MidiPort
	 */
	public int getPortCount() {
		return midiPortList.size();
	}

	/**
	 * Returns the port at the specified index
	 * @param index the index
	 * @return the port at the specified index
	 * @see MidiPort
	 */
	public MidiPort getPort(int index) {
		return (MidiPort) midiPortList.get(index);
	}

	/**
	 * Returns the name of this driver
	 * @return name of this driver
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name of this driver
	 * @return name of this driver
	 */
	public String toString() {
		return getName();
	}
	
	/**
	 * Specifies the default port
	 * @param index
	 * @see MidiPort 
	 */
	public void setDefaultPort(int index) {
		this.defaultPortIndex = index;
	}
	
	/**
	 * Returns the index of the default port
	 * @return the index of the default port
	 * @see MidiPort
	 */
	int getDefaultPortIndex() {
		return this.defaultPortIndex;
	}

	/**
	 * Returns the default port.
	 * @return the default port
	 * @see MidiPort
	 */
	public MidiPort getDefaultPort() {
		return getPort(defaultPortIndex);
	}

}
