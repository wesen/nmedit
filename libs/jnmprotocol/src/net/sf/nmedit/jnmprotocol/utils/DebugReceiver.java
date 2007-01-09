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

import java.io.PrintStream;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import net.sf.nmedit.jnmprotocol.ProtocolDebug;

public final class DebugReceiver implements Receiver
{

    private final Receiver receiver;
    private final boolean traceAlways;
    private String debugMessagePrefix;
    private PrintStream debugOut;

    public DebugReceiver(PrintStream debugOut, String debugMessagePrefix, Receiver receiver)
    {
        this(debugOut, debugMessagePrefix, receiver, false);
    }
    
    public DebugReceiver(PrintStream debugOut, String debugMessagePrefix, Receiver receiver, boolean traceAlways)
    {
        this.debugOut = debugOut;
        this.debugMessagePrefix = debugMessagePrefix;
        this.receiver = receiver;
        this.traceAlways = traceAlways;
        
        if (isTracingEnabled())
        {
            ProtocolDebug.trace(debugOut, getClass().getName()+" for "+receiver);
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
            ProtocolDebug.trace(debugOut, receiver, debugMessagePrefix+"send(message,timeStamp)"
            +"="+message.getClass().getName()
            +"("+net.sf.nmedit.jnmprotocol.utils.StringUtils.toHexadecimal(message.getMessage())
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
                ProtocolDebug.traceException(debugOut, receiver, "send()", e);
            }
            throw e;
        }
    }

    public void close()
    {
        if (isTracingEnabled())
        {
            ProtocolDebug.trace(debugOut, receiver, "close()");
        }
        try
        {
            receiver.close();
        }
        catch (RuntimeException e)
        {
            if (isTracingEnabled())
            {
                ProtocolDebug.traceException(debugOut, receiver, "close()", e);
            }
            throw e;
        }
    }

    public String toString()
    {
        return receiver.toString(); 
    }
    
}
