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


import java.util.Queue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Transmitter;

import net.sf.nmedit.jnmprotocol.utils.QueueBuffer;
import net.sf.nmedit.jnmprotocol.utils.StringUtils;
import net.sf.nmedit.jpdl.BitStream;

/**
 * The protocol. This implementation is not thread safe.
 */
public class NmProtocolST implements NmProtocol
{

    // constant: empty byte array
    private static final byte[] NO_BYTES = new byte[0];
    // timeout interval in milliseconds
    private static final long TIMEOUT_INTERVAL = 3000; /* ms */
    
    // the message handler that processes incoming messages
    private MessageHandler messageHandler;
    // the send queue
    private Queue<EnqueuedPacket> sendQueue;
    // timeout of the most current midi message
    // note: do not remove 'volatile', the variable has to be thread safe (see #getTimeout())
    private volatile long timeout;
    // absolute time of last activity (see #lastActivity()) 
    private volatile long lastActivity = 0;
    // receiver (input)
    private BufferingReceiver rin;
    // transmitter (output)
    private Transmitter transmitter;

    // the lock used in #awaitWorkSignal(long) and #sendWorkSignal() 
    private final Object waitLock = new Object();
    
    public NmProtocolST()
    {
        timeout = 0;
        this.rin = new BufferingReceiver();
        this.transmitter = new TransmitterST();
        // we use our custom queue which minimizes allocation
        // of new objects (linked list items) and garbage collection
        this.sendQueue = new QueueBuffer<EnqueuedPacket>();
    }

    public void send(MidiMessage midiMessage) throws Exception
    {
        for (Object iterationElement : midiMessage.getBitStream())
        {
            BitStream bitStream = (BitStream)iterationElement;
            sendQueue.add(new EnqueuedPacket(bitStream.toByteArray(),
                             midiMessage.expectsReply()));
        }
        
        sendWorkSignal();
    }

    private static long time()
    {
        return System.currentTimeMillis();
    }

    /**
     * Returns the head of the input message qeue or a empty byte array
     * if the queue is empty.
     */
    private final byte[] receiveBytes()
    {
        byte[] bytes = rin.poll();
        return (bytes != null) ? bytes : NO_BYTES;
    }
    
    /**
     * Sends the specified midi message to the transmitter (output).
     */
    private void sendMessage(javax.sound.midi.MidiMessage message)
    {
        Receiver r = transmitter.getReceiver();
        if (r != null)
        {
            try
            {
                r.send(message, -1);
            }
            catch (IllegalStateException e)
            {
                // Receiver not open
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Sends the specified sysex message to the transmitter (output).
     */
    private void sendBytes(byte[] b)
    {
        SysexMessage sm = new SysexMessage();
        try
        {
            sm.setMessage(b, b.length);
        }
        catch (InvalidMidiDataException e)
        {
            // no op ???
            // TODO 
            return;
        }
        sendMessage(sm);
    }

    public void heartbeat() throws Exception
    {
        // check for timeout
        if (timeout != 0 && time() > timeout) 
        {
            sendQueue.clear();
            long rememberTimeout = timeout; 
            timeout = 0;
            throw new MidiException("Communication timed out.", (int) rememberTimeout);
        }
        
        if ((!sendQueue.isEmpty()) && timeout == 0) 
        {
            EnqueuedPacket packet = sendQueue.peek();
            sendBytes(packet.getContent());
            // Set up timer for expected reply message
            if (packet.expectsReply()) 
            {
                timeout = time() + TIMEOUT_INTERVAL;
            }
            else 
            {
                timeout = 0;
                sendQueue.remove();
            }
        }

        byte[] receiveBytes = receiveBytes();
        while (receiveBytes.length > 0) 
        {
            MidiMessage midiMessage = MidiMessage.create(BitStream.wrap(receiveBytes));
            if (midiMessage != null) 
            {
                // Check if message is a reply
                if (midiMessage.isReply()) 
                {
                    if (!(sendQueue.isEmpty()) && timeout != 0) 
                    {
                        sendQueue.remove();
                        timeout = 0;
                    }
                    else 
                    {
                        throw new MidiException("Unexpected reply message received: "
                                +StringUtils.toHexadecimal(receiveBytes), 0);
                    }
                }
                // process message
                processMessage(midiMessage);
            }
            else if (timeout > 0) 
            {
                // Resend last message,
                // as we always expect an answer we can parse
                sendBytes((sendQueue.peek()).getContent());
                timeout = time() + TIMEOUT_INTERVAL;
            }
            receiveBytes = receiveBytes();
        }   
    }

    public final void awaitWorkSignal() throws InterruptedException
    {
        awaitWorkSignal(0);
    }
    
    public final void awaitWorkSignal(long timeout)
        throws InterruptedException
    {
        // enssure that we do not wait longer than the reply message timeout 
        long msgtimeout = getTimeout();
        
        // reply message timeout is set ...
        if (msgtimeout>0)
        {
            // adjust timeout if necessary
            if (timeout == 0 || timeout > msgtimeout)
                timeout = msgtimeout; 
        }
        
        // wait 
        synchronized (waitLock)
        {
            waitLock.wait(timeout);
        }
    }

    public final void sendWorkSignal()
    {
        // note: this method is called if messages are sent or received 
        // remember time of last activity
        lastActivity = System.currentTimeMillis();
        // wake up all waiting threads
        synchronized(waitLock)
        {
            waitLock.notifyAll();
        }
    }

    public Receiver getReceiver()
    {
        return rin;
    }

    public Transmitter getTransmitter()
    {
        return transmitter;
    }

    private void processMessage(MidiMessage message)
    {
        if (messageHandler!=null)
            messageHandler.processMessage(message);
    }
    
    public MessageHandler getMessageHandler()
    {
        return messageHandler;
    }

    public void setMessageHandler( MessageHandler handler )
    {
        this.messageHandler = handler;
    }

    public void reset()
    {
        rin.clearBuffer();
    }
    
    public long getTimeout()
    {
        return Math.min(timeout, TIMEOUT_INTERVAL);
    }

    public long lastActivity()
    {
        return lastActivity;
    }

    public String toString()
    {
        return getClass().getName();
    }

    /**
     * The transmitter
     */
    private static class TransmitterST implements Transmitter
    {
        private Receiver receiver;
        
        public synchronized void setReceiver( Receiver receiver )
        {
            this.receiver = receiver;
        }

        public synchronized Receiver getReceiver()
        {
            return receiver;
        }

        public void close()
        {
            // no op
        }
    }

    /**
     * The receiver.
     * The receiver enqueues incoming messages and sends 
     * the {@link NmProtocolST#sendWorkSignal()}.
     */
    private class BufferingReceiver implements Receiver
    {
        
        private final QueueBuffer<byte[]> messageQueue;

        // if delay 
        public BufferingReceiver()
        {
            messageQueue = new QueueBuffer<byte[]>();
        }

        public void send( javax.sound.midi.MidiMessage message, long timeStamp )
        {
            synchronized (messageQueue)
            {
                messageQueue.offer(message.getMessage());
            }
            // send the work signal to NmProtocol
            sendWorkSignal();
        }
        
        public byte[] poll()
        {
            synchronized (messageQueue)
            {
                return messageQueue.poll();
            }
        }

        public void clearBuffer()
        {
            synchronized (messageQueue)
            {
                messageQueue.clear();
            }
        }

        public void dropBuffer()
        {
            synchronized (messageQueue)
            {
                messageQueue.clear();
                messageQueue.clearCache();
            }
        }

        public void close()
        {
            // no op
        }
        
        public String toString()
        {
            return getClass().getName();
        }

    }

}

