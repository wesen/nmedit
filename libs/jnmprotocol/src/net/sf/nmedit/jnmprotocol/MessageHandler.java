package net.sf.nmedit.jnmprotocol;



/**
 * Processes incoming messages.  
 */
public interface MessageHandler
{

    void processMessage(MidiMessage message);
    
}
