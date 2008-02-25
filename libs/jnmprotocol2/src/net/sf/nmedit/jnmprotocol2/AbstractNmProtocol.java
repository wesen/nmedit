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

package net.sf.nmedit.jnmprotocol2;

import java.awt.EventQueue;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Transmitter;

import net.sf.nmedit.jnmprotocol2.utils.QueueBuffer;
import net.sf.nmedit.jpdl2.stream.BitStream;

/**
 * Receives and sends javax.sound.midi.MidiMessage.
 * Incoming javax.sound.midi.MidiMessages are
 * transformed to net.sf.nmedit.jnmprotocol2.MidiMessages.
 * 
 * This class is thread safe.
 */
public abstract class AbstractNmProtocol
{

    // empty byte array
    private static final byte[] NO_BYTES = new byte[0];

    // lock for getLock().wait() / getLock().notify() / waitForActivity() calls
    private final Object lock = new Object();

    // lock for the sendQueue
    private final Object sendLock = new Object();
    // lock for the receivedQueue
    private final Object receiveLock = new Object();
    // lock for the event queue
    private final Object eventsLock = new Object();
    // lock for heartbeat() calls
    private ReentrantLock heartbeatLock = new ReentrantLock(false);
    
    // queue containing the outgoing packets
    private QueueBuffer<EnqueuedPacket> sendQueue = new QueueBuffer<EnqueuedPacket>();
    // queue containing the data of incoming javax.sound.midi.MidiMessages
    private QueueBuffer<byte[]> receivedQueue = new QueueBuffer<byte[]>();
    // queue containing incoming net.sf.nmedit.jnmprotocol2.MidiMessages
    private QueueBuffer<MidiMessage> eventQueue = new QueueBuffer<MidiMessage>();
    private QueueBuffer<byte[]> sysexReceiveQueue = new QueueBuffer<byte[]>();
    private byte[] tmpSysexData = null;

    // remembers when activity() was called the last time
    private volatile long recentActivity = 0;

    // transmitter (output)
    private Transmitter transmitter = new ProtocolTransmitter();
    // receiver (input)
    private Receiver receiver = new ProtocolReceiver(this);
    // message handler to process messages from the event queue
    private MessageHandler messageHandler;
    
    public AbstractNmProtocol()
    {
        super();
    }

    /**
     * Clears all message queues.
     */
    public void reset()
    {
		synchronized (sendLock)
        {
            sendQueue.clear();
        }
        synchronized (receiveLock)
        {
            receivedQueue.clear();
        }
        synchronized (eventsLock)
        {
            eventQueue.clear();
        }
    }

    /**
     * Sets the message handler.
     * @param messageHandler the message handler
     */
    public synchronized void setMessageHandler(MessageHandler messageHandler)
    {
        this.messageHandler = messageHandler;
    }

    /**
     * Returns the message handler.
     * @return the message handler
     */
    public synchronized MessageHandler getMessageHandler()
    {
        return messageHandler;
    }

    /**
     * Returns the current timeout value.
     * @return the current timeout value
     */
    protected long getTimeout()
    {
        return 0;
    }    

    /**
     * Processes all pending messages and dispatches incoming events.
     * 
     * If heartbeat() is invoked while another thread holds
     * the lock then heartbeat() returns immediatelly. 
     * 
     * @throws MidiException a midi exception occured
     */
    public final void heartbeat() throws MidiException
    {
        // return when heartbeat() is invoked recursively
        if (heartbeatLock.isHeldByCurrentThread())
            return;

        // the reentrant lock is not necessary but 
        // it avoids that invokations of heartbeat()
        // from multiple thread block each other unecessarily
        
        // see if the lock is held by another thread
        // if so then the heartbeat implementation is not called
        if (heartbeatLock.tryLock())
        {
            // no other thread holds the lock
            try
            {
                // call the heartbeat implementation
                heartbeatImpl();
            }
            finally
            {
                // release the lock - event if an exception occured
                heartbeatLock.unlock();
            }
        }
    }

    /**
     * Processes all pending messages and dispatches incoming events.
     * 
     * @throws MidiException a midi exception occured
     */
    protected abstract void heartbeatImpl() throws MidiException;

    public final void waitForActivity()
    {
        waitForActivity(0);
    }

