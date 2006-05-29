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
package net.sf.nmedit.nomad.patch.virtual;

import java.util.AbstractList;

import net.sf.nmedit.nomad.patch.virtual.event.EventChain;
import net.sf.nmedit.nomad.patch.virtual.event.EventListener;
import net.sf.nmedit.nomad.patch.virtual.event.Listenable;
import net.sf.nmedit.nomad.patch.virtual.event.MorphEvent;

/**
 * Set of available morph groups.
 * @author Christian Schneider
 */
public class MorphSet extends AbstractList<Morph> implements Listenable<MorphEvent>
{

    /**
     * the morph groups
     */
    private Morph[] morphGroup;
    
    /**
     * the event listeners
     */
    private EventChain<MorphEvent> listenerList;
    
    MorphSet()
    {
        morphGroup = new Morph[4];
        for (int i=0;i<morphGroup.length;i++)
            morphGroup[i] = new Morph(this, i);
    }

    @Override
    public Morph get( int index )
    {
        return morphGroup[index];
    }

    @Override
    public int size()
    {
        return morphGroup.length;
    }

    public void addListener(EventListener<MorphEvent> l)
    {
        listenerList = new EventChain<MorphEvent>(l, listenerList);
    }

    public void removeListener(EventListener<MorphEvent> l)
    {
        if (listenerList!=null)
            listenerList = listenerList.remove(l);
    }
    
    /**
     * @see net.sf.nmedit.nomad.patch.virtual.event.ListenableAdapter#fireEvent(T)
     */
    void fireEvent(MorphEvent e)
    {
        if (listenerList!=null)
            listenerList.fireEvent(e);
    }
    
}
