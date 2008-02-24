package net.sf.nmedit.jsynth.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;


public class MidiDescription {
	private String name;
	private String vendor;
	private String version;
	private String description;
	private boolean isInput;
	private int id;
	
	public MidiDescription(String name, String vendor, String version, String description, int isInput, int id) {
		this.name = name;
		this.vendor = vendor;
		this.version = version;
		this.description = description;
		this.isInput = isInput == 1;
		this.id = id;
	}
	
	public MidiDescription(MidiDevice.Info info, int isInput) {
		MidiID midiId = new MidiID(MidiSystem.getMidiDeviceInfo());
		this.name = info.getName();
		this.vendor = info.getVendor();
		this.version = info.getVersion();
		this.description = info.getDescription();
		this.isInput = isInput == 1;
		this.id = midiId.getID(info);
	}

	public String getName() {
		return name;
	}

	public String getVendor() {
		return vendor;
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

	public boolean isInput() {
		return isInput;
	}
	
	public int getId() {
		return id;
	}
}