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
import org.nomad.xml.dom.theme.NomadDOMComponent;


/**
 * @author Christian Schneider
 */
public class PropertySet {

	private Property fallBack = null;
	private ArrayList propertySetListenerList = null;
	private HashMap propertyMap = new HashMap();
	
	private ArrayList propertyList = new ArrayList();
	private ChangeListener broadCast = new ChangeListener() {
		public void stateChanged(ChangeEvent event) {
			firePropertySetEvent(PropertySetEvent.createPropertyChangedEvent(PropertySet.this,
					(Property) event.getSource(), event));
		}};

	public PropertySet() {
		super();
	}

	public void addPropertySetListener(PropertySetListener l) {
		if (propertySetListenerList==null) 
			propertySetListenerList = new ArrayList();
		if (!propertySetListenerList.contains(l))
			propertySetListenerList.add(l);
	}
	
	public void removePropertySetListener(PropertySetListener l) {
		if (propertySetListenerList!=null)
			propertySetListenerList.remove(l);
	}
	
	public void firePropertySetEvent(PropertySetEvent event) {
		if (propertySetListenerList!=null) {
			for (int i=propertySetListenerList.size()-1;i>=0;i--)
				((PropertySetListener)propertySetListenerList.get(i)).propertySetEvent(event);
		}
	}
	
	public void add(Property p) {
		if (!propertyList.contains(p)) {
			propertyList.add(p);
			propertyMap.put(p.getName(),p);
			p.addChangeListener(broadCast);
			firePropertySetEvent(PropertySetEvent.createPropertyAddedEvent(this, p));
		}
	}
	
	public Property get(int index) {
		return (Property) propertyList.get(index);
	}
	
	public void remove(Property p) {
		if (propertyList.remove(p)) {
			propertyMap.remove(p.getName());
			p.removeChangeListener(broadCast);
			firePropertySetEvent(PropertySetEvent.createPropertyRemovedEvent(this, p));
		}
	}
	
	public Property remove(int index) {
		Property p = (Property) propertyList.remove(index);
		if (p!=null) {
			propertyMap.remove(p.getName());
			p.removeChangeListener(broadCast);
			firePropertySetEvent(PropertySetEvent.createPropertyRemovedEvent(this, p));
		}
		return p;
	}
	
	public int size() {
		return propertyList.size();
	}

	/**
	 * @param string
	 * @return
	 */
	public Property byName(String name) {
		if (name==null) return null;
		Property p = (Property) propertyMap.get(name);
		if (p==null && fallBack!=null) {
			p=fallBack;
			p.setName(name);
		}
		return p;
	}

	public void setFallbackProperty(Property p) {
		fallBack = p;
	}
	
	/**
	 * @param property
	 */
	public int indexOf(Property property) {
		return propertyList.indexOf(property);
	}
	
	public Iterator iterator() {
		return propertyList.iterator();
	}

	public void exportToDOM(NomadDOMComponent node) {
		for (Iterator iter=iterator();iter.hasNext();) {
			Property p = (Property) iter.next();
			if (p.isExportable())
				node.createPropertyNode(p.getName()).setValue(p.getValueString());
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
	
}
