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

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import net.sf.nmedit.jnmprotocol.ProtocolDebug;

public final class DebugReceiver implements Receiver
{

    private final Receiver receiver;
    private final boolean traceAlways;
    private String debugMessagePrefix;

    public DebugReceiver(String debugMessagePrefix, Receiver receiver)
    {
        this(debugMessagePrefix, receiver, false);
    }
    
    public DebugReceiver(String debugMessagePrefix, Receiver receiver, boolean traceAlways)
    {
        this.debugMessagePrefix = debugMessagePrefix;
        this.receiver = receiver;
        this.traceAlways = traceAlways;
        
        if (isTracingEnabled())
        {
            ProtocolDebug.trace(getClass().getName()+" for "+receiver);
        }
    }
    
    private boolean isTracingEnabled()
    {
        return ProtocolDebug.TraceDebugReceiver || traceAlways;
    }

    public void send( MidiMessage message, long timeStamp )
    {
        if (isTracingEnabled())
        {
            ProtocolDebug.trace(receiver, debugMessagePrefix+"send(message,timeStamp)"
            +"="+message.getClass().getName()
            +"("+net.sf.nmedit.jnmprotocol.utils.StringUtils.asHex(message.getMessage())
            +","+timeStamp+")");
        }
        
        try
        {
            receiver.send(message, timeStamp);
        }
        catch (RuntimeException e)
        {
            if (isTracingEnabled())
            {
                ProtocolDebug.traceException(receiver, "send()", e);
            }
            throw e;
        }
    }

    public void close()
    {
        if (isTracingEnabled())
        {
            ProtocolDebug.trace(receiver, "close()");
        }
        try
        {
            receiver.close();
        }
        catch (RuntimeException e)
        {
            if (isTracingEnabled())
            {
                ProtocolDebug.traceException(receiver, "close()", e);
            }
            throw e;
        }
    }

    public String toString()
    {
        return receiver.toString(); 
    }
    
}
