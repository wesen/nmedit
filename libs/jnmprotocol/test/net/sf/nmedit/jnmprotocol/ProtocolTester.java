package net.sf.nmedit.jnmprotocol;

import junit.framework.*;
import javax.sound.midi.*;

public class ProtocolTester extends TestCase
{
    protected void setUp()
    {
    }
    
    public void testMidiDriver()
	throws Exception
    {
	MidiDriver md = new MidiDriver();

	MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
	
	System.out.println("");
	System.out.println("MIDI devices:");
	for (int i = 0; i < info.length; i++) {
	    System.out.println(info[i].getName() + " "
			       + info[i].getDescription() + " "
			       + info[i].getVendor());
	    System.out.println("Receivers: " + MidiSystem.getMidiDevice(info[i]).getMaxReceivers());
	    System.out.println("Transmitters: " + MidiSystem.getMidiDevice(info[i]).getMaxTransmitters());
	}

	md.connect(info[0], info[1]);
	for (int i = 0; i < 10; i++) {
	    System.out.print("" + i + ":");
	    byte[] data = md.receive();
	    for (int j = 0; j < data.length; j++) {
		System.out.print(" " + data[j]);
	    }
	    System.out.println("");
	    Thread.sleep(100);
	}
	md.disconnect();
    }

    public void testProtocol()
	throws Exception
    {
	try {
	    MidiMessage.usePdlFile("/usr/local/lib/nmprotocol/midi.pdl", null);
	    MidiDriver md = new MidiDriver();
	    MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
	    md.connect(info[0], info[1]);
	    NmProtocol p = new NmProtocol(md);
	    p.addListener(new Listener());
	    p.send(new IAmMessage());
	    int n = 0;
	    while(n < 200) {
		n++;
		p.heartbeat();
		Thread.sleep(10);
	    }
	}
	catch (Throwable e) {
	    e.printStackTrace();
	}
    }

    class Listener extends NmProtocolListener
    {
	public void messageReceived(IAmMessage message)
	{
	    System.out.println("IAmMessage: " +
			       "sender:" + message.get("sender") + " " +
			       "versionHigh:" + message.get("versionHigh") + " " +
			       "versionLow:" + message.get("versionLow"));
	    if (message.get("sender") == IAmMessage.MODULAR) {
		System.out.println("IAmMessage: " +
				   "unknown1:" + message.get("unknown1") + " " +
				   "unknown2:" + message.get("unknown2") + " " +
				   "unknown3:" + message.get("unknown3") + " " +
				   "unknown4:" + message.get("unknown4"));
	    }
	}
	
	public void messageReceived(LightMessage message)
	{
	    System.out.println("LightMessage: " +
			       "slot:" + message.get("slot") + " " +	
			       "pid:" + message.get("pid") + " " +
			       message.get("light0") +
			       message.get("light1") +
			       message.get("light2") +
			       message.get("light3") +
			       message.get("light4") +
			       message.get("light5") +
			       message.get("light6") +
			       message.get("light7") +
			       message.get("light8") +
			       message.get("light9") +
			       message.get("light10") +
			       message.get("light11") +
			       message.get("light12") +
			       message.get("light13") +
			       message.get("light14") +
			       message.get("light15") +
			       message.get("light16") +
			       message.get("light17") +
			       message.get("light18") +
			       message.get("light19"));
	}

	//    public void messageReceived(PatchMessage message) {}

	public void messageReceived(AckMessage message)
	{
	    System.out.println("AckMessage: " +
			       "slot:" + message.get("slot") + " " +
			       "pid1:" + message.get("pid1") + " " +
			       "type:" + message.get("type") + " " +
			       "pid2:" + message.get("pid2"));
	}

	//    public void messageReceived(PatchListMessage message) {}

	public void messageReceived(NewPatchInSlotMessage message)
	{
	    System.out.println("NewPatchInSlotMessage: " +
			       "slot:" + message.get("slot") + " " +	
			       "pid:" + message.get("pid"));	    
	}
	
	public void messageReceived(VoiceCountMessage message)
	{
	    System.out.println("VoiceCountMessage: " +
			       "voiceCount0:" + message.get("voiceCount0") + " " +
			       "voiceCount1:" + message.get("voiceCount1") + " " +
			       "voiceCount2:" + message.get("voiceCount2") + " " +
			       "voiceCount3:" + message.get("voiceCount3"));	    
	}
	
	public void messageReceived(SlotsSelectedMessage message)
	{
	    System.out.println("SlotsSelectedMessage: " +
			       "slot0Selected:" + message.get("slot0Selected") + " " +
			       "slot1Selected:" + message.get("slot1Selected") + " " +
			       "slot2Selected:" + message.get("slot2Selected") + " " +
			       "slot3Selected:" + message.get("slot3Selected"));
	}
	
	public void messageReceived(SlotActivatedMessage message)
	{
	    System.out.println("SlotActivatedMessage: " +
			       "activeSlot:" + message.get("activeSlot"));
	}
	
	public void messageReceived(ParameterMessage message)
	{
	    System.out.println("ParameterMessage: " +
			       "slot:" + message.get("slot") + " " +
			       "pid:" + message.get("pid") + " " +
			       "section:" + message.get("section") + " " +
			       "module:" + message.get("module") + " " +
			       "parameter:" + message.get("parameter") + " " +
			       "value:" + message.get("value"));
	}
    }
}
