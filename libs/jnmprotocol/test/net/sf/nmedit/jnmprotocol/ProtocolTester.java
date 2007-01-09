package net.sf.nmedit.jnmprotocol;

import junit.framework.*;
import javax.sound.midi.*;

import net.sf.nmedit.jnmprotocol.utils.NmLookup;
import net.sf.nmedit.jpdl.*;

public class ProtocolTester extends TestCase
{
    static private int pid0;
    
    MidiDevice.Info[] nmDevice = new MidiDevice.Info[0];
    MidiDriver nmDriver; 
    
    protected void setUp()
    {
        nmDevice = NmLookup.lookup(NmLookup.getHardwareDevices(), 1,
                1000 /* 1 second timeout for each midi device pair*/
               );
        if (nmDevice.length != 2)
            throw new RuntimeException("Nord Modular device not available");
        
        nmDriver = new MidiDriver(nmDevice[0], nmDevice[1]);
    }
    
    private NmProtocol createProtocol(MidiDriver driver, MessageHandler messageHandler)
    {
        NmProtocol protocol = new NmProtocolST();
        protocol.getTransmitter().setReceiver(driver.getReceiver());
        driver.getTransmitter().setReceiver(protocol.getReceiver());
        protocol.setMessageHandler(messageHandler);
        return protocol;
    }
    
    public void testMidiDriver()
	throws Exception
    {

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

    nmDriver.connect();
    
    try
    {
        /*
    	for (int i = 0; i < 10; i++) {
    	    System.out.print("" + i + ":");
    	    byte[] data = md.receive();
    	    for (int j = 0; j < data.length; j++) {
    		System.out.print(" " + data[j]);
    	    }
    	    System.out.println("");
    	    Thread.sleep(100);
    	}
        */
    }
    finally
    {
        nmDriver.disconnect();
    }
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
	    Assert.assertEquals(data, correctEncoding[n]);
	    n++;
	}
    }

    public void testNoParameterNewModuleMessage()
	throws Exception
    {
	//NewModuleMessage.usePdlFile("/patch.pdl", new TestTracer());
	//MidiMessage.usePdlFile("/midi.pdl", new TestTracer());
	NewModuleMessage nm = new NewModuleMessage();
	nm.set("pid", 5);
	BitStream bitStream = null;
	try {
	    int[] parameterValues = {};
	    int[] customValues = {};
	    nm.newModule(1, 1, 9, 0, 28, "Some1", parameterValues, customValues);
	    bitStream = (BitStream)nm.getBitStream().get(0);
	} catch(Exception e) {
	    e.printStackTrace();
	}
	int n = 0;
	System.out.println("Size: " + bitStream.getSize());
	while (bitStream.isAvailable(8)) {
	    int data = bitStream.getInt(8);
	    //System.out.println(" " + data + " " + correctEncoding[n]);
	    n++;
	}	
    }

    public void testProtocol()
	throws Exception
    {
	try {
        nmDriver.connect();
        MessageMulticaster multicaster = new MessageMulticaster();
	    NmProtocol p = createProtocol(nmDriver, multicaster);
        multicaster.addProtocolListener(new Listener(p, multicaster));
	    p.send(new IAmMessage());
	    p.send(new RequestPatchMessage());
	    int n = 0;
	    while(n < 100) {
		n++;
		p.heartbeat();
		Thread.sleep(10);
	    }
	    pid0 = multicaster.getActivePid(0);
	}
	catch (MidiException me) {
	    me.printStackTrace();
	    System.out.println("" + me.getError());
	}
	catch (Throwable e) {
	    e.printStackTrace();
	}
    finally
    {
        nmDriver.disconnect();
    }
    }

    public void testModuleMessages()
	throws Exception
    {
	System.out.println("testModuleMessages: " + pid0);
	try {
        nmDriver.connect();
        MessageMulticaster multicaster = new MessageMulticaster();
        NmProtocol p = createProtocol(nmDriver, multicaster);
	    //MidiMessage.usePdlFile("/midi.pdl", new TestTracer());
        multicaster.addProtocolListener(new Listener(p, multicaster));
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
	    NewModuleMessage nm2 = new NewModuleMessage();
	    nm2.set("pid", pid0);
	    int[] parameterValues2 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15,0,0};
	    int[] customValues2 = {};
	    nm2.newModule(91, 1, 11, 1, 30, "CtrlSeqN",
			  parameterValues2, customValues2);
	    p.send(nm2);
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
    finally
    {
        nmDriver.disconnect();
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
    private MessageMulticaster multicaster;

	public Listener(NmProtocol p, MessageMulticaster multicaster)
	{
	    this.p = p;
        this.multicaster = multicaster;
	}

	public void messageReceived(IAmMessage message)
	{
	    System.out.println(message);
	}
	
	public void messageReceived(LightMessage message)
	{
	    System.out.println(message);
	}

	public void messageReceived(MeterMessage message)
	{
	    System.out.println(message);
	}

	public void messageReceived(PatchMessage message)
	{
	    System.out.println(message);
	}

	public void messageReceived(AckMessage message)
	{
	    System.out.println(message);

	    try {
		GetPatchMessage gpm = new GetPatchMessage();
		gpm.set("pid", multicaster.getActivePid(0));
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
        System.out.println(message);
	}
	
	public void messageReceived(NewPatchInSlotMessage message)
	{
	    System.out.println(message);	    
	}
	
	public void messageReceived(VoiceCountMessage message)
	{
	    System.out.println(message);	    
	}
	
	public void messageReceived(SlotsSelectedMessage message)
	{
	    System.out.println(message);
	}
	
	public void messageReceived(SlotActivatedMessage message)
	{
	    System.out.println(message);
	}
	
	public void messageReceived(ParameterMessage message)
	{
	    System.out.println(message);
	}

	public void messageReceived(ErrorMessage message)
	{
	    System.out.println(message);
	}
    }
}
