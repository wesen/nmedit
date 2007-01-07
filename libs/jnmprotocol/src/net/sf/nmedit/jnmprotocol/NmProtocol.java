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


import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;


/**
 * TODO documentation 
 */
public interface NmProtocol
{

    /**
     * Adds the specified midi message to the send-buffer.
     * The messages will be forwarded to the device by the
     * next call to {@link #heartbeat()}.
     * 
     * @param midiMessage the message
     * @throws Exception TODO documentation
     */
    void send(MidiMessage midiMessage) throws Exception;
    
    /**
     * Sends midi messages in the send-buffer and
     * processes incoming messages by forwarding them to the
     * {@link MessageHandler}. 
     * 
     * @throws Exception TODO documentation
     * @see #getMessageHandler()
     */
    void heartbeat() throws Exception;
    

    /**
     * Receives low level {@link javax.sound.midi.MidiMessage}s.
     * 
     * The receiver has to be connected to the transmitter of the external device.
     * @return the receiver (input) of the protocol
     */
    Receiver getReceiver();
    
    /**
     * Sends low level {@link javax.sound.midi.MidiMessage}s.
     * 
     * The transmitter has to be connected to the receiver of the external device.
     * @return the transmitter (output) of the protocol
     */
    Transmitter getTransmitter();
    
    /**
     * Returns the message handler.
     * @return the message handler
     * @see #setMessageHandler(MessageHandler)
     */
    MessageHandler getMessageHandler();

    /**
     * Sets the message handler which handles incoming messages.
     * @param handler the message handler
     */
    void setMessageHandler(MessageHandler handler);
    
    /**
     * Clears all buffered midi messages.
     */
    void reset();


    /**
     * Causes the current thread to wait until it is signalled by {@link #sendWorkSignal()}
     * or {@link Thread#interrupt interrupted}.
     * 
     * If a reply message is expected the thread will not wait longer then the 
     * current {@link #getTimeout() timeout}. 
     * 
     * The work signal indicates that the buffer contains new messages and 
     * a call to {@link #heartbeat()} might be necessary.
     * 
     * A call to this method is equal to calling <code>awaitWorkSignal(0)</code>.
     * 
     * This method is thread safe.
     * 
     * @throws InterruptedException the operation was interrupted
     */
    void awaitWorkSignal() 
        throws InterruptedException;
    
    /**
     * Causes the current thread to wait until it is signalled by {@link #sendWorkSignal()}
     * or {@link Thread#interrupt interrupted}.
     * 
     * If a reply message is expected the thread will not wait longer then the 
     * current {@link #getTimeout() timeout}.
     * 
     * If timeout is 0 (zero), the current thread will wait until the signal is received
     * or an {@link InterruptedException} is thrown.
     * 
     * The work signal indicates that the buffer contains new messages and 
     * a call to {@link #heartbeat()} might be necessary.
     * 
     * This method is thread safe.
     * 
     * @param timeout timeout in milliseconds 
     * @throws InterruptedException the operation was interrupted
     */
    void awaitWorkSignal(long timeout)
        throws InterruptedException;

    /**
     * Wakes up all threads awaiting the work signal by
     * {@link #awaitWorkSignal()} or {@link #awaitWorkSignal(long)}. 
     * 
     * The work signal indicates that the buffer contains new messages and 
     * a call to {@link #heartbeat()} might be necessary.
     * 
     * The operation is triggered by {@link #send(MidiMessage)}
     * or if the {@link #getReceiver() receiver} receives messages.
     * 
     * This method is thread safe.
     */
    void sendWorkSignal();

    /**
     * Returns the reply timeout of the current midi message or 0 (zero)
     * if no reply is expected.
     *
     * This operation is thread safe.
     *  
     * @return reply timeout
     */
    
    long getTimeout();
    
    /**
     * Returns the time in milliseconds when {@link #sendWorkSignal()}
     * signal was sent, {@link #send(MidiMessage)} was called or the receiver
     * received messages at last.
     *  
     * This operation is thread safe.
     * 
     * @return time when the last activity was detected
     */
    long lastActivity();
    
}
