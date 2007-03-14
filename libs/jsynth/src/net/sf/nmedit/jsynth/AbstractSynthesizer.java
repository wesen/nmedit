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
 * Created on Jan 8, 2007
 */
package net.sf.nmedit.jsynth;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;

public abstract class AbstractSynthesizer implements Synthesizer
{

    private EventListenerList listenerList = new EventListenerList();
    
    protected void fireSynthesizerStateChanged()
    {
        SynthesizerStateListener[] list = 
            listenerList.<SynthesizerStateListener>getListeners(SynthesizerStateListener.class);
        
        if (list.length>0)
        {
            SynthesizerEvent e = new SynthesizerEvent(this);
            for (int i=0;i<list.length;i++)
            {
                list[i].synthConnectionStateChanged(e);
            }
        }
    }
    
    public void addSynthesizerStateListener( SynthesizerStateListener l )
    {
        listenerList.add(SynthesizerStateListener.class, l);
    }

    public void removeSynthesizerStateListener( SynthesizerStateListener l )
    {
        listenerList.remove(SynthesizerStateListener.class, l);
    }

}
