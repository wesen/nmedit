package org.nomad.port;

/**
 * Interface for the MidiPort
 * @author Christian Schneider
 */
public class MidiPort {
	
	/**
	 * Name of the port
	 */
	private String name = null;

	/**
	 * Creates a new MidiPort with the specified name
	 * @param name name of the MidiPort
	 */
	public MidiPort(String name) {
		this.name = name;
	}

	/**
	 * @return name of the MidiPort
	 */
	public String getName() {
		return name;
	}

	public String toString() {
		return getName();
	}

}
