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

import javax.sound.midi.*;
import java.util.*;

public class MidiDriver implements Receiver
{
    private static final byte SYSEX_END = (byte)0xf7;

    private LinkedList inputBuffer;

    private MidiDevice inputDevice;
    private MidiDevice outputDevice;

    private Receiver out;
    private Transmitter in;
	
    public MidiDriver()
    {
	inputBuffer = new LinkedList();
    }

    /**
       Get lists with midi i/o ports to be used in connect().
    */
    public String[] getMidiInputPorts()
    {
	return getMidiOutputPorts();
    }

    public String[] getMidiOutputPorts()
    {
	MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
	String[] ports = new String[info.length];
	for (int i = 0; i < info.length; i++) {
	    ports[i] = info[i].getName();
	}
	return ports;
    }
    
    public void connect(String midiInputPort, String midiOutputPort)
	throws Exception
    {	
	MidiDevice.Info inputInfo = null;
	MidiDevice.Info outputInfo = null;
	MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();

	for (int i = 0; i < info.length; i++) {
	    if (info[i].getName().equals(midiInputPort)) {
		inputInfo = info[i];
	    }
	    if (info[i].getName().equals(midiOutputPort)) {
		outputInfo = info[i];
	    }
	}
	if (inputInfo == null) {
	    throw new MidiException("Unable to find port: " + midiInputPort,
				    0);
	}
	if (outputInfo == null) {
	    throw new MidiException("Unable to find port: " + midiOutputPort,
				    0);
	}

	inputDevice = MidiSystem.getMidiDevice(inputInfo);
	inputDevice.open();
	if (inputInfo == outputInfo) {
	    outputDevice = inputDevice;
	}
	else {
	    outputDevice = MidiSystem.getMidiDevice(outputInfo);
	    outputDevice.open();
	}

	out = MidiSystem.getMidiDevice(outputInfo).getReceiver();
	in = MidiSystem.getMidiDevice(inputInfo).getTransmitter();
	in.setReceiver(this);
    }

    public void disconnect()
    {
	inputDevice.close();
	if (outputDevice != inputDevice) {
	    outputDevice.close();
	}
    }

    /**
       Send bytes to the outputPort given in connect(). The call
       blocks until all bytes are sent.
    */
    public void send(byte[] bytes)
	throws Exception
    {
	SysexMessage sysex = new SysexMessage();
	sysex.setMessage(bytes, bytes.length);
	out.send(sysex, -1);
    }

    /**
       Receive bytes from the inputPort given in connect(). The call
       is non-blocking and will return zero bytes if it fails to fetch
       one full sysex packet from the inputPort. No more than one
       sysex packet is returned each time receive() is called.
    */
    public synchronized byte[] receive()
    {
	if (inputBuffer.size() > 0) {
	    return (byte[])inputBuffer.removeFirst();
	}
	else {
	    return new byte[0];
	}
    }

    /**
     * This is where messages are received from the synth.
     */
    public synchronized void send(javax.sound.midi.MidiMessage message,
				  long timestamp)
    {
	inputBuffer.add(message.getMessage());
    }

    public void close()
    {
    }
}
