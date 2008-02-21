/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package net.sf.nmedit.jnmprotocol2.utils;

import net.sf.nmedit.jnmprotocol2.PDLData;
import net.sf.nmedit.jnmprotocol2.PatchMessage;
import net.sf.nmedit.jnmprotocol2.utils.NmCharacter;
import net.sf.nmedit.jnmprotocol2.utils.PatchNameExtractor;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.dom.PDLDocument;
import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLMessage;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.PDLPacketParser;

public class PatchNameExtractor
{

    public final static int S_NAME_1 = 55;
    public final static int S_NAME_2 = 39;
    private static PatchNameExtractor instance = new PatchNameExtractor();
    private PDLPacketParser parser;
    
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
        return extract(message.getBitStream());
    }
    
    private String extract(BitStream stream)
    {
        if (parser == null)
        {
            PDLDocument doc = PDLData.getPatchDoc();
            parser = new PDLPacketParser(doc);
        }
        try
        {
            PDLMessage message = parser.parseMessage(stream);
            PDLPacket packet = message.getPacket();
            do 
            {
                PDLPacket section = packet.getPacket("section");
                PDLPacket sectionData = section.getPacket("data");
                String s = extractFromSection(section.getVariable("type"), sectionData);
                
                if (s != null)
                    return s;
                
                packet = packet.getPacket("next");
            } 
            while (packet != null);
        }
        catch (PDLException e)
        {
            // ignore
        }
        return null;
    }

    private String extractFromSection(int section, PDLPacket sectionData)
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
