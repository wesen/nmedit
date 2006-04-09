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
 * Created on Mar 30, 2006
 */
package net.sf.nmedit.nomad.core.application;

import java.io.File;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;


public class Logger extends PrintWriter
{

    public final static int LINE_SIZE = 80;

    public Logger( OutputStream out )
    {
        this( new OutputStreamWriter( out ) );
    }

    public Logger( Writer out )
    {
        super( out, true );
    }

    public static Logger create( String fileName )
    {
        if (fileName == null)
        {
            return Logger.create( (File) null );
        }
        else
        {
            return Logger.create( new File( fileName ) );
        }
    }

    public static Logger create( File file )
    {
        Logger logger;
        logger = null;

        try
        {
            logger = new Logger( new FileWriter( file ) );
            
            // make System.out/System.err additionally print to the log file 
            System.setOut(new PrintStream(new LineCapturer(System.out, logger, "[OUT]")));
            System.setErr(new PrintStream(new LineCapturer(System.err, logger, "[ERR]")));
        }
        catch (Throwable t)
        {
            logger = new Logger( System.out );
        }

        return logger;
    }

    public void printStatusLine( String status )
    {
        println( "[STATUS] " + status );
    }

    public void printHeader()
    {

        String appName = Application.getProperty( Const.APPLICATION_NAME );
        String appVersion = Application.getProperty( Const.APPLICATION_VERSION );

        println();
        repeatln( '*', LINE_SIZE );
        println( appName + " Version " + appVersion );
        println();

        // os
        println( "[os]" );
        print( System.getProperty( Const.SYS_OS_NAME ) + " \\ " );
        print( System.getProperty( Const.SYS_OS_VERSION ) + " \\ " );
        print( System.getProperty( Const.SYS_OS_ARCH ) );
        println();
        println();

        // java vm
        println( "[java vm]" );
        print( System.getProperty( Const.SYS_JAVA_VM_NAME ) + " \\ " );
        print( System.getProperty( Const.SYS_JAVA_VM_VENDOR ) + " \\ " );
        print( System.getProperty( Const.SYS_JAVA_VM_VERSION ) );
        println();
        println();

        // java path
        println( "[java path]" );
        println( Const.SYS_JAVA_LIBRARY_PATH + "="
                + System.getProperty( Const.SYS_JAVA_LIBRARY_PATH ) );
        println( Const.SYS_JAVA_CLASS_PATH + "="
                + System.getProperty( Const.SYS_JAVA_CLASS_PATH ) );
        println();

        repeatln( '*', LINE_SIZE );
        println();
        // System.getProperties().list(this);
    }

    public void repeat( char c, int count )
    {
        StringBuffer sb = new StringBuffer( count );
        for (int i = 0; i < count; i++)
            sb.append( c );
        print( sb );
    }

    public void repeatln( char c, int count )
    {
        repeat( c, count );
        println();
    }

    private static class LineCapturer extends FilterOutputStream {

        private PrintWriter out2;
        private StringBuffer capturing ;
        private String capturedLine  ;
        private String prefix;

        public LineCapturer( PrintStream out1, PrintWriter out2, String prefix )
        {
            super( out1 );
            this.out2 = out2;
            this.prefix = prefix;
            this.capturing = new StringBuffer();
            this.capturedLine = ""; 
        }
        
        private void lineCaptured()
        {
            // store captured line
            capturedLine = new String(capturing);
            
            // clear capturing buffer
            capturing.replace(0, capturing.length(), "");
            
            // write to second stream
            out2.print(prefix);
            out2.println(capturedLine);;
        }
        
        public void write(int b) throws IOException
        {
            if (b == '\n')
            {
                lineCaptured();
            } else {
                capturing.append((char) b);
            }
            
            super.write(b);
        }
        
    }
    
}
