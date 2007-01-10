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

public class GetPatchMessageReplyAcceptor extends NmProtocolListener
{

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
        return replyPatch >= 13;
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
        if (replyPatch<13)
        {
            replyPatch++;
        }
    }
    
    public int getPatchMessageReplyCount()
    {
        return replyPatch;
    }
    
}