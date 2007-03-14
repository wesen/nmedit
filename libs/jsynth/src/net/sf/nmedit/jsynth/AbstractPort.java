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
 * Created on Jan 2, 2007
 */
package net.sf.nmedit.jsynth;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jsynth.event.PortAttachmentEvent;
import net.sf.nmedit.jsynth.event.PortAttachmentListener;

public abstract class AbstractPort implements Port
{

    private Synthesizer synth;
    private EventListenerList eventListeners = null;
    
    public AbstractPort(Synthesizer synth)
    {
        this.synth = synth;
    }

    public Synthesizer getSynthesizer()
    {
        return synth;
    }
    
    public void addPortAttachmentListener(PortAttachmentListener l)
    {
        if (eventListeners == null)
            eventListeners = new EventListenerList();
        eventListeners.add(PortAttachmentListener.class, l);
    }
    
    public void removePortAttachmentListener(PortAttachmentListener l)
    {
        if (eventListeners != null)
            eventListeners.remove(PortAttachmentListener.class, l);
    }
    
    protected void firePlugAttached(Plug oldPlug, Plug newPlug)
    {
        if (eventListeners == null)
            return;
        
        PortAttachmentListener[] list = eventListeners.getListeners(PortAttachmentListener.class);
        if (list.length<=0)
            return;

        PortAttachmentEvent e = new PortAttachmentEvent(this, oldPlug, newPlug);
        for (int i=0;i<list.length;i++)
            list[i].plugAttachmentChanged(e);
    }

    public String toString()
    {
        return getClass().getSimpleName()+"[name="+getName()+",synth.device="+getSynthesizer().getDeviceName()
        +",synth.name="+getSynthesizer().getName()+"]";
    }

}
