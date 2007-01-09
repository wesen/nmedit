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

/*
 * Created on Jan 5, 2007
 */
package net.sf.nmedit.jnmprotocol.example;

import javax.sound.midi.MidiDevice;

import net.sf.nmedit.jnmprotocol.DebugProtocol;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MessageHandler;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jnmprotocol.NmProtocolMT;
import net.sf.nmedit.jnmprotocol.NmProtocolST;
import net.sf.nmedit.jnmprotocol.ProtocolDebug;
import net.sf.nmedit.jnmprotocol.utils.NmLookup;
import net.sf.nmedit.jnmprotocol.utils.ProtocolRunner;
import net.sf.nmedit.jnmprotocol.utils.ProtocolThreadExecutionPolicy;
import net.sf.nmedit.jnmprotocol.utils.StoppableThread;

/**
 * Example demonstrating the use of NmProtocol.
 */
public class Example1 implements MessageHandler
{

    public static void main(String[] args) throws Exception
    {
        (new Example1()).example();
    }
    
    private MidiDriver driver;
    private NmProtocol protocol;
    private StoppableThread protocolThread; 

    public void example() throws Exception
    {
        // lookup the midi device connected to the nord modular
        MidiDevice.Info[] midiDevList = NmLookup.lookup(NmLookup.getHardwareDevices(), 1, 600);
        
        if (midiDevList.length != 2)
            throw new RuntimeException("Nord Modular not found.");
        
        driver = new MidiDriver(midiDevList[0], midiDevList[1]);
        driver.connect();
        
        // create NmProtocol
        protocol = new DebugProtocol(new NmProtocolMT(new NmProtocolST()));
        protocol.setMessageHandler(this);
        protocol.getTransmitter().setReceiver(driver.getReceiver());
        driver.getTransmitter().setReceiver(protocol.getReceiver());
        
        // send the IAmMessage
        protocol.send(new IAmMessage());
        
        // create the thread that calls heartbeat()
        protocolThread = new StoppableThread(new ProtocolThreadExecutionPolicy(protocol), new ProtocolRunner(protocol));
        protocolThread.start();
        
        
        // a second thread that will automatically shutdown this application after
        // the specified amount of time (milliseconds)
        (new Thread(new AutoShutdown(30*1000))).start();
        
    }

    public void processMessage( MidiMessage message )
    {
       System.out.println(getClass().getName()+": "+message);
    }
    
    private class AutoShutdown implements Runnable
    {
    
        private long timeout;

        public AutoShutdown( long timeout )
        {
            this.timeout = timeout;
            System.out.println("application will shutdown after "+(timeout/1000)+" seconds");
        }

        public void run()
        {
            long time = System.currentTimeMillis();
            try
            {
                while (System.currentTimeMillis() - time < timeout && !protocolThread.isStopped())
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        // no op
                    }
                }
            }
            finally
            {
                shutdown();
            }
        }
    }

    private void shutdown()
    {
        protocolThread.stop();
        driver.disconnect();
        System.out.println("shutdown");
    }

}
