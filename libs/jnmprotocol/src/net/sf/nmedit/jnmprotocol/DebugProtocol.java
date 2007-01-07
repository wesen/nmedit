package net.sf.nmedit.jnmprotocol;


import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import net.sf.nmedit.jnmprotocol.utils.DebugReceiver;

public class DebugProtocol implements NmProtocol
{

    private NmProtocol protocol;
    private Receiver debugReceiver;
    private Transmitter debugTransmitter;
    private DebugMessageHandler debugMessageHandler;

    public DebugProtocol(NmProtocol protocol)
    {
        this.protocol = protocol;
        this.debugReceiver = new DebugReceiver("incoming::", protocol.getReceiver());
        this.debugTransmitter = new DebugTransmitter(protocol.getTransmitter());
        this.debugMessageHandler = new DebugMessageHandler(protocol.getMessageHandler());
        protocol.setMessageHandler(debugMessageHandler);
    }

    public Receiver getReceiver()
    {
        return debugReceiver;
    }

    public Transmitter getTransmitter()
    {
        return debugTransmitter;
    }

    public MessageHandler getMessageHandler()
    {
        return debugMessageHandler.getMessageHandler();
    }

    public void send( MidiMessage midiMessage ) throws Exception
    {
        if (ProtocolDebug.TraceSendProtocolMessage)
            ProtocolDebug.trace(protocol, "sending "+midiMessage);
        
        try
        {
            protocol.send( midiMessage );
        }
        catch (Exception e)
        {
            ProtocolDebug.traceException(protocol, "send("+midiMessage+")", e);
            throw e;
        }
    }

    public void setMessageHandler( MessageHandler handler )
    {
        debugMessageHandler.setMessageHandler( handler );
    }

    private int heartbeatTrace = 0;
    private long heartbeatTraceTime = System.currentTimeMillis();
    private boolean firstCall = true;
    
    public void heartbeat() throws Exception
    {
        if (ProtocolDebug.TraceHeartbeats)
        {
            synchronized (this)
            {
                if (firstCall)
                {
                    ProtocolDebug.trace(protocol, "heartbeat() (first call)");   
                    heartbeatTraceTime = System.currentTimeMillis();
                    firstCall = false;
                }
                heartbeatTrace ++;
                long time = System.currentTimeMillis()-heartbeatTraceTime;
                if (time >= 1000)
                {
                    heartbeatTraceTime += 1000;
                    ProtocolDebug.trace(protocol, "heartbeat() "+heartbeatTrace+" calls/"+Math.round(time/10d)/100d+"s");
                    heartbeatTrace = 0;
                }
            }
        }
        
        try
        {
            protocol.heartbeat();
        }
        catch (Exception e)
        {
            ProtocolDebug.traceException(protocol, "heartbeat()", e);
            throw e;
        }
    }

    public void reset()
    {
        protocol.reset();
    }

    public void awaitWorkSignal() throws InterruptedException
    {
        awaitWorkSignal(0);
    }

    private long lastWorkSignalTime = 0;
    private int awaitWorkSignalCount = 0;
    
    public void awaitWorkSignal( long timeout ) throws InterruptedException
    {
        if (ProtocolDebug.TraceAwaitWorkSignal)
        {
            synchronized (this)
            {
                awaitWorkSignalCount ++;
                if (timeout == 0 || System.currentTimeMillis()-lastWorkSignalTime >= 1000)
                {
                    String t = timeout == 0 ? "forever" : Long.toString(timeout);
                    ProtocolDebug.trace(protocol, "awaiting work signal (timeout="+t+") "+awaitWorkSignalCount+" more");
                }
                lastWorkSignalTime = System.currentTimeMillis();
            }
        }
        protocol.awaitWorkSignal(timeout);
    }

    public void sendWorkSignal()
    {
        protocol.sendWorkSignal();
    }

    public long lastActivity()
    {
        return protocol.lastActivity();
    }
    
    public String toString()
    {
        return getClass().getName()+"("+protocol+")";
    }

    private static class DebugTransmitter implements Transmitter
    {

        private Receiver receiver;
        private Transmitter transmitter;

        public DebugTransmitter( Transmitter t )
        {
            this.transmitter = t;
        }

        public void setReceiver( Receiver receiver )
        {
            this.receiver = receiver;
            Receiver wrapper = receiver != null ? wrap(receiver) : null;
            transmitter.setReceiver(wrapper);
        }

        protected Receiver wrap( Receiver r )
        {
            return new DebugReceiver("sending::", r);
        }

        public Transmitter getWrappedTransmitter()
        {
            return transmitter;
        }
        
        public Receiver getWrappedReceiver()
        {
            return receiver;
        }

        public Receiver getReceiver()
        {
            return transmitter.getReceiver();
        }

        public void close()
        {
            transmitter.close();
        }

        public String toString()
        {
            return transmitter.toString();
        }
        
    }

    public long getTimeout()
    {
        return protocol.getTimeout();
    }
    
    private class DebugMessageHandler implements MessageHandler
    {
        
        private MessageHandler messageHandler;
        
        public DebugMessageHandler( MessageHandler messageHandler )
        {
            this.messageHandler = messageHandler;
        }

        public void setMessageHandler( MessageHandler handler )
        {
            this.messageHandler = handler;
        }

        public MessageHandler getMessageHandler()
        {
            return messageHandler;
        }

        public void processMessage( MidiMessage message )
        {
            if (ProtocolDebug.TraceReceiveProtocolMessage)
                ProtocolDebug.trace(protocol, "received "+message);
            
            messageHandler.processMessage(message);
        }
        
    }

}
