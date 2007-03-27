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

        try
        {
            for (int i=0;i<listeners.length;i++)
            {
                message.notifyListener(listeners[i]);
            }
        }
        catch (RuntimeException e)
        {
            throw e; // rethrow exception
        }
        catch (Exception e)
        {   
            // processMessage() does not allow the throw cause Exception
            // so we cast it to RuntimeException
            
            // TODO notifyListener should not throw an exception ???
            throw new RuntimeException(e);
        }
        
        // if the message is an ErrorMessage throw a RuntimeException
        if (message instanceof ErrorMessage)
            throw new RuntimeException(message.toString());
    }

}
