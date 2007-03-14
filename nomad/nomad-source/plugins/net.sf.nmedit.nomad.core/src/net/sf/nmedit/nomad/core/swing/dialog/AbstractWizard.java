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
 * Created on Oct 31, 2006
 */
package net.sf.nmedit.nomad.core.swing.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public abstract class AbstractWizard implements Wizard
{

    /**
     * event listener list
     */
    protected EventListenerList listenerList = new EventListenerList();

    public void finish()
    {
        fireActionEvent(DONE);
    }
    

    public void cancel()
    {
        fireActionEvent(CANCELED);
    }
    
    public void addActionListener( ActionListener l )
    {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener( ActionListener l )
    {
        listenerList.remove(ActionListener.class, l);
    }

    public void addChangeListener( ChangeListener l )
    {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener( ChangeListener l )
    {
        listenerList.remove(ChangeListener.class, l);
    }
    
    public void fireChangeEvent() 
    {
        ChangeEvent event = null;
        Object[] listeners = listenerList.getListenerList();

        for (int i=0; i<listeners.length; i+=2) 
        {
            if (listeners[i]==ChangeListener.class) 
            {
                if (event==null)
                    event = new ChangeEvent(this);
                
                ((ChangeListener)listeners[i+1]).stateChanged(event);
            }
        }
    }
    
    public void fireActionEvent(int action) 
    {
        ActionEvent event = null;
        Object[] listeners = listenerList.getListenerList();

        for (int i=0; i<listeners.length; i+=2) 
        {
            if (listeners[i]==ActionListener.class) 
            {
                if (event==null)
                    event = new ActionEvent(this, action, null);
                
                ((ActionListener)listeners[i+1]).actionPerformed(event);
            }
        }
    }
}
