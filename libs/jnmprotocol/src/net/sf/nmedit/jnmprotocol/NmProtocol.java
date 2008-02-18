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

import net.sf.nmedit.jnmprotocol.AbstractNmProtocol;
import net.sf.nmedit.jnmprotocol.EnqueuedPacket;
import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jpdl.BitStream;

public class NmProtocol extends AbstractNmProtocol
{

    // timeout interval in milliseconds
    private static final long TIMEOUT_INTERVAL = 3000; /* ms */
    
    // timeout of the most current midi message
    // note: do not remove 'volatile', the variable has to be thread safe (see #getTimeout())
    private volatile long timeout;

    public NmProtocol()
    {
        timeout = 0;
    }

    private static long time()
    {
        return System.currentTimeMillis();
    }

    protected void heartbeatImpl() throws MidiException 
    {
        // check for timeout
        if (timeout != 0 && time() > timeout) 
        {
            clearSendQueue();
            long rememberTimeout = timeout; 
            timeout = 0;
            throw new MidiException("Communication timed out.", (int) rememberTimeout);
        }
        
        if (timeout == 0) 
        {
            EnqueuedPacket packet = peekSendQueue();
            if (packet != null)
            {
                send(packet);
                // Set up timer for expected reply message
                if (packet.expectsReply()) 
                {
                    timeout = time() + TIMEOUT_INTERVAL;
                }
                else
                {
                    timeout = 0;
                    removeFromSendQueue();
                }
            }
        }

        byte[] receiveBytes = getReceivedBytes();
        while (receiveBytes.length > 0) 
        {
            MidiMessage midiMessage = MidiMessage.create(BitStream.wrap(receiveBytes));
            if (midiMessage != null) 
            {
                // Check if message is a reply
                if (midiMessage.isReply()) 
                {
                    if (!isSendQueueEmpty() && timeout != 0) 
                    {
                        removeFromSendQueue();
                        timeout = 0;
                    }
                }
                // enqeues the midi message in the event queue
                eventQueue_offer(midiMessage);
            }
            else if (timeout > 0) 
            {
                // Resend last message,
                // as we always expect an answer we can parse
                
                send(peekSendQueue());
                timeout = time() + TIMEOUT_INTERVAL;
            }
            receiveBytes = getReceivedBytes();
        }
        
        // dispatches all events in the event queue
        dispatchEvents();
    }
    
    protected long getTimeout()
    {
        long t = timeout;
        if (t>TIMEOUT_INTERVAL)
            t = TIMEOUT_INTERVAL;
        return t;
    }

}

