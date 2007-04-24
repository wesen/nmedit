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
package net.sf.nmedit.nordmodular;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.history.History;
import net.sf.nmedit.nomad.core.swing.document.HistoryFeature;

public class PatchHistoryFeature implements HistoryFeature, ChangeListener
{
    
    private EventListenerList listenerList = new EventListenerList();
    
    private Object target;
    
    private transient ChangeEvent changeEvent;

    private History history;

    public PatchHistoryFeature(Object target, History history)
    {
        this.target = target;
        this.history = history;
        
        history.addChangeListener(this);
    }
    
    public void addChangeListener(ChangeListener l)
    {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l)
    {
        listenerList.remove(ChangeListener.class, l);
    }

    public void stateChanged(ChangeEvent e)
    {
        fireStateChanged();
    }

    protected void fireStateChanged()
    {     // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(target);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }

    public boolean canRedo()
    {
        return history.canRedo();
    }

    public boolean canUndo()
    {
        return history.canUndo();
    }

    public boolean isChanged()
    {
        return history.isChanged();
    }

    public void redo()
    {
        history.redo();
    }

    public void undo()
    {
        history.undo();
    }

}
