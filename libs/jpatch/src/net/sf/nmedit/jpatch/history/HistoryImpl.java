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
package net.sf.nmedit.jpatch.history;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class HistoryImpl implements History
{

    private EventList undoList = new EventList();
    private EventList redoList = new EventList();
    private EventList targetList = undoList;
    private int recordDepth = 0;
    private int recordStartSize = 0;
    private int limit = 30;
    private boolean changedFlag = false;
    private boolean enabled = false;
    private EventListenerList listenerList;
    

    public void addChangeListener(ChangeListener l)
    {
        if (listenerList == null)
            listenerList = new EventListenerList();
        listenerList.add(ChangeListener.class, l);
    }
    
    public void removeChangeListener(ChangeListener l)
    {
        if (listenerList != null)
            listenerList.remove(ChangeListener.class, l);
    }
    
    private transient ChangeEvent changeEvent;
    
    protected void fireStateChanged()
    {   
        if (listenerList == null)
            return;
        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }
    
    public synchronized void markChanged(boolean changed)
    {
        if (this.changedFlag != changed)
        {
            this.changedFlag = changed;
            fireStateChanged();
        }
    }
    
    public synchronized void beginRecord()
    {
        if (recordDepth == 0)
        {
            recordStartSize = targetList.size();
        }
        recordDepth ++;
    }
    
    public synchronized void endRecord()
    {
        if (recordDepth > 0)
        {
            recordDepth --;
            if (recordDepth == 0)
            {
                int start = recordStartSize;
                int stop = targetList.size();
                
                recordStartSize = 0;
                
                if (start == stop)
                    return ; // not events recorded
                else if (start > stop)
                    return ; // something went wrong
                else
                {
                    int recordCount = stop-start;
                    
                    if (recordCount == 1)
                    {
                        // we keep the event as is
                    }
                    else if (recordCount > 1)
                    {
                        // we put the events in a container 
                        
                        EventList container = new EventList();
                        while (recordCount-->0)
                            container.append(targetList.pollRecent());
                        targetList.put(container);
                    }
                }
            }
            
            applyLimit();
        }
    }
    
    private void applyLimit()
    {
        if (recordDepth == 0 && limit > 0)
        {
            while (targetList.size() >= limit)
            {
                targetList.removeOldest();
            }
        }   
    }
    
    public synchronized void put(Event e)
    {   
        if (!enabled)
            return;
        
        if (limit == 0)
            return;
        
        applyLimit();
        
        targetList.put(e);
        if (targetList.size() == 1)
            fireStateChanged();
        markChanged(true);
    }
    
    public synchronized void setEnabled(boolean enabled)
    {
        if (this.enabled != enabled)
        {
            this.enabled = enabled;
            fireStateChanged();
            clear();
        }
    }
    
    public synchronized boolean isEnabled()
    {
        return enabled;
    }
    
    public synchronized void undo()
    {
        if (!enabled)
            return;
        
        EventList eventList = undoList;
        
        if (eventList.isEmpty())
            return;

        markChanged(true);
        try
        {
            targetList = redoList;
            
            beginRecord();
            eventList.pollRecent().perform();
            endRecord();
            
        }
        finally
        {
            targetList = undoList;
        }
        
        fireStateChanged();
    }
    
    public synchronized void redo()
    {
        if (!enabled)
            return;
        
        EventList eventList = redoList;
        
        if (eventList.isEmpty())
            return;

        markChanged(true);
        try
        {
            beginRecord();
            redoList.pollRecent().perform();
        }
        finally
        {
            endRecord();
        }

        fireStateChanged();
    }
    
    public boolean canRedo()
    {
        return enabled && !redoList.isEmpty();
    }

    public boolean canUndo()
    {
        return enabled && !undoList.isEmpty();
    }

    public void clear()
    {
        if (undoList.isEmpty() && redoList.isEmpty())
            return;
        
        undoList.clear();
        redoList.clear();
        recordDepth = 0;
        recordStartSize = 0;
        fireStateChanged();
    }

    public int getRedoCount()
    {
        return redoList.size();
    }

    public int getUndoCount()
    {
        return undoList.size();
    }

    public boolean isChanged()
    {
        return changedFlag;
    }

    private static class EventList implements Event
    {
        List<Event> eventList = new LinkedList<Event>();
        void put(Event e) { eventList.add(0, e); }
        public void removeOldest() { eventList.remove(eventList.size()-1); }
        void append(Event e) { eventList.add(e); }
        Event peekRecent() { return eventList.get(0); }
        Event pollRecent() { return eventList.remove(0); }
        boolean isEmpty() { return eventList.isEmpty(); }
        int size() { return eventList.size(); }
        void clear() { eventList.clear(); }
        
        public String getTitle() { return null; }
        public void perform()
        {
            while (!isEmpty())
            {
                pollRecent().perform();
            }
        }
    }

}
