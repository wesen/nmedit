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
 * Created on Apr 21, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event;

/**
 * Linked list of event listeners.
 * @author Christian Schneider
 */
public class EventChain<L> 
{

    /**
     * the listener
     */
    private L listener;

    /**
     * chain of event listeners
     */
    private EventChain<L> chain;

    /**
     * Creates a new event chain.
     * 
     * @param l the listener
     * @param chain the current event chain or (<code>null</code> is allowed
     * here)
     */
    public EventChain( L l, EventChain<L> chain )
    {
        this.listener = l;
        this.chain = chain;
    }
    
    public EventChain<L> getChain()
    {
        return chain;
    }
    
    public L getListener()
    {
        return listener;
    }

    /**
     * Returns true if the list contains the listener.
     * 
     * @param l event listener
     * @return true if the list contains the listener
     */
    public boolean containsListener( L l )
    {
        EventChain<L> pos = this;
        do
        {
            if (pos.listener == l)
            {
                return true;
            }
            pos = pos.chain;
        } while (pos != null);
        return false;
    }

    /**
     * Removes the listener l and returns the new event chain.
     * 
     * @param l the listener
     * @return the new event chain without the listener l
     */
    public EventChain<L> remove( L l )
    {
        if (listener == l)
        {
            // the listener is at this position
            // we have to remove this link from the list:
            EventChain<L> newList = chain; // store the event chain
            chain = null; // remove reference to the event chain
            return newList; // return the new event chain
        }
        else
        {
            // we have to search for the occurence of the listener
            // thus we iterate over the event chain

            // candidate that might contain the listener
            EventChain<L> candidate = chain;

            // predecessor of the candidate
            EventChain<L> predecessor = this;

            // while the chain contains further listener
            while (candidate != null)
            {
                // check if the candidate holds the listener
                if (candidate.listener != l)
                {
                    // we still have to look for the listener
                    // set the candidates predecessor
                    predecessor = candidate;
                    // set the candidate
                    candidate = candidate.chain; 
                }
                else
                {
                    // found - remove the listener:
                    // remove the element holding the listener
                    predecessor.chain = candidate.chain;
                    // remove reference to the event chain
                    candidate.chain = null;
                    // search finished
                    break; 
                }
            }

            // we are still head of the chain
            return this;
        }
    }

}
