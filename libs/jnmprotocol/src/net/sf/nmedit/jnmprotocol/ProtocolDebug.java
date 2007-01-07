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


import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class ProtocolDebug
{

    public static final boolean LevelMessages = true;
    public static final boolean LevelThread = true;
    
    public static final boolean TraceSendProtocolMessage    = LevelMessages;
    public static final boolean TraceReceiveProtocolMessage = LevelMessages;
    public static final boolean TraceDebugReceiver          = LevelMessages;
    
    public static final boolean TraceWorkAvailableSignal    = LevelThread;
    public static final boolean TraceHeartbeats             = LevelThread;
    public static final boolean TraceAwaitWorkSignal        = LevelThread;

    public static final boolean TraceObjects = true;

    private static PrintStream printStream = null;
    
    public static PrintStream getPrintStream()
    {
        return printStream;
    }
    
    public static void setPrintStream(PrintStream out)
    {
        printStream = out;
    }
    
    public static void trace( Object sender, String message )
    {
        if (TraceObjects)
            trace("@"+sender+":"+message);
        else
            trace(message);
    }

    public static void trace( String message )
    {
        PrintStream ps = getPrintStream();
        if (ps!=null)
            ps.println(message);
    }

    public static void traceException( Object sender, String method, Throwable e )
    {
        StringWriter sw = new StringWriter();
        sw.write(method+":an exception occured \n");
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        trace(sender, sw.toString());
    }

}
