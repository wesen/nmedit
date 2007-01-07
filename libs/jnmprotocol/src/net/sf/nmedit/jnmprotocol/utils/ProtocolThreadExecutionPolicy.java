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
 * Created on Jan 6, 2007
 */
package net.sf.nmedit.jnmprotocol.utils;

import net.sf.nmedit.jnmprotocol.NmProtocol;

public class ProtocolThreadExecutionPolicy implements ThreadExecutionPolicy
{

    private NmProtocol protocol;
    private boolean exitRequest = false;
    private final Object waitLock = new Object();
    private long delay;
    private long hibernationThreshold;


    public ProtocolThreadExecutionPolicy(NmProtocol protocol)
    {
        this(protocol, 10, 3000);
    }
    
    public ProtocolThreadExecutionPolicy(NmProtocol protocol, long delay, long hibernationThreshold)
    {
        this.protocol = protocol;
        this.delay = delay;
        this.hibernationThreshold = hibernationThreshold;
    }

    public void delay() throws InterruptedException
    {
        if (System.currentTimeMillis() - protocol.lastActivity() <= hibernationThreshold)
        {
            if (delay == 0)
                Thread.yield();
            else
                protocol.awaitWorkSignal(delay);
        }
        else
        {
            protocol.awaitWorkSignal();
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
        synchronized (waitLock)
        {
            waitLock.notifyAll();
        }
    }
    
    public boolean executionRequested()
    {
        return true;
    }

    public void interruptDelay()
    {
        protocol.sendWorkSignal();
    }

}
