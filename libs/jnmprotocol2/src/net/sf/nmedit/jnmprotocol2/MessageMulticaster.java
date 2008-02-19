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

package net.sf.nmedit.jnmprotocol2;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jnmprotocol2.ActivePidListener;
import net.sf.nmedit.jnmprotocol2.ErrorMessage;
import net.sf.nmedit.jnmprotocol2.MessageHandler;
import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;

/**
 * Broadcasts midi messages to subscribed listeners. 
 */
public class MessageMulticaster implements MessageHandler
{

    private EventListenerList listenerList = new EventListenerList();
    private ActivePidListener activePidListener;
    private boolean errorMessageCausesException = true;
    
    public MessageMulticaster(ActivePidListener apl)
    {
        this.activePidListener = apl;
        addProtocolListener(activePidListener);
    }
    
    public MessageMulticaster()
    {
        this(new ActivePidListener());
    }

    public void setErrorMessageCausesExceptionEnabled(boolean enabled)
    {
        this.errorMessageCausesException = enabled;
    }
    
    public boolean isErrorMessageCausesExceptionEnabled()
    {
        return errorMessageCausesException;
    }
    
    public int getActivePid(int slot)
    {
        return activePidListener.getActivePid(slot);
    }
    
    public void addProtocolListener(NmProtocolListener l)
    {
        listenerList.add(NmProtocolListener.class, l);
    }
    
    public void removeProtocolListener(NmProtocolListener l)
    {
        listenerList.remove(NmProtocolListener.class, l);
    }

    public void processMessage( MidiMessage message ) 
    {
        // if (!(message instanceof LightMessage))
        //    System.out.println(message);
        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            // always true: listeners[i]==NmProtocolListener.class
            message.notifyListener((NmProtocolListener)listeners[i+1]);
        }
        
        // if the message is an ErrorMessage throw a RuntimeException
        if (errorMessageCausesException && message instanceof ErrorMessage)
        {
            ErrorMessage e = (ErrorMessage) message;
            //if (e.isFatal())
                throw new RuntimeException(e.getErrorMessage());
        }
    }

}
