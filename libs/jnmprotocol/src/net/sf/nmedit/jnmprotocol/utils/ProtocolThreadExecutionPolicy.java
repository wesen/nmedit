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

package net.sf.nmedit.jnmprotocol.utils;

import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jnmprotocol.utils.ThreadExecutionPolicy;

/**
 * A policy to control the calls to {@link net.sf.nmedit.jnmprotocol.NmProtocol#heartbeat()}
 * from a separate thread.
 */
public class ProtocolThreadExecutionPolicy implements ThreadExecutionPolicy
{

    // the protocol
    private NmProtocol protocol;
    // status variable, indicating the thread should exit
    private boolean exitRequest = false;
    // delay time in milliseconds
    private long delay;
    // threshold in milliseconds
    private long hibernationThreshold;


    public ProtocolThreadExecutionPolicy(NmProtocol protocol)
    {
        this(protocol, 10, 3000);
    }
    
    /**
     * Creates a new thread execution policy for the specified protocol.
     * 
     * @param protocol
     * @param delay specified the delay between two calls to {@link NmProtocol#heartbeat()} in milliseconds
     * @param hibernationThreshold specifies a threshold in milliseconds. If no activity was determined
     * for this amount of time (and the threshold value is &gt; 0), 
     * a call to delay() will block until the awaitWorkSignal() is received.
     */
    public ProtocolThreadExecutionPolicy(NmProtocol protocol, long delay, long hibernationThreshold)
    {
        this.protocol = protocol;
        this.delay = delay;
        this.hibernationThreshold = hibernationThreshold;
    }

    public void delay()
    {
        // Check if the hibernation threshold is reached or hibernation mode is disabled 
        if (System.currentTimeMillis() - protocol.getRecentActivity() <= hibernationThreshold || hibernationThreshold == 0)
        {
            // awaitWorkSignal(0) would not return until the work signal is received
            // instead share some time using Thread.yield()
            if (delay == 0)
                Thread.yield();
            else
                protocol.waitForActivity(delay);
        }
        else
        {
            // block until signal is received
            protocol.waitForActivity();
        }
    }

    public synchronized boolean exitRequested()
    {
        return exitRequest;
    }

    public void setExitRequested()
    {
        synchronized (this)
        {
            this.exitRequest = true;
        }
        // interrupt delay otherwise it might block forever
        // and the thread has no chance to exit
        interruptDelay();
    }
    
    public boolean executionRequested()
    {
        // hearbeat() can be called always
        return true;
    }

    public void interruptDelay()
    {
        // interrups delay() which waits on the work signal
        protocol.activity();
    }

}
