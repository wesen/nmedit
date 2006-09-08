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
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03;

import java.util.AbstractList;
import java.util.Iterator;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.AssignmentChangeListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventBuilder;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventChain;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.Assignment;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.Filter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.FilteringIterator;

public class KnobSet extends AbstractList<Knob>
{

    private Knob[] knobs;

    private Patch patch;
    
    public Patch getPatch()
    {
        return patch;
    }
    
    public KnobSet(Patch patch)
    {        
        this.patch = patch;
        knobs = new Knob[21];
        for (int i = 0; i < 18; i++)
        {
            knobs[i] = new Knob( this, i );
        }
        knobs[18] = new Knob( this, 19 );
        knobs[19] = new Knob( this, 20 );
        knobs[20] = new Knob( this, 22 );
    }

    public Knob getByID( int ID )
    {
        int index = ID;
        switch (index)
        {
            case 19:index=18;break;
            case 20:index=19;break;
            case 22:index=20;break;
        }
        try
        {
        return knobs[index];
        } catch (ArrayIndexOutOfBoundsException e)
        {
            throw new RuntimeException("Invalid knob ID:"+ID+" (index="+index+")");
        }
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

    private EventChain<AssignmentChangeListener> listenerList = null;
    
    public void addAssignmentChangeListener(AssignmentChangeListener l)
    {
        listenerList = new EventChain<AssignmentChangeListener>(l, listenerList);
    }
    
    public void removeAssignmentChangeListener(AssignmentChangeListener l)
    {
        if (listenerList != null)
            listenerList = listenerList.remove(l);
    }
    
    public void fireAssignmentChanged( Knob knob, Assignment oldValue, Assignment newValue)
    {
        if (listenerList!=null)
        {
            Event e = EventBuilder.assignmentChanged(patch, knob, oldValue, newValue);
            EventChain <AssignmentChangeListener> l = listenerList;
            do
            {
                l.getListener().assignmentChanged(e);
                l = l.getChain();
            }
            while ( l != null );
        }
    }

}
