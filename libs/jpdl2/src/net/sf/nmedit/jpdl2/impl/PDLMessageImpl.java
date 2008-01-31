package net.sf.nmedit.jpdl2.impl;

import net.sf.nmedit.jpdl2.PDLMessage;
import net.sf.nmedit.jpdl2.PDLPacket;

public class PDLMessageImpl implements PDLMessage
{
    
    private PDLPacket packet;
    private String messageId;

    public PDLMessageImpl(PDLPacket packet, String messageId)
    {
        this.packet = packet;
        this.messageId = messageId;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public PDLPacket getPacket()
    {
        return packet;
    }

}
