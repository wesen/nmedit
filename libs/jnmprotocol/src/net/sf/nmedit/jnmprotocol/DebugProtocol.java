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

package net.sf.nmedit.jnmprotocol;


import java.io.PrintStream;

import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import net.sf.nmedit.jnmprotocol.utils.DebugReceiver;

public class DebugProtocol implements NmProtocol
{

    private NmProtocol protocol;
    private Receiver debugReceiver;
    private Transmitter debugTransmitter;
    private DebugMessageHandler debugMessageHandler;
    private PrintStream debugOut;

    public DebugProtocol(NmProtocol protocol)
    {
        this(System.out, protocol);
    }
    
    public DebugProtocol(PrintStream debugOut, NmProtocol protocol)
    {
        this.debugOut = debugOut;
        this.protocol = protocol;
        this.debugReceiver = new DebugReceiver(debugOut, "in@", protocol.getReceiver());
        this.debugTransmitter = new DebugTransmitter(debugOut, protocol.getTransmitter());
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
            ProtocolDebug.trace(debugOut, protocol, "sending "+midiMessage);
        
        try
        {
            protocol.send( midiMessage );
        }
        catch (Exception e)
        {
            ProtocolDebug.traceException(debugOut, protocol, "send("+midiMessage+")", e);
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
                    ProtocolDebug.trace(debugOut, protocol, "heartbeat() (first call)");   
                    heartbeatTraceTime = System.currentTimeMillis();
                    firstCall = false;
                }
                heartbeatTrace ++;
                long time = System.currentTimeMillis()-heartbeatTraceTime;
                if (time >= 1000)
                {
                    heartbeatTraceTime = System.currentTimeMillis();
                    ProtocolDebug.trace(debugOut, protocol, "heartbeat() "+heartbeatTrace+" calls/"+Math.round(time/10d)/100d+"s");
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
            ProtocolDebug.traceException(debugOut, protocol, "heartbeat()", e);
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
                    ProtocolDebug.trace(debugOut, protocol, "awaiting work signal (timeout="+t+") "+awaitWorkSignalCount+" more");
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
        private PrintStream debugOut;

        public DebugTransmitter( PrintStream debugOut, Transmitter t )
        {
            this.debugOut = debugOut;
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
            return new DebugReceiver(debugOut, "out@", r);
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
                ProtocolDebug.trace(debugOut, protocol, "received "+message);
            
            if (messageHandler != null)
                messageHandler.processMessage(message);
        }
        
    }

}
