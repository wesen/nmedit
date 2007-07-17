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
 */package net.sf.nmedit.jsynth;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jsynth.event.BankUpdateEvent;
import net.sf.nmedit.jsynth.event.BankUpdateListener;

public abstract class AbstractBank<S extends Synthesizer> implements Bank<S>
{

    private int bankIndex;
    
    private S synth;
    
    protected EventListenerList listenerList = new EventListenerList();

    protected AbstractBank(S synth, int bankIndex)
    {
        this.synth = synth;
        this.bankIndex = bankIndex;
    }

    public String getName()
    {
        return "Bank "+(bankIndex+1);
    }

    public S getSynthesizer()
    {
        return synth;
    }

    public int getBankIndex()
    {
        return bankIndex;
    }

    public void update()
    {
        update(0, getPatchCount());
    }

    public void addBankUpdateListener(BankUpdateListener l)
    {
        listenerList.add(BankUpdateListener.class, l);
    }

    public void removeBankUpdateListener(BankUpdateListener l)
    {
        listenerList.remove(BankUpdateListener.class, l);
    }

    protected void fireBankUpdateEvent(int beginIndex, int endIndex)
    {
        if (beginIndex>endIndex || beginIndex<0 || endIndex>getPatchCount())
            throw new IllegalArgumentException("invalid beginIndex:"+beginIndex+", endIndex:"+endIndex);
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        BankUpdateEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==BankUpdateListener.class) {
                // Lazily create the event:
                if (event == null)
                    event = new BankUpdateEvent(this, beginIndex, endIndex);
                ((BankUpdateListener)listeners[i+1]).bankUpdated(event);
            }
        }
    }
    
}
