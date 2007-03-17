package net.sf.nmedit.jsynth;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;

public abstract class AbstractSlot implements Slot
{
    
    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    protected EventListenerList eventListenerList = new EventListenerList();

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        changeSupport.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener l)
    {
        changeSupport.addPropertyChangeListener(propertyName, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        changeSupport.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String propertyName,
            PropertyChangeListener l)
    {
        changeSupport.removePropertyChangeListener(propertyName, l);
    }

    public void addSlotListener(SlotListener l)
    {
        eventListenerList.add(SlotListener.class, l);
    }

    public void removeSlotListener(SlotListener l)
    {
        eventListenerList.remove(SlotListener.class, l);
    }

    protected void fireNewPatchInSlotEvent()
    {
        if (eventListenerList == null)
            return;
        
        SlotListener[] list = eventListenerList.getListeners(SlotListener.class);
 
        if (list.length == 0)
            return;
        
        SlotEvent e = new SlotEvent(SlotEvent.SYNTH_SLOT_NEWPATCH, this);
        
        for (int i=0;i<list.length;i++)
        {
            list[i].newPatchInSlot(e);
        }
    }

}
