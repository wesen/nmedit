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
import org.nomad.theme.property.editor.PropertyEditor;

/**
 * @author Christian Schneider
 */
public abstract class BooleanProperty extends Property {

	public BooleanProperty(NomadComponent component) {
		super(component);
	}
	
	public void setupForEditing() {
		super.setupForEditing();

		setHandler(Boolean.class, new PropertyValueHandler(){
			public void writeValue(Object value) throws IllegalArgumentException {
				try {
					setBooleanValue((Boolean)value);
				} catch (ClassCastException c) {
					throw new IllegalArgumentException(c);
				}
			}});
	}

	/*public void setValue(Object value) {
		if (!(value instanceof String && fromString((String)value)==null) && !(value instanceof Boolean))
			throw new IllegalArgumentException("Incompatible value "+value);
	}*/

	private final static String STR_TRUE = "true";
	private final static String STR_FALSE = "false";
	
	public static Boolean fromString(String b) {
		if (b.length()<4) return null;
		switch (b.charAt(0)) {
			case't':return b.equals(STR_TRUE) ? Boolean.TRUE : null;
			case'f':return b.equals(STR_FALSE)? Boolean.FALSE: null;
			default:return null;
		}
	}

	public Object getValue() {
		return getBooleanValue();
	}

	public void setBooleanValue(Boolean value) {
		setBooleanValue(value.booleanValue());		
	}
	public abstract void setBooleanValue(boolean value);
	public Boolean getBooleanValue() {
		return new Boolean(getBoolean());
	}
	public abstract boolean getBoolean();
	
	/*public PropertyEditor getEditor() {
		Boolean b = (Boolean) getValue();
		return new PropertyEditor.ComboBoxEditor(this, 
			new Boolean[]{b, new Boolean(!b.booleanValue())});
	}*/
	
	public PropertyEditor getEditor() {
		Boolean b = (Boolean) getValue();
		return new PropertyEditor.CheckBoxEditor(this, b.booleanValue());
	}
	
	public void setValueFromString(String value) {
		Boolean b = fromString((String)value);
		if (b!=null) setBooleanValue(b.booleanValue()); else throw new IllegalArgumentException();
	}
	
}
