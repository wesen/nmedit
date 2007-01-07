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


import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class MidiDriver
{

    // private static final byte SYSEX_END = (byte)0xf7;

    private MidiDevice.Info infoInputDevice;
    private MidiDevice.Info infoOutputDevice;
    
    private MidiDevice inputDevice;
    private MidiDevice outputDevice;
    
    private Receiver receiver;
    private Transmitter transmitter;

    private boolean allowCloseInput = false;
    private boolean allowCloseOutput = false;
    
    private boolean connected = false;

    public MidiDriver(MidiDevice.Info infoInputDevice, MidiDevice.Info infoOutputDevice) 
    {
        this.infoInputDevice = infoInputDevice;
        this.infoOutputDevice = infoOutputDevice;
    }
    
    public MidiDevice.Info getInputInfo()
    {
        return infoInputDevice;
    }
    
    public MidiDevice.Info getOutputInfo()
    {
        return infoOutputDevice;
    }
    
    public void connect() throws MidiUnavailableException
    {   
        if (connected)
            return;
        
        inputDevice = MidiSystem.getMidiDevice(infoInputDevice);
        outputDevice = MidiSystem.getMidiDevice(infoOutputDevice);
        
        try
        {
            allowCloseInput = false;
            allowCloseOutput = false;
            if (!inputDevice.isOpen())
            {
                allowCloseInput = true;
                inputDevice.open();
            }
            if (!outputDevice.isOpen())
            {
                allowCloseOutput = true;
                outputDevice.open();
            }
            
            receiver = outputDevice.getReceiver();
            transmitter = inputDevice.getTransmitter();
        }
        catch (MidiUnavailableException e)
        {
            disconnect();
            throw e;
        }
        
        connected = true;
    }

    public void disconnect()
    {
        try
        {
            if (receiver!=null)
                receiver.close();
            if (transmitter!=null)
                transmitter.close();
            if ((inputDevice!=null) && inputDevice.isOpen() && allowCloseInput)
                inputDevice.close();
            if ((outputDevice!=null) && outputDevice.isOpen() && allowCloseOutput)
                outputDevice.close();
        }
        finally
        {
            connected = false;
        }
    }
    
    public Receiver getReceiver() 
    {
        if (!connected)
            throw new IllegalStateException("receiver not available");
        
        return receiver;
    }
    
    public Transmitter getTransmitter()
    {
        if (!connected)
            throw new IllegalStateException("transmitter not available");
        
        return transmitter;
    }

    public boolean isConnected()
    {
        return connected;
    }
    
    

}
