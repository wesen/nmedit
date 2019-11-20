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
package net.waldorf.miniworks4pole.jprotocol;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.swing.event.EventListenerList;

import net.sf.nmedit.jsynth.SynthException;

public class MWListenerSupport implements Receiver
{

    private EventListenerList listeners = new EventListenerList();
    private EventListenerList parameterListeners = new EventListenerList();
    private WProtocol protocol;

    public MWListenerSupport (WProtocol protocol)
    {
        this.protocol = protocol;
    }
    
    public void addListener(MWMidiListener l)
    {
        listeners.add(MWMidiListener.class, l);
    }
    
    public void removeListener(MWMidiListener l)
    {
        listeners.remove(MWMidiListener.class, l);
    }

    public void addParameterListener(MWMidiListener l)
    {
        parameterListeners.add(MWMidiListener.class, l);
    }
    
    public void removeParameterListener(MWMidiListener l)
    {
        parameterListeners.remove(MWMidiListener.class, l);
    }
    
    public void notifyListeners(MiniworksMidiMessage message)
    {
        if (message == null) // note on/off
            return;
        
        switch (message.getMessageType())
        {
            case MiniworksMidiMessage.MESSAGE_TYPE_BANKCHANGE:
                bankChangeReceived(message);
                return;
            case MiniworksMidiMessage.MESSAGE_TYPE_CONTROLCHANGE:
                controlChangeReceived(message);
                return;
            case MiniworksMidiMessage.MESSAGE_TYPE_ALIVE:
                aliveMessageReceived(message);
                return;
            case MiniworksMidiMessage.MESSAGE_TYPE_SYSEX:
                switch (message.getDumpType())
                {
                    case MiniworksMidiMessage.DUMP_TYPE_PROGRAM_DUMP:
                        programDumpReceived(message);
                        return;
                    case MiniworksMidiMessage.DUMP_TYPE_PROGRAM_BULK_DUMP:
                        programBulkDumpReceived(message);
                        return;
                    case MiniworksMidiMessage.DUMP_TYPE_ALL_DUMP:
                        allDumpReceived(message);
                        return;
                }
                break;
        }

        System.err.println("unknown message: "+message);
        
    }

    private void allDumpReceived(MiniworksMidiMessage message)
    {
        Object[] list = listeners.getListenerList();   
        for (int i=list.length-2;i>=0;i-=2)
        {
            if (MWMidiListener.class == list[i])
            {
                ((MWMidiListener)list[i+1]).allDumpMessage(message);
            }
        }
    }

    private void aliveMessageReceived(MiniworksMidiMessage message)
    {
        Object[] list = listeners.getListenerList();   
        for (int i=list.length-2;i>=0;i-=2)
        {
            if (MWMidiListener.class == list[i])
            {
                ((MWMidiListener)list[i+1]).aliveMessage(message);
            }
        }
    }

    private void programBulkDumpReceived(MiniworksMidiMessage message)
    {
        Object[] list = listeners.getListenerList();   
        for (int i=list.length-2;i>=0;i-=2)
        {
            if (MWMidiListener.class == list[i])
            {
                ((MWMidiListener)list[i+1]).programBulkDumpMessage(message);
            }
        }
    }

    private void programDumpReceived(MiniworksMidiMessage message)
    {
        Object[] list = listeners.getListenerList();   
        for (int i=list.length-2;i>=0;i-=2)
        {
            if (MWMidiListener.class == list[i])
            {
                ((MWMidiListener)list[i+1]).programDumpMessage(message);
            }
        }
    }

    private void bankChangeReceived(MiniworksMidiMessage message)
    {
        Object[] list = listeners.getListenerList();   
        for (int i=list.length-2;i>=0;i-=2)
        {
            if (MWMidiListener.class == list[i])
            {
                ((MWMidiListener)list[i+1]).bankChangeMessage(message);
            }
        }
    }
    
    private void controlChangeReceived(MiniworksMidiMessage message)
    {
        Object[] list = parameterListeners.getListenerList();
        for (int i=list.length-2;i>=0;i-=2)
        {
            if (MWMidiListener.class == list[i])
            {
                ((MWMidiListener)list[i+1]).parameterMessage(message);
            }
        }
    }

    public void close()
    {
        // ignore
    }

    public void send(MidiMessage message, long timeStamp)
    {
        MiniworksMidiMessage mmm;
        try
        {
            mmm = protocol.createMessage(message);
        }
        catch (SynthException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        notifyListeners(mmm);
    }

}
