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
 * Created on Jan 8, 2006
 */
package org.nomad.theme.property;

import org.nomad.theme.component.NomadComponent;

/**
 * @author Christian Schneider
 */
public abstract class IntegerProperty extends Property {

	public IntegerProperty(NomadComponent component) {
		super(component);
		setName("integer");
	}
	public void setupForEditing() {
		super.setupForEditing();
		setHandler(Integer.class, new PropertyValueHandler(){
			public void writeValue(Object value) throws IllegalArgumentException {
				setInteger((Integer)value); 
			} 
		});
	}
	
	public abstract void setIntegerValue(int integer);
	public abstract int getIntegerValue();

	public void setInteger(Integer integer) { setIntegerValue(integer.intValue()); }
	public Integer getInteger() { return new Integer(getIntegerValue()); }
	public Object getValue() {
		return getInteger();
	}

	public void setValueFromString(String value) {
		try {
			setIntegerValue(Integer.parseInt((String)value));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
