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
