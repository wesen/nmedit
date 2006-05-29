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
 * Created on May 18, 2006
 */
package net.sf.nmedit.nomad.synth.nord.protocol;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jnmprotocol.EnqueuedPacket;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.nomad.synth.DeviceIOException;
import net.sf.nmedit.nomad.synth.SynthDevice;

public class ProtocolImpl extends Protocol
{

    private final long HIBERNATION_DELAY = 5000;
    private final long PROCESS_MESSAGE_DELAY = 10;
    
    private ProtocolThread thread = null;
    private CustomizedProtocol protocol = null;

    public ProtocolImpl( SynthDevice device )
    {
        super( device );
    }

    public void start( Info midiIn, Info midiOut ) throws DeviceIOException
    {
        super.start(midiIn, midiOut);
        
        // create protocol class
        protocol = new CustomizedProtocol(driver);
        
        // initialise, create and start thread
        
        thread = new ProtocolThread();
        thread.start();
    }

    protected MidiDriver createDriver()
    {
        return new CustomizedDriver();
    }

    public void stop()
    {
        final long timeout = 500;
        if (!thread.dieAndWait(timeout))
            System.err.println("Thread not stopping.");
        
        if (!thread.isAlive())
        {
            thread = null;
            protocol = null;
        }
        
        super.stop();
    }
    
    @Override
    public void send( MidiMessage message ) throws Exception
    {
        protocol.send(message);
        thread.signalPendingWork();
    }
    
    private class ProtocolThread extends SelfRegulatingThread
    {

        public ProtocolThread()
        {
            super( HIBERNATION_DELAY, PROCESS_MESSAGE_DELAY );
            setDaemon(true);
        }

        @Override
        protected boolean processMessages()
        {
            try
            {
                return protocol.heartbeat();
            }
            //catch (MidiException m)
            //{
//              TODO handle midi exception  
            //}
            catch (Exception e)
            {
                System.err.println("TODO:auto stop thread");
                e.printStackTrace();
                return true;
            }
        }
    }
    
    private class CustomizedDriver extends MidiDriver
    {
        protected void messageReceived()
        {
            thread.signalPendingWork();
        }
    }

    private class CustomizedProtocol extends NmProtocol2
    {

        public CustomizedProtocol( MidiDriver midiDriver )
        {
            super( midiDriver );
        }
        
        protected Queue<EnqueuedPacket> createSendQueue()
        {
            return new ConcurrentLinkedQueue<EnqueuedPacket>();
        }

        @Override
        protected void processMessage( MidiMessage midiMessage )
        {
            SwingUtilities.invokeLater( new MidiMessageNotifier(midiMessage) );
        }
        
    }
    
    private class MidiMessageNotifier implements Runnable
    {
        private MidiMessage m;

        public MidiMessageNotifier(MidiMessage m)
        {
            this.m = m;
        }

        public void run()
        {
            notifyListeners(m);
        }
    }
}
