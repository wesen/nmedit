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

import java.util.Iterator;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.nmutils.iterator.ArrayIterator;

public class DefaultSlotManager<T extends Synthesizer, L extends Slot> implements SlotManager<L>
{
    
    private EventListenerList listenerList = new EventListenerList();
    
    private T synth;
    private L[] slots;
    
    @SuppressWarnings("unchecked")
    public DefaultSlotManager(T synth)
    {
        this.synth = synth;
        this.slots = (L[]) new Slot[0];
    }
    
    public DefaultSlotManager(T synth, L[] slots)
    {
        this.synth = synth;
        this.slots = slots;
    }
    
    public void setSlots(L[] slots)
    {
        Slot[] old = this.slots;
        this.slots = slots;
        
        for (Slot removed: old)
            notifyListeners(removed, false);
        for (Slot added: slots)
            notifyListeners(added, true);
    }
    
    public L[] getSlots()
    {
        return slots.clone();
    }

    public L getSlot(int index)
    {
        return slots[index];
    }

    public int getSlotCount()
    {
        return slots.length;
    }

    public T getSynth()
    {
        return synth;
    }

    public void addSlotManagerListener(SlotManagerListener l)
    {
        listenerList.add(SlotManagerListener.class, l);
    }

    public void removeSlotManagerListener(SlotManagerListener l)
    {
        listenerList.remove(SlotManagerListener.class, l);
    }
    
    protected void notifyListeners(Slot slot, boolean slotAdded)
    {
        SlotManagerListener[] list = listenerList.getListeners(SlotManagerListener.class);
        if (list.length == 0)
            return;
        
        SlotEvent event = new SlotEvent(slotAdded ? SlotEvent.SYNTH_SLOT_ADDED :
            SlotEvent.SYNTH_SLOT_REMOVED, slot);
        
        if (slotAdded)
        {
            for (int i=0;i<list.length;i++)
            {
                list[i].slotAdded(event);
            }
        }
        else
        {
            for (int i=0;i<list.length;i++)
            {
                list[i].slotRemoved(event);
            }
        }
    }

    public Iterator<L> iterator()
    {
        return new ArrayIterator<L>(slots);
    }
    
}
