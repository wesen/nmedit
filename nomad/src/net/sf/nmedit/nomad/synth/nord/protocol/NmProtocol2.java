/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on May 18, 2006
 */
package net.sf.nmedit.nomad.synth.nord.protocol;

import java.util.List;
import java.util.Queue;

import net.sf.nmedit.jnmprotocol.EnqueuedPacket;
import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jpdl.BitStream;

public abstract class NmProtocol2
{

    private MidiDriver midiDriver;
    private Queue<EnqueuedPacket> sendQueue;

    private final static long TIMEOUT_DISABLED = 0;
    private long timeout;
    static private long TIMEOUT_INTERVAL = 3000; /* ms */

    public NmProtocol2( MidiDriver midiDriver )
    {
        timeout = 0;
        this.midiDriver = midiDriver;
        sendQueue = createSendQueue();
    }

    protected abstract void processMessage( MidiMessage midiMessage );
    protected abstract Queue<EnqueuedPacket> createSendQueue();

    public boolean heartbeat() throws Exception
    {
        boolean workDone = false;
        
        if (timeout == TIMEOUT_DISABLED)
        {
            // send next packet   
            EnqueuedPacket head = sendQueue.peek();
            if (head!=null)
            {
                workDone = true;
                midiDriver.send( head.getContent() );
                // Set up timer for expected reply message
                if (head.expectsReply())
                {
                    // enable timeout for packet
                    timeout = System.currentTimeMillis() + TIMEOUT_INTERVAL;
                }
                else
                {
                    sendQueue.remove(); // remove head of queue (packet)
                }
            }
        }
        
        {
    
            BitStream bitStream = new BitStream();
            byte[] receiveBytes = midiDriver.receive();
            MidiMessage midiMessage = null;
            if (receiveBytes.length>0)
            {
                workDone = true;
                do 
                {
                    for (int i = 0; i < receiveBytes.length; i++)
                    {
                        bitStream.append( receiveBytes[i], 8 );
                    }
    
                    try
                    {
                    midiMessage = MidiMessage.create( bitStream );
                    } catch (Exception e)
                    {
                        timeout = TIMEOUT_DISABLED;
                        
                        if (!e.getMessage().startsWith("parse failed:  240"))
                        {
                        sendQueue.clear();
                        throw e;
                        }
                        else
                        {
                            if (!sendQueueIsEmpty())
                                sendQueue.remove();
                            //sendQueue.clear(); 
                            System.err.println("Ignore Message: "+e.getMessage());
                        }
                    }
    
                    if (midiMessage != null)
                    {
                        // Check if message is a reply
                        if (midiMessage.isReply())
                        {
                            if (timeout != 0 && sendQueue.size() > 0)
                            {
                                timeout = TIMEOUT_DISABLED;
                                sendQueue.remove();
                            }
                            else
                            {
                                throw new MidiException("Unexpected reply message received.", 0 );
                            }
                        }
                        processMessage( midiMessage );
                    }
                    else if (timeout > 0)
                    {
                        // Resend last message,
                        // as we always expect an answer we can parse
                        midiDriver.send( sendQueue.peek().getContent() );
                        timeout = System.currentTimeMillis() + TIMEOUT_INTERVAL;
                    }
    
                    bitStream.setSize( 0 );
                    
                    receiveBytes = midiDriver.receive();
                } 
                while (receiveBytes.length > 0);
            }

            if (timeout != 0 && System.currentTimeMillis() > timeout)
            {
                final int rememberTimeout = (int) timeout; 
                timeout = TIMEOUT_DISABLED; 
                sendQueue.clear();
                throw new MidiException( "Communication timed out.", rememberTimeout );
            }
            
        }
        //System.out.println(timeout);
        return workDone || (timeout!=TIMEOUT_DISABLED);
    }
    
    /*
    public boolean heartbeat() throws Exception
    {
        boolean workDone = false;
        
        if (timeout == 0)
        { 
            EnqueuedPacket head = sendQueue.peek();
            if (head!=null)
            {
                workDone = true;
                midiDriver.send( head.getContent() );
                // Set up timer for expected reply message
                if (head.expectsReply())
                {
                    timeout = System.currentTimeMillis() + TIMEOUT_INTERVAL;
                }
                else
                {
                    timeout = 0;
                    sendQueue.remove(); // remove head of queue (packet)
                }
            }
        }

        byte[] receiveBytes;
        BitStream bitStream = new BitStream();
        while ((receiveBytes = midiDriver.receive()).length > 0)
        {
            workDone = true;
            for (int i = 0; i < receiveBytes.length; i++)
            {
                bitStream.append( receiveBytes[i], 8 );
            }
            System.out.println("MidiMessage::create(received)::BEGIN");
            MidiMessage midiMessage;
            try
            {
                midiMessage = MidiMessage.create( bitStream );
            } 
            catch (Exception e)
            {
                timeout = 0;
                throw e;
            }
            System.out.println("/END");
            if (midiMessage != null)
            {
                // Check if message is a reply
                if (midiMessage.isReply())
                {
                    if (timeout != 0 && sendQueue.size() > 0)
                    {
                        timeout = 0;
                        sendQueue.remove();
                    }
                    else
                    {
                        timeout = 0;
                        throw new MidiException("Unexpected reply message received.", 0 );
                    }
                }
                processMessage( midiMessage );
            }
            else if (timeout > 0)
            {
                // Resend last message,
                // as we always expect an answer we can parse
                midiDriver.send( sendQueue.peek().getContent() );
                timeout = System.currentTimeMillis() + TIMEOUT_INTERVAL;
            }

            bitStream.setSize( 0 );
        }

        if (timeout != 0 && System.currentTimeMillis() > timeout)
        {
            final int rememberTimeout = (int) timeout; 
            timeout = 0; 
            sendQueue.clear();
            throw new MidiException( "Communication timed out.", rememberTimeout );
        }
        
        return workDone||(timeout!=TIMEOUT_DISABLED);
    }*/

    @SuppressWarnings("unchecked")
    public void send( MidiMessage midiMessage ) throws Exception
    {
        List<BitStream> list = midiMessage.getBitStream();
        for (BitStream bitStream : list)
        {
            byte[] sendBytes = new byte[bitStream.getSize() / 8];
            for (int n = 0; n < sendBytes.length; n++)
            {
                sendBytes[n] = (byte) bitStream.getInt( 8 );
            }
            sendQueue.offer( new EnqueuedPacket( sendBytes, midiMessage
                    .expectsReply() ) );
        }
    }

    public boolean sendQueueIsEmpty()
    {
        return sendQueue.size() == 0;
    }

}
