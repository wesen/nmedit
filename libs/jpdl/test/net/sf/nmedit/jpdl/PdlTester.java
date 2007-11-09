package net.sf.nmedit.jpdl;

import junit.framework.*;

public class PdlTester extends TestCase
{
    protected void setUp()
    {
	System.out.println("");
    }
    
    private String testpdl = null;
    
    public String getTestPdlFileName()
    {
        if (testpdl == null)
        {
            // get location of test.pdl
            testpdl = getClass().getClassLoader().getResource("test.pdl").getFile();
        }
        return testpdl;
    }
    
    public void testDecode()
	throws Exception
    {
	try {
	    Protocol p = new Protocol(getTestPdlFileName());
	    p.useTracer(new TestTracer());
	    
	    BitStream stream = new BitStream();
	    Packet packet = new Packet();
	    
	    stream.append(0xf0, 8);
	    stream.append(0x33, 8);
	    stream.append(   0, 1);
	    stream.append(0x16, 5);
	    stream.append(   0, 2);
	    stream.append(0x06, 8);
	    stream.append(   0, 1);
	    stream.append(  15, 7);
	    stream.append(   0, 1);
	    stream.append(0x7f, 7);
	    stream.append(   0, 1);
	    stream.append(  16, 7);
	    stream.append(   0, 1);
	    stream.append( 127, 7);
	    stream.append(0xf7, 8);
	    
	    p.getPacketParser("Sysex").parse(stream, packet);
	    
	    Assert.assertEquals(0x16, packet.getVariable("cc"));
	    Assert.assertEquals(0, packet.getVariable("slot"));
	    Assert.assertEquals("ACK", packet.getPacket("data").getName());
	    Assert.assertEquals(15, packet.getPacket("data")
				.getVariable("pid1"));
	    Assert.assertEquals(16, packet.getPacket("data")
				.getVariable("pid2"));
	    Assert.assertEquals(127, packet.getPacket("data")
				.getVariable("checksum"));
	}
	catch(Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    public void testEncode()
	throws Exception
    {
	Protocol p = new Protocol(getTestPdlFileName());
	p.useTracer(new TestTracer());
	
	BitStream bstream = new BitStream();
	IntStream istream = new IntStream();
	
	istream.append(0x16);
	istream.append(3);
	istream.append(4);
	istream.append(5);
	istream.append(6);

	p.getPacketParser("Sysex").generate(istream, bstream);
	
	Assert.assertEquals(0xf0, bstream.getInt(8));
	Assert.assertEquals(0x33, bstream.getInt(8));
	Assert.assertEquals(0, bstream.getInt(1));
	Assert.assertEquals(0x16, bstream.getInt(5));
	Assert.assertEquals(3, bstream.getInt(2));
	Assert.assertEquals(0x06, bstream.getInt(8));
	Assert.assertEquals(4, bstream.getInt(8));
	Assert.assertEquals(0x7f, bstream.getInt(8));
	Assert.assertEquals(5, bstream.getInt(8));
	Assert.assertEquals(6, bstream.getInt(8));
	Assert.assertEquals(0xf7, bstream.getInt(8));
    }

    public void testGreedy()
	throws Exception
    {
	try {
	    Protocol p = new Protocol(getTestPdlFileName());
	    p.useTracer(new TestTracer());
	    
	    BitStream stream = new BitStream();
	    Packet packet = new Packet();
	    
	    stream.append(1, 8);
	    stream.append(2, 8);
	    stream.append(3, 8);
	    stream.append(4, 8);
	    stream.append(5, 8);
	    stream.append(6, 8);
	    
	    p.getPacketParser("Variable").parse(stream, packet);
	}
	catch(Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    class TestTracer implements Tracer
    {
	public void trace(String msg)
	{
	    System.out.println("TRACE: " + msg);
	}
    }
}
