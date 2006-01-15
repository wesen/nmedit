package org.nomad.port;

import java.util.ArrayList;

/**
 * Interface for the midi driver
 * @author Christian Schneider
 * @has 1 - * nomad.com.MidiPort
 */
public class MidiDriver {
	
	/**
	 * Name of the driver
	 */
	private String name = null;
	
	/**
	 * List containing the ports that can be used
	 */
	private ArrayList midiInPortList = new ArrayList();
	private ArrayList midiOutPortList = new ArrayList();
	
	/**
	 * index of the selected port
	 */
	private int defaultPortInIndex = -1;
	private int defaultPortOutIndex = -1;

	/**
	 * Creates a new MidiDriver object.
	 * @param name
	 */
	protected MidiDriver(String name) {
		this.name = name;
	}

	/**
	 * Adds port to the list of supported ports
	 * @param port the port
	 * @see MidiPort
	 */
	protected void registerPortIn(MidiPort port) {
		midiInPortList.add(port);
		if (midiInPortList.size()==1)
			setDefaultPortIn(0);
	}

	/**
	 * Adds port to the list of supported ports
	 * @param port the port
	 * @see MidiPort
	 */
	protected void registerPortOut(MidiPort port) {
		midiOutPortList.add(port);
		if (midiOutPortList.size()==1)
			setDefaultPortOut(0);
	}

	/**
	 * Returns the number of available input ports.
	 * @return the number of available input ports.
	 * @see MidiPort
	 */
	public int getPortCountIn() {
		return midiInPortList.size();
	}

	/**
	 * Returns the number of available output ports.
	 * @return the number of available output ports.
	 * @see MidiPort
	 */
	public int getPortCountOut() {
		return midiOutPortList.size();
	}

	/**
	 * Returns the port at the specified index
	 * @param index the index
	 * @return the port at the specified index
	 * @see MidiPort
	 */
	public MidiPort getPortIn(int index) {
		return (MidiPort) midiInPortList.get(index);
	}

	/**
	 * Returns the port at the specified index
	 * @param index the index
	 * @return the port at the specified index
	 * @see MidiPort
	 */
	public MidiPort getPortOut(int index) {
		return (MidiPort) midiOutPortList.get(index);
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
	public void setDefaultPortIn(int index) {
		this.defaultPortInIndex = index;
	}

	/**
	 * Specifies the default port
	 * @param index
	 * @see MidiPort 
	 */
	public void setDefaultPortOut(int index) {
		this.defaultPortOutIndex = index;
	}
	
	/**
	 * Returns the index of the default port
	 * @return the index of the default port
	 * @see MidiPort
	 */
	int getDefaultPortInIndex() {
		return this.defaultPortInIndex;
	}
	
	/**
	 * Returns the index of the default port
	 * @return the index of the default port
	 * @see MidiPort
	 */
	int getDefaultPortOutIndex() {
		return this.defaultPortOutIndex;
	}

	/**
	 * Returns the default port.
	 * @return the default port
	 * @see MidiPort
	 */
	public MidiPort getDefaultPortIn() {
		return getPortIn(defaultPortInIndex);
	}

	/**
	 * Returns the default port.
	 * @return the default port
	 * @see MidiPort
	 */
	public MidiPort getDefaultPortOut() {
		return getPortOut(defaultPortOutIndex);
	}

}
