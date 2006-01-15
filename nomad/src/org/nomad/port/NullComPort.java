package org.nomad.port;

import org.nomad.patch.Patch;
import org.nomad.port.message.AckMessage;
import org.nomad.port.message.GetPatchMessage;
import org.nomad.port.message.IAmMessage;
import org.nomad.port.message.LightMessage;
import org.nomad.port.message.MessageBuilder;
import org.nomad.port.message.MidiMessage;
import org.nomad.port.message.NewPatchInSlotMessage;
import org.nomad.port.message.ParameterMessage;
import org.nomad.port.message.PatchListMessage;
import org.nomad.port.message.PatchMessage;
import org.nomad.port.message.RequestPatchMessage;
import org.nomad.port.message.SlotActivatedMessage;
import org.nomad.port.message.SlotsSelectedMessage;
import org.nomad.port.message.VoiceCountMessage;


/**
 * The NullComPort is an implementation of the ComPort interface that
 * does ignore any messages and actions and logs them to System.out.
 * @author Christian Schneider
 * @see org.nomad.port.ComPort
 */
public class NullComPort extends ComPort {

	/**
	 * True if connection is open (simulated).
	 */
	private boolean portActive = false;
	
	/**
	 * True if messages should be logged to System.out
	 */
	private boolean verbose = true;

	/**
	 * Creates the NullComPort implementation
	 */
	public NullComPort() {
		super(new NullMessageBuilder()); 
		this.getDrivers().registerDriver(new NullMidiDriver());
	}

	public void send(MidiMessage m) {
		statusMessage("send("+m+")");
	}
	
	/**
	 * Enables verbose mode that logs events to System.out.
	 * Verbose is enabled by default.
	 * @param enabled true if events should be logged
	 */
	public void setVerbose(boolean enabled) {
		this.verbose = enabled;
	}
	
	/**
	 * Returns true if verbose is enabled.
	 * @return true if verbose is enabled.
	 */
	public boolean getVerbose() {
		return verbose;
	}

	/**
	 * Prints message to System.out if verbose is enabled.
	 * @param message
	 * @see #setVerbose(boolean)
	 */
	protected void statusMessage(String message) {
		if (verbose)
			System.out.println("** NullComPort:"+message);
	}

	public void heartbeat() {
		statusMessage("heartbeat()");
	}

	public void openPort() throws ComPortException {
		if (portActive)
			throw new ComPortException("Port is already open");

		portActive = true;
		statusMessage("openPort()");
	}

	public void closePort() throws ComPortException {
		if (!portActive)
			throw new ComPortException("Port is not open");

		portActive = false;
		statusMessage("closePort()");
	}
	
	public boolean isPortOpen() {
		return portActive;
	}
	
	private class NullMidiDriver extends MidiDriver {
		NullMidiDriver() {
			super("NullMidiDriver");
			this.registerPortIn(new NullMidiPort());
			this.registerPortOut(new NullMidiPort());
		}
	}

	private class NullMidiPort extends MidiPort {
		NullMidiPort() {
			super("NullPort");
		}
	}
	
	void sendMessage(MidiMessage message) {
		System.out.println("Sending message requested for:"+message);
	}
}

class NullMessageBuilder implements MessageBuilder {
	public AckMessage newAckMessage() {
		return new NullAckMessage();
	}
	public GetPatchMessage newGetPatchMessage(int slot, int pid) {
		return new NullGetPatchMessage();
	}
	public IAmMessage newIAmMessage() {
		return new NullIAmMessage();
	}
	public LightMessage newLightMessage() {
		return null;
	}
	public NewPatchInSlotMessage newNewPatchInSlotMessage() {
		return null;
	}
	public ParameterMessage newParameterMessage(int pid, int section, int module, int parameter, int value) {
		return new NullParameterMessage(pid,section,module,parameter,value);
	}
	public PatchListMessage newPatchListMessage(int section, int position) {
		return null;
	}
	public PatchMessage newPatchMessage(Patch patch) {
		return new NullPatchMessage(patch);
	}
	public RequestPatchMessage newRequestPatchMessage() {
		return null;
	}
	public SlotActivatedMessage newSlotActivatedMessage() {
		return null;
	}
	public SlotsSelectedMessage newSlotsSelectedMessage() {
		return null;
	}
	public VoiceCountMessage newVoiceCountMessage() {
		return null;
	}
}

class NullAckMessage extends AckMessage {
	public void setPid1(int pid) { }
	public void setPid2(int pid) { }
	public int getPid1() {return 0;}
	public int getPid2() {return 0;}
	public boolean expectsReply() {return false;}
	public boolean isReply() {return false;}
	public void setSlot(int slot) {}
	public int getSlot() {return 0;}
	public String toString() {return this.getClass().getName();}
}

class NullGetPatchMessage extends GetPatchMessage {
	public boolean expectsReply() {return false;}
	public boolean isReply() {return false;}
	public void setSlot(int slot) {}
	public int getSlot() {return 0;}
	public String toString() {return this.getClass().getName();}
}

class NullIAmMessage extends IAmMessage {
	public void setVersion(int high, int low) {}
	public boolean isSenderModular() {return false;}
	public int getVersionHigh() {return 3;}
	public int getVersionLow() {return 3;}
	public boolean expectsReply() {return false;}
	public boolean isReply() {return false;}
	public void setSlot(int slot) {}
	public int getSlot() {return 0;}
	public String toString() {return this.getClass().getName();}
}

class NullPatchMessage extends PatchMessage {
	private Patch patch;
	public NullPatchMessage(Patch patch) {
		this.patch=patch;
	}
	public Patch getPatch() {return patch;}
	public int getPid() {return 0;}
	public boolean expectsReply() {return false;}
	public boolean isReply() {return false;}
	public void setSlot(int slot) {}
	public int getSlot() {return 0;}
	public String toString() {return this.getClass().getName();}
}
class NullParameterMessage extends ParameterMessage {
	private int pid;
	private int section;
	private int module;
	private int parameter;
	private int value;
	public NullParameterMessage(int pid, int section, int module, int parameter, int value) {
		this.pid=pid; 
		this.section=section;
		this.module=module;
		this.parameter=parameter;
		this.value=value;
	}

	public int getSection() {return section;}
	public int getModule() {return module;}
	public int getParameter() {return parameter;}
	public int getValue() {return value;}
	public int getPid() {return pid;}
	public void setParameter(int param) {}
	public void setValue(int value) {}
	public boolean expectsReply() {return false;}
	public boolean isReply() {return false;}
	public void setSlot(int slot) {}
	public int getSlot() {return 0;}
	public String toString() {return this.getClass().getName();}
}
