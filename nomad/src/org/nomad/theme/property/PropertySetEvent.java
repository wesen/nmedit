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
 * Created on Jan 12, 2006
 */
package org.nomad.theme.property;

import javax.swing.event.ChangeEvent;

public class PropertySetEvent {

	public final static int PROPERTY_ADDED = 0;
	public final static int PROPERTY_REMOVED = 2;
	public final static int PROPERTY_CHANGED = 3;

	private int eventId;
	private PropertySet propertySet;
	private Property property;
	private ChangeEvent changeEvent;
	
	protected PropertySetEvent(int eventId, PropertySet propertySet, Property property) {
		this(eventId, propertySet, property, null);
	}
	
	protected PropertySetEvent(int eventId, PropertySet propertySet, Property property, ChangeEvent changeEvent) {
		super();
		this.eventId = eventId;
		this.propertySet = propertySet;
		this.property = property;
		this.changeEvent = changeEvent;
	}

	public static PropertySetEvent createPropertyAddedEvent(PropertySet propertySet, Property property) {
		return new PropertySetEvent(PROPERTY_ADDED, propertySet, property);
	}

	public static PropertySetEvent createPropertyRemovedEvent(PropertySet propertySet, Property property) {
		return new PropertySetEvent(PROPERTY_REMOVED, propertySet, property);
	}

	public static PropertySetEvent createPropertyChangedEvent(PropertySet propertySet, Property property, ChangeEvent changeEvent) {
		return new PropertySetEvent(PROPERTY_CHANGED, propertySet, property, changeEvent);
	}
	
	public int getEventId() {
		return eventId;
	}

	public Property getProperty() {
		return property;
	}

	public PropertySet getPropertySet() {
		return propertySet;
	}

	public ChangeEvent getChangeEvent() {
		return changeEvent;
	}
	
}
