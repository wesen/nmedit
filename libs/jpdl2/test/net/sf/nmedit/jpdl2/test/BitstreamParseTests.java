package net.sf.nmedit.jpdl2.test;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import net.sf.nmedit.jpdl2.bitstream.BitStream;
import net.sf.nmedit.jpdl2.PDLBitstreamParser;
import net.sf.nmedit.jpdl2.PDLDocument;
import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLMessage;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.format.PDL2Parser;

public class BitstreamParseTests
{
    
    public PDLPacket test(BitStream stream, String pdlsrc) throws PDLException
    {
        PDL2Parser parser = new PDL2Parser(new StringReader("start start;"+pdlsrc));
        parser.parse();
        PDLDocument doc = parser.getDocument();
        
        PDLBitstreamParser bsparse = new PDLBitstreamParser();
        return bsparse.parse(stream, doc);
    }

    public PDLMessage mtest(BitStream stream, String pdlsrc) throws PDLException
    {
        PDL2Parser parser = new PDL2Parser(new StringReader("start start;"+pdlsrc));
        parser.parse();
        PDLDocument doc = parser.getDocument();
        
        PDLBitstreamParser bsparse = new PDLBitstreamParser();
        return bsparse.parseMessage(stream, doc);
    }
    
    private BitStream stream(int ... data)
    {
        BitStream bs = new BitStream();
        for (int i=0;i<data.length;i+=2)
            bs.append(data[i],data[i+1]);
        bs.setPosition(0);
        return bs;
    }

    @Test
    public void parseNothing() throws PDLException
    {
        test(stream(), "start := ;");
    }

    @Test(expected=PDLException.class) 
    public void constBitsNotAvailable() throws PDLException
    {
        test(stream(), "start := 0:1;");
    }

    @Test(expected=PDLException.class) 
    public void varBitsNotAvailable() throws PDLException
    {
        test(stream(), "start := v:1;");
    }

    @Test
    public void parseConst0x00() throws PDLException
    {
        test(stream(0x00,8), "start := 0:8;");
    }

    @Test
    public void parseConst0xFF() throws PDLException
    {
        test(stream(0xFf,8), "start := 0xFf:8;");
    }

    @Test
    public void parseVariable() throws PDLException
    {
        for (int i=0;i<=0xFf;i+=0xFf)
        {
            PDLPacket packet = test(stream(i,8), "start := v:8;");
            if (packet.getVariable("v")!=i)
                throw new PDLException("variable has wrong value: "+packet.getVariable("v")+" expected:"+i);
        }
    }

    @Test
    public void parse2Variables() throws PDLException
    {
        int[] v = {0xFf, 0x01}; 
        PDLPacket packet = test(stream(v[0],8, v[1], 8), "start := v1:8 v2:8;");
        for (int i=1;i<=2;i++)
            if (packet.getVariable("v"+i)!=v[i-1])
                throw new PDLException("variable has wrong value: "+packet.getVariable("v"+i)+" expected:"+v[i-1]);
    }

    @Test
    public void parseMessageId() throws PDLException
    {
        for (int i=-1;i<=4;i++)
        {
            PDLMessage msg = mtest(stream(i,8), 
                "start := v:8 \n" +
                " messageId(\"-1\") \n" +
                " #v = 0 => messageId(\"0\") \n" +
                " #v = 1 => messageId(\"1\") \n" +
                " #v = 2 => messageId(\"2\") \n" +
                " #v = 3 => messageId(\"3\") \n" +
                " #v = 4 => messageId(\"4\") \n" +
                ";" 
            );
            
            int msgId = Integer.parseInt(msg.getMessageId());
            
            Assert.assertEquals(i, msgId);
        }
    }

    @Test
    public void unsetMessageId() throws PDLException
    {
        for (int i=0;i<=4;i++)
        {
            // at first messageId("abc") is defined, but later it is undefined because the optional
            // part is not included in the message
            PDLMessage msg = mtest(stream(i,8),
                "start := v:8 ? { messageId(\"abc\") c:4} \n" +
                " #v = 0 => messageId(\"0\") \n" +
                " #v = 1 => messageId(\"1\") \n" +
                " #v = 2 => messageId(\"2\") \n" +
                " #v = 3 => messageId(\"3\") \n" +
                " #v = 4 => messageId(\"4\") \n" +
                ";" 
            );

            int msgId = Integer.parseInt(msg.getMessageId());
            
            Assert.assertEquals(i, msgId);
        }
    }
    
}
