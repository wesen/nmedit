package net.sf.nmedit.jnmprotocol;


import java.awt.EventQueue;

import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import net.sf.nmedit.jnmprotocol.utils.QueueBuffer;

public class NmProtocolMT implements NmProtocol
{

    private NmProtocol protocol;
    private MessageHandlerMT messageHandlerMT;
    private final Object threadLock = new Object();

    public NmProtocolMT(NmProtocol protocol)
    {
        this.protocol = protocol;
        messageHandlerMT = new MessageHandlerMT(protocol.getMessageHandler());
        protocol.setMessageHandler(messageHandlerMT);
    }

    public long getTimeout()
    {
        return protocol.getTimeout();
    }
    
    public final void awaitWorkSignal() throws InterruptedException
    {
        protocol.awaitWorkSignal();
    }
    
    public final void awaitWorkSignal(long timeout)
        throws InterruptedException
    {
        protocol.awaitWorkSignal(timeout);
    }

    public final void sendWorkSignal()
    {
        synchronized(threadLock)
        {
            protocol.sendWorkSignal();
        }
    }

    public void send(MidiMessage midiMessage) throws Exception
    {
        synchronized (threadLock)
        {
            protocol.send(midiMessage);
        }
    }
    
    public Receiver getReceiver()
    {
        return protocol.getReceiver();
    }

    public Transmitter getTransmitter()
    {
        return protocol.getTransmitter();
    }

    public MessageHandler getMessageHandler()
    {
        return messageHandlerMT.getMessageHandler();
    }

    public void setMessageHandler( MessageHandler handler )
    {
        messageHandlerMT.setMessageHandler(handler);
    }

    public void heartbeat() throws Exception
    {
        synchronized (threadLock)
        {
            protocol.heartbeat();
        }
        messageHandlerMT.processMessages();
    }

    public void reset()
    {
        synchronized (threadLock)
        {
            protocol.reset();
            messageHandlerMT.reset();
        }
    }

    private class MessageHandlerMT implements MessageHandler, Runnable
    {
        
        private MessageHandler messageHandlerST;
        private QueueBuffer<MidiMessage> messageQueue;
        private QueueBuffer<MidiMessage> messageQueueCopy;
        private volatile boolean processingMessages = false;
        
        public MessageHandlerMT( MessageHandler messageHandler )
        {
            this.messageHandlerST = messageHandler;
            this.messageQueue = new QueueBuffer<MidiMessage>();
            this.messageQueueCopy = new QueueBuffer<MidiMessage>(); 
        }

        public void reset()
        {
            synchronized(messageQueue)
            {
                messageQueue.clear();
            }
        }

        public MessageHandler getMessageHandler()
        {
            return messageHandlerST;
        }

        public void setMessageHandler( MessageHandler handler )
        {
            this.messageHandlerST = handler;
        }

        public void processMessage( MidiMessage message )
        {
            synchronized(messageQueue)
            {
                messageQueue.offer(message);
            }
        }

        private boolean hasMessages()
        {
            synchronized (messageQueue)
            {
                return !messageQueue.isEmpty();
            }
        }

        public void processMessages()
        {
            if ((!processingMessages) && hasMessages())
            {
                if (EventQueue.isDispatchThread())
                {
                    run();
                }
                else
                {   
                    EventQueue.invokeLater(this);
                }
            }
        }

        public void run()
        {
            processingMessages = true;
            try
            {
                MidiMessage message;
                for (;;)
                {
                    if ((message = messageQueueCopy.poll())!=null)
                    {
                        if (messageHandlerST != null)
                            messageHandlerST.processMessage(message);
                    }
                    else
                    {
                        synchronized (messageQueue)
                        {
                            if (!messageQueueCopy.offerAll(messageQueue))
                                break;
                        }
                    }
                }
            }
            finally
            {
                processingMessages = false;
            }
        }
    }
    
    public String toString()
    {
        return getClass().getName()+"("+protocol+")";
    }

    public long lastActivity()
    {
        return protocol.lastActivity();
    }
    
}
