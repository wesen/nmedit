/*
 * Created on Jan 10, 2007
 */
package net.sf.nmedit.jnmprotocol.helper;

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.GetPatchMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jpdl.PacketParser;
import net.sf.nmedit.jpdl.Protocol;
import net.sf.nmedit.jpdl.Tracer;

public class GetPatchMessageReplyAcceptor extends NmProtocolListener
{

    private static PacketParser patchParser = null;
    private static Protocol patchProtocol = null;
    private static String patchPdlFile = null;

    // PatchMessage section IDs
    public final static int S_NAME_1 = 55;
    public final static int S_NAME_2 = 39;
    public final static int S_HEADER = 33;
    public final static int S_MODULE = 74;
    public final static int S_NOTE = 105;
    public final static int S_CABLE = 82;
    public final static int S_MORPHMAP = 101;
    public final static int S_KNOBMAP = 98;
    public final static int S_CTRLMAP = 96;
    public final static int S_CUSTOM = 91;
    public final static int S_NAMEDUMP = 90;
    public final static int S_PARAMETER = 77;
    
    
    public static PacketParser getPatchParser()
    {
        if (patchParser==null)
            init();
        
        return patchParser;
    }
    
    private static boolean initialized = false;
    
    private static void init() {
        
        if (initialized)
            return;
        
        try {
            usePDLFile("/patch.pdl", null);
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void usePDLFile(String filename, Tracer tracer) throws Exception
    {
        patchPdlFile = filename;
        patchProtocol = new Protocol(patchPdlFile);
        patchParser = patchProtocol.getPacketParser("Patch");
        patchProtocol.useTracer(tracer);
    }

    private NmProtocol protocol;
    
    boolean replyIAM = false;
    boolean replyACK = false;
    int replyPatch = 0;
    
    public GetPatchMessageReplyAcceptor( NmProtocol protocol )
    {
        this.protocol = protocol;
    }
    
    public boolean accepted()
    {
        return replyPatch >= 14;
    }

    public void sendInitialMessage() throws Exception
    {
        protocol.send(new IAmMessage());
    }

    public void messageReceived(IAmMessage message) 
    {
        if (!replyIAM)
        {
            replyIAM = true;
            try
            {
                RequestPatchMessage msg = new RequestPatchMessage();
                msg.set("slot", 0);  
                protocol.send(msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void messageReceived(AckMessage message) 
    {
        if (!replyACK)
        {
            replyACK = true;
            try
            {
                GetPatchMessage msg = new GetPatchMessage();
                msg.set("slot", message.get("slot"));
                msg.set("pid", message.get("pid1"));
                protocol.send(msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }
    
    public void messageReceived(PatchMessage message) 
    {
        if (replyPatch<14)
        {
            countSections(message);
        }
    }
    
    private void countSections( PatchMessage message )
    {
        Packet packet = new Packet();
        PacketParser parser = getPatchParser();
    
        BitStream stream = message.getPatchStream();
        
        if (parser.parse(stream, packet)) 
        {
            do 
            {
                Packet section = packet.getPacket("section");
                // Packet sectionData = section.getPacket("data");
                packet = packet.getPacket("next");
                

                replyPatch ++;
                int sectionId = section.getVariable("type");
                String sectionName = "unknown section";
                switch (sectionId)
                {
                    case S_NAME_1:
                    case S_NAME_2: // patch name
                        sectionName="patch name";
                        break;                    
                    case S_HEADER: // Header section
                        sectionName="header";
                        break;
                    case S_MODULE: // Module section
                        sectionName="module";
                        break;
                    case S_NOTE:  // Note section
                        sectionName="note";
                        break;
                    case S_CABLE: // Cable section
                        sectionName="cable";
                        break;
                    case S_PARAMETER: // Parameter section
                        sectionName="parameter";
                        break;
                    case S_MORPHMAP: // Morphmap section
                        sectionName="morphmap";
                        break;
                    case S_KNOBMAP: // Knobmap section
                        sectionName="knobmap";
                        break;
                    case S_CTRLMAP: // Controlmap section
                        sectionName="controlmap";
                        break;
                    case S_CUSTOM: // Custom section
                        sectionName="custom";
                        break;
                    case S_NAMEDUMP: // Namedump section
                        sectionName="namedump";
                        break;
                }
                System.out.println(sectionName+":"+sectionId);
            } 
            while (packet != null);
        } 
    }

    public int getPatchMessageReplyCount()
    {
        return replyPatch;
    }
    
}