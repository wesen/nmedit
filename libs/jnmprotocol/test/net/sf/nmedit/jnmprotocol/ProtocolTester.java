package net.sf.nmedit.jnmprotocol;

import java.util.Iterator;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.nmedit.jnmprotocol.helper.GetPatchMessageReplyAcceptor;
import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.DeleteModuleMessage;
import net.sf.nmedit.jnmprotocol.DeletePatchMessage;
import net.sf.nmedit.jnmprotocol.ErrorMessage;
import net.sf.nmedit.jnmprotocol.GetPatchListMessage;
import net.sf.nmedit.jnmprotocol.GetPatchMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.LightMessage;
import net.sf.nmedit.jnmprotocol.LoadPatchMessage;
import net.sf.nmedit.jnmprotocol.MessageHandler;
import net.sf.nmedit.jnmprotocol.MessageMulticaster;
import net.sf.nmedit.jnmprotocol.MeterMessage;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.MoveModuleMessage;
import net.sf.nmedit.jnmprotocol.NewModuleMessage;
import net.sf.nmedit.jnmprotocol.NewPatchInSlotMessage;
import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.PatchListEntry;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jnmprotocol.StorePatchMessage;
import net.sf.nmedit.jnmprotocol.VoiceCountMessage;
import net.sf.nmedit.jnmprotocol.utils.NmLookup;
import net.sf.nmedit.jpdl.BitStream;

public class ProtocolTester extends TestCase
{
    static private int pid0;
    
    MidiDevice.Info[] nmDevice = new MidiDevice.Info[0];
    static MidiDriver nmDriver = null; 
    
    protected void setUp()
    {
        if (nmDriver != null)
            return;
        
        nmDevice = NmLookup.lookup(NmLookup.getHardwareDevices(), 1,
                1000 /* 1 second timeout for each midi device pair*/
               );
        if (nmDevice.length != 2)
            throw new RuntimeException("Nord Modular device not available");
        
        nmDriver = new MidiDriver(nmDevice[0], nmDevice[1]);
    }
    
    private NmProtocol createProtocol(MidiDriver driver, MessageHandler messageHandler)
    {
        return configureProtocol(new NmProtocol(), driver, messageHandler);
    }
    
