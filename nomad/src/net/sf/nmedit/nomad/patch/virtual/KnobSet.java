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
 * Created on Apr 19, 2006
 */
package net.sf.nmedit.nomad.patch.virtual;

import java.util.AbstractList;
import java.util.Iterator;

import net.sf.nmedit.nomad.patch.virtual.event.EventChain;
import net.sf.nmedit.nomad.patch.virtual.event.EventListener;
import net.sf.nmedit.nomad.patch.virtual.event.KnobEvent;
import net.sf.nmedit.nomad.patch.virtual.misc.Filter;
import net.sf.nmedit.nomad.patch.virtual.misc.FilteringIterator;

public class KnobSet extends AbstractList<Knob>
{

    private Knob[] knobs;
    private EventChain<KnobEvent> listenerList;

    public KnobSet()
    {
        knobs = new Knob[21];
        for (int i = 0; i < 18; i++)
        {
            knobs[i] = new Knob( this, i );
        }
        knobs[18] = new Knob( this, 19 );
        knobs[19] = new Knob( this, 20 );
        knobs[20] = new Knob( this, 22 );
    }

    public void addListener(EventListener<KnobEvent> l)
    {
        listenerList = new EventChain<KnobEvent>(l, listenerList);
    }

    public void removeListener(EventListener<KnobEvent> l)
    {
        if (listenerList!=null)
            listenerList = listenerList.remove(l);
    }
    
    void fireEvent(KnobEvent e)
    {
        if (listenerList!=null)
            listenerList.fireEvent(e);
    }
    
    public Knob getByID( int ID )
    {
        if (ID >= 19)
        {
            ID--;
        }
        return knobs[ID];
    }

    @Override
    public Knob get( int index )
    {
        return knobs[index];
    }

    @Override
    public int size()
    {
        return knobs.length;
    }

    public Iterator<Knob> iterator(Filter<Knob> f)
    {
        return new FilteringIterator<Knob>(knobs, f);
    }

    public Iterator<Knob> getAssignedKnobs()
    {
        return iterator( new Filter.AssignedKnobs() );
    }

}
