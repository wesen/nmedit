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
 * Created on May 2, 2006
 */
package net.sf.nmedit.nmutils.collections2;

/**
 * An efficient listener list implementation.
 * 
 * Listeners are notified in the reverse order,
 * they were added to the list.
 * 
 * @author Christian Schneider
 */
public abstract class EventListenerList<T,E>
{

    /**
     * listener list
     */
    private ListEntry<T> list = null;
    
    /**
     * Temporary variable used by {@link #notifyListeners(E)}
     * and {@link #remove(T)}.
     * 
     * The variable points to the next listener that will
     * be modified. If this listener is removed during
     * a notification cycle, the variable will be
     * updated so that the removed listener will not be notified.
     */
    private ListEntry<T> notifyNext = null;

    /**
     * Adds the specified listener.
     * @param listener the listener
     */
    public void add(T listener)
    {
        if (!contains(listener))
            list = new ListEntry<T>(listener, list);
    }

    /**
     * Returns true, when the specified listener is subscribed.
     * @param listener the listener
     * @return true, when the specified listener is subscribed
     */
    public boolean contains(T listener)
    {
        ListEntry<T> candidate = list;
        while (candidate!=null)
        {
            if (candidate.item == listener)
                return true;
            candidate = candidate.remaining;
        }
        return false;
    }
    
    /**
     * Sends the specified event to the subscribed listeners.
     * While performing the operation any listener can
     * be removed.
     * 
     * @param event the event
     */
    public void notifyListeners(E event)
    {
        ListEntry<T> pos = list;
        try
        {
            while (pos!=null)
            {
                notifyNext = pos.remaining;

                if (notifyListener(pos.item, event))
                {
                    break ;
                }
                
                pos = notifyNext;
            }
        } 
        finally
        {
            notifyNext = null;
        }
    }
    
    /**
     * Sends the specified event to the specified listener.
     * If the operation returns <code>false</code> the
     * current notification cycle is stopped. 
     * 
     * @param listener the listener
     * @param event the event
     * @return true when the current notification cycle
     * should be stopped.
     */
    protected abstract boolean notifyListener(T listener, E event);

    /**
     * Removes the specified listener if subscribed.
     * The remove operation is allowed while
     * in a notification cycle. If the listener
     * is not reached in a notification cycle, it 
     * will not be receive the pending event.
     * 
     * @param listener the listener
     */
    public void remove(T listener)
    {
        if (list == null)
            return ;
        
        if (list.item == listener)
        {
            // this operation is save while listeners
            // are notified, since nextNotified already
            // points to list.prev or ahead
            list = list.remaining;
            return ;
        }

        ListEntry<T> candidate = list.remaining;
        ListEntry<T> predecessor = list;

        if (notifyNext != null)
        {
            // we have to take care of notifyListeners()
            while (candidate!=null)
            {
                // check if the candidate holds the listener
                if (candidate.item != listener)
                {
                    // we still have to look for the listener
                    // set the candidates predecessor
                    predecessor = candidate;
                    // set the candidate
                    candidate = candidate.remaining;
                    // check if we surpass the
                    // next notified listener
                    if (candidate == notifyNext)
                    {
                        // do not take care of anymore
                        // we will go on with the loop below
                        break ;
                    }
                }
                else
                {
                    // check if we remove the next
                    // notified listener
                    if (candidate == notifyNext)
                    {
                        // yes, we remove the listener
                        // it will not be notified
                        notifyNext = candidate.remaining;
                    }
                    // found - remove the listener:
                    // remove the element holding the listener
                    predecessor.remaining = candidate.remaining;
                    // remove reference to the event chain
                    candidate.remaining = null;
                    // search finished
                    return; 
                }
            }
        }

        // we do not have to take care of notifyListeners()
        // or have to check the rest of the listeners
        while (candidate!=null)
        {
            // check if the candidate holds the listener
            if (candidate.item != listener)
            {
                // we still have to look for the listener
                // set the candidates predecessor
                predecessor = candidate;
                // set the candidate
                candidate = candidate.remaining; 
            }
            else
            {
                // found - remove the listener:
                // remove the element holding the listener
                predecessor.remaining = candidate.remaining;
                // remove reference to the event chain
                candidate.remaining = null;
                // search finished
                break; 
            }
        }
    }
    
    /**
     * Removes all listeners.
     * Calling the operation has the same behaviour
     * as calling <code>removeAll(false)</code>.
     * 
     * @see #removeAll(boolean)
     */
    public void removeAll()
    {
        removeAll(false);
    }
    
    /**
     * Removes all listeners.
     * 
     * If the specified parameter is <code>true</code>
     * the current notification cycle will be stopped.
     * 
     * @param stopNotificationCycle true when the current
     * notification cycle should be stopped
     */
    public void removeAll(boolean stopNotificationCycle)
    {
        list = null;
        if (stopNotificationCycle)
            notifyNext = null;
    }
    
}
