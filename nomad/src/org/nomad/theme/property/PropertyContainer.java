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
 * Created on Jan 6, 2006
 */
package org.nomad.theme.property;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Christian Schneider
 */
public class PropertyContainer {

	private ArrayList<ChangeListener> changeListenerList = null;
	
	public PropertyContainer() {
		super();
	}

	public void addChangeListener(ChangeListener l) {
		if (changeListenerList==null) 
			changeListenerList = new ArrayList<ChangeListener>();
		if (!changeListenerList.contains(l))
			changeListenerList.add(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		if (changeListenerList!=null)
			changeListenerList.remove(l);
	}
	
	public void fireChangeEvent() {
		fireChangeEvent(new ChangeEvent(this));
	}
	
	public void fireChangeEvent(ChangeEvent event) {
		if (changeListenerList!=null) {
			for (int i=changeListenerList.size()-1;i>=0;i--)
				changeListenerList.get(i).stateChanged(event);
		}
	}
	
}