    /**
     * Waits at most 'timeout' milliseconds until some activity is observed,
     * an InterrupedException is thrown due to another reason or
     * one of the expected reply messages timeout is reached.
     * 
     * Activity is observed when {@link #activity()} was called.
     * 
     * @param timeout in milliseconds
     * @see #activity()
     */
    public final void waitForActivity(long timeout)
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
        try
        {
            synchronized (getLock())
            {
                getLock().wait(timeout);
            }
        }
        catch (InterruptedException e)
        {
            // caused by activity() or due to another reason
        }
    }

    public final Object getLock()
    {
        return lock;
    }

    /**
     * Sets the {@link #getRecentActivity() recent activity time}
     * and notifies all threads waiting on the {@link #getLock() lock}.
     */
    public void activity()
    {
        recentActivity = System.currentTimeMillis();
        synchronized (getLock())
        {
            getLock().notify();
            getLock().notifyAll();
        }
    }

    /**
     * Returns the recent activity time in milliseconds.
     * The value is set each time {@link #activity()} is invoked.
     * @return recent activity
     */
    public long getRecentActivity()
    {
        return recentActivity;
    }
    
    /**
     * Removes a message from the send queue.
     * @throws NoSuchElementException if this send queue is empty.
     */
    protected void removeFromSendQueue()
    {
        synchronized (sendLock)
        {
            sendQueue.remove();
        }
    }
    
    /**
     * Returns true if the send queue is empty.
     * @return true if the send queue is empty
     */
    protected boolean isSendQueueEmpty()
    {
        synchronized (sendLock)
        {
            return sendQueue.isEmpty();   
        }
    }

    /**
     * Returns the next message in the send queue or null if the sendqueue is empty.
     * @return the next message in the send queue or null if the sendqueue is empty.
     */
    protected EnqueuedPacket peekSendQueue()
    {
        synchronized (sendLock)
        {
            return sendQueue.peek();   
        }
    }
    
    /**
     * Clears the send queue.
     */
    protected void clearSendQueue()
    {
        synchronized(sendLock)
        {
            sendQueue.clear();
        }
    }
    
    private int indexOfByte(byte[] data, byte search, int start) {
    	for (int i = start; i < data.length; i++) {
    		if (data[i] == search)
    			return i;
    	}
    	return -1;
    }
    
    private int indexOfByte(byte[] data, byte search) {
    	return indexOfByte(data, search, 0);
    }
    
    protected void dumpByteArray(String s, byte []arr) {
    	System.out.print(s + " [");
    	int i = 0;
    	for (byte b : arr) {
    		if (i++ == 16) {
    			System.out.println();
    			i = 0;
    		}
    		System.out.print(Integer.toHexString(b) + ", ");
    	}
    	System.out.println("]");
    }
    
    /*
     * with mmj under macosx, when a sysex is so long that it has to be split across multiple messages, sysex handling is not correct.
     * We get split sysex across receivedBytes() boundaries, and the following receivedBytes() start with 0xF7
     * Handle this until there is a mmj workaround
     */
    protected byte[] getNextReceivedSysexBytes() {
    	byte[] data = null;
    	if (sysexReceiveQueue.isEmpty()) {
    		byte bytes[];
			synchronized (receiveLock) {
				bytes= getReceivedBytes();
			}
//	        if (bytes!= null) {
//	            System.out.println("getReceived ");
//	            Hexdump.printHex(bytes);
//	            }
//			if (bytes != null && bytes.length > 0)
//				dumpByteArray("receivedBytes ", bytes);
    		if (bytes == NO_BYTES)
    			return NO_BYTES;
    		if (tmpSysexData != null) {
    			byte newSysex[] = new byte[tmpSysexData.length + bytes.length];
    			System.arraycopy(tmpSysexData, 0, newSysex, 0, tmpSysexData.length);
    			System.arraycopy(bytes, 0, newSysex, tmpSysexData.length, bytes.length);
    			tmpSysexData = newSysex;
    		} else {
    			byte newSysex[] = new byte[bytes.length];
    			tmpSysexData = newSysex;
    			System.arraycopy(bytes, 0, tmpSysexData, 0, bytes.length);
    		}
    		if (tmpSysexData == null)
    			return NO_BYTES;

//			if (bytes != null && bytes.length > 0)
//				dumpByteArray("tmpSysexData ", tmpSysexData);

    		int start = 0;
    		do {
    			int startIdx = indexOfByte(tmpSysexData, (byte)0xF0, start);
    			if (startIdx < 0) {
    				// 	discard
    				tmpSysexData = null;
    				break;
    			}
    			int endIdx = indexOfByte(tmpSysexData, (byte)0xF7, startIdx);
    			if (endIdx < 0) {
    				start = startIdx;
    				break;
    			} else {
    				byte result[] = new byte[endIdx + 1 - startIdx];
    				System.arraycopy(tmpSysexData, startIdx, result, 0, endIdx - startIdx + 1);
    				// dumpByteArray("result ", result);	
    				sysexReceiveQueue.offer(result);
    			}
    			start = endIdx + 1;
    		} while ((tmpSysexData != null) && (start < tmpSysexData.length));
    		if (tmpSysexData != null) {
    			if (start < tmpSysexData.length) {
    				byte newSysex[] = new byte[tmpSysexData.length - start];
    				System.arraycopy(tmpSysexData, start, newSysex, 0, tmpSysexData.length - start);
    				tmpSysexData = newSysex;
    			} else if (start == tmpSysexData.length) {
    				tmpSysexData = null;
    			}
    		}
    	}

    	data = sysexReceiveQueue.poll();
    	return data != null ? data : NO_BYTES;
    }
    
    /**
     * Removes and returns the next message in the received queue.
     * If the queue was empty then an empty byte array is returned. 
     * @return the next message int the received queue
     */
    protected byte[] getReceivedBytes()
    {
        byte[] data = null;
        synchronized(receiveLock)
        {
        	byte[] tmpData = null;
        	while ((tmpData = receivedQueue.poll()) != null) {
        		if (data == null) {
        			data = tmpData;
        		} else {
        			byte []tmpData2= new byte[data.length + tmpData.length];
        			System.arraycopy(data, 0, tmpData2, 0, data.length);
        			System.arraycopy(tmpData, 0, tmpData2, data.length, tmpData.length);
        			data = tmpData2;
        		}
        	}
        }

        return (data != null) ? data : NO_BYTES;
    }

    /**
     * Sends a javax.sound.midi.MidiMessage to the transmitter/device.
     * @param message the message
     */
    protected void send(javax.sound.midi.MidiMessage message)
    {
        transmitter.getReceiver().send(message, -1);
    }
    
    public void send(MidiMessage midiMessage) throws MidiException
    {
        BitStream stream = midiMessage.getBitStream();

        if (stream == null)
            throw new MidiException("stream is null in "+midiMessage, MidiException.INVALID_MIDI_DATA);
        
        EnqueuedPacket packet = EnqueuedPacket.create(stream.toByteArray(), midiMessage.expectsReply());
        synchronized (sendLock)
        {
            sendQueue.offer(packet);   
        }
        activity();
    }

    /**
     * Data of a javax.sound.midi.MidiMessage received by the receiver.
     * @param data midi message
     */
    protected void received(byte[] data)
    {
        synchronized (receiveLock)
        {
            receivedQueue.offer(data);
        }
        activity();
    }

    /**
     * Adds an incoming midi message to the event queue.
     * The messages will be passed to the {@link #getMessageHandler() message handler}
     * by {@link #dispatchEvents()}.
     * @param message incoming midi message
     */
    protected void eventQueue_offer(MidiMessage message)
    {
        synchronized (eventsLock)
        {
            eventQueue.offer(message);
        }
    }
    
    /**
     * Returns the transmitter.
     * @return the transmitter
     */
    public Transmitter getTransmitter()
    {
        return transmitter;
    }

    /**
     * Returns the receiver.
     * @return the receiver
     */
    public Receiver getReceiver()
    {
        return receiver;
    }

    /**
     * Removes and returns all events which are currently in the event queue.
     * If the event queue is empty, then null is returned.
     * @return events 
     */
    private QueueBuffer<MidiMessage> releaseEvents()
    {
        synchronized (eventsLock)
        {
            if (!eventQueue.isEmpty())
                return eventQueue.release();   
        }
        return null;
    }
    
    /**
     * Dispatches the events returned by {@link #releaseEvents()}
     * in the current thread.
     */
    public void dispatchEventsImmediatelly()
    {
        QueueBuffer<MidiMessage> events = releaseEvents();
        if (events != null)
            dispatchEvents(events);
    }

    /**
     * Dispatches the events on the AWT event dispatch thread.
     */
    public void dispatchEvents()
    {
        QueueBuffer<MidiMessage> events = releaseEvents();
        if (events == null)
            return;
        if (EventQueue.isDispatchThread())
        {
            // we are in the AWT event dispatch thread
            dispatchEvents(events);
        }
        else
        {
            // post the event to the AWT event dispatch thread
            EventQueue.invokeLater(new DispatchLater(events));
        }
    }
    
    /**
     * Dispatches the events. 
     * @param events the events
     */
    protected void dispatchEvents(QueueBuffer<MidiMessage> events)
    {
        MessageHandler mh = getMessageHandler();
        if (mh == null)
            return;
        
        for (MidiMessage message: events)
        {
            mh.processMessage(message);
        }
    }
    
    private class DispatchLater implements Runnable
    {
        private QueueBuffer<MidiMessage> events;

        public DispatchLater(QueueBuffer<MidiMessage> events)
        {
            this.events = events;
        }

        public void run()
        {
            dispatchEvents(events);
        }
    }

    private static class ProtocolReceiver implements Receiver
    {
        // private boolean closed = false;
        private AbstractNmProtocol receiver;
    
        public ProtocolReceiver(AbstractNmProtocol receiver)
        {
            this.receiver = receiver;
        }
        
        public void send(javax.sound.midi.MidiMessage message, long timeStamp)
        {/*
            if (closed)
                throw new IllegalStateException("receiver closed");*/
        	if (message instanceof SysexMessage) {
//        		System.out.println("sysex message status " + message.getStatus());
//        		Hexdump.printHex(((SysexMessage)message).getMessage());
        		if (message.getStatus() == 0xf7){
        			receiver.received(((SysexMessage)message).getData());
        		} else {
        			receiver.received(message.getMessage());
        		}
        	} else {
        		receiver.received(message.getMessage());
        	}
        }
        
        public void close()
        {
            // closed = true;
        }
    }

    /**
     * The transmitter
     */
    private static class ProtocolTransmitter implements Transmitter
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
}
