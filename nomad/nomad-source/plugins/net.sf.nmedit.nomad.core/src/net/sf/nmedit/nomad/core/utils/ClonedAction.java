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
package net.sf.nmedit.nomad.core.utils;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sf.nmedit.nomad.core.menulayout.MLEntry;

public class ClonedAction extends AbstractAction implements PropertyChangeListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 1769473263039316402L;
    private Action action;
    
    public ClonedAction(Action a)
    {
        this.action = a;
        
        if (a instanceof AbstractAction)
        {
            for (Object o: ((AbstractAction)a).getKeys())
                if (o instanceof String)
                    putValue((String)o, a.getValue((String)o));
        }
        else
        {
            putValue(DEFAULT, a.getValue(DEFAULT));
            putValue(NAME, a.getValue(NAME));
            putValue(SHORT_DESCRIPTION, a.getValue(SHORT_DESCRIPTION));
            putValue(LONG_DESCRIPTION, a.getValue(LONG_DESCRIPTION));
            putValue(SMALL_ICON, a.getValue(SMALL_ICON));
            putValue(ACTION_COMMAND_KEY, a.getValue(ACTION_COMMAND_KEY));
            putValue(ACCELERATOR_KEY, a.getValue(ACCELERATOR_KEY));
            putValue(MNEMONIC_KEY, a.getValue(MNEMONIC_KEY));
            putValue(MLEntry.SELECTED_KEY, a.getValue(MLEntry.SELECTED_KEY));
            putValue(MLEntry.DISPLAYED_MNEMONIC_INDEX_KEY, a.getValue(MLEntry.DISPLAYED_MNEMONIC_INDEX_KEY));
            putValue(MLEntry.LARGE_ICON_KEY, a.getValue(MLEntry.LARGE_ICON_KEY));
        }
        a.addPropertyChangeListener(this);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        action.actionPerformed(e);
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if ("enabled".equals(evt.getPropertyName()))
            setEnabled(action.isEnabled());
        else
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());  
    }
    
}
