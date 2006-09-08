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

public class MidiControllerSet extends AbstractList<MidiController>
{
    
    private MidiController[] midiControllerList;
    
    private Patch patch;
    
    public Patch getPatch()
    {
        return patch;
    }
    
    public MidiControllerSet(Patch patch)
    {        
        this.patch = patch;
        midiControllerList = new MidiController[120];
        for (int i=0;i<32;i++)
            midiControllerList[i] = new MidiController(this, i);
        for (int i=33;i<=120;i++)
            midiControllerList[i-1] = new MidiController(this, i);
    }

    public static boolean isValidMC(int mc)
    {
        return 0 <= mc && mc<= 120 && mc!=32;
    }
    
    public int getIndexForMC(int mc)
    {
        if (0<=mc)
        {
            if (mc<32)
            {
                return mc;
            }
            else if (mc==32)
            {
                return -1;
            }
            else if (mc<=120)
            {
                return mc-1;
            }
        }
        return -1;
    }
    
    public MidiController[] getPrimaryMidicontrollers()
    {
        return new MidiController[]{
                getByMC(1),
                getByMC(11),
                getByMC(7)};
    }
    
    public MidiController getByMC(int mc)
    {
        try
        {
        return midiControllerList[getIndexForMC(mc)];
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new RuntimeException("Invalid midi controller:"+mc);
        }
    }
    
    public int indexOf(Object obj)
    {
        MidiController mc = (MidiController) obj;
        int index = getIndexForMC(mc.getID());
        try 
        {
            if (midiControllerList[index]==mc)
            {
                return index;
            }
        } 
        catch (IndexOutOfBoundsException e)
        { }

        return -1;
    }
    
    @Override
    public MidiController get(int index)
    {
        return midiControllerList[index];
    }
    
    @Override
    public int size()
    {
        return midiControllerList.length;
    }

    public Iterator<MidiController> iterator(Filter<MidiController> f)
    {
        return new FilteringIterator<MidiController>(midiControllerList, f);
    }
    
    public Iterator<MidiController> getAssignedMidiControllers()
    {
        return iterator(new Filter.AssignedMidiControllers());
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
    
    public void fireAssignmentChanged( MidiController mctrl, Assignment oldValue, Assignment newValue)
    {
        if (listenerList!=null)
        {
            Event e = EventBuilder.assignmentChanged(patch, mctrl, oldValue, newValue);
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
