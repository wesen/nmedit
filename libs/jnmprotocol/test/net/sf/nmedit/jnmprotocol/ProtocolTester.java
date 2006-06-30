package net.sf.nmedit.jnmprotocol;

import junit.framework.*;
import javax.sound.midi.*;
import net.sf.nmedit.jpdl.*;

public class ProtocolTester extends TestCase
{
    static private int pid0;
    
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

    public void testNewModuleMessage()
	throws Exception
    {
	int[] correctEncoding = {
	    0xf0, 0x33, 0x7c, 0x06, 0x05, 0x18, 0x01, 0x60, 0x10, 0x50, 0x08,
	    0x12, 0x4f, 0x39, 0x58, 0x68, 0x13, 0x10, 0x01, 0x25, 0x00, 0x00,
	    0x13, 0x30, 0x11, 0x20, 0x78, 0x08, 0x08, 0x08, 0x00, 0x00, 0x00,
	    0x00, 0x00, 0x00, 0x5b, 0x40, 0x45, 0x00, 0x20, 0x02, 0x6a, 0x02,
	    0x0a, 0x27, 0x5c, 0x6c, 0x34, 0x09, 0x48, 0x00, 0x1e, 0xf7
	};
	//NewModuleMessage.usePdlFile("/patch.pdl", new TestTracer());
	//MidiMessage.usePdlFile("/midi.pdl", new TestTracer());
	NewModuleMessage nm = new NewModuleMessage();
	nm.set("pid", 5);
	BitStream bitStream = null;
	    try {
	    int[] parameterValues = {64, 64, 64, 64, 0, 0, 0, 0, 0, 0};
	    int[] customValues = {0};
	    nm.newModule(7, 1, 10, 2, 9, "OscA2", parameterValues, customValues);
	    bitStream = (BitStream)nm.getBitStream().get(0);
	} catch(Exception e) {
	    e.printStackTrace();
	}
	int n = 0;
	System.out.println("Size: " + bitStream.getSize());
	while (bitStream.isAvailable(8)) {
	    int data = bitStream.getInt(8);
	    //System.out.println(" " + data + " " + correctEncoding[n]);
	    assert(data == correctEncoding[n]);
	    n++;
	}
    }

    public void testProtocol()
	throws Exception
    {
	try {
	    MidiDriver md = new MidiDriver();
	    MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
	    md.connect(info[0], info[1]);
	    NmProtocol p = new NmProtocol(md);
	    p.addListener(new Listener(p));
	    p.send(new IAmMessage());
	    p.send(new RequestPatchMessage());
	    int n = 0;
	    while(n < 100) {
		n++;
		p.heartbeat();
		Thread.sleep(10);
	    }
	    pid0 = p.getActivePid(0);
	}
	catch (MidiException me) {
	    me.printStackTrace();
	    System.out.println("" + me.getError());
	}
	catch (Throwable e) {
	    e.printStackTrace();
	}
    }

    public void testModuleMessages()
	throws Exception
    {
	System.out.println("testModuleMessages: " + pid0);
	try {
	    MidiDriver md = new MidiDriver();
	    MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
	    md.connect(info[0], info[1]);
	    NmProtocol p = new NmProtocol(md);
	    //MidiMessage.usePdlFile("/midi.pdl", new TestTracer());
	    p.addListener(new Listener(p));
	    MoveModuleMessage mm = new MoveModuleMessage();
	    mm.set("pid", pid0);
	    mm.moveModule(1, 10, 40, 40);
	    p.send(mm);
	    DeleteModuleMessage dm = new DeleteModuleMessage();
	    dm.set("pid", pid0);
	    dm.deleteModule(1, 10);
	    p.send(dm);
	    int n = 0;
	    NewModuleMessage nm = new NewModuleMessage();
	    nm.set("pid", pid0);
	    int[] parameterValues = {64, 64, 64, 64, 0, 0, 0, 0, 0, 0};
	    int[] customValues = {0};
	    nm.newModule(7, 1, 10, 2, 90, "OscA2",
			 parameterValues, customValues);
	    p.send(nm);
	    while(n < 100) {
		n++;
		p.heartbeat();
		Thread.sleep(10);
	    }
	}
	catch (MidiException me) {
	    me.printStackTrace();
	    System.out.println("" + me.getError());
	}
	catch (Throwable e) {
	    e.printStackTrace();
	}
    }

    class TestTracer implements net.sf.nmedit.jpdl.Tracer
    {
	public void trace(String message)
	{
	    System.out.println("T: " + message);
	}
    }

    class Listener extends NmProtocolListener
    {
	private NmProtocol p;

	public Listener(NmProtocol p)
	{
	    this.p = p;
	}

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

	public void messageReceived(PatchMessage message)
	{
	    System.out.println("PatchMessage:");
	}

	public void messageReceived(AckMessage message)
	{
	    System.out.println("AckMessage: " +
			       "slot:" + message.get("slot") + " " +
			       "pid1:" + message.get("pid1") + " " +
			       "type:" + message.get("type") + " " +
			       "pid2:" + message.get("pid2"));

	    try {
		GetPatchMessage gpm = new GetPatchMessage();
		gpm.set("pid", p.getActivePid(0));
		p.send(gpm);
	    }
	    catch (MidiException me) {
		me.printStackTrace();
		System.out.println("" + me.getError());
	    }
	    catch (Throwable e) {
		e.printStackTrace();
	    }
	}

	public void messageReceived(PatchListMessage message)
	{
	}
	
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

	public void messageReceived(ErrorMessage message)
	{
	    System.out.println("ErrorMessage: " +
			       "slot:" + message.get("slot") + " " +
			       "pid:" + message.get("pid") + " " +
			       "code:" + message.get("code"));
	}
    }
}
