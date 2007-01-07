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
