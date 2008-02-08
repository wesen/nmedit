package net.sf.nmedit.jnmprotocol.utils;

import net.sf.nmedit.jnmprotocol.PDLData;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jpdl.PacketParser;

public class PatchNameExtractor
{

    public final static int S_NAME_1 = 55;
    public final static int S_NAME_2 = 39;
    private static PatchNameExtractor instance = new PatchNameExtractor();
    
    /**
     * Returns the patch name stored in the specified message.
     * If the patch name is not part of the message, null is returned.
     * 
     * The message can be requested from the synth using
     * GetPatchMessage(slot, pid, GetPatchMessage.PatchPart.Header);
     * 
     * @param message patch message
     * @return the patch name
     */
    public static String extractPatchName(PatchMessage message)
    {
        return instance.extract(message);
    }

    public String extract(PatchMessage message)
    {
        return extract(message.getPatchStream());
    }
    
    private String extract(BitStream stream)
    {
        PacketParser parser = PDLData.getPatchParser();
        Packet packet = new Packet();
    
        if (parser.parse(stream, packet)) 
        {
            do 
            {
                Packet section = packet.getPacket("section");
                Packet sectionData = section.getPacket("data");
                String s = extractFromSection(section.getVariable("type"), sectionData);
                
                if (s != null)
                    return s;
                
                packet = packet.getPacket("next");
            } 
            while (packet != null);
        }
        
        return null;
    }

    private String extractFromSection(int section, Packet sectionData)
    { 
        switch (section)
        {
            // Name section
            case S_NAME_1:
            case S_NAME_2:
                // patch name
                return NmCharacter.extractName(sectionData.getPacket("name"));
            default:
                return null;
        }
    }
    
}
