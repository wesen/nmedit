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

import javax.sound.midi.*;
import java.util.*;
import net.sf.nmedit.jpdl.*;

public class NmProtocol
{
    public NmProtocol(MidiDriver midiDriver)
    {
	timeout = 0;
	this.midiDriver = midiDriver;
	listeners = new LinkedList();
	sendQueue = new LinkedList();
	
	activePidListener = new ActivePidListener();
	addListener(activePidListener);
    }
  
    public void addListener(NmProtocolListener listener)
    {
	listeners.add(listener);
    }

    public void removeListener(NmProtocolListener listener)
    {
	listeners.remove(listener);
    }

    public void heartbeat()
	throws Exception
    {
	byte[] receiveBytes;
	BitStream bitStream = new BitStream();
	
	if (timeout != 0 && time() > timeout) {
	    sendQueue.clear();
	    MidiException me =
		new MidiException("Communication timed out.", (int)timeout);
	    timeout = 0;
	    throw me;
	}
	
	if (sendQueue.size() > 0 && timeout == 0) {
	    midiDriver.send(((EnqueuedPacket)sendQueue.get(0)).getContent());
	    
	    // Set up timer for expected reply message
	    if (((EnqueuedPacket)sendQueue.get(0)).expectsReply()) {
		timeout = time() + TIMEOUT_INTERVAL;
	    }
	    else {
		timeout = 0;
		sendQueue.remove(0);
	    }
	}
	
	receiveBytes = midiDriver.receive();
	
	while (receiveBytes.length > 0) {
	    for (int i = 0; i < receiveBytes.length; i++) {
		bitStream.append(receiveBytes[i], 8);
	    }
	    MidiMessage midiMessage = MidiMessage.create(bitStream);
	    if (midiMessage != null) {
		// Check if message is a reply
		if (midiMessage.isReply()) {
		    if (sendQueue.size() > 0 && timeout != 0) {
			sendQueue.remove(0);
			timeout = 0;
		    }
		    else {
			throw new 
			    MidiException("Unexpected reply message received.",
					  0);
		    }
		}
		notifyListeners(midiMessage);
	    }
	    else if (timeout > 0) {
		// Resend last message,
		// as we always expect an answer we can parse
		midiDriver.send(((EnqueuedPacket)sendQueue.get(0)).getContent());
		timeout = time() + TIMEOUT_INTERVAL;
	    }
	    
	    bitStream.setSize(0);
	    receiveBytes = midiDriver.receive();
	}	
    }
    
    public void send(MidiMessage midiMessage)
	throws Exception
    {
	List bitStreamList;
	
	bitStreamList = midiMessage.getBitStream();
	for(Iterator i = bitStreamList.iterator(); i.hasNext(); ) {
	    BitStream bitStream = (BitStream)i.next();
	    byte[] sendBytes = new byte[bitStream.getSize() / 8];
	    for (int n = 0; n < sendBytes.length; n++) {
		sendBytes[n] = (byte)bitStream.getInt(8);
	    }
	    sendQueue.add(new EnqueuedPacket(sendBytes,
					     midiMessage.expectsReply()));
	}	
    }

    public boolean sendQueueIsEmpty()
    {
	return sendQueue.size() == 0;
    }

    public int getActivePid(int slot)
    {
	return activePidListener.getActivePid(slot);
    }

    private void notifyListeners(MidiMessage midiMessage)
	throws Exception
    {
	for (Iterator i = listeners.iterator(); i.hasNext(); ) {
	    midiMessage.notifyListener((NmProtocolListener)i.next());
	}
    }

    private long time()
    {
	return (new Date()).getTime();
    }

    private MidiDriver midiDriver;
    private LinkedList listeners;
    private LinkedList sendQueue;

    private long timeout;
    static private long TIMEOUT_INTERVAL = 3000; /* ms */
    
    private ActivePidListener activePidListener;
}
