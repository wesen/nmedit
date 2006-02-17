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
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.xml.XMLFileWriter;
import org.nomad.xml.dom.theme.ComponentNode;


/**
 * @author Christian Schneider
 */
public class PropertySet implements Iterable<Property> {

	private Property fallBack = null;
	private ArrayList<PropertySetListener> propertySetListenerList = null;
	private HashMap<String,Property> propertyMap = new HashMap<String,Property>();
	private boolean flagUseEventListening = false;

	private ChangeListener broadCast = null;

	public PropertySet() {
		super();
	}

	public void addPropertySetListener(PropertySetListener l) {
		if (propertySetListenerList==null) 
			propertySetListenerList = new ArrayList<PropertySetListener>();
		if (!propertySetListenerList.contains(l))
			propertySetListenerList.add(l);
	}
	
	public void removePropertySetListener(PropertySetListener l) {
		if (propertySetListenerList!=null)
			propertySetListenerList.remove(l);
	}

	public void firePropertyAddedEvent(Property p) {
		if (propertySetListenerList!=null) {
			firePropertySetEventNoCheck(PropertySetEvent.createPropertyAddedEvent(this,p));
		}
	}
	
	public void firePropertyRemovedEvent(Property p) {
		if (propertySetListenerList!=null) {
			firePropertySetEventNoCheck(PropertySetEvent.createPropertyRemovedEvent(this,p));
		}
	}
	
	public void firePropertyChangedEvent(Property p, ChangeEvent event) {
		if (propertySetListenerList!=null) {
			firePropertySetEventNoCheck(PropertySetEvent.createPropertyChangedEvent(this,p,event));
		}
	}
	
	public void firePropertySetEvent(PropertySetEvent event) {
		if (propertySetListenerList!=null) {
			firePropertySetEventNoCheck(event);
		}
	}
	
	protected void firePropertySetEventNoCheck(PropertySetEvent event) {
		for (PropertySetListener l : propertySetListenerList) {
			l.propertySetEvent(event);
		}
	}
	
	public void add(Property p) {
		// this will override properties with same name
		propertyMap.put(p.getName(),p);
		if (flagUseEventListening)
			firePropertyAddedEvent(p);
	}

	public void remove(Property p) {
		if ((propertyMap.remove(p.getName())!=null)) {
			propertyMap.remove(p.getName());
			/*p.addChangeListener(broadCast);
			p.removeChangeListener(broadCast);*/
			if (flagUseEventListening)
				firePropertyRemovedEvent(p);
		}
	}
	
	/**
	 * @param string
	 * @return
	 */
	public Property get(String name) {
		//if ((name==null)) return null;
		Property p = propertyMap.get(name);
		if (p==null && fallBack!=null) {
			p=fallBack;
			p.setName(name);
		}
		return p;
	}

	public void setFallbackProperty(Property p) {
		fallBack = p;
	}

	public Iterator<Property> iterator() {
		return propertyMap.values().iterator();
	}

	public void exportToDOM(ComponentNode node) {
		for (Iterator iter=iterator();iter.hasNext();) {
			Property p = (Property) iter.next();
			if (p.isExportable() && (!p.isInDefaultState())) {
				if (p.getName().equals("type"))
					System.out.println(p.getName()+"="+p.getValue());
				
				node.createPropertyNode(p.getName()).setValue(p.getValue());
			}
		}
	}

	public void exportToXml(XMLFileWriter xml) {
		xml.beginTag("properties", true);
		
		for (Iterator iter=iterator();iter.hasNext();) {
			Property p = (Property) iter.next();
			p.exportToXml(xml);
		}
		
		xml.endTag();
	}

	public void clear() {
		for (Property p : new ArrayList<Property>(propertyMap.values()) ) {
			p.removeChangeListener(broadCast);
			propertyMap.remove(p.getName());
		}
	}

}
