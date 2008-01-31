package net.sf.nmedit.jpdl2;

import net.sf.nmedit.jpdl2.bitstream.BitStream;

public interface PDLParseContext
{
    
    BitStream getBitStream();
    
    int getLabel(String name);
    
    boolean hasLabel(String name);
    
    PDLPacket getPacket();
    
}
