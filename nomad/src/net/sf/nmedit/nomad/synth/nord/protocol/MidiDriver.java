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

package net.sf.nmedit.nomad.synth.nord.protocol;

import java.util.LinkedList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Transmitter;

@SuppressWarnings("unchecked")
public class MidiDriver
{
   // private static final byte SYSEX_END = (byte)0xf7;

    private final SysexMessage sysex = new SysexMessage();
    private final byte[] NO_DATA = new byte[0];

    private MidiDevice inputDevice;
    private MidiDevice outputDevice;

    private Transmitter in;
    private Receiver out;

    private final LinkedList inputBuffer;

    public MidiDriver() 
    {
        inputBuffer = new LinkedList();
    }

    public void connect(MidiDevice.Info inputInfo, MidiDevice.Info outputInfo) 
        throws Exception
    {
        inputDevice = MidiSystem.getMidiDevice(inputInfo);
        inputDevice.open();
        in = inputDevice.getTransmitter();
        in.setReceiver(new BufferingReceiver());

        outputDevice = MidiSystem.getMidiDevice(outputInfo);
        outputDevice.open();
        out = outputDevice.getReceiver();
    }

    public void disconnect()
    {
        inputDevice.close();
        outputDevice.close();
    }

    /**
       Send bytes to the outputPort given in connect(). The call
       blocks until all bytes are sent.
     * @throws InvalidMidiDataException 
    */
    public void send(byte[] bytes) throws Exception
    {
        synchronized (sysex)
        {
            sysex.setMessage(bytes, bytes.length);
            out.send(sysex, -1);
        }
    }

    /**
       Receive bytes from the inputPort given in connect(). The call
       is non-blocking and will return zero bytes if it fails to fetch
       one full sysex packet from the inputPort. No more than one
       sysex packet is returned each time receive() is called.
    */
    public byte[] receive()
    {
        if (!inputBuffer.isEmpty())
        {
            synchronized (inputBuffer)
            { 
                return (byte[]) inputBuffer.removeFirst(); 
            }
        }
        else
        {
            return NO_DATA;
        }
    }
    
    private class BufferingReceiver implements Receiver
    {
        
        /**
         * This is where messages are received from the synth.
         */
        public void send( MidiMessage message, long timeStamp )
        {
            synchronized (inputBuffer)
            {
                inputBuffer.add(message.getMessage());
            }
            messageReceived();
        }

        public void close()
        { 
            synchronized (inputBuffer)
            {
                inputBuffer.clear();
            }
        }
        
    }

    protected void messageReceived()
    {
    }
    
}
