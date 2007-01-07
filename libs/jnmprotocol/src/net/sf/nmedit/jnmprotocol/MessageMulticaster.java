package net.sf.nmedit.jnmprotocol;


import java.util.ArrayList;
import java.util.List;


/**
 * Broadcasts midi messages to subscribed listeners. 
 */
public class MessageMulticaster implements MessageHandler
{

    private List<NmProtocolListener> listenerList = new ArrayList<NmProtocolListener>();
    // keeps a copy of listenerList
    private transient NmProtocolListener[] tempList = null;
    
    private ActivePidListener activePidListener;
    
    public MessageMulticaster()
    {
        activePidListener = new ActivePidListener();
        addProtocolListener(activePidListener);
    }
    
    public int getActivePid(int slot)
    {
        return activePidListener.getActivePid(slot);
    }
    
    public void addProtocolListener(NmProtocolListener l)
    {
        if (!listenerList.contains(l))
            listenerList.add(l);
        // listener list was altered, copy has to be recreated
        tempList = null;
    }
    
    public void removeProtocolListener(NmProtocolListener l)
    {
        listenerList.remove(l);
        // listener list was altered, copy has to be recreated
        tempList = null;
    }

    public void processMessage( MidiMessage message )
    {
        NmProtocolListener[] listeners = tempList;
        // check if listener list has been altered
        if (listeners == null)
        {
            listeners = listenerList.toArray(new NmProtocolListener[listenerList.size()]);
            tempList = listeners;
        }

        for (int i=0;i<listeners.length;i++)
        {
            try
            {
                message.notifyListener(listeners[i]);
            }
            catch (Exception e)
            {
                // TODO notifyListener should not throw an exception
                // no op
                e.printStackTrace();
            }
        }
    }

}