    private NmProtocol configureProtocol(NmProtocol protocol,
            MidiDriver driver, MessageHandler messageHandler)
    {
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

    /**
     * Tests if each of the 13 PatchMessages is received
     */
    public void testGetPatchMessage() throws Exception
    {        
        System.out.println("test GetPatchMessage");
        try
        {
            nmDriver.connect();
            
            MessageMulticaster multicaster = new MessageMulticaster();
            NmProtocol protocol = new NmProtocol();
            GetPatchMessageReplyAcceptor acceptor = new GetPatchMessageReplyAcceptor(protocol);
            configureProtocol(protocol, nmDriver, multicaster);
            multicaster.addProtocolListener(acceptor);
            
            acceptor.sendInitialMessage();
            
            long timeout = System.currentTimeMillis()+20*1000;
            while (System.currentTimeMillis()<timeout && !acceptor.accepted())
            {
                protocol.heartbeat();
                protocol.waitForActivity(1000);
            }
            
            assertFalse("replies expected (PatchMessages:"+acceptor.getPatchMessageReplyCount()+", expected 13)", !acceptor.accepted());            
        }
        finally
        {
            nmDriver.disconnect();
        }
    }

    /**
     * Tests if the PatchListMessage is received
     */
    public void testGetPatchListMessage() throws Exception
    {
        System.out.println("test GetPatchListMessage");
        nmDriver.connect();
        try
        {
            NmProtocol protocol = configureProtocol(new NmProtocol(), nmDriver, null);
            MessageAcceptor acceptor = new MessageAcceptor(protocol, PatchListMessage.class);
            protocol.setMessageHandler(acceptor);
            
            protocol.send(new IAmMessage());
            
            int section = 0;
            int position = 0;
            
            do
            {
                GetPatchListMessage gpl = new GetPatchListMessage();
                gpl.set("section", section);
                gpl.set("position", position);
                protocol.send(gpl);
                // returns if the message is received or otherwise throws an exception
                acceptor.waitUntilAccepted(3000);
                // everything is ok
                
                System.out.print("section("+section+"),position("+position+")=");
                PatchListMessage patchListMessage = 
		    (PatchListMessage)acceptor.getMessage();
		for (Iterator<PatchListEntry> i =
			 patchListMessage.getEntries().iterator();
		     i.hasNext(); ) {
		    System.out.print("" + i.next() + ", ");
		}
		System.out.println();
		section = patchListMessage.getNextSection();
		position = patchListMessage.getNextPosition();

                acceptor.reset();
            } while (section>=0 && position>=0);
            System.out.println("success");
        }
        catch (Exception e)
        {
	    e.printStackTrace();
            System.out.println("failed");
            throw e;
        }
        finally
        {
            nmDriver.disconnect();
        }

    }

    public void testLoadPatchMessage()
	throws Exception
    {
        System.out.println("test LoadPatchMessage");
	try {
	    nmDriver.connect();
	    MessageMulticaster multicaster = new MessageMulticaster();
	    NmProtocol p = createProtocol(nmDriver, multicaster);
	    multicaster.addProtocolListener(new Listener(p, multicaster));

	    LoadPatchMessage lpm = new LoadPatchMessage();
	    lpm.set("loadslot", 0);
	    lpm.set("section", 0);
	    lpm.set("position", 0);
	    p.send(lpm);

	    lpm.set("loadslot", 1);
	    lpm.set("section", 1);
	    lpm.set("position", 1);
	    p.send(lpm);

	    lpm.set("loadslot", 2);
	    lpm.set("section", 2);
	    lpm.set("position", 2);
	    p.send(lpm);

	    lpm.set("loadslot", 3);
	    lpm.set("section", 3);
	    lpm.set("position", 3);
	    p.send(lpm);

	    int n = 0;
	    while(n < 400) {
		n++;
		p.heartbeat();
		Thread.sleep(10);
	    }
        }
        catch (Exception e)
        {
	    e.printStackTrace();
            System.out.println("failed");
            throw e;
        }
        finally
        {
            nmDriver.disconnect();
        }
    }

    
    public void testStorePatchMessage()
	throws Exception
    {
        System.out.println("test StorePatchMessage");
	try {
	    nmDriver.connect();
	    MessageMulticaster multicaster = new MessageMulticaster();
	    NmProtocol p = createProtocol(nmDriver, multicaster);
	    multicaster.addProtocolListener(new Listener(p, multicaster));

	    StorePatchMessage lpm = new StorePatchMessage();
	    lpm.set("storeslot", 0);
	    lpm.set("section", 8);
	    lpm.set("position", 95);
	    p.send(lpm);

	    lpm.set("storeslot", 1);
	    lpm.set("section", 8);
	    lpm.set("position", 96);
	    p.send(lpm);

	    lpm.set("storeslot", 2);
	    lpm.set("section", 8);
	    lpm.set("position", 97);
	    p.send(lpm);

	    lpm.set("storeslot", 3);
	    lpm.set("section", 8);
	    lpm.set("position", 98);
	    p.send(lpm);

	    int n = 0;
	    while(n < 400) {
		n++;
		p.heartbeat();
		Thread.sleep(10);
	    }
        }
        catch (Exception e)
        {
	    e.printStackTrace();
            System.out.println("failed");
            throw e;
        }
        finally
        {
            nmDriver.disconnect();
        }
    }

    
    public void testDeletePatchMessage()
	throws Exception
    {
        System.out.println("test DeletePatchMessage");
	try {
	    nmDriver.connect();
	    MessageMulticaster multicaster = new MessageMulticaster();
	    NmProtocol p = createProtocol(nmDriver, multicaster);
	    multicaster.addProtocolListener(new Listener(p, multicaster));

	    DeletePatchMessage lpm = new DeletePatchMessage();
	    lpm.set("section", 8);
	    lpm.set("position", 95);
	    p.send(lpm);

	    lpm.set("section", 8);
	    lpm.set("position", 96);
	    p.send(lpm);

	    lpm.set("section", 8);
	    lpm.set("position", 97);
	    p.send(lpm);

	    lpm.set("section", 8);
	    lpm.set("position", 98);
	    p.send(lpm);

	    int n = 0;
	    while(n < 400) {
		n++;
		p.heartbeat();
		Thread.sleep(10);
	    }
        }
        catch (Exception e)
        {
	    e.printStackTrace();
            System.out.println("failed");
            throw e;
        }
        finally
        {
            nmDriver.disconnect();
        }
    }

    
    public void testSlotsSelectedMessage()
	throws Exception
    {
        System.out.println("test SlotsSelectedMessage");
	try {
	    nmDriver.connect();
	    MessageMulticaster multicaster = new MessageMulticaster();
	    NmProtocol p = createProtocol(nmDriver, multicaster);
	    multicaster.addProtocolListener(new Listener(p, multicaster));

	    SlotsSelectedMessage lpm = new SlotsSelectedMessage();
	    lpm.set("slot0Selected", 0);
	    lpm.set("slot1Selected", 1);
	    lpm.set("slot2Selected", 0);
	    lpm.set("slot3Selected", 1);
	    p.send(lpm);

	    int n = 0;
	    while(n < 100) {
		n++;
		p.heartbeat();
		Thread.sleep(10);
	    }
        }
        catch (Exception e)
        {
	    e.printStackTrace();
            System.out.println("failed");
            throw e;
        }
        finally
        {
            nmDriver.disconnect();
        }
    }

    private static class MessageAcceptor implements MessageHandler
    {
        // TODO use NmMessageAcceptor instead of this class
        
        private final Class<? extends MidiMessage> messageClass;
        private final NmProtocol protocol;
        private MidiMessage acceptedMessage = null;

        public MessageAcceptor(NmProtocol protocol, Class<? extends MidiMessage> messageClass)
        {
            this.protocol = protocol;
            this.messageClass = messageClass;
        }

        public void reset()
        {
            acceptedMessage = null;
        }

        public MidiMessage getMessage()
        {
            return acceptedMessage;
        }

        public void processMessage( MidiMessage message )
        {
            if (message.getClass().equals(messageClass) && !accepted())
            {
                this.acceptedMessage = message;
            }
        }
        
        public boolean accepted()
        {
            return acceptedMessage != null;
        }
        
        public void waitUntilAccepted(final long timeout)
            throws Exception
        {
            final long threshold = System.currentTimeMillis()+timeout;
            
            while(!accepted())
            {
                if (System.currentTimeMillis()>threshold)
                    throw new InterruptedException("timeout: "+timeout);
                
                try
                {
                    protocol.heartbeat();
                }
                catch (Exception e1)
                {
                    throw e1;
                }
                
                protocol.waitForActivity(10);
                
            }
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
	    for (Iterator<PatchListEntry> i = message.getEntries().iterator();
		 i.hasNext(); ) {
		System.out.print("" + i.next() + ", ");
	    }
	    System.out.println();
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
